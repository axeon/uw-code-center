package uw.code.center.service.swagger;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * api目录组，作为一级菜单。
 */
public class ApiCatalogInfo {

    /**
     * 名称
     */
    private String function;

    /**
     * 路径
     */
    private String path;

    /**
     * 名字。
     */
    private String title;

    /**
     * apiInfoList。
     */
    private List<ApiGroupInfo> apiGroupInfoList = new ArrayList<>();


    public ApiCatalogInfo(String function, String path, String title) {
        this.function = function;
        this.path = path;
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ApiCatalogInfo that = (ApiCatalogInfo) o;

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
                .append("apiGroupInfoList", apiGroupInfoList)
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

    public List<ApiGroupInfo> getApiGroupInfoList() {
        return apiGroupInfoList;
    }

    public void setApiGroupInfoList(List<ApiGroupInfo> apiGroupInfoList) {
        this.apiGroupInfoList = apiGroupInfoList;
    }

    public void addToApiGroupInfoList(ApiGroupInfo apiGroupInfo) {
        this.apiGroupInfoList.add(apiGroupInfo);
    }
}
