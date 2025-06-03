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
import uw.code.center.dto.CodeTemplateInfoQueryParam;
import uw.code.center.entity.CodeTemplateInfo;
import uw.code.center.template.TemplateHelper;
import uw.common.app.constant.CommonState;
import uw.common.app.dto.IdStateQueryParam;
import uw.common.app.dto.SysCritLogQueryParam;
import uw.common.app.dto.SysDataHistoryQueryParam;
import uw.common.app.entity.SysCritLog;
import uw.common.app.entity.SysDataHistory;
import uw.common.app.helper.SysDataHistoryHelper;
import uw.common.dto.ResponseData;
import uw.common.util.SystemClock;
import uw.dao.DaoManager;
import uw.dao.DataList;


/**
 * 代码模版管理。
 */
@RestController
@RequestMapping("/ops/template/info")
@Tag(name = "代码模版管理", description = "代码模版增删改查列管理")
@MscPermDeclare(user = UserType.OPS)
public class CodeTemplateInfoController {

    private final DaoManager dao = DaoManager.getInstance();

    /**
     * 列表代码模版。
     *
     * @param queryParam
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "列表代码模版", description = "列表代码模版")
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.REQUEST)
    public ResponseData<DataList<CodeTemplateInfo>> list(CodeTemplateInfoQueryParam queryParam) {
        AuthServiceHelper.logRef(CodeTemplateInfo.class);
        return dao.list(CodeTemplateInfo.class, queryParam);
    }

    /**
     * 轻量级列表代码模版，一般用于select控件。
     *
     * @return
     */
    @GetMapping("/liteList")
    @Operation(summary = "轻量级列表代码模版", description = "轻量级列表代码模版，一般用于select控件。")
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.USER, log = ActionLog.NONE)
    public ResponseData<DataList<CodeTemplateInfo>> liteList(CodeTemplateInfoQueryParam queryParam) {
        queryParam.SELECT_SQL("SELECT id,group_id,template_type,template_name,template_desc,template_filename,create_date,modify_date,state from code_template_info ");
        return dao.list(CodeTemplateInfo.class, queryParam);
    }

    /**
     * 加载代码模版。
     *
     * @param id
     */
    @GetMapping("/load")
    @Operation(summary = "加载代码模版", description = "加载代码模版")
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.REQUEST)
    public ResponseData<CodeTemplateInfo> load(@Parameter(description = "主键ID", required = true) @RequestParam long id) {
        AuthServiceHelper.logRef(CodeTemplateInfo.class, id);
        return dao.load(CodeTemplateInfo.class, id);
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
    public ResponseData<DataList<SysDataHistory>> listDataHistory(SysDataHistoryQueryParam queryParam) {
        AuthServiceHelper.logRef(CodeTemplateInfo.class, queryParam.getEntityId());
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
    public ResponseData<DataList<SysCritLog>> listCritLog(SysCritLogQueryParam queryParam) {
        AuthServiceHelper.logRef(CodeTemplateInfo.class, queryParam.getBizId());
        queryParam.setBizTypeClass(CodeTemplateInfo.class);
        return dao.list(SysCritLog.class, queryParam);
    }

    /**
     * 新增代码模版。
     *
     * @param codeTemplateInfo
     * @return
     */
    @PostMapping("/save")
    @Operation(summary = "新增代码模版", description = "新增代码模版")
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    public ResponseData<CodeTemplateInfo> save(@RequestBody CodeTemplateInfo codeTemplateInfo) {
        long id = dao.getSequenceId(CodeTemplateInfo.class);
        AuthServiceHelper.logRef(CodeTemplateInfo.class, id);
        codeTemplateInfo.setId(id);
        codeTemplateInfo.setCreateDate(SystemClock.nowDate());
        codeTemplateInfo.setModifyDate(null);
        codeTemplateInfo.setState(CommonState.ENABLED.getValue());
        //保存历史记录
        return dao.save(codeTemplateInfo).onSuccess(savedEntity -> {
            SysDataHistoryHelper.saveHistory(codeTemplateInfo);
        });
    }

    /**
     * 修改代码模版。
     *
     * @param codeTemplateInfo
     * @return
     */
    @PutMapping("/update")
    @Operation(summary = "修改代码模版", description = "修改代码模版")
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    public ResponseData<CodeTemplateInfo> update(@RequestBody CodeTemplateInfo codeTemplateInfo, @Parameter(description = "备注") @RequestParam String remark) {
        AuthServiceHelper.logInfo(CodeTemplateInfo.class, codeTemplateInfo.getId(), remark);
        return dao.load(CodeTemplateInfo.class, codeTemplateInfo.getId()).onSuccess(codeTemplateInfoDb -> {
            codeTemplateInfoDb.setGroupId(codeTemplateInfo.getGroupId());
            codeTemplateInfoDb.setTemplateType(codeTemplateInfo.getTemplateType());
            codeTemplateInfoDb.setTemplateName(codeTemplateInfo.getTemplateName());
            codeTemplateInfoDb.setTemplateDesc(codeTemplateInfo.getTemplateDesc());
            codeTemplateInfoDb.setTemplateFilename(codeTemplateInfo.getTemplateFilename());
            codeTemplateInfoDb.setTemplateBody(codeTemplateInfo.getTemplateBody());
            codeTemplateInfoDb.setModifyDate(SystemClock.nowDate());
            return dao.update(codeTemplateInfoDb).onSuccess(updatedEntity -> {
                TemplateHelper.init();
                SysDataHistoryHelper.saveHistory(codeTemplateInfoDb, remark);
            });
        });
    }


    /**
     * 启用代码模版。
     *
     * @param id
     */
    @PutMapping("/enable")
    @Operation(summary = "启用代码模版", description = "启用代码模版")
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    public ResponseData enable(@Parameter(description = "主键ID") @RequestParam long id, @Parameter(description = "备注") @RequestParam String remark) {
        AuthServiceHelper.logInfo(CodeTemplateInfo.class, id, remark);
        return dao.update(new CodeTemplateInfo().modifyDate(SystemClock.nowDate()).state(CommonState.ENABLED.getValue()), new IdStateQueryParam(id, CommonState.DISABLED.getValue()));
    }

    /**
     * 禁用代码模版。
     *
     * @param id
     */
    @PutMapping("/disable")
    @Operation(summary = "禁用代码模版", description = "禁用代码模版")
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    public ResponseData disable(@Parameter(description = "主键ID") @RequestParam long id, @Parameter(description = "备注") @RequestParam String remark) {
        AuthServiceHelper.logInfo(CodeTemplateInfo.class, id, remark);
        return dao.update(new CodeTemplateInfo().modifyDate(SystemClock.nowDate()).state(CommonState.DISABLED.getValue()), new IdStateQueryParam(id, CommonState.ENABLED.getValue()));
    }

    /**
     * 删除代码模版。
     *
     * @param id
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除代码模版", description = "删除代码模版")
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    public ResponseData delete(@Parameter(description = "主键ID") @RequestParam long id, @Parameter(description = "备注") @RequestParam String remark) {
        AuthServiceHelper.logInfo(CodeTemplateInfo.class, id, remark);
        return dao.update(new CodeTemplateInfo().modifyDate(SystemClock.nowDate()).state(CommonState.DELETED.getValue()), new IdStateQueryParam(id, CommonState.DISABLED.getValue()));
    }

}