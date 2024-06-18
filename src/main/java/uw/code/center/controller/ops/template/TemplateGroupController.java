package uw.code.center.controller.ops.template;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import uw.app.common.helper.SysDataHistoryHelper;
import uw.auth.service.AuthServiceHelper;
import uw.auth.service.annotation.MscPermDeclare;
import uw.auth.service.constant.ActionLog;
import uw.auth.service.constant.AuthType;
import uw.auth.service.constant.UserType;
import uw.common.dto.ResponseData;
import uw.code.center.constant.TemplateGroupType;
import uw.code.center.dto.CodeTemplateGroupQueryParam;
import uw.code.center.entity.CodeTemplateGroup;
import uw.dao.DaoFactory;
import uw.dao.DataList;
import uw.dao.TransactionException;

import java.util.Date;
import java.util.List;

/**
 * 用于对模板组进行增删改查列管理。
 */
@RestController
@Tag(name = "模板组管理", description = "用于对模板组进行增删改查列管理")
@RequestMapping("/ops/template/templateGroup")
@MscPermDeclare(type = UserType.OPS)
public class TemplateGroupController {

    DaoFactory dao = DaoFactory.getInstance();

    /**
     * 列表模板组记录。
     *
     * @param queryParam
     * @return
     * @throws TransactionException
     */
    @Operation(summary = "列表模板组记录", description = "列表模板组记录", operationId = "list")
    @GetMapping("/list")
    @MscPermDeclare(type = UserType.OPS, log = ActionLog.REQUEST)
    public DataList<CodeTemplateGroup> list(@ModelAttribute CodeTemplateGroupQueryParam queryParam) throws TransactionException {
        queryParam.setState( 1 );
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
    @MscPermDeclare(type = UserType.OPS, log = ActionLog.REQUEST)
    public List<CodeTemplateGroup> listForSelect(@Parameter(description = "模板组类型", example = "1") @RequestParam int groupType) throws TransactionException {
        if (groupType > 0) {
            return dao.list( CodeTemplateGroup.class, "select id,group_name,group_type from code_template_group where group_type=? and state=1", new Object[]{groupType} ).results();
        } else {
            return dao.list( CodeTemplateGroup.class, "select id,group_name,group_type from code_template_group where state=1" ).results();
        }
    }

    /**
     * 模板组类型
     *
     * @return
     */
    @GetMapping("/type")
    @Operation(summary = "模板组类型", description = "模板组类型")
    @MscPermDeclare(type = UserType.OPS, log = ActionLog.REQUEST)
    public TemplateGroupType[] type() {
        return TemplateGroupType.values();
    }

    /**
     * 加载模板组记录。
     *
     * @param id
     * @throws TransactionException
     */
    @MscPermDeclare(type = UserType.OPS, log = ActionLog.REQUEST)
    @Operation(summary = "加载模板组记录", description = "加载模板组记录")
    @GetMapping("/load")
    public CodeTemplateGroup load(@Parameter(description = "主键ID", required = true, example = "1") @RequestParam long id) throws TransactionException {
        return dao.load( CodeTemplateGroup.class, id );
    }

    /**
     * 新增模板组。
     *
     * @param codeTemplateGroup
     * @return
     * @throws TransactionException
     */
    @MscPermDeclare(type = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    @Operation(summary = "新增模板组", description = "新增模板组")
    @PostMapping("/save")
    public ResponseData<CodeTemplateGroup> save(@RequestBody CodeTemplateGroup codeTemplateGroup) throws TransactionException {
        long id = dao.getSequenceId( CodeTemplateGroup.class );
        AuthServiceHelper.logInfo( CodeTemplateGroup.class, id, "新增模板组" );
        codeTemplateGroup.setId( id );
        codeTemplateGroup.setCreateDate( new Date() );
        codeTemplateGroup.setModifyDate( null );
        codeTemplateGroup.setState( 1 );
        dao.save( codeTemplateGroup );
        //保存历史记录
        SysDataHistoryHelper.saveHistory( codeTemplateGroup.getId(), codeTemplateGroup,"模板组","新建模板组!" );
        return ResponseData.success( codeTemplateGroup );
    }

    /**
     * 修改模板组。
     *
     * @param codeTemplateGroup
     * @return
     * @throws TransactionException
     */
    @MscPermDeclare(type = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    @Operation(summary = "修改模板组", description = "修改模板组")
    @PutMapping("/update")
    public ResponseData<CodeTemplateGroup> update(@RequestBody CodeTemplateGroup codeTemplateGroup, @Parameter(name = "remark", description = "备注") @RequestParam String remark) throws TransactionException {
        AuthServiceHelper.logInfo( CodeTemplateGroup.class, codeTemplateGroup.getId(), "修改代码模版组!" + remark );
        CodeTemplateGroup codeTemplateGroupDb = dao.load( CodeTemplateGroup.class, codeTemplateGroup.getId() );
        if (codeTemplateGroupDb == null) {
            return ResponseData.errorMsg( "未找到指定ID的数值！" );
        }
        codeTemplateGroupDb.setGroupType( codeTemplateGroup.getGroupType() );
        codeTemplateGroupDb.setGroupName( codeTemplateGroup.getGroupName() );
        codeTemplateGroupDb.setGroupDesc( codeTemplateGroup.getGroupDesc() );
        //保存新记录。
        codeTemplateGroupDb.setModifyDate( new Date() );
        dao.update( codeTemplateGroupDb );
        SysDataHistoryHelper.saveHistory( codeTemplateGroupDb.getId(), codeTemplateGroupDb,"模板组","修改代码模版组!" + remark  );
        return ResponseData.success( codeTemplateGroupDb );
    }

    /**
     * 删除模板组。
     *
     * @param id
     * @throws TransactionException
     */
    @MscPermDeclare(type = UserType.OPS, auth = AuthType.PERM, log = ActionLog.CRIT)
    @Operation(summary = "删除模板组", description = "删除模板组")
    @DeleteMapping("/delete")
    public ResponseData delete(@Parameter(name = "id", description = "主键ID", example = "1") @RequestParam long id,
                               @Parameter(name = "remark", description = "备注") @RequestParam String remark) throws TransactionException {
        CodeTemplateGroup codeTemplateGroup = dao.load( CodeTemplateGroup.class, id );
        if (codeTemplateGroup != null) {
            codeTemplateGroup.setModifyDate( new Date() );
            codeTemplateGroup.setState( -1 );
            dao.update( codeTemplateGroup );
            AuthServiceHelper.logInfo( CodeTemplateGroup.class, id, "删除代码模版组成功！" + remark );
            return ResponseData.successMsg( "删除代码模版组成功！" + remark );
        } else {
            AuthServiceHelper.logInfo( CodeTemplateGroup.class, id, "删除代码模版组失败！" + remark );
            return ResponseData.errorMsg( "删除代码模版组失败！" + remark );
        }
    }


}
