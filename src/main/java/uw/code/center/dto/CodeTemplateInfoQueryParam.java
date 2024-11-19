package uw.code.center.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import uw.dao.PageQueryParam;
import uw.dao.annotation.QueryMeta;

import java.util.Date;

/**
* 代码模版列表查询参数。
*/
@Schema(title = "代码模版列表查询参数", description = "代码模版列表查询参数")
public class CodeTemplateInfoQueryParam extends PageQueryParam{


    /**
    * id。
    */
    @QueryMeta(expr = "id=?")
    @Schema(title="id", description = "id")
    private Long id;

    /**
    * 模板分组id。
    */
    @QueryMeta(expr = "group_id=?")
    @Schema(title="模板分组id", description = "模板分组id")
    private Long groupId;

    /**
    * 数据类型。
    */
    @QueryMeta(expr = "template_type=?")
    @Schema(title="数据类型", description = "数据类型")
    private Integer templateType;

    /**
    * 模板名称。
    */
    @QueryMeta(expr = "template_name like ?")
    @Schema(title="模板名称", description = "模板名称")
    private String templateName;

    /**
    * 模板描述。
    */
    @QueryMeta(expr = "template_desc like ?")
    @Schema(title="模板描述", description = "模板描述")
    private String templateDesc;

    /**
    * 输出文件名模板。
    */
    @QueryMeta(expr = "template_filename like ?")
    @Schema(title="输出文件名模板", description = "输出文件名模板")
    private String templateFilename;
    /**
    * 创建日期范围。
    */
    @QueryMeta(expr = "create_date between ? and ?")
    @Schema(title="创建日期范围", description = "创建日期范围")
    private Date[] createDateRange;

    /**
    * 修改日期范围。
    */
    @QueryMeta(expr = "modify_date between ? and ?")
    @Schema(title="修改日期范围", description = "修改日期范围")
    private Date[] modifyDateRange;

    /**
    * 状态。1正常-1标记删除。
    */
    @QueryMeta(expr = "state=?")
    @Schema(title="状态。1正常-1标记删除", description = "状态。1正常-1标记删除")
    private Integer state;

    /**
    * 数组状态。1正常-1标记删除。
    */
    @QueryMeta(expr = "state in (?)")
    @Schema(title="数组状态。1正常-1标记删除", description = "状态。1正常-1标记删除数组，可同时匹配多个状态。")
    private Integer[] states;

    /**
    * 大于等于状态。1正常-1标记删除。
    */
    @QueryMeta(expr = "state>=?")
    @Schema(title="大于等于状态。1正常-1标记删除", description = "大于等于状态。1正常-1标记删除")
    private Integer stateGte;

    /**
    * 小于等于状态。1正常-1标记删除。
    */
    @QueryMeta(expr = "state<=?")
    @Schema(title="小于等于状态。1正常-1标记删除", description = "小于等于状态。1正常-1标记删除")
    private Integer stateLte;


    /**
    * 获取id。
    */
    public Long getId(){
        return this.id;
    }

    /**
    * 设置id。
    */
    public void setId(Long id){
        this.id = id;
    }
    /**
    * 获取模板分组id。
    */
    public Long getGroupId(){
        return this.groupId;
    }

    /**
    * 设置模板分组id。
    */
    public void setGroupId(Long groupId){
        this.groupId = groupId;
    }
    /**
    * 获取数据类型。
    */
    public Integer getTemplateType(){
        return this.templateType;
    }

    /**
    * 设置数据类型。
    */
    public void setTemplateType(Integer templateType){
        this.templateType = templateType;
    }

    /**
    * 获取模板名称。
    */
    public String getTemplateName(){
        return this.templateName;
    }

    /**
    * 设置模板名称。
    */
    public void setTemplateName(String templateName){
        this.templateName = templateName;
    }

    /**
    * 获取模板描述。
    */
    public String getTemplateDesc(){
        return this.templateDesc;
    }

    /**
    * 设置模板描述。
    */
    public void setTemplateDesc(String templateDesc){
        this.templateDesc = templateDesc;
    }

    /**
    * 获取输出文件名模板。
    */
    public String getTemplateFilename(){
        return this.templateFilename;
    }

    /**
    * 设置输出文件名模板。
    */
    public void setTemplateFilename(String templateFilename){
        this.templateFilename = templateFilename;
    }
    /**
    * 获取创建日期范围。
    */
    public Date[] getCreateDateRange(){
        return this.createDateRange;
    }

    /**
    * 设置创建日期范围。
    */
    public void setCreateDateRange(Date[] createDateRange){
        this.createDateRange = createDateRange;
    }
    /**
    * 获取修改日期范围。
    */
    public Date[] getModifyDateRange(){
        return this.modifyDateRange;
    }

    /**
    * 设置修改日期范围。
    */
    public void setModifyDateRange(Date[] modifyDateRange){
        this.modifyDateRange = modifyDateRange;
    }
    /**
    * 获取状态。1正常-1标记删除。
    */
    public Integer getState(){
        return this.state;
    }

    /**
    * 设置状态。1正常-1标记删除。
    */
    public void setState(Integer state){
        this.state = state;
    }

    /**
    * 获取数组状态。1正常-1标记删除。
    */
    public Integer[] getStates(){
        return this.states;
    }

    /**
    * 设置数组状态。1正常-1标记删除。
    */
    public void setStates(Integer[] states){
        this.states = states;
    }
    
    /**
    * 获取大于等于状态。1正常-1标记删除。
    */
    public Integer getStateGte(){
        return this.stateGte;
    }

    /**
    * 设置大于等于状态。1正常-1标记删除。
    */
    public void setStateGte(Integer stateGte){
        this.stateGte = stateGte;
    }
    
    /**
    * 获取小于等于状态。1正常-1标记删除。
    */
    public Integer getStateLte(){
        return this.stateLte;
    }

    /**
    * 获取小于等于状态。1正常-1标记删除。
    */
    public void setStateLte(Integer stateLte){
        this.stateLte = stateLte;
    }
    

}