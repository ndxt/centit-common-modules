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
public class SQLDataSetReader implements DataSetReader {


    /**
     * 读取 dataSet 数据集
     * @param params 模块的自定义参数
     * @return dataSet 数据集
     */
    @Override
    public DataSet load(Map<String, Object> params) {
        return null;
    }

}
