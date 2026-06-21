package uw.code.center.service.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * 数据库元数据读取接口。
 * <p>
 * 屏蔽不同数据库（MySQL / PostgreSQL / Oracle）在元数据读取上的差异，由 {@link DatabaseMetaParser}
 * 根据连接的 {@code Dialect} 选取对应实现。所有方法在读取失败时抛出 {@link RuntimeException}，
 * 由上层统一回传错误，避免静默返回空结果。
 * </p>
 *
 * @since 2017/9/11
 */
public interface DataMetaInterface {


    /**
     * 获取数据库连接。
     *
     * @return Connection
     * @throws SQLException SQL异常
     */
    Connection getConnection() throws SQLException;

    /**
     * 获取生成表的元数据。
     *
     * @param tables 表名集合（用于过滤）；为空时返回全部表/视图
     * @return 表元数据列表
     */
    List<MetaTableInfo> getTablesAndViews(Set<String> tables);

    /**
     * 获取生成表的列数据。
     *
     * @param tableName 表名
     * @param pkList    主键信息集合（用于标注列是否为主键）
     * @return 列元数据列表
     */
    List<MetaColumnInfo> getColumnList(String tableName, List<MetaPrimaryKeyInfo> pkList);

    /**
     * 获取生成表的主键数据。
     *
     * @param tableName 表名
     * @return 主键元数据列表
     */
    List<MetaPrimaryKeyInfo> getPrimaryKey(String tableName);
}
