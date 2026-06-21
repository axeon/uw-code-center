package uw.code.center.service.dao;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 数据库表的列（字段）元数据，作为数据库驱动代码生成的上下文对象。
 * <p>
 * 由 {@link DataMetaInterface#getColumnList(String, java.util.List)} 产出，FreeMarker 模板中通过
 * {@code columnList} 变量引用。布尔语义字段（isNullable / isAutoIncrement / isPrimaryKey）用字符串
 * {@code "true"}/{@code null} 表示，便于模板中直接判空使用。
 * </p>
 *
 * @author axeon
 */
public class MetaColumnInfo {

    /**
     * 原始列名（已统一转为小写）。
     */
    private String columnName;

    /**
     * 由列名派生的 Java 属性名（camelCase，如 {@code user_name} → {@code userName}）。
     */
    private String propertyName;

    /**
     * Java 属性类型（基本类型或全限定类名，如 {@code long}、{@code String}、{@code java.util.Date}）。
     */
    private String propertyType;

    /**
     * Java 属性的包装类型（如 {@code Long}、{@code String}、{@code java.util.Date}），用于泛型或集合元素。
     */
    private String propertyObject;

    /**
     * JDBC 字段数据类型，对应 {@link java.sql.Types} 中的常量值。
     */
    private int dataType;

    /**
     * 数据库原生类型名称（小写，如 {@code varchar}、{@code int4}、{@code timestamptz}）。
     */
    private String typeName;

    /**
     * 列的大小/精度。
     */
    private int columnSize;

    /**
     * 列注释；为空时由调用方回退为列名。
     */
    private String remarks;

    /**
     * 是否允许为 null：{@code "true"} 允许，否则 {@code "false"}。
     */
    private String isNullable;

    /**
     * 是否自增列：{@code "true"} 表示自增，否则为 null。
     */
    private String isAutoIncrement;

    /**
     * 是否主键：{@code "true"} 表示主键，否则为 null。
     */
    private String isPrimaryKey;

    /**
     * 获取 Java 属性名。
     *
     * @return Java 属性名
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * 设置 Java 属性名。
     *
     * @param propertyName Java 属性名
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * 获取 Java 属性类型。
     *
     * @return Java 属性类型
     */
    public String getPropertyType() {
        return propertyType;
    }

    /**
     * 设置 Java 属性类型。
     *
     * @param propertyType Java 属性类型
     */
    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    /**
     * 获取 Java 属性的包装类型。
     *
     * @return Java 属性的包装类型
     */
    public String getPropertyObject() {
        return propertyObject;
    }

    /**
     * 设置 Java 属性的包装类型。
     *
     * @param propertyObject Java 属性的包装类型
     */
    public void setPropertyObject(String propertyObject) {
        this.propertyObject = propertyObject;
    }

    /**
     * 获取是否主键标识。
     *
     * @return {@code "true"} 表示主键，否则为 null
     */
    public String getIsPrimaryKey() {
        return isPrimaryKey;
    }

    /**
     * 设置是否主键标识。
     *
     * @param isPrimaryKey {@code "true"} 表示主键，否则为 null
     */
    public void setIsPrimaryKey(String isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    /**
     * 返回列名、类型、注释等关键信息的简明描述，便于日志输出。
     *
     * @return 列元数据描述字符串
     */
    @Override
    public String toString() {
        return "MetaColumnInfo:" + columnName + "," + dataType + "," + typeName + "," + columnSize + "," + remarks + ","
                + isNullable + "," + isAutoIncrement;
    }

    /**
     * 基于 columnName 与 dataType 判定相等，用于去重。
     *
     * @param o 待比较对象
     * @return 相等返回 true
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        MetaColumnInfo that = (MetaColumnInfo) o;

        return new EqualsBuilder().append(dataType, that.dataType).append(columnName, that.columnName).isEquals();
    }

    /**
     * 与 {@link #equals(Object)} 对应的哈希值。
     *
     * @return 哈希码
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(columnName).append(dataType).toHashCode();
    }

    /**
     * 获取原始列名（小写）。
     *
     * @return 原始列名
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * 设置原始列名。
     *
     * @param columnName 原始列名
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
     * 获取 JDBC 数据类型常量值。
     *
     * @return JDBC 数据类型常量值
     */
    public int getDataType() {
        return dataType;
    }

    /**
     * 设置 JDBC 数据类型常量值。
     *
     * @param dataType JDBC 数据类型常量值
     */
    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    /**
     * 获取数据库原生类型名称。
     *
     * @return 数据库原生类型名称
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * 设置数据库原生类型名称。
     *
     * @param typeName 数据库原生类型名称
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * 获取列大小/精度。
     *
     * @return 列大小/精度
     */
    public int getColumnSize() {
        return columnSize;
    }

    /**
     * 设置列大小/精度。
     *
     * @param columnSize 列大小/精度
     */
    public void setColumnSize(int columnSize) {
        this.columnSize = columnSize;
    }

    /**
     * 获取列注释。
     *
     * @return 列注释
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * 设置列注释。
     *
     * @param remarks 列注释
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * 获取是否允许为 null。
     *
     * @return {@code "true"} 允许，否则 {@code "false"}
     */
    public String getIsNullable() {
        return isNullable;
    }

    /**
     * 设置是否允许为 null。
     *
     * @param isNullable {@code "true"} 允许，否则 {@code "false"}
     */
    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    /**
     * 获取是否自增列。
     *
     * @return {@code "true"} 表示自增，否则为 null
     */
    public String getIsAutoIncrement() {
        return isAutoIncrement;
    }

    /**
     * 设置是否自增列。
     *
     * @param isAutoIncrement {@code "true"} 表示自增，否则为 null
     */
    public void setIsAutoIncrement(String isAutoIncrement) {
        this.isAutoIncrement = isAutoIncrement;
    }

}
