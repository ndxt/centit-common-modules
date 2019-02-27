package com.centit.support.quartz;

import com.centit.support.algorithm.ReflectionOpt;
import com.centit.support.common.LeftRightPair;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class JavaBeanJob extends AbstractQuartzJob {

    private String beanName;
    private String methodName;// get put post delete
    private Map<String,Object> params;

    @Override
    public boolean runRealJob(JobExecutionContext context) throws JobExecutionException {

        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        Object object = webApplicationContext.getBean(beanName);
        LeftRightPair<Method, Object[]> mp = ReflectionOpt.getMatchBestMethod(object.getClass(), methodName, params);
        boolean ret = true;
        if(/*mp !=null &&*/ mp.getLeft() != null){
            try {
                if(mp.getRight() == null) {
                    mp.getLeft().invoke(object);
                }else{
                    mp.getLeft().invoke(object, mp.getRight());
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                logger.error(e.getLocalizedMessage(),e);
                ret = false;
            }
        }
        return ret;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
