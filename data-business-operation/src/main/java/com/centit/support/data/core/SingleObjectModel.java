package com.centit.support.data.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SingleObjectModel extends BizModel {

    public SingleObjectModel(){
        super();
        bizData = new ArrayList<>(1);
    }

    public SingleObjectModel(Object obj){
        super();
        bizData = new ArrayList<>(1);
        bizData.add(new SingleObjectDataSet(obj));
    }

    public void setObject(Object obj){
        if(bizData == null){
            bizData = new ArrayList<>(1);
            bizData.add(new SingleObjectDataSet(obj));
        } else if(bizData.size() == 0){
            bizData.add(new SingleObjectDataSet(obj));
        } else {
            bizData.set(0, new SingleObjectDataSet(obj));
        }
    }

    public Object getObject(){
        if(bizData == null || bizData.size() == 0){
            return null;
        } else {
            DataSet dataSet = bizData.get(0);
            List<Map<String, Object>> data = dataSet.getData();
            if (data == null || data.size() == 0) {
                return null;
            } else {
                return data.get(0).get(SingleObjectDataSet.SINGLE_DATA_FIELD_NAME);
            }
        }
    }
}
