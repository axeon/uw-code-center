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
import uw.code.center.dto.CodeTemplateGroupQueryParam;
import uw.code.center.entity.CodeTemplateGroup;
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
import uw.dao.TransactionException;


/**
 * 代码模版组管理。
 */
@RestController
@RequestMapping("/ops/template/group")
@Tag(name = "代码模版组管理", description = "代码模版组增删改查列管理")
@MscPermDeclare(user = UserType.OPS)
public class CodeTemplateGroupController {

    private final DaoManager dao = DaoManager.getInstance();

    /**
     * 列表代码模版组。
     *
     * @param queryParam
     * @return
     * @throws TransactionException
     */
    @GetMapping("/list")
    @Operation(summary = "列表代码模版组", description = "列表代码模版组")
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.REQUEST)
    public ResponseData<DataList<CodeTemplateGroup>> list(CodeTemplateGroupQueryParam queryParam){
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
    @Operation(summary = "模板组select列表", description = "模板组select列表")
    @GetMapping("/liteList")
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.USER, log = ActionLog.REQUEST)
    public ResponseData<DataList<CodeTemplateGroup>> liteList(@Parameter(description = "模板组类型") @RequestParam int groupType){
        if (groupType > 0) {
            return dao.list( CodeTemplateGroup.class, "select id,group_name,group_type from code_template_group where group_type=? and state=1", new Object[]{groupType} );
        } else {
            return dao.list( CodeTemplateGroup.class, "select id,group_name,group_type from code_template_group where state=1" );
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
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.REQUEST)
    public ResponseData<CodeTemplateGroup> load(@Parameter(description = "主键ID", required = true) @RequestParam long id){
        AuthServiceHelper.logInfo( CodeTemplateGroup.class, id, "加载代码模版组" );
        return dao.load( CodeTemplateGroup.class, id );
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
    public ResponseData<DataList<SysDataHistory>> listDataHistory(SysDataHistoryQueryParam queryParam){
        AuthServiceHelper.logRef(CodeTemplateGroup.class, queryParam.getEntityId());
        queryParam.setEntityClass(CodeTemplateGroup.class);
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
    public ResponseData<DataList<SysCritLog>> listCritLog(SysCritLogQueryParam queryParam)  {
        AuthServiceHelper.logRef(CodeTemplateGroup.class, queryParam.getBizId());
        queryParam.setBizTypeClass(CodeTemplateGroup.class);
        return dao.list(SysCritLog.class, queryParam);
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
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    public ResponseData<CodeTemplateGroup> save(@RequestBody CodeTemplateGroup codeTemplateGroup){
        long id = dao.getSequenceId( CodeTemplateGroup.class );
        AuthServiceHelper.logInfo( CodeTemplateGroup.class, id, "新增代码模版组" );
        codeTemplateGroup.setId( id );
        codeTemplateGroup.setCreateDate( SystemClock.nowDate() );
        codeTemplateGroup.setModifyDate( null );
        codeTemplateGroup.setState( CommonState.ENABLED.getValue() );
        //保存历史记录
        return dao.save( codeTemplateGroup ).onSuccess(savedEntity -> {
            SysDataHistoryHelper.saveHistory(codeTemplateGroup);
        });
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
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    public ResponseData<CodeTemplateGroup> update(@RequestBody CodeTemplateGroup codeTemplateGroup, @Parameter( description = "备注") @RequestParam String remark){
        AuthServiceHelper.logInfo(CodeTemplateGroup.class,codeTemplateGroup.getId(),remark);
        return  dao.load( CodeTemplateGroup.class, codeTemplateGroup.getId() ).onSuccess(codeTemplateGroupDb-> {
            codeTemplateGroupDb.setGroupType(codeTemplateGroup.getGroupType());
            codeTemplateGroupDb.setGroupName(codeTemplateGroup.getGroupName());
            codeTemplateGroupDb.setGroupDesc(codeTemplateGroup.getGroupDesc());
            codeTemplateGroupDb.setModifyDate(SystemClock.nowDate());
            return dao.update( codeTemplateGroupDb ).onSuccess(updatedEntity -> {
                SysDataHistoryHelper.saveHistory( codeTemplateGroupDb,remark );
            } );
        } );
    }

    /**
     * 启用代码模版组。
     *
     * @param id
     *
     */
    @PutMapping("/enable")
    @Operation(summary = "启用代码模版组", description = "启用代码模版组")
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    public ResponseData enable(@Parameter(description = "主键ID") @RequestParam long id, @Parameter(description = "备注") @RequestParam String remark){
        AuthServiceHelper.logInfo(CodeTemplateGroup.class,id,remark);
        return dao.update(new CodeTemplateGroup().modifyDate(SystemClock.nowDate()).state(CommonState.ENABLED.getValue()), new IdStateQueryParam(id, CommonState.DISABLED.getValue()));
    }

    /**
     * 禁用代码模版组。
     *
     * @param id
     *
     */
    @PutMapping("/disable")
    @Operation(summary = "禁用代码模版组", description = "禁用代码模版组")
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    public ResponseData disable(@Parameter(description = "主键ID") @RequestParam long id, @Parameter(description = "备注") @RequestParam String remark){
        AuthServiceHelper.logInfo(CodeTemplateGroup.class,id,remark);
        return dao.update(new CodeTemplateGroup().modifyDate(SystemClock.nowDate()).state(CommonState.DISABLED.getValue()), new IdStateQueryParam(id, CommonState.ENABLED.getValue()));
    }

    /**
     * 删除代码模版组。
     *
     * @param id
     *
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除代码模版组", description = "删除代码模版组")
    @MscPermDeclare(user = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    public ResponseData delete(@Parameter(description = "主键ID") @RequestParam long id, @Parameter(description = "备注") @RequestParam String remark){
        AuthServiceHelper.logInfo(CodeTemplateGroup.class,id,remark);
        return dao.update(new CodeTemplateGroup().modifyDate(SystemClock.nowDate()).state(CommonState.DELETED.getValue()), new IdStateQueryParam(id, CommonState.DISABLED.getValue()));
    }

}