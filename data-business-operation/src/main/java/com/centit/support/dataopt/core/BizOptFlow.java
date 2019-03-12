package com.centit.support.dataopt.core;

import com.centit.support.dataopt.bizopt.SimpleBizSupplier;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务流
 */
public class BizOptFlow {

    public BizOptFlow(){

    }
    /**
     * 业务数据 生产者
     */
    public BizSupplier supplier;
    /**
     * 业务数据的 处理流程
     */
    public List<BizOperation> operations;

    /**
     * @return 返回真正运行的次数, 如果小于 0 表示報錯
     */
    public int run(){
        int n = 0;
        do{
            BizModel bm = supplier.get();
            if(bm == null/*|| bm.isEmpty()*/){
                break;
            }
            n ++;
            for(BizOperation opt : operations){
                bm = opt.apply(bm);
            }
        }while(supplier.isBatchWise());
        return n;
    }

    public BizSupplier getSupplier() {
        return supplier;
    }

    public BizOptFlow setSupplier(BizSupplier supplier) {
        this.supplier = supplier;
        return this;
    }

    public List<BizOperation> getOperations() {
        return operations;
    }

    public void setOperations(List<BizOperation> operations) {
        this.operations = operations;
    }

    public BizOptFlow addOperation(BizOperation operation) {
        if(this.operations == null){
            this.operations = new ArrayList<>(5);
        }
        this.operations.add(operation);
        return this;
    }

    public static BizOptFlow makeSingleOptFlow(BizOperation operation){
        BizOptFlow optFlow = new BizOptFlow();
        return optFlow.setSupplier(SimpleBizSupplier.DUMMY_BIZ_SUPPLIER)
                      .addOperation(operation);
    }
}
