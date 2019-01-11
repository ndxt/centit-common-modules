package com.centit.support.data.utils;

import com.centit.support.data.bizopt.DataLoadSupplier;
import com.centit.support.data.bizopt.PersistenceOperation;
import com.centit.support.data.core.BizOptFlow;

public abstract class BizOptFlowUtil {
    public static BizOptFlow createDataExchangeFlow(
        DataLoadSupplier loadData, PersistenceOperation saveData){
        return new BizOptFlow().setSupplier(loadData).addOperation(saveData);
    }
}
