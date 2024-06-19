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
import uw.code.center.dto.CodeTemplateGroupQueryParam;
import uw.code.center.entity.CodeTemplateGroup;
import uw.common.constant.StateCommon;
import uw.common.dto.ResponseData;
import uw.dao.DaoFactory;
import uw.dao.DataList;
import uw.dao.TransactionException;

import java.util.Date;
import java.util.List;


/**
 * 代码模版组管理。
 */
@RestController
@RequestMapping("/ops/template/group")
@Tag(name = "代码模版组管理", description = "代码模版组增删改查列管理")
@MscPermDeclare(type = UserType.OPS)
public class CodeTemplateGroupController {

    DaoFactory dao = DaoFactory.getInstance();

    /**
     * 列表代码模版组。
     *
     * @param queryParam
     * @return
     * @throws TransactionException
     */
    @GetMapping("/list")
    @Operation(summary = "列表代码模版组", description = "列表代码模版组")
    @MscPermDeclare(type = UserType.OPS, auth = AuthType.PERM, log = ActionLog.REQUEST)
    public DataList<CodeTemplateGroup> list(CodeTemplateGroupQueryParam queryParam) throws TransactionException {
        AuthServiceHelper.logInfo( CodeTemplateGroup.class, "列表代码模版组" );
        return dao.list( CodeTemplateGroup.class, queryParam );
    }

    /**
     * 模板组select列表。
     *
     * @param groupType
     * @return
     * @throws TransactionException
     */
    @Operation(summary = "模板组select列表", description = "模板组select列表", operationId = "listForSelect")
    @GetMapping("/listForSelect")
    @MscPermDeclare(type = UserType.OPS, auth = AuthType.USER, log = ActionLog.REQUEST)
    public List<CodeTemplateGroup> listForSelect(@Parameter(description = "模板组类型", example = "1") @RequestParam int groupType) throws TransactionException {
        if (groupType > 0) {
            return dao.list( CodeTemplateGroup.class, "select id,group_name,group_type from code_template_group where group_type=? and state=1", new Object[]{groupType} ).results();
        } else {
            return dao.list( CodeTemplateGroup.class, "select id,group_name,group_type from code_template_group where state=1" ).results();
        }
    }

    /**
     * 加载代码模版组。
     *
     * @param id
     * @throws TransactionException
     */
    @GetMapping("/load")
    @Operation(summary = "加载代码模版组", description = "加载代码模版组")
    @MscPermDeclare(type = UserType.OPS, auth = AuthType.PERM, log = ActionLog.REQUEST)
    public CodeTemplateGroup load(@Parameter(description = "主键ID", required = true, example = "1") @RequestParam long id) throws TransactionException {
        AuthServiceHelper.logInfo( CodeTemplateGroup.class, id, "加载代码模版组" );
        return dao.load( CodeTemplateGroup.class, id );
    }

    /**
     * 列表代码模版组历史。
     *
     * @param
     * @return
     */
    @GetMapping("/history")
    @Operation(summary = "代码模版组修改历史", description = "代码模版组修改历史")
    @MscPermDeclare(type = UserType.OPS, auth = AuthType.PERM, log = ActionLog.REQUEST)
    public DataList<SysDataHistory> history(SysDataHistoryQueryParam queryParam) throws TransactionException {
        AuthServiceHelper.logInfo( CodeTemplateGroup.class, queryParam.getEntityId(), "列表代码模版组的历史" );
        queryParam.setEntityClass( CodeTemplateGroup.class );
        return SysDataHistoryHelper.listHistory( queryParam );
    }

