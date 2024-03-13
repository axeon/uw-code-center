package uw.code.center.service.swagger;


import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * api信息
 */
public class ApiInfo {

    /**
     * 方法名。
     */
    private String function;

    /**
     * 访问方法
     */
    private String method;

    /**
     * 功能分组。
     */
    private String group;

    /**
     * 路径信息
     */
    private String path;

    /**
     * 描述信息
     */
    private String title;

    /**
     * 参数列表
     */
    private List<ParameterInfo> parameterInfoList;

    /**
     * 请求体。
     */
    private RequestInfo requestInfo;

    /**
     * 返回值。
     */
    private ResponseInfo responseInfo;


    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public List<ParameterInfo> getParameterInfoList() {
        return parameterInfoList;
    }

    public void setParameterInfoList(List<ParameterInfo> parameterInfoList) {
        this.parameterInfoList = parameterInfoList;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("function", function)
                .append("method", method)
                .append("group", group)
                .append("path", path)
                .append("title", title)
                .append("parameterInfoList", parameterInfoList)
                .append("requestInfo", requestInfo)
                .append("responseInfo", responseInfo)
                .toString();
    }

    /**
     * 参数信息。
     */
    public static class ParameterInfo {

        /**
         * 名称
         */
        private String name;

        /**
         * 描述
         */
        private String title;

        /**
         * 数据类型
         */
        private String type;

        /**
         * 是否必须。
         */
        private boolean required;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isRequired() {
            return required;
        }

        public void setRequired(boolean required) {
            this.required = required;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("name", name)
                    .append("title", title)
                    .append("type", type)
                    .append("required", required)
                    .toString();
        }
    }


    /**
     * 请求体。
     */
    public static class RequestInfo {

        private String title;

        private String type;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return "RequestInfo{" +
                    " type='" + type + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    /**
     * 返回信息。
     */
    public static class ResponseInfo {

        private String type;

        private String title;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return "ResponseInfo{" +
                    "type='" + type + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }
    }
}
