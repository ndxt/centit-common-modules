package com.centit.support.data.bizopt;

import com.centit.support.data.core.BizModel;
import com.centit.support.data.core.BizOperation;
import com.centit.support.data.utils.BizOptUtils;
import com.centit.support.data.utils.JSRuntimeContext;
import org.apache.commons.lang3.StringUtils;

public class JSBizOperation implements BizOperation {

    private String javaScript;
    private String jsFuncName;
    private JSRuntimeContext jsRuntimeContext;

    @Override
    public BizModel apply(BizModel bizModel) {
        if(jsRuntimeContext == null){
            jsRuntimeContext = new JSRuntimeContext();
        }
        if(StringUtils.isNotBlank(javaScript)){
            jsRuntimeContext.compileScript(javaScript);
        }
        Object object = jsRuntimeContext.callJsFunc(
            StringUtils.isBlank(jsFuncName)? "runOpt" : jsFuncName, bizModel);
        return BizOptUtils.castObjectToBizModel(object);
    }

    public void setJavaScript(String javaScript) {
        this.javaScript = javaScript;
    }

    public JSRuntimeContext getJsRuntimeContext() {
        return jsRuntimeContext;
    }

    public void setJsRuntimeContext(JSRuntimeContext jsRuntimeContext) {
        this.jsRuntimeContext = jsRuntimeContext;
    }

    public void setJsFuncName(String jsFuncName) {
        this.jsFuncName = jsFuncName;
    }
}
