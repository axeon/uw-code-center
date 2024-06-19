package uw.code.center.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import uw.dao.PageQueryParam;
import uw.dao.annotation.QueryMeta;

import java.util.Date;

/**
 * 代码模版列表查询参数。
 */
@Schema(title = "代码模版列表查询参数", description = "代码模版列表查询参数")
public class CodeTemplateInfoQueryParam extends PageQueryParam {


    /**
     * id
     */
    @QueryMeta(expr = "id=?")
    @Schema(title = "id", description = "id")
    private Long id;

    /**
     * 模板分组id
     */
    @QueryMeta(expr = "group_id=?")
    @Schema(title = "模板分组id", description = "模板分组id")
    private Long groupId;

    /**
     * 数据类型
     */
    @QueryMeta(expr = "template_type=?")
    @Schema(title = "数据类型", description = "数据类型")
    private Integer templateType;

    /**
     * 模板名称
     */
    @QueryMeta(expr = "template_name like ?")
    @Schema(title = "模板名称", description = "模板名称")
    private String templateName;

    /**
     * 模板描述
     */
    @QueryMeta(expr = "template_desc like ?")
    @Schema(title = "模板描述", description = "模板描述")
    private String templateDesc;

    /**
     * 输出文件名模板
     */
    @QueryMeta(expr = "template_filename like ?")
    @Schema(title = "输出文件名模板", description = "输出文件名模板")
    private String templateFilename;
    /**
     * 创建日期范围
     */
    @QueryMeta(expr = "create_date between ? and ?")
    @Schema(title = "创建日期范围", description = "创建日期范围")
    private Date[] createDateRange;

    /**
     * 修改日期范围
     */
    @QueryMeta(expr = "modify_date between ? and ?")
    @Schema(title = "修改日期范围", description = "修改日期范围")
    private Date[] modifyDateRange;

    /**
     * 状态。1正常-1标记删除
     */
    @QueryMeta(expr = "state=?")
    @Schema(title = "状态。1正常-1标记删除", description = "状态。1正常-1标记删除")
    private Integer state;

    /**
     * 正常状态。1正常-1标记删除
     */
    @QueryMeta(expr = "state>-1")
    @Schema(title = "正常状态。1正常-1标记删除", description = "正常状态。1正常-1标记删除")
    private Boolean stateOn;

    /**
     * 状态。1正常-1标记删除数组
     */
    @QueryMeta(expr = "state in (?)")
    @Schema(title = "状态。1正常-1标记删除数组", description = "状态。1正常-1标记删除数组，可同时匹配多个状态。")
    private Integer[] states;

    /**
     * 状态。1正常-1标记删除运算条件。
     * 可以使用运算符号。
     */
    @QueryMeta(expr = "state ?")
    @Schema(title = "状态。1正常-1标记删除运算条件", description = "状态。1正常-1标记删除运算条件，可使用><=!比较运算符。")
    private String stateOp;


    /**
     * 获得id。
     */
    public Long getId() {
        return this.id;
    }

    /**
     * 设置id。
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获得模板分组id。
     */
    public Long getGroupId() {
        return this.groupId;
    }

    /**
     * 设置模板分组id。
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    /**
     * 获得数据类型。
     */
    public Integer getTemplateType() {
        return this.templateType;
    }

    /**
     * 设置数据类型。
     */
    public void setTemplateType(Integer templateType) {
        this.templateType = templateType;
    }

    /**
     * 获得模板名称。
     */
    public String getTemplateName() {
        return this.templateName;
    }

    /**
     * 设置模板名称。
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * 获得模板描述。
     */
    public String getTemplateDesc() {
        return this.templateDesc;
    }

    /**
     * 设置模板描述。
     */
    public void setTemplateDesc(String templateDesc) {
        this.templateDesc = templateDesc;
    }

    /**
     * 获得输出文件名模板。
     */
    public String getTemplateFilename() {
        return this.templateFilename;
    }

    /**
     * 设置输出文件名模板。
     */
    public void setTemplateFilename(String templateFilename) {
        this.templateFilename = templateFilename;
    }

    /**
     * 获得创建日期范围。
     */
    public Date[] getCreateDateRange() {
        return this.createDateRange;
    }

    /**
     * 设置创建日期范围。
     */
    public void setCreateDateRange(Date[] createDateRange) {
        this.createDateRange = createDateRange;
    }

    /**
     * 获得修改日期范围。
     */
    public Date[] getModifyDateRange() {
        return this.modifyDateRange;
    }

    /**
     * 设置修改日期范围。
     */
    public void setModifyDateRange(Date[] modifyDateRange) {
        this.modifyDateRange = modifyDateRange;
    }

    /**
     * 获得状态。1正常-1标记删除。
     */
    public Integer getState() {
        return this.state;
    }

    /**
     * 设置状态。1正常-1标记删除。
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获得正常状态。1正常-1标记删除。
     */
    public Boolean getStateOn() {
        return this.stateOn;
    }

    /**
     * 设置正常状态。1正常-1标记删除。
     */
    public void setStateOn(Boolean stateOn) {
        this.stateOn = stateOn;
    }

    /**
     * 获得状态。1正常-1标记删除数组。
     */
    public Integer[] getStates() {
        return this.states;
    }

    /**
     * 设置状态。1正常-1标记删除数组。
     */
    public void setStates(Integer[] states) {
        this.states = states;
    }

    /**
     * 获得状态。1正常-1标记删除运算条件。
     */
    public String getStateOp() {
        return this.stateOp;
    }

    /**
     * 设置状态。1正常-1标记删除运算条件。
     */
    public void setStateOp(String stateOp) {
        this.stateOp = stateOp;
    }

}