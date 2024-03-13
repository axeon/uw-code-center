package uw.code.center.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import uw.dao.PageQueryParam;
import uw.dao.annotation.QueryMeta;

import java.util.Date;

/**
* code关键日志列表查询参数。
*/
@Schema(title = "code关键日志列表查询参数", description = "code关键日志列表查询参数")
public class CodeCritLogQueryParam extends PageQueryParam{



    /**
     * 主键ID
     */
    @QueryMeta(expr = "id=?")
    @Schema(title="主键ID", description = "主键ID")
    private Long id;

    /**
     * 运营商编号
     */
    @QueryMeta(expr = "saas_id=?")
    @Schema(title="运营商编号", description = "运营商编号")
    private Long saasId;

    /**
     * 商户id
     */
    @QueryMeta(expr = "mch_id=?")
    @Schema(title="商户id", description = "商户id")
    private Long mchId;

    /**
     * 操作人id
     */
    @QueryMeta(expr = "user_id=?")
    @Schema(title="操作人id", description = "操作人id")
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
     * 操作人名称
     */
    @QueryMeta(expr = "user_name like ?")
    @Schema(title="操作人名称", description = "操作人名称")
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
     * 操作对象id
     */
    @QueryMeta(expr = "object_id=?")
    @Schema(title="操作对象id", description = "操作对象id")
    private String objectId;

    /**
     * 操作对象类型
     */
    @QueryMeta(expr = "object_type=?")
    @Schema(title="操作对象类型", description = "操作对象类型")
    private String objectType;

    /**
     * 请求uri
     */
    @QueryMeta(expr = "uri like ?")
    @Schema(title="请求uri", description = "请求uri")
    private String uri;

    /**
     * 请求毫秒数范围
     */
    @QueryMeta(expr = "response_millis between ? and ?")
    @Schema(title="请求毫秒数范围", description = "请求毫秒数范围")
    private Long[] responseMillisRange;

    /**
     * 响应状态码
     */
    @QueryMeta(expr = "status_code=?")
    @Schema(title="响应状态码", description = "响应状态码")
    private Integer statusCode;

    /**
     * 响应状态码范围
     */
    @QueryMeta(expr = "status_code in (?)")
    @Schema(title="响应状态码范围", description = "响应状态码范围")
    private Integer[] statusCodes;

    /**
     * 应用信息
     */
    @QueryMeta(expr = "app_info like ?")
    @Schema(title="应用信息", description = "应用信息")
    private String appInfo;

    /**
     * 应用主机
     */
    @QueryMeta(expr = "app_host like ?")
    @Schema(title="应用主机", description = "应用主机")
    private String appHost;

    /**
     * 操作人ip
     */
    @QueryMeta(expr = "user_ip=?")
    @Schema(title="操作人ip", description = "操作人ip")
    private String userIp;
    /**
     * 创建时间范围
     */
    @QueryMeta(expr = "request_date between ? and ?")
    @Schema(title="创建时间范围", description = "创建时间范围")
    private java.util.Date[] requestDateRange;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSaasId() {
        return saasId;
    }

    public void setSaasId(Long saasId) {
        this.saasId = saasId;
    }

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Long[] getResponseMillisRange() {
        return responseMillisRange;
    }

    public void setResponseMillisRange(Long[] responseMillisRange) {
        this.responseMillisRange = responseMillisRange;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Integer[] getStatusCodes() {
        return statusCodes;
    }

    public void setStatusCodes(Integer[] statusCodes) {
        this.statusCodes = statusCodes;
    }

    public String getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(String appInfo) {
        this.appInfo = appInfo;
    }

    public String getAppHost() {
        return appHost;
    }

    public void setAppHost(String appHost) {
        this.appHost = appHost;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public Date[] getRequestDateRange() {
        return requestDateRange;
    }

    public void setRequestDateRange(Date[] requestDateRange) {
        this.requestDateRange = requestDateRange;
    }
}