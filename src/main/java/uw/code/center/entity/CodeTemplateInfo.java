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
 * CodeTemplateInfo实体类
 * 代码模版
 *
 * @author axeon
 */
@TableMeta(tableName="code_template_info",tableType="table")
@Schema(title = "代码模版", description = "代码模版")
public class CodeTemplateInfo implements DataEntity,Serializable{


    /**
     * id
     */
    @ColumnMeta(columnName="id", dataType="long", dataSize=19, nullable=false, primaryKey=true)
    @Schema(title = "id", description = "id", maxLength=19, nullable=false )
    private long id;

    /**
     * 模板分组id
     */
    @ColumnMeta(columnName="group_id", dataType="long", dataSize=19, nullable=false)
    @Schema(title = "模板分组id", description = "模板分组id", maxLength=19, nullable=false )
    private long groupId;

    /**
     * 数据类型
     */
    @ColumnMeta(columnName="template_type", dataType="int", dataSize=10, nullable=true)
    @Schema(title = "数据类型", description = "数据类型", maxLength=10, nullable=true )
    private int templateType;

    /**
     * 模板名称
     */
    @ColumnMeta(columnName="template_name", dataType="String", dataSize=100, nullable=true)
    @Schema(title = "模板名称", description = "模板名称", maxLength=100, nullable=true )
    private String templateName;

    /**
     * 模板描述
     */
    @ColumnMeta(columnName="template_desc", dataType="String", dataSize=100, nullable=true)
    @Schema(title = "模板描述", description = "模板描述", maxLength=100, nullable=true )
    private String templateDesc;

    /**
     * 输出文件名模板
     */
    @ColumnMeta(columnName="template_filename", dataType="String", dataSize=100, nullable=true)
    @Schema(title = "输出文件名模板", description = "输出文件名模板", maxLength=100, nullable=true )
    private String templateFilename;

    /**
     * 模板内容
     */
    @ColumnMeta(columnName="template_body", dataType="String", dataSize=2147483646, nullable=true)
    @Schema(title = "模板内容", description = "模板内容", maxLength=2147483646, nullable=true )
    private String templateBody;

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
     * 轻量级状态下更新列表list.
     */
    private transient Set<String> _UPDATED_COLUMN = null;

    /**
     * 更新的信息.
     */
    private transient StringBuilder _UPDATED_INFO = null;


    /**
     * 是否加载完成.
     */
    private transient boolean _IS_LOADED;


    /**
     * 获得实体的表名。
     */
    @Override
    public String ENTITY_TABLE(){
        return "code_template_info";
    }

    /**
     * 获得实体的表注释。
     */
    @Override
    public String ENTITY_NAME(){
        return "代码模版";
    }

    /**
     * 获得主键
     */
    @Override
    public Serializable ENTITY_ID(){
        return getId();
    }


    /**
     * 获取更改的字段列表.
     */
    @Override
    public Set<String> GET_UPDATED_COLUMN() {
        return _UPDATED_COLUMN;
    }

    /**
     * 获取文本更新信息.
     */
    @Override
    public String GET_UPDATED_INFO() {
        if (this._UPDATED_INFO == null) {
            return null;
        } else {
            return this._UPDATED_INFO.toString();
        }
    }

    /**
     * 清除更新信息.
     */
    @Override
    public void CLEAR_UPDATED_INFO() {
        _UPDATED_COLUMN = null;
        _UPDATED_INFO = null;
    }

    /**
     * 初始化set相关的信息.
     */
    private void _INIT_UPDATE_INFO() {
        this._UPDATED_COLUMN = new HashSet<String>();
        this._UPDATED_INFO = new StringBuilder("表code_template_info主键\"" + 
        this.id+ "\"更新为:\r\n");
    }


    /**
     * 获取id。
     */
    public long getId(){
        return this.id;
    }

    /**
     * 获取模板分组id。
     */
    public long getGroupId(){
        return this.groupId;
    }

    /**
     * 获取数据类型。
     */
    public int getTemplateType(){
        return this.templateType;
    }

    /**
     * 获取模板名称。
     */
    public String getTemplateName(){
        return this.templateName;
    }

    /**
     * 获取模板描述。
     */
    public String getTemplateDesc(){
        return this.templateDesc;
    }

    /**
     * 获取输出文件名模板。
     */
    public String getTemplateFilename(){
        return this.templateFilename;
    }

    /**
     * 获取模板内容。
     */
    public String getTemplateBody(){
        return this.templateBody;
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
        if (!_IS_LOADED||!Objects.equals(this.id, id)){
            if (this._UPDATED_COLUMN == null) {
                _INIT_UPDATE_INFO();
            }
            this._UPDATED_COLUMN.add("id");
            this._UPDATED_INFO.append("id:\"").append(this.id).append("\"=>\"").append(id).append("\"\n");
            this.id = id;
        }
    }

    /**
     *  设置id链式调用。
     */
    public CodeTemplateInfo id(long id){
        setId(id);
        return this;
        }

    /**
     * 设置模板分组id。
     */
    public void setGroupId(long groupId){
        if (!_IS_LOADED||!Objects.equals(this.groupId, groupId)){
            if (this._UPDATED_COLUMN == null) {
                _INIT_UPDATE_INFO();
            }
            this._UPDATED_COLUMN.add("group_id");
            this._UPDATED_INFO.append("group_id:\"").append(this.groupId).append("\"=>\"").append(groupId).append("\"\n");
            this.groupId = groupId;
        }
    }

    /**
     *  设置模板分组id链式调用。
     */
    public CodeTemplateInfo groupId(long groupId){
        setGroupId(groupId);
        return this;
        }

