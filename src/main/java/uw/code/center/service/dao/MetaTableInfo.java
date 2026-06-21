package uw.code.center.service.dao;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 数据库表/视图的元数据信息，作为数据库驱动代码生成的上下文对象。
 * <p>
 * 由 {@link DataMetaInterface#getTablesAndViews(java.util.Set)} 产出，FreeMarker 模板中通过
 * {@code tableMeta} 变量引用。表名统一小写，实体名/路径/父级名称由表名按命名约定派生。
 * </p>
 *
 * @author axeon
 */
public class MetaTableInfo {

    /**
     * 原始表名（已统一转为小写）。
     */
    private String tableName;

    /**
     * 由表名派生的实体类名（首字母大写驼峰，如 {@code user_info} → {@code UserInfo}）。
     */
    private String entityName;

    /**
     * 由表名派生的路径风格名称（如 {@code user_info} → {@code user/info}）。
     */
    private String entityPath;

    /**
     * 表名最后一段（去除前缀目录，如 {@code a/b/user_info} → {@code user_info}）。
     */
    private String entityParent;

    /**
     * 表类型，取自 JDBC 元数据 {@code TABLE_TYPE}（如 {@code table}、{@code view}）。
     */
    private String tableType;

    /**
     * 表注释；为空时回退为表名。
     */
    private String remarks;

    /**
     * 返回表名、类型与注释的简明描述，便于日志输出。
     *
     * @return 形如 {@code MetaTableInfo:user_info,table,用户信息} 的字符串
     */
    @Override
    public String toString() {
        return "MetaTableInfo:" + tableName + "," + tableType + "," + remarks;
    }

    /**
     * 基于 tableName 与 tableType 判定相等，用于去重（同名同类型视为同一表）。
     *
     * @param o 待比较对象
     * @return 相等返回 true
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        MetaTableInfo that = (MetaTableInfo) o;

        return new EqualsBuilder().append(tableName, that.tableName).append(tableType, that.tableType).isEquals();
    }

    /**
     * 与 {@link #equals(Object)} 对应的哈希值。
     *
     * @return 哈希码
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(tableName).append(tableType).toHashCode();
    }

    /**
     * 获取实体类名。
     *
     * @return 实体类名
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * 设置实体类名。
     *
     * @param entityName 实体类名
     */
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    /**
     * 获取路径风格名称。
     *
     * @return 路径风格名称
     */
    public String getEntityPath() {
        return entityPath;
    }

    /**
     * 设置路径风格名称。
     *
     * @param entityPath 路径风格名称
     */
    public void setEntityPath(String entityPath) {
        this.entityPath = entityPath;
    }

    /**
     * 获取表名最后一段。
     *
     * @return 表名最后一段
     */
    public String getEntityParent() {
        return entityParent;
    }

    /**
     * 设置表名最后一段。
     *
     * @param entityParent 表名最后一段
     */
    public void setEntityParent(String entityParent) {
        this.entityParent = entityParent;
    }

    /**
     * 获取原始表名（小写）。
     *
     * @return 原始表名
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * 设置原始表名。
     *
     * @param tableName 原始表名
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 获取表类型。
     *
     * @return 表类型
     */
    public String getTableType() {
        return tableType;
    }

    /**
     * 设置表类型。
     *
     * @param tableType 表类型
     */
    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    /**
     * 获取表注释。
     *
     * @return 表注释
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * 设置表注释。
     *
     * @param remarks 表注释
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
