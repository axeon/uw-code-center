package uw.code.center.service.dao;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uw.code.center.util.DaoStringUtils;
import uw.dao.connectionpool.ConnectionManager;

import java.sql.*;
import java.util.*;

/**
 * 数据库信息的工具类.
 *
 * @author axeon
 */
public class MySQLDataMetaImpl implements DataMetaInterface {

    /**
     * 日志.
     */
    private static final Logger logger = LoggerFactory.getLogger(MySQLDataMetaImpl.class);

    /**
     * 连接名.
     */
    private String connName = null;

    /**
     * Oracle schema,默认等于连接名.
     */
    private String schema = null;


    /**
     * 构造函数.
     *
     * @param connName 连接名
     * @param schema   Oracle schema
     */
    public MySQLDataMetaImpl(String connName, String schema) {
        this.connName = connName;
        this.schema = StringUtils.isBlank(schema) ? connName : schema;
    }

    /**
     * 获取数据库链接.
     *
     * @return Connection对象
     * @throws SQLException SQL异常
     */
    @Override
    public Connection getConnection() throws SQLException {
        if (StringUtils.isBlank(connName)) {
            return ConnectionManager.getConnection();
        } else {
            return ConnectionManager.getConnection(connName);
        }
    }

    /**
     * 获取数据库中的表名称与视图名称.
     *
     * @param tables 表集合
     * @return 表信息的列表
     */
    @Override
    public List<MetaTableInfo> getTablesAndViews(Set<String> tables) {
        Connection conn = null;
        ResultSet rs = null;
        List<MetaTableInfo> list = new ArrayList<MetaTableInfo>();
        try {
            conn = getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            rs = metaData.getTables(schema, null, null, new String[]{"TABLE", "VIEW"});
            while (rs.next()) {
                MetaTableInfo meta = new MetaTableInfo();
                meta.setTableName(rs.getString("TABLE_NAME").toLowerCase());
                meta.setEntityName(DaoStringUtils.toUpperFirst(DaoStringUtils.toClearCase(meta.getTableName())));
                meta.setEntityPath(DaoStringUtils.toPathStyle(meta.getTableName()));
                meta.setParentPath(DaoStringUtils.getParentPath(meta.getEntityPath()));
                meta.setTableType(rs.getString("TABLE_TYPE").toLowerCase());
                meta.setRemarks(rs.getString("REMARKS"));
                if (meta.getRemarks() == null || "".equals(meta.getRemarks())) {
                    meta.setRemarks(meta.getTableName());
                }
                if (tables.size() > 0) {
                    if (tables.contains(meta.getTableName())) {
                        list.add(meta);
                    }
                } else {
                    list.add(meta);
                }
            }
            rs.close();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return new ArrayList<>(new LinkedHashSet<>(list));
    }

    /**
     * 利用表名和数据库用户名查询出该表对应的字段类型.
     *
     * @param tableName 表名
     * @return 表字段信息的列表
     * @throws Exception 异常
     */
    @Override
    public List<MetaColumnInfo> getColumnList(String tableName, List<MetaPrimaryKeyInfo> pklist) {
        Connection conn = null;
        ResultSet rs = null;
        List<MetaColumnInfo> list = new ArrayList<MetaColumnInfo>();
        // 加载主键列表
        HashSet<String> pset = new HashSet<String>();
        for (MetaPrimaryKeyInfo pk : pklist) {
            pset.add(pk.getColumnName());
        }
        try {
            conn = getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            rs = metaData.getColumns(schema, null, tableName, null);
            while (rs.next()) {
                MetaColumnInfo meta = new MetaColumnInfo();
                meta.setColumnName(rs.getString("COLUMN_NAME").toLowerCase()); // 列名
                meta.setPropertyName(DaoStringUtils.toClearCase(meta.getColumnName()));
                meta.setDataType(rs.getInt("DATA_TYPE")); // 字段数据类型(对应java.sql.Types中的常量)
                meta.setTypeName(rs.getString("TYPE_NAME").toLowerCase()); // 字段类型名称(例如：VACHAR2)
                meta.setColumnSize(rs.getInt("COLUMN_SIZE")); // 列的大小
                meta.setRemarks(rs.getString("REMARKS").replaceAll("\"", "")); // 描述列的注释
                meta.setIsNullable(rs.getString("IS_NULLABLE").equals("YES") ? "true" : "false"); // 确定列是否包括
                // null
                meta.setIsAutoIncrement(rs.getString("IS_AUTOINCREMENT").equals("YES") ? "true" : null);// 确定列是否包括
                // null
                if (pset.contains(meta.getColumnName())) {
                    meta.setIsPrimaryKey("true");
                }
                switch (meta.getDataType()) {
                    case Types.NUMERIC:
                        meta.setPropertyType("java.math.BigDecimal");
                        meta.setPropertyObject("java.math.BigDecimal");
                        break;
                    case Types.VARCHAR:
                    case Types.LONGVARCHAR:
                    case Types.CLOB:
                        meta.setPropertyType("String");
                        meta.setPropertyObject("String");
                        break;
                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                        meta.setPropertyType("java.util.Date");
                        meta.setPropertyObject("java.util.Date");
                        break;
                    case Types.BIGINT:
                        meta.setPropertyType("long");
                        meta.setPropertyObject("Long");
                        break;
                    case Types.INTEGER:
                    case Types.SMALLINT:
                    case Types.TINYINT:
                    case Types.BIT:
                        meta.setPropertyType("int");
                        meta.setPropertyObject("Integer");
                        break;
                    case Types.FLOAT:
                        meta.setPropertyType("float");
                        meta.setPropertyObject("Float");
                        break;
                    case Types.DOUBLE:
                        meta.setPropertyType("double");
                        meta.setPropertyObject("Double");
                        break;
                    default:
                        meta.setPropertyType("Object");
                        meta.setPropertyObject("Object");
                }
                list.add(meta);
            }
            rs.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return new ArrayList<>(new LinkedHashSet<>(list));

    }

    /**
     * 获取主键名.
     *
     * @param tableName 表名
     * @return 主键列表
     * @throws Exception 异常
     */
    @Override
    public List<MetaPrimaryKeyInfo> getPrimaryKey(String tableName) {
        Connection conn = null;
        ResultSet rs = null;
        List<MetaPrimaryKeyInfo> list = new ArrayList<MetaPrimaryKeyInfo>();
        try {
            conn = getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            rs = metaData.getPrimaryKeys(null, null, tableName);
            while (rs.next()) {
                MetaPrimaryKeyInfo meta = new MetaPrimaryKeyInfo();
                meta.setTableName(rs.getString("TABLE_NAME").toLowerCase());
                meta.setColumnName(rs.getString("COLUMN_NAME").toLowerCase());
                meta.setPropertyName(DaoStringUtils.toClearCase(meta.getColumnName()));
                meta.setKeySeq(rs.getInt("KEY_SEQ"));
                meta.setPkName(rs.getString("PK_NAME").toLowerCase());
                list.add(meta);
            }
            rs.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return list;
    }
}
