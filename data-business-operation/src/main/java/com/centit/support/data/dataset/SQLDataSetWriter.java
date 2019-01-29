package com.centit.support.data.dataset;

import com.centit.support.data.core.DataSet;
import com.centit.support.data.core.DataSetReader;
import com.centit.support.data.core.DataSetWriter;

import java.util.Map;

/**
 * 数据库数据集 读取和写入类
 * 需要设置的参数有：
 *      数据库连接信息 DatabaseInfo
 *      对应的表信息 SimpleTableInfo
 */
public class SQLDataSetWriter implements DataSetWriter {

    /**
     * 将 dataSet 数据集 持久化
     *
     * @param dataSet 数据集
     */
    @Override
    public void save(DataSet dataSet) {

    }

    /**
     * 默认和 save 等效
     * 对于数据库类型的持久化来说可以有差别，比如合并，以避免主键冲突
     *
     * @param dataSet 数据集
     */
    @Override
    public void merge(DataSet dataSet) {

    }
}
