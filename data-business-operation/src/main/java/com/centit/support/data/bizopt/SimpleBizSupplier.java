package com.centit.support.data.bizopt;

import com.centit.support.data.core.BizModel;
import com.centit.support.data.core.BizSupplier;

public class SimpleBizSupplier implements BizSupplier {

    private BizModel bizModel;
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
        return bizModel;
    }

    public BizModel getBizModel() {
        return bizModel;
    }

    public void setBizModel(BizModel bizModel) {
        this.bizModel = bizModel;
    }
}
