package uw.code.center.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import uw.dao.PageQueryParam;
import uw.dao.annotation.QueryMeta;

import java.util.Date;

/**
* 代码模版组列表查询参数。
*/
@Schema(title = "代码模版组列表查询参数", description = "代码模版组列表查询参数")
public class CodeTemplateGroupQueryParam extends PageQueryParam{


    /**
    * id。
    */
    @QueryMeta(expr = "id=?")
    @Schema(title="id", description = "id")
    private Long id;

    /**
    * 数据源类型。
    */
    @QueryMeta(expr = "group_type=?")
    @Schema(title="数据源类型", description = "数据源类型")
    private Integer groupType;

    /**
    * 模板组名。
    */
    @QueryMeta(expr = "group_name like ?")
    @Schema(title="模板组名", description = "模板组名")
    private String groupName;

    /**
    * 模板组描述。
    */
    @QueryMeta(expr = "group_desc like ?")
    @Schema(title="模板组描述", description = "模板组描述")
    private String groupDesc;
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
    * 获取数据源类型。
    */
    public Integer getGroupType(){
        return this.groupType;
    }

    /**
    * 设置数据源类型。
    */
    public void setGroupType(Integer groupType){
        this.groupType = groupType;
    }

    /**
    * 获取模板组名。
    */
    public String getGroupName(){
        return this.groupName;
    }

    /**
    * 设置模板组名。
    */
    public void setGroupName(String groupName){
        this.groupName = groupName;
    }

    /**
    * 获取模板组描述。
    */
    public String getGroupDesc(){
        return this.groupDesc;
    }

    /**
    * 设置模板组描述。
    */
    public void setGroupDesc(String groupDesc){
        this.groupDesc = groupDesc;
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
    public void setStateLte(Integer stateOn){
        this.stateLte = stateLte;
    }
    

}