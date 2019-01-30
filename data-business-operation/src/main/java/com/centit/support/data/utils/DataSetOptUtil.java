package com.centit.support.data.utils;

import com.centit.framework.common.ObjectException;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.data.core.DataSet;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;

public abstract class DataSetOptUtil {

    public static DataSet groupbyStat(DataSet inData, List<String>  groupbyFields){
        // 根据维度进行排序

        // 这个函数需要传入 行头 列头

        return inData;
    }
    /***
     * 交叉制表 数据处理
     * @param inData 输入数据集
     * @return 输出数据集
     */
    public static DataSet crossTabulation(DataSet inData, List<String>  rowHeaderFields, List<String>  colHeaderFields){

        // 根据维度进行排序
        // 这个函数需要传入 行头 列头

        if(inData == null){
            return null;
        }
        List<Map<String, Object>> data = inData.getData();
        if(data == null || data.size() == 0){
            return inData;
        }
        if(rowHeaderFields.size() + colHeaderFields.size() + 1 != data.size()){
            throw new ObjectException("数据不合法");
        }

        List<Map<String, Object>> newData = new ArrayList<>();

        Map<String, Object> newRow = new HashMap<>();//data.size()/0.75
        for(Map<String, Object> row : data){

            for(Map.Entry<String, Object> entry : row.entrySet()) {
                for(String rowHeader : rowHeaderFields){
                    Object rowData = "";
                    if(Objects.equals(entry.getKey(), rowHeader)){
                        rowData = entry.getValue();
                        continue;
                    }
                }
                StringBuilder key = new StringBuilder();
                for(String colHeader : colHeaderFields){

                    if(Objects.equals(entry.getKey(), colHeader)){
                        key.append(entry.getValue()+":");
                        //continue;
                    }
                }
                //key:列头数据；value:除行头和列头数据
                newRow.put(key.toString(), entry.getValue());
            }
        }

        return inData;
    }

    public static DataSet compareTabulation(DataSet currData, DataSet lastData){
        // 这个函数需要传入 主键列
        // 需要根据主键排序
        return currData;
    }
}
