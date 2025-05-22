package uw.code.center.entity;

import com.fasterxml.jackson.annotation.JsonRawValue;
import io.swagger.v3.oas.annotations.media.Schema;
import uw.common.util.JsonUtils;
import uw.dao.DataEntity;
import uw.dao.DataUpdateInfo;
import uw.dao.annotation.ColumnMeta;
import uw.dao.annotation.TableMeta;

import java.io.Serializable;


/**
 * CodeTemplateGroup实体类
 * 代码模版组
 *
 * @author axeon
 */
@TableMeta(tableName="code_template_group",tableType="table")
@Schema(title = "代码模版组", description = "代码模版组")
public class CodeTemplateGroup implements DataEntity,Serializable{


    /**
     * id
     */
    @ColumnMeta(columnName="id", dataType="long", dataSize=19, nullable=false, primaryKey=true)
    @Schema(title = "id", description = "id", maxLength=19, nullable=false )
    private long id;

    /**
     * 数据源类型
     */
    @ColumnMeta(columnName="group_type", dataType="int", dataSize=10, nullable=true)
    @Schema(title = "数据源类型", description = "数据源类型", maxLength=10, nullable=true )
    private int groupType;

    /**
     * 模板组名
     */
    @ColumnMeta(columnName="group_name", dataType="String", dataSize=100, nullable=true)
    @Schema(title = "模板组名", description = "模板组名", maxLength=100, nullable=true )
    private String groupName;

    /**
     * 模板组描述
     */
    @ColumnMeta(columnName="group_desc", dataType="String", dataSize=100, nullable=true)
    @Schema(title = "模板组描述", description = "模板组描述", maxLength=100, nullable=true )
    private String groupDesc;

    /**
     * 创建日期
     */
    @ColumnMeta(columnName="create_date", dataType="java.util.Date", dataSize=23, nullable=true)
    @Schema(title = "创建日期", description = "创建日期", maxLength=23, nullable=true )
    private java.util.Date createDate;

    /**
     * 修改日期
     */
    @ColumnMeta(columnName="modify_date", dataType="java.util.Date", dataSize=23, nullable=true)
    @Schema(title = "修改日期", description = "修改日期", maxLength=23, nullable=true )
    private java.util.Date modifyDate;

    /**
     * 状态。1正常-1标记删除
     */
    @ColumnMeta(columnName="state", dataType="int", dataSize=10, nullable=false)
    @Schema(title = "状态。1正常-1标记删除", description = "状态。1正常-1标记删除", maxLength=10, nullable=false )
    private int state;

    /**
     * 数据更新信息.
     */
    private transient DataUpdateInfo _UPDATED_INFO = null;

    /**
     * 是否加载完成.
     */
    private transient boolean _IS_LOADED;

    /**
     * 获得实体的表名。
     */
    @Override
    public String ENTITY_TABLE(){
        return "code_template_group";
    }

    /**
     * 获得实体的表注释。
     */
    @Override
    public String ENTITY_NAME(){
        return "代码模版组";
    }

    /**
     * 获得主键
     */
    @Override
    public Serializable ENTITY_ID(){
        return getId();
    }

    /**
     * 获取更新信息.
     */
    @Override
    public DataUpdateInfo GET_UPDATED_INFO() {
        return this._UPDATED_INFO;
    }

    /**
     * 清除更新信息.
     */
    @Override
    public void CLEAR_UPDATED_INFO() {
        _UPDATED_INFO = null;
    }


    /**
     * 获取id。
     */
    public long getId(){
        return this.id;
    }

    /**
     * 获取数据源类型。
     */
    public int getGroupType(){
        return this.groupType;
    }

    /**
     * 获取模板组名。
     */
    public String getGroupName(){
        return this.groupName;
    }

    /**
     * 获取模板组描述。
     */
    public String getGroupDesc(){
        return this.groupDesc;
    }

    /**
     * 获取创建日期。
     */
    public java.util.Date getCreateDate(){
        return this.createDate;
    }

    /**
     * 获取修改日期。
     */
    public java.util.Date getModifyDate(){
        return this.modifyDate;
    }

    /**
     * 获取状态。1正常-1标记删除。
     */
    public int getState(){
        return this.state;
    }


    /**
     * 设置id。
     */
    public void setId(long id){
        _UPDATED_INFO = DataUpdateInfo.addUpdateInfo(_UPDATED_INFO, "id", this.id, id, !_IS_LOADED );
        this.id = id;
    }

    /**
     *  设置id链式调用。
     */
    public CodeTemplateGroup id(long id){
        setId(id);
        return this;
    }

    /**
     * 设置数据源类型。
     */
    public void setGroupType(int groupType){
        _UPDATED_INFO = DataUpdateInfo.addUpdateInfo(_UPDATED_INFO, "groupType", this.groupType, groupType, !_IS_LOADED );
        this.groupType = groupType;
    }

    /**
     *  设置数据源类型链式调用。
     */
    public CodeTemplateGroup groupType(int groupType){
        setGroupType(groupType);
        return this;
    }

    /**
     * 设置模板组名。
     */
    public void setGroupName(String groupName){
        _UPDATED_INFO = DataUpdateInfo.addUpdateInfo(_UPDATED_INFO, "groupName", this.groupName, groupName, !_IS_LOADED );
        this.groupName = groupName;
    }

    /**
     *  设置模板组名链式调用。
     */
    public CodeTemplateGroup groupName(String groupName){
        setGroupName(groupName);
        return this;
    }

    /**
     * 设置模板组描述。
     */
    public void setGroupDesc(String groupDesc){
        _UPDATED_INFO = DataUpdateInfo.addUpdateInfo(_UPDATED_INFO, "groupDesc", this.groupDesc, groupDesc, !_IS_LOADED );
        this.groupDesc = groupDesc;
    }

    /**
     *  设置模板组描述链式调用。
     */
    public CodeTemplateGroup groupDesc(String groupDesc){
        setGroupDesc(groupDesc);
        return this;
    }

    /**
     * 设置创建日期。
     */
    public void setCreateDate(java.util.Date createDate){
        _UPDATED_INFO = DataUpdateInfo.addUpdateInfo(_UPDATED_INFO, "createDate", this.createDate, createDate, !_IS_LOADED );
        this.createDate = createDate;
    }

    /**
     *  设置创建日期链式调用。
     */
    public CodeTemplateGroup createDate(java.util.Date createDate){
        setCreateDate(createDate);
        return this;
    }

    /**
     * 设置修改日期。
     */
    public void setModifyDate(java.util.Date modifyDate){
        _UPDATED_INFO = DataUpdateInfo.addUpdateInfo(_UPDATED_INFO, "modifyDate", this.modifyDate, modifyDate, !_IS_LOADED );
        this.modifyDate = modifyDate;
    }

    /**
     *  设置修改日期链式调用。
     */
    public CodeTemplateGroup modifyDate(java.util.Date modifyDate){
        setModifyDate(modifyDate);
        return this;
    }

    /**
     * 设置状态。1正常-1标记删除。
     */
    public void setState(int state){
        _UPDATED_INFO = DataUpdateInfo.addUpdateInfo(_UPDATED_INFO, "state", this.state, state, !_IS_LOADED );
        this.state = state;
    }

    /**
     *  设置状态。1正常-1标记删除链式调用。
     */
    public CodeTemplateGroup state(int state){
        setState(state);
        return this;
    }

    /**
     * 重载toString方法.
     */
    @Override
    public String toString() {
        return JsonUtils.toString(this);
    }

}