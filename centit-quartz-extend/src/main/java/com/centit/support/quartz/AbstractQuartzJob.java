package com.centit.support.quartz;

import com.alibaba.fastjson.JSON;
import com.centit.framework.components.OperationLogCenter;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DisallowConcurrentExecution
public abstract class AbstractQuartzJob implements Job {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractQuartzJob.class);

    protected abstract boolean runRealJob(JobExecutionContext context) throws JobExecutionException;

    protected abstract void loadExecutionContext(JobExecutionContext context);

    protected void beforeRun(JobExecutionContext context){
        logger.info("beforeRun");
        JobKey jobKey = context.getJobDetail().getKey();
        OperationLogCenter.log("quartz", "start ",jobKey.getGroup()+ ":"+ jobKey.getName(),
            JSON.toJSONString(context.getMergedJobDataMap()));
    }

    protected void onSuccess(JobExecutionContext context){
        logger.info("onSuccess");
        JobKey jobKey = context.getJobDetail().getKey();
        OperationLogCenter.log("quartz", "success ",jobKey.getGroup()+ ":"+ jobKey.getName(),
            JSON.toJSONString(context.getMergedJobDataMap()));
    }

    protected void onError(JobExecutionContext context){
        logger.error("onError");
        JobKey jobKey = context.getJobDetail().getKey();
        OperationLogCenter.logError("quartz", "error ",jobKey.getGroup()+ ":"+ jobKey.getName(),
            JSON.toJSONString(context.getMergedJobDataMap()));
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 这边记录 任务启动日志，内容需要重构
        loadExecutionContext(context);
        beforeRun(context);
        boolean success = runRealJob(context);
        // 这边记录 任务完成日志，内容需要重构
        if(success) {
            onSuccess(context);
        } else {
            onError(context);
        }
    }
}
