package com.centit.support.quartz;

import com.centit.support.algorithm.CollectionsOpt;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import java.util.concurrent.TimeUnit;

public class TestQuartz {

    public static void main(String[] args) throws SchedulerException, InterruptedException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        QuartzJobUtils.registerJobType("msg", PrintMessageJob.class);
        QuartzJobUtils.createOrReplaceSimpleJob(scheduler, "print",
            "test", "msg", 3,
            CollectionsOpt.createHashMap("message", "hello quartz"));
        System.out.println("--------scheduler start ! ------------");
        scheduler.start();
        TimeUnit.SECONDS.sleep(10);
        QuartzJobUtils.createOrReplaceSimpleJob(scheduler, "print",
            "test", "msg", 4,
            CollectionsOpt.createHashMap("message", "Hellow quartz!"));
        TimeUnit.SECONDS.sleep(10);
        scheduler.shutdown();
        System.out.println("--------scheduler shutdown ! ------------");
    }
}
