package uw.code.center.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonRawValue;
import io.swagger.v3.oas.annotations.media.Schema;
import uw.dao.DataEntity;
import uw.dao.annotation.ColumnMeta;
import uw.dao.annotation.TableMeta;

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
    @Schema(title = "id", description = "id")
    private long id;

    /**
     * 数据源类型
     */
    @ColumnMeta(columnName="group_type", dataType="int", dataSize=10, nullable=true)
    @Schema(title = "数据源类型", description = "数据源类型")
    private int groupType;

    /**
     * 模板组名
     */
    @ColumnMeta(columnName="group_name", dataType="String", dataSize=100, nullable=true)
    @Schema(title = "模板组名", description = "模板组名")
    private String groupName;

    /**
     * 模板组描述
     */
    @ColumnMeta(columnName="group_desc", dataType="String", dataSize=100, nullable=true)
    @Schema(title = "模板组描述", description = "模板组描述")
    private String groupDesc;

    /**
     * 创建日期
     */
    @ColumnMeta(columnName="create_date", dataType="java.util.Date", dataSize=23, nullable=true)
    @Schema(title = "创建日期", description = "创建日期")
    private java.util.Date createDate;

    /**
     * 修改日期
     */
    @ColumnMeta(columnName="modify_date", dataType="java.util.Date", dataSize=23, nullable=true)
    @Schema(title = "修改日期", description = "修改日期")
    private java.util.Date modifyDate;

    /**
     * 状态。1正常-1标记删除
     */
    @ColumnMeta(columnName="state", dataType="int", dataSize=10, nullable=false)
    @Schema(title = "状态。1正常-1标记删除", description = "状态。1正常-1标记删除")
    private int state;

    /**
     * 轻量级状态下更新列表list.
     */
    private transient Set<String> UPDATED_COLUMN = null;

    /**
     * 更新的信息.
     */
    private transient StringBuilder UPDATED_INFO = null;

    /**
     * 获取更改的字段列表.
     */
    @Override
    public Set<String> GET_UPDATED_COLUMN() {
        return UPDATED_COLUMN;
    }

    /**
     * 获取文本更新信息.
     */
    @Override
    public String GET_UPDATED_INFO() {
        if (this.UPDATED_INFO == null) {
            return null;
        } else {
            return this.UPDATED_INFO.toString();
        }
    }

    /**
     * 清除更新信息.
     */
    @Override
    public void CLEAR_UPDATED_INFO() {
        UPDATED_COLUMN = null;
        UPDATED_INFO = null;
    }

    /**
     * 初始化set相关的信息.
     */
    private void _INIT_UPDATE_INFO() {
        this.UPDATED_COLUMN = new HashSet<String>();
        this.UPDATED_INFO = new StringBuilder("表code_template_group主键\"" + 
        this.id+ "\"更新为:\r\n");
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
        if (!Objects.equals(this.id, id)){
            if (this.UPDATED_COLUMN == null) {
                _INIT_UPDATE_INFO();
            }
            this.UPDATED_COLUMN.add("id");
            this.UPDATED_INFO.append("id:\"" + this.id+ "\"=>\"" + id + "\"\r\n");
            this.id = id;
        }
    }

    /**
     * 设置数据源类型。
     */
    public void setGroupType(int groupType){
        if (!Objects.equals(this.groupType, groupType)){
            if (this.UPDATED_COLUMN == null) {
                _INIT_UPDATE_INFO();
            }
            this.UPDATED_COLUMN.add("group_type");
            this.UPDATED_INFO.append("group_type:\"" + this.groupType+ "\"=>\"" + groupType + "\"\r\n");
            this.groupType = groupType;
        }
    }

    /**
     * 设置模板组名。
     */
    public void setGroupName(String groupName){
        if (!Objects.equals(this.groupName, groupName)){
            if (this.UPDATED_COLUMN == null) {
                _INIT_UPDATE_INFO();
            }
            this.UPDATED_COLUMN.add("group_name");
            this.UPDATED_INFO.append("group_name:\"" + this.groupName+ "\"=>\"" + groupName + "\"\r\n");
            this.groupName = groupName;
        }
    }

    /**
     * 设置模板组描述。
     */
    public void setGroupDesc(String groupDesc){
        if (!Objects.equals(this.groupDesc, groupDesc)){
            if (this.UPDATED_COLUMN == null) {
                _INIT_UPDATE_INFO();
            }
            this.UPDATED_COLUMN.add("group_desc");
            this.UPDATED_INFO.append("group_desc:\"" + this.groupDesc+ "\"=>\"" + groupDesc + "\"\r\n");
            this.groupDesc = groupDesc;
        }
    }

    /**
     * 设置创建日期。
     */
    public void setCreateDate(java.util.Date createDate){
        if (!Objects.equals(this.createDate, createDate)){
            if (this.UPDATED_COLUMN == null) {
                _INIT_UPDATE_INFO();
            }
            this.UPDATED_COLUMN.add("create_date");
            this.UPDATED_INFO.append("create_date:\"" + this.createDate+ "\"=>\"" + createDate + "\"\r\n");
            this.createDate = createDate;
        }
    }

    /**
     * 设置修改日期。
     */
    public void setModifyDate(java.util.Date modifyDate){
        if (!Objects.equals(this.modifyDate, modifyDate)){
            if (this.UPDATED_COLUMN == null) {
                _INIT_UPDATE_INFO();
            }
            this.UPDATED_COLUMN.add("modify_date");
            this.UPDATED_INFO.append("modify_date:\"" + this.modifyDate+ "\"=>\"" + modifyDate + "\"\r\n");
            this.modifyDate = modifyDate;
        }
    }

    /**
     * 设置状态。1正常-1标记删除。
     */
    public void setState(int state){
        if (!Objects.equals(this.state, state)){
            if (this.UPDATED_COLUMN == null) {
                _INIT_UPDATE_INFO();
            }
            this.UPDATED_COLUMN.add("state");
            this.UPDATED_INFO.append("state:\"" + this.state+ "\"=>\"" + state + "\"\r\n");
            this.state = state;
        }
    }

    /**
     * 重载toString方法.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id:\"" + this.id + "\"\r\n");
        sb.append("group_type:\"" + this.groupType + "\"\r\n");
        sb.append("group_name:\"" + this.groupName + "\"\r\n");
        sb.append("group_desc:\"" + this.groupDesc + "\"\r\n");
        sb.append("create_date:\"" + this.createDate + "\"\r\n");
        sb.append("modify_date:\"" + this.modifyDate + "\"\r\n");
        sb.append("state:\"" + this.state + "\"\r\n");
        return sb.toString();
    }

}