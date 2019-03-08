package com.centit.support.data.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimpleBizModel implements BizModel{
    /**
     * 模型名称
     */
    private String modelName;
    /**
     * 模型的标识， 就是对应的主键
     * 或者对应关系数据库查询的参数（数据源参数）
     */
    private Map<String, Object> modeTag;
    /**
     * 模型数据
     */
    protected List<DataSet> bizData;

    public SimpleBizModel(){

    }

    public SimpleBizModel(String modelName){
        this.modelName = modelName;
    }

    public void addDataSet(DataSet dataSet) {
        if(this.bizData == null){
            this.bizData = new ArrayList<>(5);
        }
        this.bizData.add(dataSet);
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Map<String, Object> getModeTag() {
        return modeTag;
    }

    public void setModeTag(Map<String, Object> modeTag) {
        this.modeTag = modeTag;
    }

    public List<DataSet> getBizData() {
        return bizData;
    }

    public void setBizData(List<DataSet> bizData) {
        this.bizData = bizData;
    }
}
