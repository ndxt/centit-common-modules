package com.centit.support.data.utils;

import com.alibaba.fastjson.JSON;
import com.centit.framework.common.ObjectException;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.data.core.DataSet;
import com.centit.support.data.core.SimpleDataSet;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

public abstract class DataSetOptUtil {

    public static DataSet groupbyStat(DataSet inData, List<String> groupbyFields) {
        // 根据维度进行排序

        // 这个函数需要传入 行头 列头

        return inData;
    }

    /***
     * 交叉制表 数据处理
     * @param inData 输入数据集
     * @return 输出数据集
     */
    public static DataSet crossTabulation(DataSet inData, List<String> rowHeaderFields, List<String> colHeaderFields) {
        if (inData == null) {
            return null;
        }
        List<Map<String, Object>> data = inData.getData();
        if (data == null || data.size() == 0) {
            return inData;
        }
        if (rowHeaderFields.size() + colHeaderFields.size() + 1 != data.get(0).size()) {
            throw new RuntimeException("数据不合法");
        }
        // TODO 根据维度进行排序 行头、列头


        List<Map<String, Object>> newData = new ArrayList<>();

        Map<String, Object> newRow = new LinkedHashMap<>();//data.size()/0.75

        String[] rowDataCompare = null;
        for (Map<String, Object> row : data) {
            StringBuilder key = new StringBuilder();
            Object value = null;

            String[] rowData = new String[rowHeaderFields.size()];
            String[] colData = new String[colHeaderFields.size()];

            for (Map.Entry<String, Object> entry : row.entrySet()) {
                String oldKey = entry.getKey();

                if (rowHeaderFields.contains(oldKey)) {
                    int rowIndex = 0;
                    for (String rowHeader : rowHeaderFields) {
                        if (Objects.equals(oldKey, rowHeader)) {
                            rowData[rowIndex++] = StringBaseOpt.castObjectToString(entry.getValue());
                            break;
                        }
                        rowIndex++;
                    }
                    continue;
                }
                if (colHeaderFields.contains(oldKey)) {
                    int colIndex = 0;
                    for (String colHeader : colHeaderFields) {
                        if (Objects.equals(oldKey, colHeader)) {
                            colData[colIndex++] = StringBaseOpt.castObjectToString(entry.getValue());
                            break;
                        }
                        colIndex++;
                    }
                    continue;
                }
                value = entry.getValue();
            }
            key.append(colData[0]);
            for (int i = 1; i < colData.length; i++) {
                key.append(":");
                key.append(colData[i]);
            }

            if (rowDataCompare != null && !Arrays.equals(rowData, rowDataCompare)) {
                Map<String, Object> map = new LinkedHashMap<>();//data.size()/0.75
                map.putAll(newRow);
                newRow.clear();
                newData.add(map);
            }
            if (rowDataCompare == null || !Arrays.equals(rowData, rowDataCompare)) {//行头
                for (int i = 0; i < rowData.length; i++) {
                    newRow.put(rowHeaderFields.get(i), rowData[i]);
                }
            }
            rowDataCompare = ArrayUtils.clone(rowData);
            newRow.put(key.toString(), value);
        }
        newData.add(newRow);

        inData.getData().clear();
        inData.getData().addAll(newData);
        return inData;
    }

    public static DataSet compareTabulation(DataSet currData, DataSet lastData) {
        // 这个函数需要传入 主键列
        // 需要根据主键排序
        return currData;
    }

    private static void sortByField(List<Map<String, Object>> data, List<String> fields) {

        for(Map<String, Object> row : data){

        }
    }

    public static void main(String[] args) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("century", "21");
            map.put("year", "2018");
            map.put("season", i + 1);
            map.put("month", (i + 1) * 3);
            map.put("seal", 10 + i);
            list.add(map);
        }
        for (int i = 0; i < 3; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("century", "21");
            map.put("year", "2019");
            map.put("season", i + 1);
            map.put("month", (i + 1) * 3);
            map.put("seal", 30 + i);
            list.add(map);
        }

        List<String> rowHeader = new ArrayList<>();
        List<String> colHeader = new ArrayList<>();
        rowHeader.add("century");
        rowHeader.add("year");
        colHeader.add("season");
        colHeader.add("month");
        SimpleDataSet dataSet = new SimpleDataSet();
        dataSet.setData(list);
        DataSet result = crossTabulation(dataSet, rowHeader, colHeader);
        System.out.println(JSON.toJSONString(result.getData()));
    }
}
