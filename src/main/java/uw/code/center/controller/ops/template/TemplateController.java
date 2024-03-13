package uw.code.center.controller.ops.template;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import uw.auth.service.AuthServiceHelper;
import uw.auth.service.annotation.MscPermDeclare;
import uw.auth.service.constant.ActionLog;
import uw.auth.service.constant.AuthType;
import uw.auth.service.constant.UserType;
import uw.auth.service.dto.ResponseData;
import uw.code.center.constant.TemplateType;
import uw.code.center.dto.CodeTemplateQueryParam;
import uw.code.center.entity.CodeTemplate;
import uw.code.center.service.CodeDataHistoryHelper;
import uw.code.center.template.TemplateHelper;
import uw.dao.DaoFactory;
import uw.dao.DataList;
import uw.dao.TransactionException;

import java.util.Date;


/**
 * 用于对模板进行增删改查列管理。
 */
@RestController
@Tag(name = "模板管理", description = "用于对模板进行增删改查列管理")
@RequestMapping("/ops/template/template")
@MscPermDeclare(type = UserType.OPS)
public class TemplateController {

    DaoFactory dao = DaoFactory.getInstance();

    /**
     * 列表模板记录。
     *
     * @param queryParam
     * @return
     * @throws TransactionException
     */
    @Operation(summary = "列表模板记录", description = "列表模板记录", operationId = "list")
    @GetMapping("/list")
    @MscPermDeclare(type = UserType.OPS, log = ActionLog.REQUEST)
    public DataList<CodeTemplate> list(CodeTemplateQueryParam queryParam) throws TransactionException {
        queryParam.setState( 1 );
        return dao.list( CodeTemplate.class, queryParam );
    }

    /**
     * 模板类型
     *
     * @return
     */
    @GetMapping("/type")
    @MscPermDeclare(type = UserType.OPS, auth = AuthType.USER, log = ActionLog.REQUEST)
    @Operation(summary = "模板类型", description = "模板类型")
    public TemplateType[] type() {
        return TemplateType.values();
    }


    /**
     * 加载模板记录。
     *
     * @param id
     * @throws TransactionException
     */
    @Operation(summary = "加载模板记录", description = "加载模板记录")
    @GetMapping("/load")
    @MscPermDeclare(type = UserType.OPS, log = ActionLog.REQUEST)
    public CodeTemplate load(@Parameter(description = "主键ID", example = "1") @RequestParam long id) throws TransactionException {
        return dao.load( CodeTemplate.class, id );
    }

    /**
     * 保存模板记录。
     *
     * @param codeTemplate
     * @return
     * @throws TransactionException
     */
    @Operation(summary = "保存模板记录", description = "保存模板记录")
    @PostMapping("/save")
    @MscPermDeclare(type = UserType.OPS, log = ActionLog.ALL)
    public ResponseData<CodeTemplate> save(@RequestBody CodeTemplate codeTemplate) throws TransactionException {
        long id = dao.getSequenceId( CodeTemplate.class );
        AuthServiceHelper.logInfo( CodeTemplate.class, id, "保存模板记录" );
        codeTemplate.setId( id );
        codeTemplate.setCreateDate( new Date() );
        codeTemplate.setModifyDate( null );
        codeTemplate.setState( 1 );
        dao.save( codeTemplate );
        TemplateHelper.init();
        //保存历史记录
        CodeDataHistoryHelper.saveHistory( codeTemplate.getId(), codeTemplate );
        return ResponseData.success( codeTemplate );
    }