    /**
     * 设置数据类型。
     */
    public void setTemplateType(int templateType){
        if (!_IS_LOADED||!Objects.equals(this.templateType, templateType)){
            if (this._UPDATED_COLUMN == null) {
                _INIT_UPDATE_INFO();
            }
            this._UPDATED_COLUMN.add("template_type");
            this._UPDATED_INFO.append("template_type:\"").append(this.templateType).append("\"=>\"").append(templateType).append("\"\n");
            this.templateType = templateType;
        }
    }

    /**
     *  设置数据类型链式调用。
     */
    public CodeTemplateInfo templateType(int templateType){
        setTemplateType(templateType);
        return this;
        }

    /**
     * 设置模板名称。
     */
    public void setTemplateName(String templateName){
        if (!_IS_LOADED||!Objects.equals(this.templateName, templateName)){
            if (this._UPDATED_COLUMN == null) {
                _INIT_UPDATE_INFO();
            }
            this._UPDATED_COLUMN.add("template_name");
            this._UPDATED_INFO.append("template_name:\"").append(this.templateName).append("\"=>\"").append(templateName).append("\"\n");
            this.templateName = templateName;
        }
    }

    /**
     *  设置模板名称链式调用。
     */
    public CodeTemplateInfo templateName(String templateName){
        setTemplateName(templateName);
        return this;
        }

    /**
     * 设置模板描述。
     */
    public void setTemplateDesc(String templateDesc){
        if (!_IS_LOADED||!Objects.equals(this.templateDesc, templateDesc)){
            if (this._UPDATED_COLUMN == null) {
                _INIT_UPDATE_INFO();
            }
            this._UPDATED_COLUMN.add("template_desc");
            this._UPDATED_INFO.append("template_desc:\"").append(this.templateDesc).append("\"=>\"").append(templateDesc).append("\"\n");
            this.templateDesc = templateDesc;
        }
    }

    /**
     *  设置模板描述链式调用。
     */
    public CodeTemplateInfo templateDesc(String templateDesc){
        setTemplateDesc(templateDesc);
        return this;
        }

    /**
     * 设置输出文件名模板。
     */
    public void setTemplateFilename(String templateFilename){
        if (!_IS_LOADED||!Objects.equals(this.templateFilename, templateFilename)){
            if (this._UPDATED_COLUMN == null) {
                _INIT_UPDATE_INFO();
            }
            this._UPDATED_COLUMN.add("template_filename");
            this._UPDATED_INFO.append("template_filename:\"").append(this.templateFilename).append("\"=>\"").append(templateFilename).append("\"\n");
            this.templateFilename = templateFilename;
        }
    }

    /**
     *  设置输出文件名模板链式调用。
     */
    public CodeTemplateInfo templateFilename(String templateFilename){
        setTemplateFilename(templateFilename);
        return this;
        }

    /**
     * 设置模板内容。
     */
    public void setTemplateBody(String templateBody){
        if (!_IS_LOADED||!Objects.equals(this.templateBody, templateBody)){
            if (this._UPDATED_COLUMN == null) {
                _INIT_UPDATE_INFO();
            }
            this._UPDATED_COLUMN.add("template_body");
            this._UPDATED_INFO.append("template_body:\"").append(this.templateBody).append("\"=>\"").append(templateBody).append("\"\n");
            this.templateBody = templateBody;
        }
    }

    /**
     *  设置模板内容链式调用。
     */
    public CodeTemplateInfo templateBody(String templateBody){
        setTemplateBody(templateBody);
        return this;
        }

    /**
     * 设置创建日期。
     */
    public void setCreateDate(java.util.Date createDate){
        if (!_IS_LOADED||!Objects.equals(this.createDate, createDate)){
            if (this._UPDATED_COLUMN == null) {
                _INIT_UPDATE_INFO();
            }
            this._UPDATED_COLUMN.add("create_date");
            this._UPDATED_INFO.append("create_date:\"").append(this.createDate).append("\"=>\"").append(createDate).append("\"\n");
            this.createDate = createDate;
        }
    }

    /**
     *  设置创建日期链式调用。
     */
    public CodeTemplateInfo createDate(java.util.Date createDate){
        setCreateDate(createDate);
        return this;
        }

    /**
     * 设置修改日期。
     */
    public void setModifyDate(java.util.Date modifyDate){
        if (!_IS_LOADED||!Objects.equals(this.modifyDate, modifyDate)){
            if (this._UPDATED_COLUMN == null) {
                _INIT_UPDATE_INFO();
            }
            this._UPDATED_COLUMN.add("modify_date");
            this._UPDATED_INFO.append("modify_date:\"").append(this.modifyDate).append("\"=>\"").append(modifyDate).append("\"\n");
            this.modifyDate = modifyDate;
        }
    }

    /**
     *  设置修改日期链式调用。
     */
    public CodeTemplateInfo modifyDate(java.util.Date modifyDate){
        setModifyDate(modifyDate);
        return this;
        }

    /**
     * 设置状态。1正常-1标记删除。
     */
    public void setState(int state){
        if (!_IS_LOADED||!Objects.equals(this.state, state)){
            if (this._UPDATED_COLUMN == null) {
                _INIT_UPDATE_INFO();
            }
            this._UPDATED_COLUMN.add("state");
            this._UPDATED_INFO.append("state:\"").append(this.state).append("\"=>\"").append(state).append("\"\n");
            this.state = state;
        }
    }

    /**
     *  设置状态。1正常-1标记删除链式调用。
     */
    public CodeTemplateInfo state(int state){
        setState(state);
        return this;
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