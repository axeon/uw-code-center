package uw.code.center.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonRawValue;
import io.swagger.v3.oas.annotations.media.Schema;
import uw.dao.DataEntity;
import uw.dao.annotation.ColumnMeta;
import uw.dao.annotation.TableMeta;

/**
 * CodeTemplate实体类
 * 代码模版
 *
 * @author axeon
 */
@TableMeta(tableName="code_template",tableType="table")
@Schema(title = "代码模版", description = "代码模版")
public class CodeTemplate implements DataEntity,Serializable{


    /**
     * id
     */
    @ColumnMeta(columnName="id", dataType="long", dataSize=19, nullable=false, primaryKey=true)
    @Schema(title = "id", description = "id")
    private long id;

    /**
     * 模板分组id
     */
    @ColumnMeta(columnName="group_id", dataType="long", dataSize=19, nullable=false)
    @Schema(title = "模板分组id", description = "模板分组id")
    private long groupId;

    /**
     * 数据类型
     */
    @ColumnMeta(columnName="template_type", dataType="int", dataSize=10, nullable=true)
    @Schema(title = "数据类型", description = "数据类型")
    private int templateType;

    /**
     * 模板名称
     */
    @ColumnMeta(columnName="template_name", dataType="String", dataSize=100, nullable=true)
    @Schema(title = "模板名称", description = "模板名称")
    private String templateName;

    /**
     * 模板描述
     */
    @ColumnMeta(columnName="template_desc", dataType="String", dataSize=100, nullable=true)
    @Schema(title = "模板描述", description = "模板描述")
    private String templateDesc;

    /**
     * 输出文件名模板
     */
    @ColumnMeta(columnName="template_filename", dataType="String", dataSize=100, nullable=true)
    @Schema(title = "输出文件名模板", description = "输出文件名模板")
    private String templateFilename;

    /**
     * 模板内容
     */
    @ColumnMeta(columnName="template_body", dataType="String", dataSize=2147483647, nullable=true)
    @Schema(title = "模板内容", description = "模板内容")
    private String templateBody;

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
     * 获得更改的字段列表.
     */
    @Override
    public Set<String> GET_UPDATED_COLUMN() {
        return UPDATED_COLUMN;
    }

    /**
     * 获得文本更新信息.
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
        this.UPDATED_INFO = new StringBuilder("表code_template主键\"" + 
        this.id+ "\"更新为:\r\n");
    }


    /**
     * 获得id。
     */
    public long getId(){
        return this.id;
    }

    /**
     * 获得模板分组id。
     */
    public long getGroupId(){
        return this.groupId;
    }

    /**
     * 获得数据类型。
     */
    public int getTemplateType(){
        return this.templateType;
    }

    /**
     * 获得模板名称。
     */
    public String getTemplateName(){
        return this.templateName;
    }

    /**
     * 获得模板描述。
     */
    public String getTemplateDesc(){
        return this.templateDesc;
    }

    /**
     * 获得输出文件名模板。
     */
    public String getTemplateFilename(){
        return this.templateFilename;
    }

    /**
     * 获得模板内容。
     */
    public String getTemplateBody(){
        return this.templateBody;
    }

    /**
     * 获得创建日期。
     */
    public java.util.Date getCreateDate(){
        return this.createDate;
    }

    /**
     * 获得修改日期。
     */
    public java.util.Date getModifyDate(){
        return this.modifyDate;
    }

    /**
     * 获得状态。1正常-1标记删除。
     */
    public int getState(){
        return this.state;
    }


    /**
     * 设置id。
     */
    public void setId(long id){
        if ((!String.valueOf(this.id).equals(String.valueOf(id)))) {
            if (this.UPDATED_COLUMN == null) {
                _INIT_UPDATE_INFO();
            }
            this.UPDATED_COLUMN.add("id");
            this.UPDATED_INFO.append("id:\"" + this.id+ "\"=>\"" + id + "\"\r\n");
            this.id = id;
        }
    }

