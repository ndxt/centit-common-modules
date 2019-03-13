package com.centit.support.dataopt.core;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public interface BizModel {
    SimpleBizModel EMPTY_BIZ_MODEL
        = new SimpleBizModel("EMPTY_BIZ_MODEL");
    /**
     * @return 模型名称
     */
    String getModelName();
    /**
     * 模型的标识， 就是对应的主键
     * @return  或者对应关系数据库查询的参数（数据源参数）
     */
    Map<String, Object> getModeTag();
    /**
     * @return  模型数据
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

    default DataSet fetchDataSetByName(String dataSetName){
        List<DataSet>  dss = getBizData();
        if(dss == null) {
            return null;
        }

        for(DataSet ds : dss){
            if(StringUtils.equals(ds.getDataSetName(),dataSetName)) {
                return ds;
            }
        }
        return null;
    }
}
