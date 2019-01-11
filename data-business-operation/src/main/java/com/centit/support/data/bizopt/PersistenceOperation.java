package com.centit.support.data.bizopt;

import com.centit.support.data.core.BizModel;
import com.centit.support.data.core.BizOperation;
import com.centit.support.data.core.DataSet;
import com.centit.support.data.core.DataSetWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据持久化操作
 */
public class PersistenceOperation implements BizOperation {

    public static final String WRITER_INDICATE_SAVE = "save";
    public static final String WRITER_INDICATE_APPEND = "append";
    public static final String WRITER_INDICATE_MERGE = "merge";
    public static final String WRITER_INDICATE_UPDATE = "merge";



    private Map<String, DataSetWriter> dataWriters;
    private Map<String, String> writerIndicates;



    @Override
    public BizModel apply(BizModel bizModel) {
        if(dataWriters==null || bizModel==null || bizModel.isEmpty()){
            return bizModel;
        }
        for(Map.Entry<String, DataSet> ent : bizModel.getBizData().entrySet()){
            DataSetWriter dataSetWriter = dataWriters.get(ent.getKey());
            if(dataSetWriter!=null){
                switch (getWriterIndicate(ent.getKey())) {
                case WRITER_INDICATE_APPEND:
                    dataSetWriter.append(ent.getValue());
                    break;
                case WRITER_INDICATE_MERGE:
                    dataSetWriter.merge(ent.getValue());
                    break;
                default:
                    dataSetWriter.save(ent.getValue());
                    break;
                }
            }
        }
        return bizModel;
    }

    public void setWriterIndicate(String dataSetName, String indicate){
        if(writerIndicates == null){
            writerIndicates = new HashMap<>(5);
        }
        writerIndicates.put(dataSetName, indicate);
    }

    public void setDataWriter(String dataSetName, DataSetWriter writer){
        if(dataWriters == null){
            dataWriters = new HashMap<>(5);
        }
        dataWriters.put(dataSetName, writer);
    }

    public Map<String, DataSetWriter> getDataWriters() {
        return dataWriters;
    }

    public void setDataWriters(Map<String, DataSetWriter> dataWriters) {
        this.dataWriters = dataWriters;
    }

    private String getWriterIndicate(String dataSetName){
        if(writerIndicates == null){
            return WRITER_INDICATE_SAVE;
        }
        return writerIndicates.get(dataSetName);
    }
}
