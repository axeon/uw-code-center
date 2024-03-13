package uw.code.center.service.swagger;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * api功能组，类似权限菜单或者路由节点。
 */
public class ApiGroupInfo {

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
    private List<ApiInfo> apiInfoList = new ArrayList<>();


    public ApiGroupInfo(String function, String path, String title) {
        this.function = function;
        this.path = path;
        this.title = title;
    }

    /**
     * 是否含有list函数。
     * @return
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

    public void addToApiInfoList(ApiInfo apiInfo) {
        this.apiInfoList.add(apiInfo);
    }
}
