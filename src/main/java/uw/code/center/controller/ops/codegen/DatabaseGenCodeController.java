package uw.code.center.controller.ops.codegen;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uw.auth.service.annotation.MscPermDeclare;
import uw.auth.service.constant.ActionLog;
import uw.auth.service.constant.UserType;
import uw.code.center.constant.TemplateType;
import uw.code.center.entity.CodeTemplate;
import uw.code.center.entity.CodeTemplateGroup;
import uw.code.center.service.dao.*;
import uw.code.center.template.TemplateHelper;
import uw.dao.DaoFactory;
import uw.dao.DataList;
import uw.dao.TransactionException;
import uw.dao.conf.DaoConfigManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@Tag(name = "数据库代码生成", description = "数据库代码生成")
@RequestMapping("/ops/codegen/databaseGenCode")
@MscPermDeclare(type = UserType.OPS)
public class DatabaseGenCodeController {

    private final DaoFactory dao = DaoFactory.getInstance();

    /**
     * 获得数据库连接列表。
     *
     * @return
     */
    @Operation(summary = "获得数据库连接列表", description = "获得数据库连接列表")
    @GetMapping("/list")
    @MscPermDeclare(type = UserType.OPS, log = ActionLog.REQUEST)
    public List<String> getConnectionList() {
        return DaoConfigManager.getConnPoolNameList();
    }

    /**
     * 获得数据库表列表。
     * 对于oracle数据库，还需要传入connName，
     *
     * @param connName
     * @param schemaName
     * @param filterTableNames
     * @return
     */
    @MscPermDeclare(type = UserType.OPS, log = ActionLog.REQUEST)
    @Operation(summary = "获得数据库表列表", description = "获得数据库表列表")
    @GetMapping("/tableInfoList")
    public List<MetaTableInfo> getTableInfo(@Parameter(description = "connName", example = "test") @RequestParam String connName,
                                            @Parameter(description = "schemaName", example = "test") @RequestParam String schemaName,
                                            @Parameter(description = "过滤表名称", example = "filter_table_1,filter_table_2") @RequestParam Set<String> filterTableNames) {
        DataMetaInterface dataMetaInterface = DatabaseMetaParser.getDataMetaInterface(connName, schemaName);
        return dataMetaInterface.getTablesAndViews(filterTableNames);
    }

    /**
     * 生成代码。
     *
     * @param templateId
     * @param tableName
     * @return
     */
    @MscPermDeclare(type = UserType.OPS, log = ActionLog.REQUEST)
    @Operation(summary = "生成代码", description = "生成代码")
    @GetMapping("/genCode")
    public String genCode(@Parameter(description = "模板Id", example = "1") @RequestParam long templateId,
                          @Parameter(description = "表名称", example = "1") @RequestParam String tableName) {
        return null;
    }

    /**
     * 批量下载代码。
     *
     * @param templateGroupId
     * @param filterTableNames
     */
    @MscPermDeclare(type = UserType.OPS, log = ActionLog.REQUEST)
    @Operation(summary = "批量下载代码", description = "批量下载代码")
    @GetMapping("/downloadCode")
    public void downloadCode(HttpServletResponse response,
                             @RequestParam() String connName,
                             @RequestParam() String schemaName,
                             @Parameter(description = "模板组Id", example = "1", required = false) @RequestParam long templateGroupId,
                             @Parameter(description = "过滤表集合(set)", example = "filter_table_1,filter_table_2", schema = @Schema(type = "string")) @RequestParam() Set<String> filterTableNames) throws TransactionException, IOException {
        DataMetaInterface dataMetaInterface = DatabaseMetaParser.getDataMetaInterface(connName, schemaName);
        List<MetaTableInfo> tablelist = dataMetaInterface.getTablesAndViews(filterTableNames);
        CodeTemplateGroup codeTemplateGroup = dao.load(CodeTemplateGroup.class, templateGroupId);
        DataList<CodeTemplate> ctList = dao.list(CodeTemplate.class, "select * from code_template where group_id=? and state=1", new Object[]{templateGroupId});
        if (ctList.size() > 0 && tablelist.size() > 0) {
            //设置文件下载格式
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + URLEncoder.encode(codeTemplateGroup.getGroupName(), "utf-8") + "_" + System.currentTimeMillis() + ".zip");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            OutputStream outputStream = response.getOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
            //拼参数
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("author", "axeon");
            map.put("date", new Date());
            for (CodeTemplate ct : ctList) {
                //判定类型再输出。
                if (ct.getTemplateType() == TemplateType.DbEntity.getValue()) {
                    for (MetaTableInfo metaTableInfo : tablelist) {
                        map.put("tableMeta", metaTableInfo);
                        // 获得主键列表
                        List<MetaPrimaryKeyInfo> pklist = dataMetaInterface.getPrimaryKey(metaTableInfo.getTableName());
                        map.put("pkList", pklist);
                        // 获得列列表
                        List<MetaColumnInfo> columnlist = dataMetaInterface.getColumnList(metaTableInfo.getTableName(), pklist);
                        map.put("columnList", columnlist);
                        String fileName = TemplateHelper.buildTemplate(ct.getId() + "#filename", map);
                        String fileBody = TemplateHelper.buildTemplate(ct.getId() + "#body", map);
                        zipOutputStream.putNextEntry(new ZipEntry(fileName));
                        zipOutputStream.write(fileBody.getBytes());
                        zipOutputStream.closeEntry();
                    }
                }
            }
            zipOutputStream.flush();
            zipOutputStream.finish();
            zipOutputStream.close();
            outputStream.close();
        }
    }

}