    /**
     * 设置模板分组id。
     */
    public void setGroupId(long groupId){
        if ((!String.valueOf(this.groupId).equals(String.valueOf(groupId)))) {
            if (this.UPDATED_COLUMN == null) {
                _INIT_UPDATE_INFO();
            }
            this.UPDATED_COLUMN.add("group_id");
            this.UPDATED_INFO.append("group_id:\"" + this.groupId+ "\"=>\"" + groupId + "\"\r\n");
            this.groupId = groupId;
        }
    }

    /**
     * 设置数据类型。
     */
    public void setTemplateType(int templateType){
        if ((!String.valueOf(this.templateType).equals(String.valueOf(templateType)))) {
            if (this.UPDATED_COLUMN == null) {
                _INIT_UPDATE_INFO();
            }
            this.UPDATED_COLUMN.add("template_type");
            this.UPDATED_INFO.append("template_type:\"" + this.templateType+ "\"=>\"" + templateType + "\"\r\n");
            this.templateType = templateType;
        }
    }

    /**
     * 设置模板名称。
     */
    public void setTemplateName(String templateName){
        if ((!String.valueOf(this.templateName).equals(String.valueOf(templateName)))) {
            if (this.UPDATED_COLUMN == null) {
                _INIT_UPDATE_INFO();
            }
            this.UPDATED_COLUMN.add("template_name");
            this.UPDATED_INFO.append("template_name:\"" + this.templateName+ "\"=>\"" + templateName + "\"\r\n");
            this.templateName = templateName;
        }
    }

    /**
     * 设置模板描述。
     */
    public void setTemplateDesc(String templateDesc){
        if ((!String.valueOf(this.templateDesc).equals(String.valueOf(templateDesc)))) {
            if (this.UPDATED_COLUMN == null) {
                _INIT_UPDATE_INFO();
            }
            this.UPDATED_COLUMN.add("template_desc");
            this.UPDATED_INFO.append("template_desc:\"" + this.templateDesc+ "\"=>\"" + templateDesc + "\"\r\n");
            this.templateDesc = templateDesc;
        }
    }

    /**
     * 设置输出文件名模板。
     */
    public void setTemplateFilename(String templateFilename){
        if ((!String.valueOf(this.templateFilename).equals(String.valueOf(templateFilename)))) {
            if (this.UPDATED_COLUMN == null) {
                _INIT_UPDATE_INFO();
            }
            this.UPDATED_COLUMN.add("template_filename");
            this.UPDATED_INFO.append("template_filename:\"" + this.templateFilename+ "\"=>\"" + templateFilename + "\"\r\n");
            this.templateFilename = templateFilename;
        }
    }

    /**
     * 设置模板内容。
     */
    public void setTemplateBody(String templateBody){
        if ((!String.valueOf(this.templateBody).equals(String.valueOf(templateBody)))) {
            if (this.UPDATED_COLUMN == null) {
                _INIT_UPDATE_INFO();
            }
            this.UPDATED_COLUMN.add("template_body");
            this.UPDATED_INFO.append("template_body:\"" + this.templateBody+ "\"=>\"" + templateBody + "\"\r\n");
            this.templateBody = templateBody;
        }
    }

    /**
     * 设置创建日期。
     */
    public void setCreateDate(java.util.Date createDate){
        if ((!String.valueOf(this.createDate).equals(String.valueOf(createDate)))) {
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
        if ((!String.valueOf(this.modifyDate).equals(String.valueOf(modifyDate)))) {
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
        if ((!String.valueOf(this.state).equals(String.valueOf(state)))) {
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
        sb.append("group_id:\"" + this.groupId + "\"\r\n");
        sb.append("template_type:\"" + this.templateType + "\"\r\n");
        sb.append("template_name:\"" + this.templateName + "\"\r\n");
        sb.append("template_desc:\"" + this.templateDesc + "\"\r\n");
        sb.append("template_filename:\"" + this.templateFilename + "\"\r\n");
        sb.append("template_body:\"" + this.templateBody + "\"\r\n");
        sb.append("create_date:\"" + this.createDate + "\"\r\n");
        sb.append("modify_date:\"" + this.modifyDate + "\"\r\n");
        sb.append("state:\"" + this.state + "\"\r\n");
        return sb.toString();
    }

}