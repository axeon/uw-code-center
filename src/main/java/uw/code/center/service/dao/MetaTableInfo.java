package uw.code.center.service.dao;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 数据库表的信息.
 *
 * @author axeon
 */
public class MetaTableInfo {

    /**
     * 表名.
     */
    private String tableName;

    /**
     * 实体名.
     */
    private String entityName;

    /**
     * 路径
     */
    private String entityPath;

    /**
     * 上级路径。
     */
    private String parentPath;

    /**
     * 表类型.
     */
    private String tableType;

    /**
     * 备注.
     */
    private String remarks;

    /**
     * 转化成字符串形式.
     *
     * @return String
     */
    @Override
    public String toString() {
        return "MetaTableInfo:" + tableName + "," + tableType + "," + remarks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        MetaTableInfo that = (MetaTableInfo) o;

        return new EqualsBuilder().append(tableName, that.tableName).append(tableType, that.tableType).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(tableName).append(tableType).toHashCode();
    }

    /**
     * @return the entityName
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * @param entityName the entityName to set
     */
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityPath() {
        return entityPath;
    }

    public void setEntityPath(String entityPath) {
        this.entityPath = entityPath;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return the tableType
     */
    public String getTableType() {
        return tableType;
    }

    /**
     * @param tableType the tableType to set
     */
    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    /**
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
