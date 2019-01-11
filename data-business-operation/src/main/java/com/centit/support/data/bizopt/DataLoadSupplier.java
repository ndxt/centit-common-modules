package com.centit.support.data.bizopt;

import com.centit.support.data.core.*;

import java.util.HashMap;
import java.util.Map;

public class DataLoadSupplier implements BizSupplier {

    private Map<String, DataSetReader> dataReaders;

    private String modelName;
    /**
     * 模型的标识， 就是对应的主键
     */
    private Map<String, Object> modeTag;

    protected BizModel loadData(){
        if(dataReaders == null || dataReaders.isEmpty()){
            return null;
        }
        BizModel bizModel = new BizModel();
        for(Map.Entry<String, DataSetReader> ent : dataReaders.entrySet()) {
            DataSet ds = ent.getValue().load(modeTag);
            if(ds != null && !ds.isEmpty()) {
                bizModel.addDataSet(ent.getKey(), ds);
            }
        }


        return  bizModel;
    }

    /**
     * 业务数据是否是 批量的
     * 如果是，处理器将反复调用 。知道 get() 返回 null 结束
     * @return 否是 批量的
     */
    public boolean isBatchWise(){
        return false;
    }

    /**
     * Gets a result.
     *
     * @return a result
     */
    @Override
    public BizModel get() {
        return loadData();
    }

    public void setDataReader(String dataSetName, DataSetReader reader){
        if(dataReaders == null){
            dataReaders = new HashMap<>(5);
        }
        dataReaders.put(dataSetName, reader);
    }

    public Map<String, DataSetReader> getDataReaders() {
        return dataReaders;
    }

    public void setDataReaders(Map<String, DataSetReader> dataReaders) {
        this.dataReaders = dataReaders;
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
}