    /**
     * 新增代码模版组。
     *
     * @param codeTemplateGroup
     * @return
     * @throws TransactionException
     */
    @PostMapping("/save")
    @Operation(summary = "新增代码模版组", description = "新增代码模版组")
    @MscPermDeclare(type = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    public ResponseData<CodeTemplateGroup> save(@RequestBody CodeTemplateGroup codeTemplateGroup) throws TransactionException {
        long id = dao.getSequenceId( CodeTemplateGroup.class );
        AuthServiceHelper.logInfo( CodeTemplateGroup.class, id, "新增代码模版组" );
        codeTemplateGroup.setId( id );
        codeTemplateGroup.setCreateDate( new Date() );
        codeTemplateGroup.setModifyDate( null );
        codeTemplateGroup.setState( 1 );
        dao.save( codeTemplateGroup );
        //保存历史记录
        SysDataHistoryHelper.saveHistory( codeTemplateGroup.getId(), codeTemplateGroup, "代码模版组", "新增代码模版组" );
        return ResponseData.success( codeTemplateGroup );
    }

    /**
     * 修改代码模版组。
     *
     * @param codeTemplateGroup
     * @return
     * @throws TransactionException
     */
    @PutMapping("/update")
    @Operation(summary = "修改代码模版组", description = "修改代码模版组")
    @MscPermDeclare(type = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    public ResponseData<CodeTemplateGroup> update(@RequestBody CodeTemplateGroup codeTemplateGroup, @Parameter(name = "remark", description = "备注") @RequestParam String remark) throws TransactionException {
        AuthServiceHelper.logInfo( CodeTemplateGroup.class, codeTemplateGroup.getId(), "修改代码模版组！操作备注：" + remark );
        CodeTemplateGroup codeTemplateGroupDb = dao.load( CodeTemplateGroup.class, codeTemplateGroup.getId() );
        if (codeTemplateGroupDb == null) {
            return ResponseData.warnMsg( "未找到指定ID的代码模版组！" );
        }
        codeTemplateGroupDb.setGroupType( codeTemplateGroup.getGroupType() );
        codeTemplateGroupDb.setGroupName( codeTemplateGroup.getGroupName() );
        codeTemplateGroupDb.setGroupDesc( codeTemplateGroup.getGroupDesc() );
        codeTemplateGroupDb.setModifyDate( new Date() );
        dao.update( codeTemplateGroupDb );
        SysDataHistoryHelper.saveHistory( codeTemplateGroupDb.getId(), codeTemplateGroupDb, "代码模版组", "修改代码模版组！操作备注：" + remark );
        return ResponseData.success( codeTemplateGroupDb );
    }

    /**
     * 启用代码模版组。
     *
     * @param id
     * @throws TransactionException
     */
    @PatchMapping("/enable")
    @Operation(summary = "启用代码模版组", description = "启用代码模版组")
    @MscPermDeclare(type = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    public ResponseData enable(@Parameter(name = "id", description = "主键ID", example = "1") @RequestParam long id,
                               @Parameter(name = "remark", description = "备注") @RequestParam String remark) throws TransactionException {
        AuthServiceHelper.logInfo( CodeTemplateGroup.class, id, "启用代码模版组！操作备注：" + remark );
        CodeTemplateGroup codeTemplateGroup = dao.load( CodeTemplateGroup.class, id );
        if (codeTemplateGroup != null) {
            if (codeTemplateGroup.getState() != StateCommon.DISABLED.getValue()) {
                return ResponseData.warnMsg( "启用代码模版组失败！当前状态不是禁用状态！" );
            }
            codeTemplateGroup.setModifyDate( new Date() );
            codeTemplateGroup.setState( StateCommon.ENABLED.getValue() );
            dao.update( codeTemplateGroup );
            return ResponseData.successMsg( "启用代码模版组成功！" );
        } else {
            return ResponseData.warnMsg( "未找到指定id的代码模版组！" );
        }
    }

    /**
     * 禁用代码模版组。
     *
     * @param id
     * @throws TransactionException
     */
    @PatchMapping("/disable")
    @Operation(summary = "禁用代码模版组", description = "禁用代码模版组")
    @MscPermDeclare(type = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    public ResponseData disable(@Parameter(name = "id", description = "主键ID", example = "1") @RequestParam long id,
                                @Parameter(name = "remark", description = "备注") @RequestParam String remark) throws TransactionException {
        AuthServiceHelper.logInfo( CodeTemplateGroup.class, id, "禁用代码模版组！操作备注：" + remark );
        CodeTemplateGroup codeTemplateGroup = dao.load( CodeTemplateGroup.class, id );
        if (codeTemplateGroup != null) {
            if (codeTemplateGroup.getState() != StateCommon.ENABLED.getValue()) {
                return ResponseData.warnMsg( "禁用代码模版组失败！当前状态不是启用状态！" );
            }
            codeTemplateGroup.setModifyDate( new Date() );
            codeTemplateGroup.setState( StateCommon.DISABLED.getValue() );
            dao.update( codeTemplateGroup );
            return ResponseData.successMsg( "禁用代码模版组成功！" );
        } else {
            return ResponseData.warnMsg( "未找到指定id的代码模版组！" );
        }
    }

    /**
     * 删除代码模版组。
     *
     * @param id
     * @throws TransactionException
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除代码模版组", description = "删除代码模版组")
    @MscPermDeclare(type = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    public ResponseData delete(@Parameter(name = "id", description = "主键ID", example = "1") @RequestParam long id,
                               @Parameter(name = "remark", description = "备注") @RequestParam String remark) throws TransactionException {
        AuthServiceHelper.logInfo( CodeTemplateGroup.class, id, "删除代码模版组！操作备注：" + remark );
        CodeTemplateGroup codeTemplateGroup = dao.load( CodeTemplateGroup.class, id );
        if (codeTemplateGroup != null) {
            if (codeTemplateGroup.getState() != StateCommon.DISABLED.getValue()) {
                return ResponseData.warnMsg( "删除代码模版组失败！当前状态不是禁用状态！" );
            }
            codeTemplateGroup.setModifyDate( new Date() );
            codeTemplateGroup.setState( StateCommon.DELETED.getValue() );
            dao.update( codeTemplateGroup );
            return ResponseData.successMsg( "删除代码模版组成功！" );
        } else {
            return ResponseData.warnMsg( "未找到指定id的代码模版组！" );
        }
    }

}