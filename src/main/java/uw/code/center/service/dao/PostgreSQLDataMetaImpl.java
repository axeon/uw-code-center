package uw.code.center.service.dao;

import org.apache.commons.lang3.StringUtils;
import uw.code.center.util.DaoStringUtils;
import uw.dao.connectionpool.ConnectionManager;

import java.sql.*;
import java.util.*;

/**
 * PostgreSQL数据库信息的工具类.
 *
 * <p>与 {@link MySQLDataMetaImpl} 结构保持一致，差异点：</p>
 * <ul>
 *     <li>schema 默认值为 {@code public}（PG 默认 schema），而非连接名；</li>
 *     <li>读取列/表元数据时统一传入 schema，避免跨 schema 混淆同名对象；</li>
 *     <li>补充 PostgreSQL 特有类型映射：{@code serial/bigserial}（自增整型）、{@code uuid/jsonb/xml}（驱动报告为 OTHER）。</li>
 * </ul>
 *
 * @author axeon
 */
public class PostgreSQLDataMetaImpl implements DataMetaInterface {

    /**
     * PostgreSQL默认schema。
     */
    private static final String DEFAULT_SCHEMA = "public";

    /**
     * 连接名.
     */
    private String connName = null;

    /**
     * PostgreSQL schema,默认为public.
     */
    private String schema = null;


