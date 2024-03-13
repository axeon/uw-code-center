package uw.code.center.service.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uw.code.center.util.DaoStringUtils;
import uw.dao.DaoFactory;
import uw.dao.DataSet;
import uw.dao.TransactionException;
import uw.dao.connectionpool.ConnectionManager;

import java.sql.*;
import java.util.*;

/**
 * Oracle表生成信息处理.
 *
 * 
 * @since 2017/9/11
 */
public class OracleDataMetaImpl implements DataMetaInterface {

    /**
     * 日志.
     */
    private static final Logger logger = LoggerFactory.getLogger(OracleDataMetaImpl.class);

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
    public OracleDataMetaImpl(String connName, String schema) {
        this.connName = connName;
        this.schema = schema == null || schema.equals("") ? connName.toUpperCase() : schema.toUpperCase();
    }

    /**
     * 获得数据库链接.
     *
     * @return Connection对象
     */
    @Override
    public Connection getConnection() throws SQLException {
        if (connName == null || connName.equals("")) {
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
        for (String tableName : tables) {
            try {

                String sql = "SELECT\n" + "	c.TABLE_NAME,\n" + "	c.TABLE_TYPE,\n"
                        + "	DECODE(nvl(c.COMMENTS,'null'),'null','',c.COMMENTS) as REMARKS\n" + "FROM\n"
                        + "	all_tables T,\n" + "	user_tab_comments c\n" + "WHERE\n" + "	T . OWNER = '" + schema
                        + "'\n" + "AND c.table_name = T .table_name\n" + "AND c.table_name = upper('" + tableName
                        + "')";
                DaoFactory daoFactory = DaoFactory.getInstance();
                DataSet dataSet = daoFactory.queryForDataSet(connName, sql);
                while (dataSet.next()) {
                    MetaTableInfo meta = new MetaTableInfo();
                    meta.setTableName(tableName.toLowerCase());
                    dataSet.getString("TABLE_NAME");
                    meta.setEntityName(DaoStringUtils.toUpperFirst(DaoStringUtils.toClearCase(meta.getTableName())));
                    meta.setEntityPath(DaoStringUtils.toPathStyle(meta.getTableName()));
                    meta.setParentPath(DaoStringUtils.getParentPath(meta.getEntityPath()));
                    meta.setTableType(dataSet.getString("TABLE_TYPE").toLowerCase());
                    meta.setRemarks(dataSet.getString("REMARKS"));
                    if (meta.getRemarks() == null || "".equals(meta.getRemarks())) {
                        meta.setRemarks(tableName);
                    }
                    if (tables.size() > 0) {
                        if (tables.contains(tableName.toLowerCase())) {
                            list.add(meta);
                        }
                    } else {
                        list.add(meta);
                    }
                }
            } catch (TransactionException e) {
                logger.error(e.getMessage(), e);
            }
        }

        return list;
    }

    /**
     * 利用表名和数据库用户名查询出该表对应的字段类型.
     *
     * @param tableName 表名
     * @return 表字段信息的列表
     * @throws Exception
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
            HashMap<String, String> remarkhs = new HashMap<String, String>();
            // 查询注释
            DaoFactory daoFactory = DaoFactory.getInstance();
            DataSet dataSet = daoFactory.queryForDataSet(connName, "select  t.table_name,t.column_name,t.comments from USER_COL_COMMENTS t where t.TABLE_NAME=upper('" + tableName + "')");
            while (dataSet.next()) {
                remarkhs.put(dataSet.getString("column_name"), dataSet.getString("comments"));
            }
            conn = getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            rs = metaData.getColumns(null, conn.getSchema(), tableName.toUpperCase(), null);
            while (rs.next()) {
                MetaColumnInfo meta = new MetaColumnInfo();
                meta.setColumnName(rs.getString("COLUMN_NAME").toLowerCase()); // 列名
                meta.setPropertyName(DaoStringUtils.toClearCase(meta.getColumnName()));
                meta.setDataType(rs.getInt("DATA_TYPE")); // 字段数据类型(对应java.sql.Types中的常量)
                meta.setTypeName(rs.getString("TYPE_NAME").toLowerCase()); // 字段类型名称(例如：VACHAR2)
                meta.setColumnSize(rs.getInt("COLUMN_SIZE")); // 列的大小
                meta.setRemarks(remarkhs.get(rs.getString("COLUMN_NAME"))); // 描述列的注释
                meta.setIsNullable(rs.getString("IS_NULLABLE").equals("YES") ? "true" : "false"); // 确定列是否包括
                // null
                meta.setIsAutoIncrement(rs.getString("IS_AUTOINCREMENT").equals("YES") ? "true" : null); // 确定列是否包括
                // null
                if (pset.contains(meta.getColumnName())) {
                    meta.setIsPrimaryKey("true");
                }
                switch (meta.getDataType()) {
                    case Types.NUMERIC:
                        if (rs.getInt("COLUMN_SIZE") < 10) {
                            meta.setPropertyType("int");
                            meta.setPropertyObject("Integer");
                        } else if (rs.getInt("COLUMN_SIZE") < 18) {
                            meta.setPropertyType("long");
                            meta.setPropertyObject("Long");
                        } else {
                            meta.setPropertyType("java.math.BigDecimal");
                            meta.setPropertyObject("java.math.BigDecimal");
                        }
                        break;
                    case Types.VARCHAR:
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
        return list;
    }

    /**
     * 获得主键名.
     *
     * @param tableName 表名
     * @return 主键列表
     * @throws Exception 异常
     */
    @Override
    public List<MetaPrimaryKeyInfo> getPrimaryKey(String tableName) {
        List<MetaPrimaryKeyInfo> list = new ArrayList<MetaPrimaryKeyInfo>();
        try {
            String sql = "SELECT\n" + "	\"SYS\".\"ALL_CONS_COLUMNS\".\"OWNER\",\n"
                    + "	\"SYS\".\"ALL_CONS_COLUMNS\".\"CONSTRAINT_NAME\" PK_NAME,\n"
                    + "	\"SYS\".\"ALL_CONS_COLUMNS\".\"TABLE_NAME\",\n"
                    + "	\"SYS\".\"ALL_CONS_COLUMNS\".\"COLUMN_NAME\" COLUMN_NAME\n" + "FROM\n"
                    + "	\"SYS\".\"ALL_CONS_COLUMNS\"\n" + "JOIN \"SYS\".\"ALL_CONSTRAINTS\" ON (\n"
                    + "	\"SYS\".\"ALL_CONS_COLUMNS\".\"OWNER\" = \"SYS\".\"ALL_CONSTRAINTS\".\"OWNER\"\n"
                    + "	AND \"SYS\".\"ALL_CONS_COLUMNS\".\"CONSTRAINT_NAME\" = \"SYS\".\"ALL_CONSTRAINTS\".\"CONSTRAINT_NAME\"\n"
                    + ")\n" + "WHERE\n" + "	(\n" + "		\"SYS\".\"ALL_CONSTRAINTS\".\"CONSTRAINT_TYPE\" = 'P'\n"
                    + "		AND \"SYS\".\"ALL_CONSTRAINTS\".\"CONSTRAINT_NAME\" NOT LIKE 'BIN$%'\n"
                    + "		AND UPPER (\n" + "			\"SYS\".\"ALL_CONS_COLUMNS\".\"OWNER\"\n" + "		) IN ('"
                    + schema + "')\n" + "		AND \"SYS\".\"ALL_CONS_COLUMNS\".\"TABLE_NAME\" = upper('" + tableName
                    + "')\n" + "	)\n" + "ORDER BY\n" + "	\"SYS\".\"ALL_CONS_COLUMNS\".\"OWNER\" ASC,\n"
                    + "	\"SYS\".\"ALL_CONS_COLUMNS\".\"CONSTRAINT_NAME\" ASC,\n"
                    + "	\"SYS\".\"ALL_CONS_COLUMNS\".\"POSITION\" ASC";
            DaoFactory daoFactory = DaoFactory.getInstance();
            DataSet dataSet = daoFactory.queryForDataSet(connName, sql);
            while (dataSet.next()) {
                MetaPrimaryKeyInfo meta = new MetaPrimaryKeyInfo();
                meta.setTableName(dataSet.getString("TABLE_NAME").toLowerCase());
                meta.setColumnName(dataSet.getString("COLUMN_NAME").toLowerCase());
                meta.setPropertyName(DaoStringUtils.toClearCase(meta.getColumnName()));
                meta.setKeySeq(0); // 好像没有用到?
                meta.setPkName(dataSet.getString("PK_NAME").toLowerCase());
                list.add(meta);
            }
        } catch (TransactionException e) {
            logger.error(e.getMessage(), e);
        }

        return list;
    }
}
