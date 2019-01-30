package com.centit.support.data.dataset;

import com.centit.support.data.core.DataSet;
import com.centit.support.data.core.DataSetWriter;
import com.centit.support.data.utils.DBBatchUtils;
import com.centit.support.database.metadata.TableInfo;
import com.centit.support.database.utils.DataSourceDescription;
import com.centit.support.database.utils.TransactionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库数据集 读取和写入类
 * 需要设置的参数有：
 *      数据库连接信息 DatabaseInfo
 *      对应的表信息 SimpleTableInfo
 */
public class SQLDataSetWriter implements DataSetWriter {

    private static final Logger logger = LoggerFactory.getLogger(SQLDataSetWriter.class);
    private DataSourceDescription dataSource;

    private TableInfo tableInfo;

    private Connection connection;

    public SQLDataSetWriter(){
        connection = null;
    }
    /**
     * 将 dataSet 数据集 持久化
     *
     * @param dataSet 数据集
     */
    @Override
    public void save(DataSet dataSet) {
        try{
            if(connection == null) {
                TransactionHandler.executeInTransaction(dataSource,
                    (conn) -> DBBatchUtils.batchInsertObjects(conn,
                        tableInfo, dataSet.getData()));
            }else{
                TransactionHandler.executeInTransaction(connection,
                    (conn) -> DBBatchUtils.batchInsertObjects(conn,
                        tableInfo, dataSet.getData()));
            }
        } catch (SQLException e){
            logger.error(e.getLocalizedMessage());
        }
    }

    /**
     * 默认和 save 等效
     * 对于数据库类型的持久化来说可以有差别，比如合并，以避免主键冲突
     *
     * @param dataSet 数据集
     */
    @Override
    public void merge(DataSet dataSet) {
        try{
            if(connection == null) {
                TransactionHandler.executeInTransaction(dataSource,
                    (conn) -> DBBatchUtils.batchMergeObjects(conn,
                        tableInfo, dataSet.getData()));
            }else{
                TransactionHandler.executeInTransaction(connection,
                    (conn) -> DBBatchUtils.batchMergeObjects(conn,
                        tableInfo, dataSet.getData()));
            }
        } catch (SQLException e){
            logger.error(e.getLocalizedMessage());
        }

    }

    public void setDataSource(DataSourceDescription dataSource) {
        this.dataSource = dataSource;
    }

    public void setTableInfo(TableInfo tableInfo) {
        this.tableInfo = tableInfo;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
