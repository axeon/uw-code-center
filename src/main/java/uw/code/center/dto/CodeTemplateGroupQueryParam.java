package uw.code.center.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import uw.dao.PageQueryParam;
import uw.dao.annotation.QueryMeta;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
* 代码模版组列表查询参数。
*/
@Schema(title = "代码模版组列表查询参数", description = "代码模版组列表查询参数")
public class CodeTemplateGroupQueryParam extends PageQueryParam{

    /**
     * 允许的排序属性。
     * key:排序名 value:排序字段
     *
     * @return
     */
    @Override
    public Map<String, String> ALLOWED_SORT_PROPERTY() {
        return new HashMap<>() {{
            put( "id", "id" );
            put( "groupType", "group_type" );
            put( "groupName", "group_name" );
            put( "groupDesc", "group_desc" );
            put( "createDate", "create_date" );
            put( "modifyDate", "modify_date" );
            put( "state", "state" );
        }};
    }

    /**
    * id。
    */
    @QueryMeta(expr = "id=?")
    @Schema(title="id", description = "id")
    private Long id;

    /**
    * ID数组。
    */
    @QueryMeta(expr = "id in (?)")
    @Schema(title="ID数组", description = "ID数组，可同时匹配多个。")
    private Long[] ids;

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
    * 状态。1正常-1标记删除数组。
    */
    @QueryMeta(expr = "state in (?)")
    @Schema(title="状态。1正常-1标记删除数组", description = "状态。1正常-1标记删除数组，可同时匹配多个状态。")
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
    * 设置id链式调用。
    */
    public CodeTemplateGroupQueryParam id(Long id) {
        setId(id);
        return this;
    }

    /**
    * 获取ID数组。
    */
    public Long[] getIds() {
        return this.ids;
    }

    /**
    * 设置ID数组。
    */
    public void setIds(Long[] ids) {
        this.ids = ids;
    }

    /**
    * 设置ID数组链式调用。
    */
    public CodeTemplateGroupQueryParam ids(Long[] ids) {
        setIds(ids);
        return this;
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
    * 设置数据源类型链式调用。
    */
	public CodeTemplateGroupQueryParam groupType(Integer groupType){
        setGroupType(groupType);
        return this;
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
    * 设置模板组名链式调用。
    */
    public CodeTemplateGroupQueryParam groupName(String groupName) {
        setGroupName(groupName);
        return this;
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
    * 设置模板组描述链式调用。
    */
    public CodeTemplateGroupQueryParam groupDesc(String groupDesc) {
        setGroupDesc(groupDesc);
        return this;
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
    * 设置创建日期范围链式调用。
    */
    public CodeTemplateGroupQueryParam createDateRange(Date[] createDateRange) {
        setCreateDateRange(createDateRange);
        return this;
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
    * 设置修改日期范围链式调用。
    */
    public CodeTemplateGroupQueryParam modifyDateRange(Date[] modifyDateRange) {
        setModifyDateRange(modifyDateRange);
        return this;
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
    * 设置状态。1正常-1标记删除链式调用。
    */
    public CodeTemplateGroupQueryParam state(Integer state) {
        setState(state);
        return this;
    }

    /**
    * 获取状态。1正常-1标记删除数组。
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
    * 设置状态。1正常-1标记删除数组链式调用。
    */
    public CodeTemplateGroupQueryParam states(Integer[] states) {
        setStates(states);
        return this;
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
    * 设置大于等于状态。1正常-1标记删除链式调用。
    */
    public CodeTemplateGroupQueryParam stateGte(Integer stateGte) {
        setStateGte(stateGte);
        return this;
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
	
    /**
    * 获取小于等于状态。1正常-1标记删除链式调用。
    */
    public CodeTemplateGroupQueryParam stateLte(Integer stateLte) {
        setStateLte(stateLte);
        return this;
    }
    

}