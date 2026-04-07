package ru.lakeevda.quartzandworker.config;

import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import ru.lakeevda.quartzandworker.job.CreateTaskJob;
import ru.lakeevda.quartzandworker.job.WorkerTaskJob;

@Configuration
public class QuartzStartupJobs {

    @Bean(name = "jobWorkerTask")
    public JobDetailFactoryBean JobDetailFactoryBeanWorkerTask() {
        return QuartzConfiguration.createJobDetail(WorkerTaskJob.class,
                WorkerTaskJob.class.getName(),
                WorkerTaskJob.class.getName());
    }

    @Bean(name = "triggerWorkerTask")
    public CronTriggerFactoryBean CronTriggerFactoryBeanWorkerTask(@Qualifier("jobWorkerTask") JobDetail jobDetail) {
        return QuartzConfiguration.createCronTrigger(jobDetail,
                "0/5 * * * * ?",
                WorkerTaskJob.class.getName());
    }

    @Bean(name = "jobCreateTask")
    public JobDetailFactoryBean JobDetailFactoryBeanCreateTask() {
        return QuartzConfiguration.createJobDetail(CreateTaskJob.class,
                CreateTaskJob.class.getName(),
                CreateTaskJob.class.getName());
    }

    @Bean(name = "triggerCreateTask")
    public CronTriggerFactoryBean CronTriggerFactoryBeanCreateTask(@Qualifier("jobCreateTask") JobDetail jobDetail) {
        return QuartzConfiguration.createCronTrigger(jobDetail,
                "0/1 * * * * ?",
                CreateTaskJob.class.getName());
    }
}
