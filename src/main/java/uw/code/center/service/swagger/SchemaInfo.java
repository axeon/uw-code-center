package uw.code.center.service.swagger;

import java.util.List;

/**
 * schema信息。
 */
public class SchemaInfo {

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String title;


    /**
     * 属性列表。
     */
    private List<PropertyInfo> propertyList;

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

    public List<PropertyInfo> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<PropertyInfo> propertyList) {
        this.propertyList = propertyList;
    }

    @Override
    public String toString() {
        return "SchemaInfo{" +
                "name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", propertyList=" + propertyList +
                '}';
    }

    /**
     * 属性信息
     */
    public static class PropertyInfo {

        /**
         * 属性名
         */
        private String name;

        /**
         * 属性描述
         */
        private String title;

        /**
         * 类型。
         */
        private String type;

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

        @Override
        public String toString() {
            return "PropertyInfo{" +
                    "name='" + name + '\'' +
                    ", title='" + title + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }
}
