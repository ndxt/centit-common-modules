package com.centit.support.data.core;

import java.util.List;
import java.util.Map;

public interface BizModel {
    SimpleBizModel EMPTY_BIZ_MODEL
        = new SimpleBizModel("EMPTY_BIZ_MODEL");
    /**
     * 模型名称
     */
    String getModelName();
    /**
     * 模型的标识， 就是对应的主键
     * 或者对应关系数据库查询的参数（数据源参数）
     */
    Map<String, Object> getModeTag();
    /**
     * 模型数据
     */
    List<DataSet> getBizData();


    default boolean isEmpty(){
        return getBizData() == null ||
            getBizData().isEmpty();
    }

    default DataSet getFirstDataSet(){
        if(!isEmpty()){
            return getBizData().get(0);
        }
        return null;
    }
}
