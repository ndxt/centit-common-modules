package com.centit.support.data.utils;

import com.centit.support.data.core.DataSet;

public abstract class DataSetOptUtil {
    /***
     * 交叉制表 数据处理
     * @param inData 输入数据集
     * @return 输出数据集
     */
    public static DataSet crossTabulation(DataSet inData){
        // 根据维度进行排序

        // 这个函数需要传入 行头 列头

        return inData;
    }

    public static DataSet compareTabulation(DataSet currData, DataSet lastData){
        // 这个函数需要传入 主键列
        // 需要根据主键排序
        return currData;
    }
}