    /**
     * 修改模板记录。
     *
     * @param codeTemplate
     * @return
     * @throws TransactionException
     */
    @Operation(summary = "修改模板记录", description = "修改模板记录", operationId = "update")
    @PutMapping("/update")
    @MscPermDeclare(type = UserType.OPS, log = ActionLog.ALL)
    public ResponseData<CodeTemplate> update(@RequestBody CodeTemplate codeTemplate, @Parameter(name = "remark", description = "备注") @RequestParam String remark) throws TransactionException {
        AuthServiceHelper.logInfo( CodeTemplate.class, codeTemplate.getId(), "修改代码模版!" + remark );
        CodeTemplate codeTemplateDb = dao.load( CodeTemplate.class, codeTemplate.getId() );
        if (codeTemplateDb == null) {
            return ResponseData.errorMsg( "未找到指定ID的数值！" );
        }
        codeTemplateDb.setGroupId( codeTemplate.getGroupId() );
        codeTemplateDb.setTemplateType( codeTemplate.getTemplateType() );
        codeTemplateDb.setTemplateName( codeTemplate.getTemplateName() );
        codeTemplateDb.setTemplateDesc( codeTemplate.getTemplateDesc() );
        codeTemplateDb.setTemplateFilename( codeTemplate.getTemplateFilename() );
        codeTemplateDb.setTemplateBody( codeTemplate.getTemplateBody() );
        //保存新记录。
        codeTemplateDb.setModifyDate( new Date() );
        dao.update( codeTemplateDb );
        TemplateHelper.init();
        CodeDataHistoryHelper.saveHistory( codeTemplateDb.getId(), codeTemplateDb );
        return ResponseData.success( codeTemplateDb );
    }

    /**
     * 启用代码模版。
     *
     * @param id
     * @throws TransactionException
     */
    @MscPermDeclare(type = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    @Operation(summary = "启用代码模版", description = "启用代码模版")
    @PutMapping("/enable")
    public ResponseData enable(@Parameter(name = "id", description = "主键ID", example = "1") @RequestParam long id,
                               @Parameter(name = "remark", description = "备注") @RequestParam String remark) throws TransactionException {
        CodeTemplate codeTemplate = dao.load( CodeTemplate.class, id );
        if (codeTemplate != null) {
            codeTemplate.setModifyDate( new Date() );
            codeTemplate.setState( 1 );
            dao.update( codeTemplate );
            TemplateHelper.init();
            AuthServiceHelper.logInfo( CodeTemplate.class, id, "启用代码模版成功！" + remark );
            return ResponseData.successMsg( "启用代码模版成功！" + remark );
        } else {
            AuthServiceHelper.logInfo( CodeTemplate.class, id, "启用代码模版失败！" + remark );
            return ResponseData.errorMsg( "启用代码模版失败！" + remark );
        }
    }

    /**
     * 禁用代码模版。
     *
     * @param id
     * @throws TransactionException
     */
    @MscPermDeclare(type = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    @Operation(summary = "禁用代码模版", description = "禁用代码模版")
    @PutMapping("/disable")
    public ResponseData disable(@Parameter(name = "id", description = "主键ID", example = "1") @RequestParam long id,
                                @Parameter(name = "remark", description = "备注") @RequestParam String remark) throws TransactionException {
        CodeTemplate codeTemplate = dao.load( CodeTemplate.class, id );
        if (codeTemplate != null) {
            codeTemplate.setModifyDate( new Date() );
            codeTemplate.setState( 0 );
            dao.update( codeTemplate );
            TemplateHelper.init();
            AuthServiceHelper.logInfo( CodeTemplate.class, id, "禁用代码模版成功！" + remark );
            return ResponseData.successMsg( "禁用代码模版成功！" + remark );
        } else {
            AuthServiceHelper.logInfo( CodeTemplate.class, id, "禁用代码模版失败！" + remark );
            return ResponseData.errorMsg( "禁用代码模版失败！" + remark );
        }
    }

    /**
     * 删除代码模版。
     *
     * @param id
     * @throws TransactionException
     */
    @MscPermDeclare(type = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    @Operation(summary = "删除代码模版", description = "删除代码模版")
    @DeleteMapping("/delete")
    public ResponseData delete(@Parameter(name = "id", description = "主键ID", example = "1") @RequestParam long id,
                               @Parameter(name = "remark", description = "备注") @RequestParam String remark) throws TransactionException {
        CodeTemplate codeTemplate = dao.load( CodeTemplate.class, id );
        if (codeTemplate != null) {
            codeTemplate.setModifyDate( new Date() );
            codeTemplate.setState( -1 );
            dao.update( codeTemplate );
            TemplateHelper.init();
            AuthServiceHelper.logInfo( CodeTemplate.class, id, "删除代码模版成功！" + remark );
            return ResponseData.successMsg( "删除代码模版成功！" + remark );
        } else {
            AuthServiceHelper.logInfo( CodeTemplate.class, id, "删除代码模版失败！" + remark );
            return ResponseData.errorMsg( "删除代码模版失败！" + remark );
        }
    }


}
