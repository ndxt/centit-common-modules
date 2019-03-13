package com.centit.support.dataopt.utils;

import com.centit.support.dataopt.bizopt.DataLoadSupplier;
import com.centit.support.dataopt.bizopt.PersistenceOperation;
import com.centit.support.dataopt.core.BizOperation;
import com.centit.support.dataopt.core.BizOptFlow;
import com.centit.support.dataopt.core.BizSupplier;

public abstract class BizOptFlowUtil {

    public static int runDataExchange(
        DataLoadSupplier loadData, PersistenceOperation saveData){
        BizOptFlow bof = new BizOptFlow().setSupplier(loadData).addOperation(saveData);
        return bof.run();
    }

    public static int runDataExchange(
        DataLoadSupplier loadData, BizOperation dataTrans, PersistenceOperation saveData){
        BizOptFlow bof = new BizOptFlow().setSupplier(loadData)
            .addOperation(dataTrans)
            .addOperation(saveData);
        return bof.run();
    }

    public static BizOptFlow createOptFlow(BizSupplier bizSupplier, String optDescJson){
        BizOptFlow bof = new BizOptFlow().setSupplier(bizSupplier);
        //TODO 解析json 目前支持 map filter
        return bof;
    }
}
