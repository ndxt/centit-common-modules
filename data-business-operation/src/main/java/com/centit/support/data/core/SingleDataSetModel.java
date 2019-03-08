package com.centit.support.data.core;

import java.util.ArrayList;

public class SingleDataSetModel extends SimpleBizModel {

    public SingleDataSetModel(){
        super();
        bizData = new ArrayList<>(1);
    }

    public SingleDataSetModel(String modelName){
        super(modelName);
        bizData = new ArrayList<>(1);
    }

    public SingleDataSetModel(DataSet dataSet){
        super();
        bizData = new ArrayList<>(1);
        bizData.add(dataSet);
    }

    public SingleDataSetModel(String modelName, DataSet dataSet){
        super(modelName);
        bizData = new ArrayList<>(1);
        bizData.add(dataSet);
    }

    public void setDataSet(DataSet dataSet){
        if(bizData == null){
            bizData = new ArrayList<>(1);
            bizData.add(dataSet);
        } else if(bizData.size() == 0){
            bizData.add(dataSet);
        } else {
            bizData.set(0, dataSet);
        }
    }

    public DataSet getDataSet(){
        if(bizData == null || bizData.size() == 0){
            return null;
        } else {
            return bizData.get(0);
        }
    }
}