    /**
     * 构造函数.
     *
     * @param connName 连接名
     * @param schema   PostgreSQL schema，为空时默认 public
     */
    public PostgreSQLDataMetaImpl(String connName, String schema) {
        this.connName = connName;
        this.schema = StringUtils.isBlank(schema) ? DEFAULT_SCHEMA : schema;
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
        List<MetaTableInfo> list = new ArrayList<MetaTableInfo>();
        try (Connection conn = getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            try (ResultSet rs = metaData.getTables(null, schema, null, new String[]{"TABLE", "VIEW"})) {
                while (rs.next()) {
                    MetaTableInfo meta = new MetaTableInfo();
                    meta.setTableName(rs.getString("TABLE_NAME").toLowerCase());
                    meta.setEntityName(DaoStringUtils.toUpperFirst(DaoStringUtils.toClearCase(meta.getTableName())));
                    meta.setEntityParent(DaoStringUtils.getTableParent(meta.getTableName()));
                    meta.setEntityPath(DaoStringUtils.toPathStyle(meta.getTableName()));
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
            }
        } catch (SQLException e) {
            // 读取表元数据失败属于异常情况，向上抛出由调用方/全局异常处理统一回传错误，避免静默返回空结果误导用户
            throw new RuntimeException("获取数据库表列表失败: " + e.getMessage(), e);
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
        List<MetaColumnInfo> list = new ArrayList<MetaColumnInfo>();
        // 加载主键列表
        HashSet<String> pset = new HashSet<String>();
        for (MetaPrimaryKeyInfo pk : pklist) {
            pset.add(pk.getColumnName());
        }
        try (Connection conn = getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            try (ResultSet rs = metaData.getColumns(null, schema, tableName, null)) {
                while (rs.next()) {
                    MetaColumnInfo meta = new MetaColumnInfo();
                    meta.setColumnName(rs.getString("COLUMN_NAME").toLowerCase()); // 列名
                    meta.setPropertyName(DaoStringUtils.toClearCase(meta.getColumnName()));
                    meta.setDataType(rs.getInt("DATA_TYPE")); // 字段数据类型(对应java.sql.Types中的常量)
                    String typeName = rs.getString("TYPE_NAME"); // 字段类型名称(例如：int4、varchar、timestamptz)
                    meta.setTypeName(typeName != null ? typeName.toLowerCase() : null);
                    meta.setColumnSize(rs.getInt("COLUMN_SIZE")); // 列的大小
                    meta.setRemarks(getSafeRemarks(rs.getString("REMARKS"))); // 描述列的注释
                    meta.setIsNullable(rs.getString("IS_NULLABLE").equals("YES") ? "true" : "false"); // 确定列是否包括
                    // null
                    meta.setIsAutoIncrement(rs.getString("IS_AUTOINCREMENT").equals("YES") ? "true" : null);// 确定列是否包括
                    // null
                    if (pset.contains(meta.getColumnName())) {
                        meta.setIsPrimaryKey("true");
                    }
                    mapJavaType(meta);
                    list.add(meta);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("获取数据库表字段信息失败: " + e.getMessage(), e);
        }
        return new ArrayList<>(new LinkedHashSet<>(list));
    }

    /**
     * PostgreSQL列类型到Java类型的映射。
     * 优先按具体类型名(serial/bigserial/uuid/json/jsonb/xml等PG特有类型)判定，退回jdbc标准类型常量。
     */
    private static void mapJavaType(MetaColumnInfo meta) {
        String typeName = meta.getTypeName();
        // PG特有类型优先处理（serial/bigserial 是自增整型；uuid/json/jsonb/xml 驱动报告为 OTHER，统一映射为 String）
        if (typeName != null) {
            switch (typeName) {
                case "serial":
                case "serial4":
                    meta.setPropertyType("int");
                    meta.setPropertyObject("Integer");
                    return;
                case "bigserial":
                case "serial8":
                    meta.setPropertyType("long");
                    meta.setPropertyObject("Long");
                    return;
                case "uuid":
                case "json":
                case "jsonb":
                case "xml":
                case "tsvector":
                case "citext":
                case "inet":
                case "cidr":
                case "macaddr":
                case "bytea":
                    meta.setPropertyType("String");
                    meta.setPropertyObject("String");
                    return;
                default:
                    break;
            }
        }
        switch (meta.getDataType()) {
            case Types.NUMERIC:
            case Types.DECIMAL:
                meta.setPropertyType("java.math.BigDecimal");
                meta.setPropertyObject("java.math.BigDecimal");
                break;
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
            case Types.CLOB:
            case Types.CHAR:
                meta.setPropertyType("String");
                meta.setPropertyObject("String");
                break;
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
            case Types.TIMESTAMP_WITH_TIMEZONE:
            case Types.TIME_WITH_TIMEZONE:
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
                meta.setPropertyType("int");
                meta.setPropertyObject("Integer");
                break;
            case Types.BIT:
            case Types.BOOLEAN:
                meta.setPropertyType("boolean");
                meta.setPropertyObject("Boolean");
                break;
            case Types.REAL:
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
    }

    private static String getSafeRemarks(String remarks) {
        return remarks != null ? remarks.replaceAll("\"", "") : null;
    }

    /**
     * 获取主键名。
     *
     * @param tableName 表名
     * @return 主键列表
     * @throws Exception 异常
     */
    @Override
    public List<MetaPrimaryKeyInfo> getPrimaryKey(String tableName) {
        List<MetaPrimaryKeyInfo> list = new ArrayList<MetaPrimaryKeyInfo>();
        try (Connection conn = getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            try (ResultSet rs = metaData.getPrimaryKeys(null, schema, tableName)) {
                while (rs.next()) {
                    MetaPrimaryKeyInfo meta = new MetaPrimaryKeyInfo();
                    meta.setTableName(rs.getString("TABLE_NAME").toLowerCase());
                    meta.setColumnName(rs.getString("COLUMN_NAME").toLowerCase());
                    meta.setPropertyName(DaoStringUtils.toClearCase(meta.getColumnName()));
                    meta.setKeySeq(rs.getInt("KEY_SEQ"));
                    meta.setPkName(rs.getString("PK_NAME") != null ? rs.getString("PK_NAME").toLowerCase() : null);
                    list.add(meta);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("获取数据库表主键信息失败: " + e.getMessage(), e);
        }
        return list;
    }
}
