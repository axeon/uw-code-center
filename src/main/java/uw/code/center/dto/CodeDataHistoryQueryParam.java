package uw.code.center.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import uw.dao.PageQueryParam;
import uw.dao.annotation.QueryMeta;

import java.util.Date;

/**
* code数据历史列表查询参数。
*/
@Schema(title = "code数据历史列表查询参数", description = "code数据历史列表查询参数")
public class CodeDataHistoryQueryParam extends PageQueryParam{


    /**
    * ID#主键
    */
    @QueryMeta(expr = "id=?")
    @Schema(title="ID#主键", description = "ID#主键")
    private Long id;

    /**
    * 修改的表记录Id
    */
    @QueryMeta(expr = "entity_id=?")
    @Schema(title="修改的表记录Id", description = "修改的表记录Id")
    private Long entityId;

    /**
    * 对应的实例类
    */
    @QueryMeta(expr = "entity_class like ?")
    @Schema(title="对应的实例类", description = "对应的实例类")
    private String entityClass;

    /**
    * saasId
    */
    @QueryMeta(expr = "saas_id=?")
    @Schema(title="saasId", description = "saasId")
    private Long saasId;

    /**
    * 商户id
    */
    @QueryMeta(expr = "mch_id=?")
    @Schema(title="商户id", description = "商户id")
    private Long mchId;

    /**
    * 用户Id
    */
    @QueryMeta(expr = "user_id=?")
    @Schema(title="用户Id", description = "用户Id")
    private Long userId;

    /**
    * 用户类型
    */
    @QueryMeta(expr = "user_type=?")
    @Schema(title="用户类型", description = "用户类型")
    private Integer userType;

    /**
    * 用户的组ID
    */
    @QueryMeta(expr = "group_id=?")
    @Schema(title="用户的组ID", description = "用户的组ID")
    private Long groupId;

    /**
    * 用户名称
    */
    @QueryMeta(expr = "user_name like ?")
    @Schema(title="用户名称", description = "用户名称")
    private String userName;

    /**
    * 操作人用户昵称
    */
    @QueryMeta(expr = "nick_name like ?")
    @Schema(title="操作人用户昵称", description = "操作人用户昵称")
    private String nickName;

    /**
    * 操作人真实名称
    */
    @QueryMeta(expr = "real_name like ?")
    @Schema(title="操作人真实名称", description = "操作人真实名称")
    private String realName;

    /**
    * 操作人的ip
    */
    @QueryMeta(expr = "user_ip like ?")
    @Schema(title="操作人的ip", description = "操作人的ip")
    private String userIp;
    /**
    * 创建日期范围
    */
    @QueryMeta(expr = "create_date between ? and ?")
    @Schema(title="创建日期范围", description = "创建日期范围")
    private Date[] createDateRange;



    /**
    * 获得ID#主键。
    */
    public Long getId(){
        return this.id;
    }

    /**
    * 设置ID#主键。
    */
    public void setId(Long id){
        this.id = id;
    }

    /**
    * 获得修改的表记录Id。
    */
    public Long getEntityId(){
        return this.entityId;
    }

    /**
    * 设置修改的表记录Id。
    */
    public void setEntityId(Long entityId){
        this.entityId = entityId;
    }


    /**
    * 获得对应的实例类。
    */
    public String getEntityClass(){
        return this.entityClass;
    }

    /**
    * 设置对应的实例类。
    */
    public void setEntityClass(String entityClass){
        this.entityClass = entityClass;
    }



    /**
    * 获得saasId。
    */
    public Long getSaasId(){
        return this.saasId;
    }

    /**
    * 设置saasId。
    */
    public void setSaasId(Long saasId){
        this.saasId = saasId;
    }

    /**
    * 获得商户id。
    */
    public Long getMchId(){
        return this.mchId;
    }

    /**
    * 设置商户id。
    */
    public void setMchId(Long mchId){
        this.mchId = mchId;
    }

    /**
    * 获得用户Id。
    */
    public Long getUserId(){
        return this.userId;
    }

    /**
    * 设置用户Id。
    */
    public void setUserId(Long userId){
        this.userId = userId;
    }

    /**
    * 获得用户类型。
    */
    public Integer getUserType(){
        return this.userType;
    }

    /**
    * 设置用户类型。
    */
    public void setUserType(Integer userType){
        this.userType = userType;
    }

    /**
    * 获得用户的组ID。
    */
    public Long getGroupId(){
        return this.groupId;
    }

    /**
    * 设置用户的组ID。
    */
    public void setGroupId(Long groupId){
        this.groupId = groupId;
    }


    /**
    * 获得用户名称。
    */
    public String getUserName(){
        return this.userName;
    }

    /**
    * 设置用户名称。
    */
    public void setUserName(String userName){
        this.userName = userName;
    }


    /**
    * 获得操作人用户昵称。
    */
    public String getNickName(){
        return this.nickName;
    }

    /**
    * 设置操作人用户昵称。
    */
    public void setNickName(String nickName){
        this.nickName = nickName;
    }


    /**
    * 获得操作人真实名称。
    */
    public String getRealName(){
        return this.realName;
    }

    /**
    * 设置操作人真实名称。
    */
    public void setRealName(String realName){
        this.realName = realName;
    }


    /**
    * 获得操作人的ip。
    */
    public String getUserIp(){
        return this.userIp;
    }

    /**
    * 设置操作人的ip。
    */
    public void setUserIp(String userIp){
        this.userIp = userIp;
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
}