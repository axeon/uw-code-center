package uw.code.center.service.swagger;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * API 功能组（二级分组），类似权限菜单或路由节点，按接口的父路径聚合。
 * <p>
 * 一个分组下包含若干 {@link ApiInfo}。Vue3 页面生成时会依据组内是否含 {@code /list} 接口来决定是否生成列表页。
 * </p>
 */
public class ApiGroupInfo {

    /**
     * 分组功能名（由父路径派生的驼峰标识）。
     */
    private String function;

    /**
     * 分组父路径。
     */
    private String path;

    /**
     * 分组标题（取自 OpenAPI 文档的 tag）。
     */
    private String title;

    /**
     * 该分组下的接口列表。
     */
    private List<ApiInfo> apiInfoList = new ArrayList<>();


    /**
     * 构造一个分组。
     *
     * @param function 功能名
     * @param path     父路径
     * @param title    标题
     */
    public ApiGroupInfo(String function, String path, String title) {
        this.function = function;
        this.path = path;
        this.title = title;
    }

    /**
     * 判断分组内是否包含列表查询接口（路径以 {@code /list} 结尾）。
     *
     * @return 含列表接口返回 true
     */
    public boolean hasListFunction(){
        for (ApiInfo apiInfo:apiInfoList) {
            if (apiInfo.getPath().endsWith("/list")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ApiGroupInfo that = (ApiGroupInfo) o;

        return new EqualsBuilder().append(path, that.path).append(title, that.title).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(path).append(title).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", function)
                .append("path", path)
                .append("title", title)
                .append("apiInfoList", apiInfoList)
                .toString();
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ApiInfo> getApiInfoList() {
        return apiInfoList;
    }

    public void setApiInfoList(List<ApiInfo> apiInfoList) {
        this.apiInfoList = apiInfoList;
    }

    /**
     * 向分组内追加一个接口。
     *
     * @param apiInfo 接口信息
     */
    public void addToApiInfoList(ApiInfo apiInfo) {
        this.apiInfoList.add(apiInfo);
    }
}
