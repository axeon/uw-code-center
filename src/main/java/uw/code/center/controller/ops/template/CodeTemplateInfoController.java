package uw.code.center.controller.ops.template;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import uw.app.common.dto.SysCritLogQueryParam;
import uw.app.common.dto.SysDataHistoryQueryParam;
import uw.app.common.entity.SysCritLog;
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
import uw.app.common.constant.CommonState;
import uw.common.dto.ResponseData;
import uw.dao.DaoFactory;
import uw.dao.DataList;
import uw.dao.TransactionException;

import java.util.Date;


/**
 * 代码模版管理。
 */
@RestController
@RequestMapping("/ops/template/info")
@Tag(name = "代码模版管理", description = "代码模版增删改查列管理")
@MscPermDeclare(user = UserType.OPS)
public class CodeTemplateInfoController {

    private final DaoFactory dao = DaoFactory.getInstance();

    /**
     * 列表代码模版。
     *
     * @param queryParam
     * @return
     * @throws TransactionException
     */
    @GetMapping("/list")
    @Operation(summary = "列表代码模版", description = "列表代码模版")
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.REQUEST)
    public DataList<CodeTemplateInfo> list(CodeTemplateInfoQueryParam queryParam) throws TransactionException {
        AuthServiceHelper.logInfo( CodeTemplateInfo.class, "列表代码模版" );
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
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.REQUEST)
    public CodeTemplateInfo load(@Parameter(description = "主键ID", required = true) @RequestParam long id) throws TransactionException {
        AuthServiceHelper.logInfo( CodeTemplateInfo.class, id, "加载代码模版" );
        return dao.load( CodeTemplateInfo.class, id );
    }

    /**
     * 查询数据历史。
     *
     * @param
     * @return
     */
    @GetMapping("/listDataHistory")
    @Operation(summary = "查询数据历史", description = "查询数据历史")
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.REQUEST)
    public DataList<SysDataHistory> listDataHistory(SysDataHistoryQueryParam queryParam) throws TransactionException {
        AuthServiceHelper.logRef( CodeTemplateInfo.class, queryParam.getEntityId());
        queryParam.setEntityClass(CodeTemplateInfo.class);
        return dao.list(SysDataHistory.class, queryParam);
    }

    /**
     * 查询操作日志。
     *
     * @param
     * @return
     */
    @GetMapping("/listCritLog")
    @Operation(summary = "查询操作日志", description = "查询操作日志")
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.REQUEST)
    public DataList<SysCritLog> listCritLog(SysCritLogQueryParam queryParam) throws TransactionException {
        AuthServiceHelper.logRef(CodeTemplateInfo.class, queryParam.getRefId());
        queryParam.setRefTypeClass(CodeTemplateInfo.class);
        return dao.list(SysCritLog.class, queryParam);
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
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    public ResponseData<CodeTemplateInfo> save(@RequestBody CodeTemplateInfo codeTemplateInfo) throws TransactionException {
        long id = dao.getSequenceId( CodeTemplateInfo.class );
        AuthServiceHelper.logInfo( CodeTemplateInfo.class, id, "新增代码模版" );
        codeTemplateInfo.setId( id );
        codeTemplateInfo.setCreateDate( new Date() );
        codeTemplateInfo.setModifyDate( null );
        codeTemplateInfo.setState( CommonState.ENABLED.getValue() );
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
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    public ResponseData<CodeTemplateInfo> update(@RequestBody CodeTemplateInfo codeTemplateInfo, @Parameter( description = "备注") @RequestParam String remark) throws TransactionException {
        AuthServiceHelper.logInfo( CodeTemplateInfo.class, codeTemplateInfo.getId(), "修改代码模版！操作备注：" + remark );
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
    @PutMapping("/enable")
    @Operation(summary = "启用代码模版", description = "启用代码模版")
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    public ResponseData enable(@Parameter(description = "主键ID") @RequestParam long id, @Parameter( description = "备注") @RequestParam String remark) throws TransactionException {
        AuthServiceHelper.logInfo( CodeTemplateInfo.class, id, "启用代码模版！操作备注：" + remark );
        CodeTemplateInfo codeTemplateInfo = dao.load( CodeTemplateInfo.class, id );
        if (codeTemplateInfo == null) {
            return ResponseData.warnMsg( "未找到指定id的代码模版！" );
        }
        if (codeTemplateInfo.getState() != CommonState.DISABLED.getValue()) {
            return ResponseData.warnMsg( "启用代码模版失败！当前状态不是禁用状态！" );
        }
        codeTemplateInfo.setModifyDate( new Date() );
        codeTemplateInfo.setState( CommonState.ENABLED.getValue() );
        dao.update( codeTemplateInfo );
        return ResponseData.success();
    }

    /**
     * 禁用代码模版。
     *
     * @param id
     * @throws TransactionException
     */
    @PutMapping("/disable")
    @Operation(summary = "禁用代码模版", description = "禁用代码模版")
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    public ResponseData disable(@Parameter(description = "主键ID") @RequestParam long id, @Parameter( description = "备注") @RequestParam String remark) throws TransactionException {
        AuthServiceHelper.logInfo( CodeTemplateInfo.class, id, "禁用代码模版！操作备注：" + remark );
        CodeTemplateInfo codeTemplateInfo = dao.load( CodeTemplateInfo.class, id );
        if (codeTemplateInfo == null) {
            return ResponseData.warnMsg( "未找到指定id的代码模版！" );
        }
        if (codeTemplateInfo.getState() != CommonState.ENABLED.getValue()) {
            return ResponseData.warnMsg( "禁用代码模版失败！当前状态不是启用状态！" );
        }
        codeTemplateInfo.setModifyDate( new Date() );
        codeTemplateInfo.setState( CommonState.DISABLED.getValue() );
        dao.update( codeTemplateInfo );
        return ResponseData.success();
    }

    /**
     * 删除代码模版。
     *
     * @param id
     * @throws TransactionException
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除代码模版", description = "删除代码模版")
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    public ResponseData delete(@Parameter(description = "主键ID") @RequestParam long id, @Parameter( description = "备注") @RequestParam String remark) throws TransactionException {
        AuthServiceHelper.logInfo( CodeTemplateInfo.class, id, "删除代码模版！操作备注：" + remark );
        CodeTemplateInfo codeTemplateInfo = dao.load( CodeTemplateInfo.class, id );
        if (codeTemplateInfo == null) {
            return ResponseData.warnMsg( "未找到指定id的代码模版！" );
        }
        if (codeTemplateInfo.getState() != CommonState.DISABLED.getValue()) {
            return ResponseData.warnMsg( "删除代码模版失败！当前状态不是禁用状态！" );
        }
        codeTemplateInfo.setModifyDate( new Date() );
        codeTemplateInfo.setState( CommonState.DELETED.getValue() );
        dao.update( codeTemplateInfo );
        return ResponseData.success();
    }
}