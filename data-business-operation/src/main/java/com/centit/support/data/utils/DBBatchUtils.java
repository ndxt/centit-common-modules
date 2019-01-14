package com.centit.support.data.utils;

import com.centit.support.database.jsonmaptable.GeneralJsonObjectDao;
import com.centit.support.database.metadata.TableInfo;
import com.centit.support.database.utils.DatabaseAccess;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据库批量操作 工具类，
 * 目的是为了提高批量处理的效率
 */
public abstract class DBBatchUtils {

    public int batchuUpdateObjects(final Connection conn,
                                   final TableInfo tableInfo,
                                   final Collection<String> fields,
                                   final List<Map<String, Object>> objects) throws SQLException {
        // 这个要重写，需要重新拼写sql语句， 直接拼写为？参数的sql语句据
        String sql = GeneralJsonObjectDao.buildUpdateSql(tableInfo, fields ,true) +
            " where " +  GeneralJsonObjectDao.buildFilterSqlByPk(tableInfo,null);
        int n = 0;
        for(Map<String, Object> object : objects ) {
            if (!GeneralJsonObjectDao.checkHasAllPkColumns(tableInfo, object)) {
                throw new SQLException("缺少主键对应的属性。");
            }
            n += DatabaseAccess.doExecuteNamedSql(conn, sql, object);
        }
        return n;
    }
}
