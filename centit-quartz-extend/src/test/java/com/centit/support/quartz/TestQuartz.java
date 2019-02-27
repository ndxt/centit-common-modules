package com.centit.support.quartz;

import com.centit.framework.components.OperationLogCenter;
import com.centit.framework.components.impl.TextOperationLogWriterImpl;
import com.centit.support.algorithm.CollectionsOpt;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import java.util.concurrent.TimeUnit;

public class TestQuartz {

    public static void main(String[] args) throws SchedulerException, InterruptedException {
        TextOperationLogWriterImpl textOperationLogWriter
            = new TextOperationLogWriterImpl();
        textOperationLogWriter.setOptLogHomePath("/D/Projects/RunData/demo_home/logs");
        OperationLogCenter.initOperationLogWriter(textOperationLogWriter);
        /** 创建 schedulerFactory
         *  在Spring 框架中无需手动创建引入 对应的配置文件就可以，并通过下面代码获取
         *  WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
         *         SchedulerFactoryBean schedulerFactoryBean = webApplicationContext.getBean(SchedulerFactoryBean.class);
         *         Scheduler scheduler = schedulerFactoryBean.getScheduler()
          */
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        /**
         * 注册 job的类型，可以自行扩展 job类别
         */
        QuartzJobUtils.registerJobType("msg", PrintMessageJob.class);
        //QuartzJobUtils.registerJobType("httpRequest", HttpRquestJob.class);
        QuartzJobUtils.registerJobType("exec", CallProcessJob.class);

        QuartzJobUtils.createOrReplaceSimpleJob(scheduler, "print",
            "test", "msg", 3,
            CollectionsOpt.createHashMap("message", "hello quartz"));
        System.out.println("--------scheduler start ! ------------");
        scheduler.start();
        TimeUnit.SECONDS.sleep(10);
        QuartzJobUtils.createOrReplaceSimpleJob(scheduler, "print",
            "test", "msg", 4,
            CollectionsOpt.createHashMap("message", "Hellow quartz!"));
        TimeUnit.SECONDS.sleep(40);
        scheduler.shutdown();
        System.out.println("--------scheduler shutdown ! ------------");
    }
}
