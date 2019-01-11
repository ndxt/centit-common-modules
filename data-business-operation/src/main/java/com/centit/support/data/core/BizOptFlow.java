package com.centit.support.data.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务流
 */
public class BizOptFlow {
    /**
     * 业务数据 生产者
     */
    public BizSupplier supplier;
    /**
     * 业务数据的 处理流程
     */
    public List<BizOperation> operations;

    /**
     * @return 返回真正运行的次数
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
}
