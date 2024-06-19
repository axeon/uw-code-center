package uw.code.center.controller.ops.template;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import uw.app.common.dto.SysDataHistoryQueryParam;
import uw.app.common.entity.SysDataHistory;
import uw.app.common.helper.SysDataHistoryHelper;
import uw.auth.service.AuthServiceHelper;
import uw.auth.service.annotation.MscPermDeclare;
import uw.auth.service.constant.ActionLog;
import uw.auth.service.constant.AuthType;
import uw.auth.service.constant.UserType;
import uw.code.center.dto.CodeTemplateInfoQueryParam;
import uw.code.center.entity.CodeTemplateInfo;
import uw.code.center.template.TemplateHelper;
import uw.common.constant.StateCommon;
import uw.common.dto.ResponseData;
import uw.dao.DaoFactory;
import uw.dao.DataList;
import uw.dao.TransactionException;

import java.util.Date;


/**
 * 代码模版管理。
 */
@RestController
@RequestMapping("/code/template/info")
@Tag(name = "代码模版管理", description = "代码模版增删改查列管理")
@MscPermDeclare(type = UserType.OPS)
public class CodeTemplateInfoController {

    DaoFactory dao = DaoFactory.getInstance();

    /**
     * 列表代码模版。
     *
     * @param queryParam
     * @return
     * @throws TransactionException
     */
    @GetMapping("/list")
    @Operation(summary = "列表代码模版", description = "列表代码模版")
    @MscPermDeclare(type = UserType.OPS, auth = AuthType.PERM, log = ActionLog.REQUEST)
    public DataList<CodeTemplateInfo> list(CodeTemplateInfoQueryParam queryParam) throws TransactionException {
        AuthServiceHelper.log( CodeTemplateInfo.class, "列表代码模版" );
        return dao.list( CodeTemplateInfo.class, queryParam );

    }

    /**
     * 加载代码模版。
     *
     * @param id
     * @throws TransactionException
     */
    @GetMapping("/load")
    @Operation(summary = "加载代码模版", description = "加载代码模版")
    @MscPermDeclare(type = UserType.OPS, auth = AuthType.PERM, log = ActionLog.REQUEST)
    public CodeTemplateInfo load(@Parameter(description = "主键ID", required = true, example = "1") @RequestParam long id) throws TransactionException {
        AuthServiceHelper.log( CodeTemplateInfo.class, id, "加载代码模版" );
        return dao.load( CodeTemplateInfo.class, id );
    }

    /**
     * 列表代码模版历史。
     *
     * @param
     * @return
     */
    @GetMapping("/history")
    @Operation(summary = "代码模版修改历史", description = "代码模版修改历史")
    @MscPermDeclare(type = UserType.OPS, auth = AuthType.PERM, log = ActionLog.REQUEST)
    public DataList<SysDataHistory> history(SysDataHistoryQueryParam queryParam) throws TransactionException {
        AuthServiceHelper.log( CodeTemplateInfo.class, queryParam.getEntityId(), "列表代码模版的历史" );
        queryParam.setEntityClass( CodeTemplateInfo.class );
        return SysDataHistoryHelper.listHistory( queryParam );
    }

