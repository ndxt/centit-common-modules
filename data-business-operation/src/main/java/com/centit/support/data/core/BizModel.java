package com.centit.support.data.core;

import java.util.HashMap;
import java.util.Map;

public class BizModel {
    public static final BizModel EMPTY_BIZ_MODEL
        = new BizModel("EMPTY_BIZ_MODEL");
    /**
     * 模型名称
     */
    private String modelName;
    /**
     * 模型的标识， 就是对应的主键
     */
    private Map<String, Object> modeTag;
    /**
     * 模型数据
     */
    private Map<String, DataSet> bizData;

    public BizModel(){

    }

    public BizModel(String modelName){
        this.modelName = modelName;

    }

    public void addDataSet(String dataSetName , DataSet dataSet) {
        if(this.bizData == null){
            this.bizData = new HashMap<>(5);
        }
        this.bizData.put(dataSetName, dataSet);
    }

    public boolean isEmpty(){
        return( bizData == null ||
            bizData.isEmpty());
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

    public Map<String, DataSet> getBizData() {
        return bizData;
    }

    public void setBizData(Map<String, DataSet> bizData) {
        this.bizData = bizData;
    }
}
