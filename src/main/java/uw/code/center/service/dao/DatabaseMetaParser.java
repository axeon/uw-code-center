package uw.code.center.service.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uw.dao.conf.DaoConfig;
import uw.dao.conf.DaoConfigManager;
import uw.dao.connectionpool.ConnectionManager;
import uw.dao.dialect.Dialect;
import uw.dao.dialect.MySQLDialect;


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
     * 根据连接名获得DataMeta操作接口。
     * 对于oracle数据库，还需要传入connName，
     *
     * @param connName
     * @param schemaName
     * @return
     */
    public static DataMetaInterface getDataMetaInterface(String connName, String schemaName) {
        DaoConfig.ConnPoolConfig connPoolConfig = DaoConfigManager.getConnPoolConfig(connName);
        Dialect dialect = ConnectionManager.getDialect(connName);
        DataMetaInterface dataMetaInterface = null;
        if (dialect instanceof MySQLDialect) {
            dataMetaInterface = new MySQLDataMetaImpl(connName, schemaName);
        } else {
            dataMetaInterface = new OracleDataMetaImpl(connName, schemaName);
        }
        return dataMetaInterface;
    }

}
