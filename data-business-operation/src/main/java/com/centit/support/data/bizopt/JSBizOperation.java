package com.centit.support.data.bizopt;

import com.centit.support.data.core.BizModel;
import com.centit.support.data.core.BizOperation;

public class JSBizOperation implements BizOperation {

    private String javaScript;


    @Override
    public BizModel apply(BizModel bizModel) {
        return bizModel;
    }

    public void setJavaScript(String javaScript) {
        this.javaScript = javaScript;
    }
}
