package uw.code.center.service.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uw.dao.conf.DaoConfig;
import uw.dao.conf.DaoConfigManager;
import uw.dao.connectionpool.ConnectionManager;
import uw.dao.dialect.Dialect;
import uw.dao.dialect.MySQLDialect;
import uw.dao.dialect.PostgreSQLDialect;


/**
 * 代码生成入口.
 *
 * @author axeon
 */
public class DatabaseMetaParser {

    /**
     * 日志.
     */
    private static final Logger logger = LoggerFactory.getLogger(DatabaseMetaParser.class);


    /**
     * 根据连接名获取 DataMeta 操作接口。
     * <p>
     * 通过 {@link ConnectionManager#getDialect(String)} 识别数据库类型：MySQL → {@link MySQLDataMetaImpl}，
     * PostgreSQL → {@link PostgreSQLDataMetaImpl}，其余（Oracle 等）→ {@link OracleDataMetaImpl}。
     * </p>
     *
     * @param connName   连接池名称
     * @param schemaName schema 名称（Oracle 需传用户名；PostgreSQL 默认 public；MySQL 可为空）
     * @return 对应数据库的 {@link DataMetaInterface} 实现
     */
    public static DataMetaInterface getDataMetaInterface(String connName, String schemaName) {
        DaoConfig.ConnPoolConfig connPoolConfig = DaoConfigManager.getConnPoolConfig(connName);
        Dialect dialect = ConnectionManager.getDialect(connName);
        DataMetaInterface dataMetaInterface = null;
        if (dialect instanceof MySQLDialect) {
            dataMetaInterface = new MySQLDataMetaImpl(connName, schemaName);
        } else if (dialect instanceof PostgreSQLDialect) {
            dataMetaInterface = new PostgreSQLDataMetaImpl(connName, schemaName);
        } else {
            dataMetaInterface = new OracleDataMetaImpl(connName, schemaName);
        }
        return dataMetaInterface;
    }

}
