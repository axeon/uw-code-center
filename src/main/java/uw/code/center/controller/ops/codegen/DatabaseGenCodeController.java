package uw.code.center.controller.ops.codegen;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;
import uw.auth.service.annotation.MscPermDeclare;
import uw.auth.service.annotation.ResponseAdviceIgnore;
import uw.auth.service.constant.ActionLog;
import uw.auth.service.constant.AuthType;
import uw.auth.service.constant.UserType;
import uw.code.center.entity.CodeTemplateGroup;
import uw.code.center.entity.CodeTemplateInfo;
import uw.code.center.service.dao.*;
import uw.code.center.template.TemplateHelper;
import uw.code.center.util.ZipUtils;
import uw.common.data.PageList;
import uw.common.util.SystemClock;
import uw.dao.DaoManager;
import uw.dao.conf.DaoConfigManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 数据库驱动的代码生成接口。
 * <p>
 * 基于 JDBC 元数据（通过 {@link DatabaseMetaParser} 选取 MySQL / PostgreSQL / Oracle 实现）读取表结构与主键，
 * 结合 FreeMarker 模板批量生成 Entity/Controller/DTO 等源文件，打包为 ZIP 下载。
 * </p>
 */
@RestController
@Tag(name = "数据库代码生成", description = "数据库代码生成")
@RequestMapping("/ops/codegen/database")
@MscPermDeclare(user = UserType.OPS)
public class DatabaseGenCodeController {

    private final DaoManager dao = DaoManager.getInstance();

    private final FastDateFormat dateFormat = FastDateFormat.getInstance("yyyyMMddHHmmss");

    /**
     * 获取数据库连接列表。
     *
     * @return 已注册的连接池名称列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取数据库连接列表", description = "获取数据库连接列表")
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.REQUEST)
    public List<String> getConnectionList() {
        return DaoConfigManager.getConnPoolNameList();
    }

    /**
     * 获取数据库表列表。
     * 对于oracle数据库，还需要传入connName，
     *
     * @param connName         连接池名称
     * @param schemaName       schema 名称（Oracle 用户名 / PostgreSQL 默认 public）
     * @param filterTableNames 过滤表名集合；为空时返回全部
     * @return 表/视图元数据列表
     */
    @GetMapping("/tableInfoList")
    @Operation(summary = "获取数据库表列表", description = "获取数据库表列表")
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.REQUEST)
    public List<MetaTableInfo> getTableInfo(@Parameter(description = "connName", example = "test") @RequestParam String connName, @Parameter(description = "schemaName", example
            = "test") @RequestParam String schemaName, @Parameter(description = "过滤表名称", example = "filter_table_1,filter_table_2") @RequestParam Set<String> filterTableNames) {
        DataMetaInterface dataMetaInterface = DatabaseMetaParser.getDataMetaInterface(connName, schemaName);
        return dataMetaInterface.getTablesAndViews(filterTableNames);
    }

    /**
     * 生成代码（保留接口，当前未实现，固定返回 null）。
     *
     * @param templateId 模板 ID
     * @param tableName  表名称
     * @return 生成的代码文本（当前实现返回 null）
     */
    @GetMapping("/genCode")
    @Operation(summary = "生成代码", description = "生成代码")
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.REQUEST)
    public String genCode(@Parameter(description = "模板Id") @RequestParam long templateId,
                          @Parameter(description = "表名称") @RequestParam String tableName) {
        return null;
    }

    /**
     * 批量下载代码。
     * <p>
     * 根据模板分组与表集合，逐表渲染 FreeMarker 模板并打包为 ZIP 下载。
     * 渲染出的文件名经 {@link ZipUtils#safeEntry(String)} 校验，防御 Zip Slip。
     * </p>
     *
     * @param response         HTTP 响应，用于写入 ZIP 流
     * @param connName         连接池名称
     * @param schemaName       schema 名称
     * @param templateGroupId  模板分组 ID
     * @param filterTableNames 过滤表名集合；为空时处理全部表
     * @throws IOException 写入响应流失败
     */
    @ResponseAdviceIgnore
    @GetMapping("/downloadCode")
    @Operation(summary = "批量下载代码", description = "批量下载代码")
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.REQUEST)
    public void downloadCode(HttpServletResponse response, @RequestParam() String connName, @RequestParam() String schemaName, @Parameter(description = "模板组Id",
            required = false) @RequestParam long templateGroupId, @Parameter(description = "过滤表集合(set)", example = "filter_table_1,filter_table_2", schema = @Schema(type =
            "string")) @RequestParam() Set<String> filterTableNames) throws IOException {
        CodeTemplateGroup codeTemplateGroup = dao.load(CodeTemplateGroup.class, templateGroupId).getData();
        PageList<CodeTemplateInfo> ctList = dao.list(CodeTemplateInfo.class, "select * from code_template_info where group_id=? and state=1", new Object[]{templateGroupId}).getData();
        if (codeTemplateGroup == null || ctList == null) {
            throw new RuntimeException("模板组或模板组下模板不存在！");
        }
        DataMetaInterface dataMetaInterface = DatabaseMetaParser.getDataMetaInterface(connName, schemaName);
        List<MetaTableInfo> tablelist = dataMetaInterface.getTablesAndViews(filterTableNames);
        if (ctList.size() > 0 && tablelist.size() > 0) {
            //设置文件下载格式
            response.setContentType("application/x-download; charset=utf-8");
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + UriUtils.encode(codeTemplateGroup.getGroupName(), StandardCharsets.UTF_8) + "_" + dateFormat.format(SystemClock.nowDate()) + ".zip");
            try (OutputStream outputStream = response.getOutputStream(); ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream, StandardCharsets.UTF_8)) {
                //拼参数
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("author", "axeon");
                map.put("date", SystemClock.nowDate());
                for (CodeTemplateInfo ct : ctList) {
                    //判定类型再输出。
//                if (ct.getTemplateType()) {
                    for (MetaTableInfo metaTableInfo : tablelist) {
                        map.put("tableMeta", metaTableInfo);
                        // 获取主键列表
                        List<MetaPrimaryKeyInfo> pklist = dataMetaInterface.getPrimaryKey(metaTableInfo.getTableName());
                        // 获取列列表
                        List<MetaColumnInfo> columnlist = dataMetaInterface.getColumnList(metaTableInfo.getTableName(), pklist);
                        map.put("columnList", columnlist);
                        //过滤生成主键
                        map.put("pkList", columnlist.stream().filter(x -> Boolean.parseBoolean(x.getIsPrimaryKey())).toList());
                        String fileName = TemplateHelper.buildTemplate(ct.getId() + "#filename", map);
                        String fileBody = TemplateHelper.buildTemplate(ct.getId() + "#body", map);
                        if (StringUtils.isBlank(fileName) || StringUtils.isBlank(fileBody)) {
                            continue;
                        }
                        ZipEntry zipEntry = ZipUtils.safeEntry(fileName);
                        if (zipEntry == null) {
                            // 模板渲染出的文件名非法（含路径穿越等），跳过避免 Zip Slip
                            continue;
                        }
                        zipOutputStream.putNextEntry(zipEntry);
                        zipOutputStream.write(fileBody.getBytes(StandardCharsets.UTF_8));
                        zipOutputStream.closeEntry();
                    }
//                }
                }
                zipOutputStream.flush();
                zipOutputStream.finish();
            }
        }
    }

}