    /**
     * 新增代码模版。
     *
     * @param codeTemplateInfo
     * @return
     * @throws TransactionException
     */
    @PostMapping("/save")
    @Operation(summary = "新增代码模版", description = "新增代码模版")
    @MscPermDeclare(type = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    public ResponseData<CodeTemplateInfo> save(@RequestBody CodeTemplateInfo codeTemplateInfo) throws TransactionException {
        long id = dao.getSequenceId( CodeTemplateInfo.class );
        AuthServiceHelper.log( CodeTemplateInfo.class, id, "新增代码模版" );
        codeTemplateInfo.setId( id );
        codeTemplateInfo.setCreateDate( new Date() );
        codeTemplateInfo.setModifyDate( null );
        codeTemplateInfo.setState( 1 );
        dao.save( codeTemplateInfo );
        TemplateHelper.init();
        //保存历史记录
        SysDataHistoryHelper.saveHistory( codeTemplateInfo.getId(), codeTemplateInfo, "代码模版", "新增代码模版" );
        return ResponseData.success( codeTemplateInfo );
    }

    /**
     * 修改代码模版。
     *
     * @param codeTemplateInfo
     * @return
     * @throws TransactionException
     */
    @PutMapping("/update")
    @Operation(summary = "修改代码模版", description = "修改代码模版")
    @MscPermDeclare(type = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    public ResponseData<CodeTemplateInfo> update(@RequestBody CodeTemplateInfo codeTemplateInfo, @Parameter(name = "remark", description = "备注") @RequestParam String remark) throws TransactionException {
        AuthServiceHelper.log( CodeTemplateInfo.class, codeTemplateInfo.getId(), "修改代码模版！操作备注：" + remark );
        CodeTemplateInfo codeTemplateInfoDb = dao.load( CodeTemplateInfo.class, codeTemplateInfo.getId() );
        if (codeTemplateInfoDb == null) {
            return ResponseData.warnMsg( "未找到指定ID的代码模版！" );
        }
        codeTemplateInfoDb.setGroupId( codeTemplateInfo.getGroupId() );
        codeTemplateInfoDb.setTemplateType( codeTemplateInfo.getTemplateType() );
        codeTemplateInfoDb.setTemplateName( codeTemplateInfo.getTemplateName() );
        codeTemplateInfoDb.setTemplateDesc( codeTemplateInfo.getTemplateDesc() );
        codeTemplateInfoDb.setTemplateFilename( codeTemplateInfo.getTemplateFilename() );
        codeTemplateInfoDb.setTemplateBody( codeTemplateInfo.getTemplateBody() );
        codeTemplateInfoDb.setModifyDate( new Date() );
        dao.update( codeTemplateInfoDb );
        TemplateHelper.init();
        SysDataHistoryHelper.saveHistory( codeTemplateInfoDb.getId(), codeTemplateInfoDb, "代码模版", "修改代码模版！操作备注：" + remark );
        return ResponseData.success( codeTemplateInfoDb );
    }

    /**
     * 启用代码模版。
     *
     * @param id
     * @throws TransactionException
     */
    @PatchMapping("/enable")
    @Operation(summary = "启用代码模版", description = "启用代码模版")
    @MscPermDeclare(type = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    public ResponseData enable(@Parameter(name = "id", description = "主键ID", example = "1") @RequestParam long id,
                               @Parameter(name = "remark", description = "备注") @RequestParam String remark) throws TransactionException {
        AuthServiceHelper.log( CodeTemplateInfo.class, id, "启用代码模版！操作备注：" + remark );
        CodeTemplateInfo codeTemplateInfo = dao.load( CodeTemplateInfo.class, id );
        if (codeTemplateInfo != null) {
            if (codeTemplateInfo.getState() != StateCommon.DISABLED.getValue()) {
                return ResponseData.warnMsg( "启用代码模版失败！当前状态不是禁用状态！" );
            }
            codeTemplateInfo.setModifyDate( new Date() );
            codeTemplateInfo.setState( StateCommon.ENABLED.getValue() );
            dao.update( codeTemplateInfo );
            return ResponseData.successMsg( "启用代码模版成功！" );
        } else {
            return ResponseData.warnMsg( "未找到指定id的代码模版！" );
        }
    }

    /**
     * 禁用代码模版。
     *
     * @param id
     * @throws TransactionException
     */
    @PatchMapping("/disable")
    @Operation(summary = "禁用代码模版", description = "禁用代码模版")
    @MscPermDeclare(type = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    public ResponseData disable(@Parameter(name = "id", description = "主键ID", example = "1") @RequestParam long id,
                                @Parameter(name = "remark", description = "备注") @RequestParam String remark) throws TransactionException {
        AuthServiceHelper.log( CodeTemplateInfo.class, id, "禁用代码模版！操作备注：" + remark );
        CodeTemplateInfo codeTemplateInfo = dao.load( CodeTemplateInfo.class, id );
        if (codeTemplateInfo != null) {
            if (codeTemplateInfo.getState() != StateCommon.ENABLED.getValue()) {
                return ResponseData.warnMsg( "禁用代码模版失败！当前状态不是启用状态！" );
            }
            codeTemplateInfo.setModifyDate( new Date() );
            codeTemplateInfo.setState( StateCommon.DISABLED.getValue() );
            dao.update( codeTemplateInfo );
            return ResponseData.successMsg( "禁用代码模版成功！" );
        } else {
            return ResponseData.warnMsg( "未找到指定id的代码模版！" );
        }
    }

    /**
     * 删除代码模版。
     *
     * @param id
     * @throws TransactionException
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除代码模版", description = "删除代码模版")
    @MscPermDeclare(type = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    public ResponseData delete(@Parameter(name = "id", description = "主键ID", example = "1") @RequestParam long id,
                               @Parameter(name = "remark", description = "备注") @RequestParam String remark) throws TransactionException {
        AuthServiceHelper.log( CodeTemplateInfo.class, id, "删除代码模版！操作备注：" + remark );
        CodeTemplateInfo codeTemplateInfo = dao.load( CodeTemplateInfo.class, id );
        if (codeTemplateInfo != null) {
            if (codeTemplateInfo.getState() != StateCommon.DISABLED.getValue()) {
                return ResponseData.warnMsg( "删除代码模版失败！当前状态不是禁用状态！" );
            }
            codeTemplateInfo.setModifyDate( new Date() );
            codeTemplateInfo.setState( StateCommon.DELETED.getValue() );
            dao.update( codeTemplateInfo );
            return ResponseData.successMsg( "删除代码模版成功！" );
        } else {
            return ResponseData.warnMsg( "未找到指定id的代码模版！" );
        }
    }

}