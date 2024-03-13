package uw.code.center.entity;

import com.fasterxml.jackson.annotation.JsonRawValue;
import io.swagger.v3.oas.annotations.media.Schema;
import uw.dao.DataEntity;
import uw.dao.annotation.ColumnMeta;
import uw.dao.annotation.TableMeta;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * code数据历史
 *
 * @author axeon
 */
@TableMeta(tableName="code_data_history",tableType="table")
@Schema(title = "code数据历史", description = "code数据历史")
public class CodeDataHistory implements DataEntity,Serializable{


	/**
	 * ID#主键
	 */
	@ColumnMeta(columnName="id", dataType="long", dataSize=19, nullable=false, primaryKey=true)
	@Schema(title = "ID#主键", description = "ID#主键")
	private long id;

	/**
	 * 修改的表记录Id
	 */
	@ColumnMeta(columnName="entity_id", dataType="long", dataSize=19, nullable=true)
	@Schema(title = "修改的表记录Id", description = "修改的表记录Id")
	private long entityId;

	/**
	 * 对应的实例类
	 */
	@ColumnMeta(columnName="entity_class", dataType="String", dataSize=255, nullable=true)
	@Schema(title = "对应的实例类", description = "对应的实例类")
	private String entityClass;

	/**
	 * 修改时被替换的旧值
	 */
	@ColumnMeta(columnName="entity_data", dataType="String", dataSize=1073741824, nullable=true)
	@Schema(title = "修改时被替换的旧值", description = "修改时被替换的旧值")
	@JsonRawValue(value = false)
	private String entityData;

	/**
	 * 修改的字段信息
	 */
	@ColumnMeta(columnName="entity_update_info", dataType="String", dataSize=65535, nullable=true)
	@Schema(title = "修改的字段信息", description = "修改的字段信息")
	private String entityUpdateInfo;

	/**
	 * saasId
	 */
	@ColumnMeta(columnName="saas_id", dataType="long", dataSize=19, nullable=true, primaryKey=true)
	@Schema(title = "saasId", description = "saasId")
	private long saasId;

	/**
	 * 商户id
	 */
	@ColumnMeta(columnName="mch_id", dataType="long", dataSize=19, nullable=true)
	@Schema(title = "商户id", description = "商户id")
	private long mchId;

	/**
	 * 用户Id
	 */
	@ColumnMeta(columnName="user_id", dataType="long", dataSize=19, nullable=true)
	@Schema(title = "用户Id", description = "用户Id")
	private long userId;

	/**
	 * 用户类型
	 */
	@ColumnMeta(columnName="user_type", dataType="int", dataSize=10, nullable=true)
	@Schema(title = "用户类型", description = "用户类型")
	private int userType;

	/**
	 * 用户的组ID
	 */
	@ColumnMeta(columnName="group_id", dataType="long", dataSize=19, nullable=true)
	@Schema(title = "用户的组ID", description = "用户的组ID")
	private long groupId;

	/**
	 * 用户名称
	 */
	@ColumnMeta(columnName="user_name", dataType="String", dataSize=100, nullable=true)
	@Schema(title = "用户名称", description = "用户名称")
	private String userName;

	/**
	 * 操作人用户昵称
	 */
	@ColumnMeta(columnName="nick_name", dataType="String", dataSize=100, nullable=true)
	@Schema(title = "操作人用户昵称", description = "操作人用户昵称")
	private String nickName;

	/**
	 * 操作人真实名称
	 */
	@ColumnMeta(columnName="real_name", dataType="String", dataSize=100, nullable=true)
	@Schema(title = "操作人真实名称", description = "操作人真实名称")
	private String realName;

	/**
	 * 操作人的ip
	 */
	@ColumnMeta(columnName="user_ip", dataType="String", dataSize=100, nullable=true)
	@Schema(title = "操作人的ip", description = "操作人的ip")
	private String userIp;

	/**
	 * 创建日期
	 */
	@ColumnMeta(columnName="create_date", dataType="java.util.Date", dataSize=23, nullable=true)
	@Schema(title = "创建日期", description = "创建日期")
	private java.util.Date createDate;

	/**
	 * 轻量级状态下更新列表list.
	 */
	public transient Set<String> UPDATED_COLUMN = null;

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
     * 得到_INFO.
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
     * 清理_INFO和UPDATED_COLUMN信息.
     */
    public void CLEAR_UPDATED_INFO() {
        UPDATED_COLUMN = null;
        UPDATED_INFO = null;
	}

	/**
	 * 初始化set相关的信息.
	 */
	private void _INIT_UPDATE_INFO() {
		this.UPDATED_COLUMN = new HashSet<String>();
		this.UPDATED_INFO = new StringBuilder("表msc_data_history主键\"" + 
		this.id+ "\"更新为:\r\n");
	}


	/**
	 * 获得ID#主键。
	 */
	public long getId(){
		return this.id;
	}

	/**
	 * 获得修改的表记录Id。
	 */
	public long getEntityId(){
		return this.entityId;
	}

