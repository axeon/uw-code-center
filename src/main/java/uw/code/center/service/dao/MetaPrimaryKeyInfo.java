package uw.code.center.service.dao;

/**
 * 数据库表的主键列元数据。
 * <p>
 * 由 {@link DataMetaInterface#getPrimaryKey(String)} 产出。读取列信息时用于标注哪些列属于主键，
 * 同时作为 {@code pkList} 变量供 FreeMarker 模板使用。
 * </p>
 *
 * @author axeon
 */
public class MetaPrimaryKeyInfo {

    /**
     * 表名（小写）。
     */
    private String tableName;

    /**
     * 主键列名（小写）。
     */
    private String columnName;

    /**
     * 由列名派生的 Java 属性名（camelCase）。
     */
    private String propertyName;

    /**
     * 主键在复合主键中的序号（从 1 开始）。
     */
    private int keySeq;

    /**
     * 主键约束名称（小写）。
     */
    private String pkName;

    /**
     * 返回表名、列名、序号与约束名的简明描述，便于日志输出。
     *
     * @return 主键元数据描述字符串
     */
    @Override
    public String toString() {
        return "MetaPrimaryKeyInfo:" + tableName + "," + columnName + "," + keySeq + "," + pkName;
    }

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
     * 获取表名。
     *
     * @return 表名
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * 设置表名。
     *
     * @param tableName 表名
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 获取主键列名。
     *
     * @return 主键列名
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * 设置主键列名。
     *
     * @param columnName 主键列名
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
     * 获取主键序号。
     *
     * @return 主键序号
     */
    public int getKeySeq() {
        return keySeq;
    }

    /**
     * 设置主键序号。
     *
     * @param keySeq 主键序号
     */
    public void setKeySeq(int keySeq) {
        this.keySeq = keySeq;
    }

    /**
     * 获取主键约束名。
     *
     * @return 主键约束名
     */
    public String getPkName() {
        return pkName;
    }

    /**
     * 设置主键约束名。
     *
     * @param pkName 主键约束名
     */
    public void setPkName(String pkName) {
        this.pkName = pkName;
    }

}
