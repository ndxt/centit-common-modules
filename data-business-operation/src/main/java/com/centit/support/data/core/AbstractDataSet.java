package com.centit.support.data.core;

import java.util.List;
import java.util.Map;

/**
 * 数据集 虚拟类
 */
public abstract class AbstractDataSet implements DataSet {

    /**
     * 返回 DataSet 的名称
     */
    String dataSetName;
    /**
     * 返回 DataSet 的类型
     */
    String dataSetType;

    /**
     * 数据集中的数据
     * 是一个 对象（Map）列表；可以类比为JSONArray
     */
    List<Map<String, Object>> data;
    /**
     * @return 是否已按照维度属性排序
     */
    @Override
    public boolean isSorted(){
        return false;
    }

    /**
     * 返回 所有数据维度，这个维度是有序的，这个属性不是必须的
     * @return 维度
     */
    @Override
    public List<String> getDimension(){
        return null;
    }

    @Override
    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }

    @Override
    public String getDataSetType() {
        return dataSetType;
    }

    public void setDataSetType(String dataSetType) {
        this.dataSetType = dataSetType;
    }

    protected abstract void fetchData();

    @Override
    public List<Map<String, Object>> getData() {
        if(data == null || data.isEmpty()){
            fetchData();
        }
        return data;
    }


    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
}
