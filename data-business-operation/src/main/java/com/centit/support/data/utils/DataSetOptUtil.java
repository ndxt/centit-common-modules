package com.centit.support.data.utils;

import com.centit.support.algorithm.GeneralAlgorithm;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.compiler.VariableFormula;
import com.centit.support.data.core.DataSet;
import com.centit.support.data.core.SimpleDataSet;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public abstract class DataSetOptUtil {
    /**
     * 数据集 映射
     * @param inData 原始数据集
     * @param fieldMap 字段映射关系
     * @return 新的数据集
     */
    public static DataSet mapDateSet(DataSet inData, Map<String, String> fieldMap) {
        List<Map<String, Object>> data = inData.getData();
        List<Map<String, Object>> newData = new ArrayList<>(data.size());
        Set<Map.Entry<String, String>> mapSet = fieldMap.entrySet();
        for(Map<String, Object> obj : data ){
            Map<String, Object> newRow = new HashMap<>(mapSet.size());
            for(Map.Entry<String, String> ent : mapSet){
                newRow.put(ent.getKey(), obj.get(ent.getValue()));
            }
            newData.add(newRow);
        }
        return new SimpleDataSet(newData);
    }

    /**
     * 数据集 映射
     * @param inData 原始数据集
     * @param formulaMap 字段映射关系， value为计算表达式
     * @return 新的数据集
     */
    public static DataSet mapDateSetByFormula(DataSet inData, Map<String, String> formulaMap) {
        List<Map<String, Object>> data = inData.getData();
        List<Map<String, Object>> newData = new ArrayList<>(data.size());
        Set<Map.Entry<String, String>> mapSet = formulaMap.entrySet();
        for(Map<String, Object> obj : data ){
            Map<String, Object> newRow = new HashMap<>(mapSet.size());
            for(Map.Entry<String, String> ent : mapSet){
                newRow.put(ent.getKey(),
                    VariableFormula.calculate(ent.getValue(), obj));
            }
            newData.add(newRow);
        }
        return new SimpleDataSet(newData);
    }

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
        Map<String, Object> newRow = new HashMap<>();

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
                    newRow.replace(statEntry.getKey(), GeneralAlgorithm.sumObjects(statEntry.getValue()));
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
            newRow.replace(statEntry.getKey(), GeneralAlgorithm.sumObjects(statEntry.getValue()));
        }
        newData.add(newRow);

        //inData.getData().clear();
        //inData.getData().addAll(newData);
        return new SimpleDataSet(newData);
    }

    /***
     * 交叉制表 数据处理
     * @param inData 输入数据集
     * @param colHeaderFields 列头信息
     * @param rowHeaderFields 行头信息
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

    /**
     * 同环比转换
     * @param currDataSet 本期数据集
     * @param lastDataSet 上期数据集
     * @param primaryFields 主键列
     */
    public static DataSet compareTabulation(DataSet currDataSet, DataSet lastDataSet, List<String> primaryFields) {
        if (currDataSet == null || lastDataSet == null) {
            return null;
        }
        List<Map<String, Object>> currData = currDataSet.getData();
        List<Map<String, Object>> lastData = lastDataSet.getData();
        if (currData == null || currData.size() == 0 || lastData == null || lastData.size() == 0) {
            throw new RuntimeException("数据不合法");
        }
        if(currData.size() != lastData.size()){
            throw new RuntimeException("数据不合法");
        }
        List<Map<String, Object>> newData = new ArrayList<>();
        // 根据主键排序
        sortByFields(currData, primaryFields);
        sortByFields(lastData, primaryFields);
        for(int i = 0; i < currData.size(); i++){
            Map<String, Object> currentRow = currData.get(i);
            Map<String, Object> lastRow = lastData.get(i);

            Map<String, Object> newRow = new LinkedHashMap<>();
            for(String primaryField : primaryFields){
                newRow.put(primaryField, currentRow.get(primaryField));
                currentRow.remove(primaryField);
            }
            for(Map.Entry<String, Object> entry : currentRow.entrySet()){
                String key = entry.getKey();
                Object value = entry.getValue();
                newRow.put(key+":上期", lastRow.get(key));
                newRow.put(key+":本期", value);
            }
            newData.add(newRow);
        }
        currDataSet.getData().clear();
        currDataSet.getData().addAll(newData);
        return currDataSet;
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
