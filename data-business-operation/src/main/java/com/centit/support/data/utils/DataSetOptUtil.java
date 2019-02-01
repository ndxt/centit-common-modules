package com.centit.support.data.utils;

import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.data.core.DataSet;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public abstract class DataSetOptUtil {

    public static DataSet groupbyStat(DataSet inData, List<String> groupbyFields) {
        if (inData == null) {
            return null;
        }
        List<Map<String, Object>> data = inData.getData();
        if (data == null || data.size() == 0) {
            return inData;
        }
        //按group by字段排序
        sortByFields(data, groupbyFields);

        List<Map<String, Object>> newData = new ArrayList<>();
        Map<String, Object> newRow = new LinkedHashMap<>();

        Object[] groupByData = new Object[groupbyFields.size()];
        Object[] groupByDataCompare = null;

        Map<String, List<Object>> statDatas = new HashMap<>();//(data.get(0).size()-groupbyFields.size())/0.75

        for (Map<String, Object> row : data) {
            int i = 0;
            Map<String, Object> statData = new HashMap<>();
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if(groupbyFields.contains(key)){
                    groupByData[i++] = value;
                    continue;
                }

                statData.put(key, value);
            }
            if(groupByDataCompare != null && !Arrays.equals(groupByData, groupByDataCompare)){
                for(Map.Entry<String, List<Object>> statEntry : statDatas.entrySet()){
                    //todo 判断类型
                    Object[] number = statEntry.getValue().toArray();
                    int sum = 0;
                    for(int j = 0; j < number.length; j++){
                        sum += NumberBaseOpt.castObjectToInteger(number[j]);
                    }
                    newRow.replace(statEntry.getKey(), sum);
                }
                Map<String, Object> map = new LinkedHashMap<>();//data.size()/0.75
                map.putAll(newRow);
                newData.add(map);
                newRow.clear();
                statDatas.clear();
            }
            for(Map.Entry<String, Object> stat : statData.entrySet()) {
                if (statDatas.get(stat.getKey()) != null) {
                    statDatas.get(stat.getKey()).add(stat.getValue());
                } else {
                    List<Object> list = new ArrayList<>();
                    list.add(stat.getValue());
                    statDatas.put(stat.getKey(), list);
                }
            }
            newRow.putAll(row);
            groupByDataCompare = ArrayUtils.clone(groupByData);
        }
        for(Map.Entry<String, List<Object>> statEntry : statDatas.entrySet()){
            //todo 判断类型
            Object[] number = statEntry.getValue().toArray();
            int sum = 0;
            for(int j = 0; j < number.length; j++){
                sum += NumberBaseOpt.castObjectToInteger(number[j]);
            }
            newRow.replace(statEntry.getKey(), sum);
        }
        newData.add(newRow);

        inData.getData().clear();
        inData.getData().addAll(newData);
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
        //根据维度进行排序 行头、列头
        sortByFields(data, ListUtils.union(rowHeaderFields, colHeaderFields));

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

    public static DataSet compareTabulation(DataSet currData, DataSet lastData, List<String> primaryFields) {
        // 这个函数需要传入 主键列
        // 需要根据主键排序
        return currData;
    }

    private static void sortByFields(List<Map<String, Object>> data, List<String> fields) {

        Collections.sort(data, (o1, o2) -> {
            int[] sort = new int[fields.size()];
            int i = 0;
            for(String field : fields){
                sort[i++] = StringUtils.compare(StringBaseOpt.castObjectToString(o1.get(field)), StringBaseOpt.castObjectToString(o2.get(field)));
            }
            for(int j = 0; j < sort.length; j++){
                if(sort[j] != 0){
                    return sort[j];
                }
            }
            return 0;
        });

    }
}
