package uw.code.center.service.swagger;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * API 目录（一级菜单），对应 OpenAPI 文档中以 {@code $-package-info-$} 标记的路径。
 * <p>
 * 一个目录下可包含多个 {@link ApiGroupInfo}（二级分组），用于在前端生成菜单/导航结构。
 * </p>
 */
public class ApiCatalogInfo {

    /**
     * 目录功能名（由路径派生的驼峰标识）。
     */
    private String function;

    /**
     * 目录路径。
     */
    private String path;

    /**
     * 目录标题（取自 OpenAPI 文档的 summary）。
     */
    private String title;

    /**
     * 该目录下的 API 分组列表。
     */
    private List<ApiGroupInfo> apiGroupInfoList = new ArrayList<>();


    /**
     * 构造一个目录节点。
     *
     * @param function 功能名
     * @param path     路径
     * @param title    标题
     */
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

    /**
     * 向目录下追加一个 API 分组。
     *
     * @param apiGroupInfo API 分组
     */
    public void addToApiGroupInfoList(ApiGroupInfo apiGroupInfo) {
        this.apiGroupInfoList.add(apiGroupInfo);
    }
}
