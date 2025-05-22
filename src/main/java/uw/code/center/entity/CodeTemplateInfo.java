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
        _UPDATED_INFO = DataUpdateInfo.addUpdateInfo(_UPDATED_INFO, "id", this.id, id, !_IS_LOADED );
        this.id = id;
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
        _UPDATED_INFO = DataUpdateInfo.addUpdateInfo(_UPDATED_INFO, "groupId", this.groupId, groupId, !_IS_LOADED );
        this.groupId = groupId;
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
        _UPDATED_INFO = DataUpdateInfo.addUpdateInfo(_UPDATED_INFO, "templateType", this.templateType, templateType, !_IS_LOADED );
        this.templateType = templateType;
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
        _UPDATED_INFO = DataUpdateInfo.addUpdateInfo(_UPDATED_INFO, "templateName", this.templateName, templateName, !_IS_LOADED );
        this.templateName = templateName;
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
        _UPDATED_INFO = DataUpdateInfo.addUpdateInfo(_UPDATED_INFO, "templateDesc", this.templateDesc, templateDesc, !_IS_LOADED );
        this.templateDesc = templateDesc;
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
        _UPDATED_INFO = DataUpdateInfo.addUpdateInfo(_UPDATED_INFO, "templateFilename", this.templateFilename, templateFilename, !_IS_LOADED );
        this.templateFilename = templateFilename;
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
        _UPDATED_INFO = DataUpdateInfo.addUpdateInfo(_UPDATED_INFO, "templateBody", this.templateBody, templateBody, !_IS_LOADED );
        this.templateBody = templateBody;
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
        _UPDATED_INFO = DataUpdateInfo.addUpdateInfo(_UPDATED_INFO, "createDate", this.createDate, createDate, !_IS_LOADED );
        this.createDate = createDate;
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
        _UPDATED_INFO = DataUpdateInfo.addUpdateInfo(_UPDATED_INFO, "modifyDate", this.modifyDate, modifyDate, !_IS_LOADED );
        this.modifyDate = modifyDate;
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
        _UPDATED_INFO = DataUpdateInfo.addUpdateInfo(_UPDATED_INFO, "state", this.state, state, !_IS_LOADED );
        this.state = state;
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
        return JsonUtils.toString(this);
    }

}