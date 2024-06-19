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
import uw.code.center.dto.CodeTemplateQueryParam;
import uw.code.center.entity.CodeTemplate;
import uw.code.center.template.TemplateHelper;
import uw.common.constant.StateCommon;
import uw.common.dto.ResponseData;
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
    @MscPermDeclare(type = UserType.OPS,  auth = AuthType.PERM, log = ActionLog.REQUEST)
    public DataList<CodeTemplate> list(CodeTemplateQueryParam queryParam) throws TransactionException {
        AuthServiceHelper.log(CodeTemplate.class,"列表代码模版");
        return dao.list(CodeTemplate.class, queryParam);
    }

    /**
     * 加载模板记录。
     *
     * @param id
     * @throws TransactionException
     */
    @Operation(summary = "加载模板记录", description = "加载模板记录")
    @GetMapping("/load")
    @MscPermDeclare(type = UserType.OPS,  auth = AuthType.PERM, log = ActionLog.REQUEST)
    public CodeTemplate load(@Parameter(description = "主键ID", required = true, example = "1") @RequestParam long id) throws TransactionException {
        AuthServiceHelper.log(CodeTemplate.class,id,"加载代码模版");
        return dao.load(CodeTemplate.class, id);
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
        AuthServiceHelper.log(CodeTemplate.class, queryParam.getEntityId(),"列表代码模版的历史");
        queryParam.setEntityClass(CodeTemplate.class);
        return SysDataHistoryHelper.listHistory(queryParam);
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
    @MscPermDeclare(type = UserType.OPS,  auth = AuthType.PERM, log = ActionLog.CRIT)
    public ResponseData<CodeTemplate> save(@RequestBody CodeTemplate codeTemplate) throws TransactionException {
        long id = dao.getSequenceId(CodeTemplate.class);
        AuthServiceHelper.log(CodeTemplate.class,id,"新增代码模版");
        codeTemplate.setId(id);
        codeTemplate.setCreateDate(new Date());
        codeTemplate.setModifyDate(null);
        codeTemplate.setState(1);
        dao.save(codeTemplate);
        //保存历史记录
        SysDataHistoryHelper.saveHistory(codeTemplate.getId(),codeTemplate,"代码模版","新增代码模版");
        return ResponseData.success(codeTemplate);
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
    @MscPermDeclare(type = UserType.OPS,  auth = AuthType.PERM, log = ActionLog.CRIT)
    public ResponseData<CodeTemplate> update(@RequestBody CodeTemplate codeTemplate, @Parameter(name = "remark", description = "备注") @RequestParam String remark) throws TransactionException {
        AuthServiceHelper.log(CodeTemplate.class,codeTemplate.getId(),"修改代码模版！操作备注："+remark);
        CodeTemplate codeTemplateDb = dao.load(CodeTemplate.class, codeTemplate.getId());
        if (codeTemplateDb == null) {
            return ResponseData.warnMsg("未找到指定ID的代码模版！");
        }
        codeTemplateDb.setGroupId(codeTemplate.getGroupId());
        codeTemplateDb.setTemplateType(codeTemplate.getTemplateType());
        codeTemplateDb.setTemplateName(codeTemplate.getTemplateName());
        codeTemplateDb.setTemplateDesc(codeTemplate.getTemplateDesc());
        codeTemplateDb.setTemplateFilename(codeTemplate.getTemplateFilename());
        codeTemplateDb.setTemplateBody(codeTemplate.getTemplateBody());
        codeTemplateDb.setModifyDate(new Date());
        dao.update(codeTemplateDb);
        TemplateHelper.init();
        SysDataHistoryHelper.saveHistory(codeTemplateDb.getId(),codeTemplateDb,"代码模版","修改代码模版！操作备注："+remark);
        return ResponseData.success(codeTemplateDb);
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
    public ResponseData enable(@Parameter(name = "id", description = "主键ID", example = "1") @RequestParam long id, @Parameter(name = "remark", description = "备注") @RequestParam String remark) throws TransactionException {
        AuthServiceHelper.log(CodeTemplate.class,id,"启用代码模版！操作备注："+remark);
        CodeTemplate codeTemplate = dao.load(CodeTemplate.class, id);
        if (codeTemplate != null) {
            if (codeTemplate.getState()!= StateCommon.DISABLED.getValue()){
                return ResponseData.warnMsg("启用代码模版失败！当前状态不是禁用状态！");
            }
            codeTemplate.setModifyDate(new Date());
            codeTemplate.setState(StateCommon.ENABLED.getValue());
            dao.update(codeTemplate);
            return ResponseData.successMsg("启用代码模版成功！");
        }else{
            return ResponseData.warnMsg("未找到指定id的代码模版！");
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
    public ResponseData disable(@Parameter(name = "id", description = "主键ID", example = "1") @RequestParam long id, @Parameter(name = "remark", description = "备注") @RequestParam String remark) throws TransactionException {
        AuthServiceHelper.log(CodeTemplate.class,id,"禁用代码模版！操作备注："+remark);
        CodeTemplate codeTemplate = dao.load(CodeTemplate.class, id);
        if (codeTemplate != null) {
            if (codeTemplate.getState()!=StateCommon.ENABLED.getValue()){
                return ResponseData.warnMsg("禁用代码模版失败！当前状态不是启用状态！");
            }
            codeTemplate.setModifyDate(new Date());
            codeTemplate.setState(StateCommon.DISABLED.getValue());
            dao.update(codeTemplate);
            return ResponseData.successMsg("禁用代码模版成功！");
        }else{
            return ResponseData.warnMsg("未找到指定id的代码模版！");
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
    public ResponseData delete(@Parameter(name = "id", description = "主键ID", example = "1") @RequestParam long id, @Parameter(name = "remark", description = "备注") @RequestParam String remark) throws TransactionException {
        AuthServiceHelper.log(CodeTemplate.class,id,"删除代码模版！操作备注："+remark);
        CodeTemplate codeTemplate = dao.load(CodeTemplate.class, id);
        if (codeTemplate != null) {
            if (codeTemplate.getState()!=StateCommon.DISABLED.getValue()){
                return ResponseData.warnMsg("删除代码模版失败！当前状态不是禁用状态！");
            }
            codeTemplate.setModifyDate(new Date());
            codeTemplate.setState(StateCommon.DELETED.getValue());
            dao.update(codeTemplate);
            return ResponseData.successMsg("删除代码模版成功！");
        }else{
            return ResponseData.warnMsg("未找到指定id的代码模版！");
        }
    }


}
