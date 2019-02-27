package com.centit.support.quartz;

import org.quartz.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class QuartzJobUtils {

    private static ConcurrentHashMap<String, Class<? extends AbstractQuartzJob>> jobTypeMap
        = new ConcurrentHashMap<>(10);
    public static void registerJobType(String jobType, Class<? extends AbstractQuartzJob> type){
        jobTypeMap.put(jobType, type);
    }

    public static void createOrReplaceCronJob(Scheduler scheduler,
                                              String jobName, String jobGroup,
                                              String jobType, String cronExpress,
                                              Map<String, Object> param) throws SchedulerException {
        /*WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        SchedulerFactoryBean schedulerFactoryBean = webApplicationContext.getBean(SchedulerFactoryBean.class);
        Scheduler scheduler = schedulerFactoryBean.getScheduler();*/
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        Trigger trigger = scheduler.getTrigger(triggerKey);
        // 新建一个任务
        if(trigger == null){
            JobDetail jobDetail = JobBuilder.newJob(jobTypeMap.get(jobType))
                .withIdentity(jobName, jobGroup)
                .usingJobData(new JobDataMap(param)).build();

            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpress);
            trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup)
                .withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
        }else {
            if (!(trigger instanceof CronTrigger)) {
                throw new SchedulerException("任务定时器类型不能改变！");
            }
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpress);
            trigger = ((CronTrigger)trigger).getTriggerBuilder()
                .withIdentity(triggerKey)
                .usingJobData(new JobDataMap(param))
                .withSchedule(scheduleBuilder).build();
            scheduler.rescheduleJob(triggerKey, trigger);
        }
    }

    public static void createOrReplaceSimpleJob(Scheduler scheduler,
                                              String jobName, String jobGroup,
                                              String jobType, int intervalInSeconds,
                                              Map<String, Object> param) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        Trigger trigger = scheduler.getTrigger(triggerKey);
        // 新建一个任务
        if(trigger == null){
            JobDetail jobDetail = JobBuilder.newJob(jobTypeMap.get(jobType))
                .withIdentity(jobName, jobGroup)
                .usingJobData(new JobDataMap(param))
                .build();

            trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup)
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().
                    withIntervalInSeconds(intervalInSeconds).repeatForever()).build();
            scheduler.scheduleJob(jobDetail, trigger);
        }else {
            if (!(trigger instanceof SimpleTrigger)) {
                throw new SchedulerException("任务定时器类型不能改变！");
            }

            trigger = ((SimpleTrigger)trigger).getTriggerBuilder()
                .withIdentity(triggerKey)
                .usingJobData(new JobDataMap(param))
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().
                    withIntervalInSeconds(intervalInSeconds).repeatForever()).build();
            scheduler.rescheduleJob(triggerKey, trigger);
        }
    }
}
