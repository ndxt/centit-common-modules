package com.centit.support.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DisallowConcurrentExecution
public abstract class AbstractQuartzJob implements Job {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractQuartzJob.class);

    protected abstract boolean runRealJob(JobExecutionContext context) throws JobExecutionException;

    protected abstract void loadExecutionContext(JobExecutionContext context);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 这边记录 任务启动日志，内容需要重构
        loadExecutionContext(context);

        logger.info("begin");
        boolean success = runRealJob(context);
        // 这边记录 任务完成日志，内容需要重构
        if(success) {
            logger.info("end");
        } else {
            logger.error("end");
        }
    }
}
