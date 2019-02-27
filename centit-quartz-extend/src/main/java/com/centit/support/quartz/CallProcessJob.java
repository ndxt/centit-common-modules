package com.centit.support.quartz;

import com.centit.support.algorithm.GeneralAlgorithm;
import com.centit.support.algorithm.StringBaseOpt;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallProcessJob extends AbstractQuartzJob {

    private String command;
    private Map<String,Object> params;
    @Override
    protected void loadExecutionContext(JobExecutionContext context){
        JobDataMap paramMap = context.getMergedJobDataMap();// .getJobDetail().getJobDataMap();
        command = paramMap.getString("command");
        Object obj = paramMap.get("params");
        if(obj!=null){
            params =(Map) GeneralAlgorithm.castObjectToType(obj, Map.class);
        }else{
            params = new HashMap<>(1);
        }
    }

    @Override
    protected boolean runRealJob(JobExecutionContext context) throws JobExecutionException {
        boolean ret = true;
        try {
            if (params == null || params.isEmpty()) {
                Runtime.getRuntime().exec(command);
            } else {
                List<String> envp = new ArrayList<>(params.size()+1);
                for(Map.Entry<String, Object> ent : params.entrySet()){
                    envp.add(ent.getKey()+"="+ StringBaseOpt.castObjectToString(ent.getValue()));
                }
                Runtime.getRuntime().exec(command, envp.toArray(new String[envp.size()]));
            }
        } catch (IOException e){
            logger.error(e.getLocalizedMessage(), e);
            ret = false;
        }
        //process.waitFor();
        return ret;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
