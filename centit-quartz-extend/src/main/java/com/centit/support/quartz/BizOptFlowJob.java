package com.centit.support.quartz;

import com.centit.support.data.core.BizOptFlow;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class BizOptFlowJob extends AbstractQuartzJob {

    private BizOptFlow optFlow;

    @Override
    public void runRealJob(JobExecutionContext context) throws JobExecutionException {
        optFlow.run();
    }
}
