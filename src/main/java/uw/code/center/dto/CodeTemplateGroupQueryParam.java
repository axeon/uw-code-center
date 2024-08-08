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
    * id
    */
    @QueryMeta(expr = "id=?")
    @Schema(title="id", description = "id")
    private Long id;

    /**
    * 数据源类型
    */
    @QueryMeta(expr = "group_type=?")
    @Schema(title="数据源类型", description = "数据源类型")
    private Integer groupType;

    /**
    * 模板组名
    */
    @QueryMeta(expr = "group_name like ?")
    @Schema(title="模板组名", description = "模板组名")
    private String groupName;

    /**
    * 模板组描述
    */
    @QueryMeta(expr = "group_desc like ?")
    @Schema(title="模板组描述", description = "模板组描述")
    private String groupDesc;
    /**
    * 创建日期范围
    */
    @QueryMeta(expr = "create_date between ? and ?")
    @Schema(title="创建日期范围", description = "创建日期范围")
    private Date[] createDateRange;

    /**
    * 修改日期范围
    */
    @QueryMeta(expr = "modify_date between ? and ?")
    @Schema(title="修改日期范围", description = "修改日期范围")
    private Date[] modifyDateRange;

    /**
    * 状态。1正常-1标记删除
    */
    @QueryMeta(expr = "state=?")
    @Schema(title="状态。1正常-1标记删除", description = "状态。1正常-1标记删除")
    private Integer state;

    /**
    * 正常状态。1正常-1标记删除
    */
    @QueryMeta(expr = "state>-1")
    @Schema(title="正常状态。1正常-1标记删除", description = "正常状态。1正常-1标记删除")
    private Boolean stateOn;

    /**
    * 状态。1正常-1标记删除数组
    */
    @QueryMeta(expr = "state in (?)")
    @Schema(title="状态。1正常-1标记删除数组", description = "状态。1正常-1标记删除数组，可同时匹配多个状态。")
    private Integer[] states;

    /**
    * 状态。1正常-1标记删除运算条件。
    * 可以使用运算符号。
    */
    @QueryMeta(expr = "state ?")
    @Schema(title="状态。1正常-1标记删除运算条件", description = "状态。1正常-1标记删除运算条件，可使用><=!比较运算符。")
    private String stateOp;


    /**
    * 获得id。
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
    * 获得数据源类型。
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
    * 获得模板组名。
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
    * 获得模板组描述。
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
    * 获得创建日期范围。
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
    * 获得修改日期范围。
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
    * 获得状态。1正常-1标记删除。
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
    * 获得正常状态。1正常-1标记删除。
    */
    public Boolean getStateOn(){
        return this.stateOn;
    }

    /**
    * 设置正常状态。1正常-1标记删除。
    */
    public void setStateOn(Boolean stateOn){
        this.stateOn = stateOn;
    }

    /**
    * 获得状态。1正常-1标记删除数组。
    */
    public Integer[] getStates(){
        return this.states;
    }

    /**
    * 设置状态。1正常-1标记删除数组。
    */
    public void setStates(Integer[] states){
        this.states = states;
    }

    /**
    * 获得状态。1正常-1标记删除运算条件。
    */
    public String getStateOp(){
        return this.stateOp;
    }

    /**
    * 设置状态。1正常-1标记删除运算条件。
    */
    public void setStateOp(String stateOp){
        this.stateOp = stateOp;
    }

}