	/**
	 * 获得对应的实例类。
	 */
	public String getEntityClass(){
		return this.entityClass;
	}

	/**
	 * 获得修改时被替换的旧值。
	 */
	public String getEntityData(){
		return this.entityData;
	}

	/**
	 * 获得修改的字段信息。
	 */
	public String getEntityUpdateInfo(){
		return this.entityUpdateInfo;
	}

	/**
	 * 获得saasId。
	 */
	public long getSaasId(){
		return this.saasId;
	}

	/**
	 * 获得商户id。
	 */
	public long getMchId(){
		return this.mchId;
	}

	/**
	 * 获得用户Id。
	 */
	public long getUserId(){
		return this.userId;
	}

	/**
	 * 获得用户类型。
	 */
	public int getUserType(){
		return this.userType;
	}

	/**
	 * 获得用户的组ID。
	 */
	public long getGroupId(){
		return this.groupId;
	}

	/**
	 * 获得用户名称。
	 */
	public String getUserName(){
		return this.userName;
	}

	/**
	 * 获得操作人用户昵称。
	 */
	public String getNickName(){
		return this.nickName;
	}

	/**
	 * 获得操作人真实名称。
	 */
	public String getRealName(){
		return this.realName;
	}

	/**
	 * 获得操作人的ip。
	 */
	public String getUserIp(){
		return this.userIp;
	}

	/**
	 * 获得创建日期。
	 */
	public java.util.Date getCreateDate(){
		return this.createDate;
	}


	/**
	 * 设置ID#主键。
	 */
	public void setId(long id){
		if ((!String.valueOf(this.id).equals(String.valueOf(id)))) {
			if (this.UPDATED_COLUMN == null) {
				_INIT_UPDATE_INFO();
			}
			this.UPDATED_COLUMN.add("id");
			this.UPDATED_INFO.append("id:\"" + this.id+ "\"=>\""
                + id + "\"\r\n");
			this.id = id;
		}
	}

	/**
	 * 设置修改的表记录Id。
	 */
	public void setEntityId(long entityId){
		if ((!String.valueOf(this.entityId).equals(String.valueOf(entityId)))) {
			if (this.UPDATED_COLUMN == null) {
				_INIT_UPDATE_INFO();
			}
			this.UPDATED_COLUMN.add("entity_id");
			this.UPDATED_INFO.append("entity_id:\"" + this.entityId+ "\"=>\""
                + entityId + "\"\r\n");
			this.entityId = entityId;
		}
	}

	/**
	 * 设置对应的实例类。
	 */
	public void setEntityClass(String entityClass){
		if ((!String.valueOf(this.entityClass).equals(String.valueOf(entityClass)))) {
			if (this.UPDATED_COLUMN == null) {
				_INIT_UPDATE_INFO();
			}
			this.UPDATED_COLUMN.add("entity_class");
			this.UPDATED_INFO.append("entity_class:\"" + this.entityClass+ "\"=>\""
                + entityClass + "\"\r\n");
			this.entityClass = entityClass;
		}
	}

	/**
	 * 设置修改时被替换的旧值。
	 */
	public void setEntityData(String entityData){
		if ((!String.valueOf(this.entityData).equals(String.valueOf(entityData)))) {
			if (this.UPDATED_COLUMN == null) {
				_INIT_UPDATE_INFO();
			}
			this.UPDATED_COLUMN.add("entity_data");
			this.UPDATED_INFO.append("entity_data:\"" + this.entityData+ "\"=>\""
                + entityData + "\"\r\n");
			this.entityData = entityData;
		}
	}

	/**
	 * 设置修改的字段信息。
	 */
	public void setEntityUpdateInfo(String entityUpdateInfo){
		if ((!String.valueOf(this.entityUpdateInfo).equals(String.valueOf(entityUpdateInfo)))) {
			if (this.UPDATED_COLUMN == null) {
				_INIT_UPDATE_INFO();
			}
			this.UPDATED_COLUMN.add("entity_update_info");
			this.UPDATED_INFO.append("entity_update_info:\"" + this.entityUpdateInfo+ "\"=>\""
                + entityUpdateInfo + "\"\r\n");
			this.entityUpdateInfo = entityUpdateInfo;
		}
	}

	/**
	 * 设置saasId。
	 */
	public void setSaasId(long saasId){
		if ((!String.valueOf(this.saasId).equals(String.valueOf(saasId)))) {
			if (this.UPDATED_COLUMN == null) {
				_INIT_UPDATE_INFO();
			}
			this.UPDATED_COLUMN.add("saas_id");
			this.UPDATED_INFO.append("saas_id:\"" + this.saasId+ "\"=>\""
                + saasId + "\"\r\n");
			this.saasId = saasId;
		}
	}

	/**
	 * 设置商户id。
	 */
	public void setMchId(long mchId){
		if ((!String.valueOf(this.mchId).equals(String.valueOf(mchId)))) {
			if (this.UPDATED_COLUMN == null) {
				_INIT_UPDATE_INFO();
			}
			this.UPDATED_COLUMN.add("mch_id");
			this.UPDATED_INFO.append("mch_id:\"" + this.mchId+ "\"=>\""
                + mchId + "\"\r\n");
			this.mchId = mchId;
		}
	}

