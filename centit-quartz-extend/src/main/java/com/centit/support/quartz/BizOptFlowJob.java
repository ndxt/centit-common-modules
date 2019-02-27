package com.centit.support.quartz;

import com.centit.support.data.core.BizOptFlow;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class BizOptFlowJob extends AbstractQuartzJob {

    private BizOptFlow optFlow;

    @Override
    public boolean runRealJob(JobExecutionContext context) throws JobExecutionException {
        return optFlow.run() >= 0;
    }

    public void setOptFlow(BizOptFlow optFlow) {
        this.optFlow = optFlow;
    }
}
