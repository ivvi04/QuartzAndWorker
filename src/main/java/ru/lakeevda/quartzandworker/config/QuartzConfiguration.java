package ru.lakeevda.quartzandworker.config;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.sql.DataSource;
import java.time.Instant;
import java.util.Date;

@Configuration
public class QuartzConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private DataSource dataSource;

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(Trigger... triggers) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setConfigLocation(new ClassPathResource("quartz.properties"));
        schedulerFactoryBean.setOverwriteExistingJobs(false);
        schedulerFactoryBean.setAutoStartup(true);
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setJobFactory(springBeanJobFactory());
        schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(true);
        schedulerFactoryBean.setTriggers(triggers);
        return schedulerFactoryBean;
    }

    public static JobDetailFactoryBean createJobDetail(Class<? extends Job> jobClass,
                                                       String jobName,
                                                       String description) {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(jobClass);
        factoryBean.setName(jobName);
        factoryBean.setDescription(description);
        factoryBean.setDurability(true);
        return factoryBean;
    }

    public static CronTriggerFactoryBean createCronTrigger(JobDetail jobDetail,
                                                           String cronExpression,
                                                           String triggerName) {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setCronExpression(cronExpression);
        factoryBean.setStartTime(Date.from(Instant.now()));
        factoryBean.setStartDelay(0L);
        factoryBean.setName(triggerName);
        factoryBean.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
        return factoryBean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