	/**
	 * 设置用户Id。
	 */
	public void setUserId(long userId){
		if ((!String.valueOf(this.userId).equals(String.valueOf(userId)))) {
			if (this.UPDATED_COLUMN == null) {
				_INIT_UPDATE_INFO();
			}
			this.UPDATED_COLUMN.add("user_id");
			this.UPDATED_INFO.append("user_id:\"" + this.userId+ "\"=>\""
                + userId + "\"\r\n");
			this.userId = userId;
		}
	}

	/**
	 * 设置用户类型。
	 */
	public void setUserType(int userType){
		if ((!String.valueOf(this.userType).equals(String.valueOf(userType)))) {
			if (this.UPDATED_COLUMN == null) {
				_INIT_UPDATE_INFO();
			}
			this.UPDATED_COLUMN.add("user_type");
			this.UPDATED_INFO.append("user_type:\"" + this.userType+ "\"=>\""
                + userType + "\"\r\n");
			this.userType = userType;
		}
	}

	/**
	 * 设置用户的组ID。
	 */
	public void setGroupId(long groupId){
		if ((!String.valueOf(this.groupId).equals(String.valueOf(groupId)))) {
			if (this.UPDATED_COLUMN == null) {
				_INIT_UPDATE_INFO();
			}
			this.UPDATED_COLUMN.add("group_id");
			this.UPDATED_INFO.append("group_id:\"" + this.groupId+ "\"=>\""
                + groupId + "\"\r\n");
			this.groupId = groupId;
		}
	}

	/**
	 * 设置用户名称。
	 */
	public void setUserName(String userName){
		if ((!String.valueOf(this.userName).equals(String.valueOf(userName)))) {
			if (this.UPDATED_COLUMN == null) {
				_INIT_UPDATE_INFO();
			}
			this.UPDATED_COLUMN.add("user_name");
			this.UPDATED_INFO.append("user_name:\"" + this.userName+ "\"=>\""
                + userName + "\"\r\n");
			this.userName = userName;
		}
	}

	/**
	 * 设置操作人用户昵称。
	 */
	public void setNickName(String nickName){
		if ((!String.valueOf(this.nickName).equals(String.valueOf(nickName)))) {
			if (this.UPDATED_COLUMN == null) {
				_INIT_UPDATE_INFO();
			}
			this.UPDATED_COLUMN.add("nick_name");
			this.UPDATED_INFO.append("nick_name:\"" + this.nickName+ "\"=>\""
                + nickName + "\"\r\n");
			this.nickName = nickName;
		}
	}

	/**
	 * 设置操作人真实名称。
	 */
	public void setRealName(String realName){
		if ((!String.valueOf(this.realName).equals(String.valueOf(realName)))) {
			if (this.UPDATED_COLUMN == null) {
				_INIT_UPDATE_INFO();
			}
			this.UPDATED_COLUMN.add("real_name");
			this.UPDATED_INFO.append("real_name:\"" + this.realName+ "\"=>\""
                + realName + "\"\r\n");
			this.realName = realName;
		}
	}

	/**
	 * 设置操作人的ip。
	 */
	public void setUserIp(String userIp){
		if ((!String.valueOf(this.userIp).equals(String.valueOf(userIp)))) {
			if (this.UPDATED_COLUMN == null) {
				_INIT_UPDATE_INFO();
			}
			this.UPDATED_COLUMN.add("user_ip");
			this.UPDATED_INFO.append("user_ip:\"" + this.userIp+ "\"=>\""
                + userIp + "\"\r\n");
			this.userIp = userIp;
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
			this.UPDATED_INFO.append("create_date:\"" + this.createDate+ "\"=>\""
                + createDate + "\"\r\n");
			this.createDate = createDate;
		}
	}

	/**
	 * 重载toString方法.
	 */
    @Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id:\"" + this.id + "\"\r\n");
		sb.append("entity_id:\"" + this.entityId + "\"\r\n");
		sb.append("entity_class:\"" + this.entityClass + "\"\r\n");
		sb.append("entity_data:\"" + this.entityData + "\"\r\n");
		sb.append("entity_update_info:\"" + this.entityUpdateInfo + "\"\r\n");
		sb.append("saas_id:\"" + this.saasId + "\"\r\n");
		sb.append("mch_id:\"" + this.mchId + "\"\r\n");
		sb.append("user_id:\"" + this.userId + "\"\r\n");
		sb.append("user_type:\"" + this.userType + "\"\r\n");
		sb.append("group_id:\"" + this.groupId + "\"\r\n");
		sb.append("user_name:\"" + this.userName + "\"\r\n");
		sb.append("nick_name:\"" + this.nickName + "\"\r\n");
		sb.append("real_name:\"" + this.realName + "\"\r\n");
		sb.append("user_ip:\"" + this.userIp + "\"\r\n");
		sb.append("create_date:\"" + this.createDate + "\"\r\n");
		return sb.toString();
	}

}
