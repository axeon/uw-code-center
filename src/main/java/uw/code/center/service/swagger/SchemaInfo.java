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

        /**
         * 最大长度。
         */
        private int maxLength;

        /**
         * 是否为空。
         */
        private boolean nullable;

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

        public int getMaxLength() {
            return maxLength;
        }

        public void setMaxLength(int maxLength) {
            this.maxLength = maxLength;
        }

        public boolean isNullable() {
            return nullable;
        }

        public void setNullable(boolean nullable) {
            this.nullable = nullable;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder( "PropertyInfo{" );
            sb.append( "name='" ).append( name ).append( '\'' );
            sb.append( ", title='" ).append( title ).append( '\'' );
            sb.append( ", type='" ).append( type ).append( '\'' );
            sb.append( ", maxLength=" ).append( maxLength );
            sb.append( ", nullable=" ).append( nullable );
            sb.append( '}' );
            return sb.toString();
        }
    }
}
