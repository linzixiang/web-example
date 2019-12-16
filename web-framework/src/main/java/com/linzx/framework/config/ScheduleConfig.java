package com.linzx.framework.config;

import com.linzx.framework.config.properties.ScheduleProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@DependsOn({"scheduleProperties"})
public class ScheduleConfig {

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setDataSource(dataSource);
        // quartz参数
        Properties prop = new Properties();
        prop.put("org.quartz.scheduler.instanceName", ScheduleProperties.instanceName);
        prop.put("org.quartz.scheduler.instanceId", ScheduleProperties.instanceId);
        // 线程池配置
        prop.put("org.quartz.threadPool.class", ScheduleProperties.threadPoolClass);
        prop.put("org.quartz.threadPool.threadCount", ScheduleProperties.threadCount);
        prop.put("org.quartz.threadPool.threadPriority", ScheduleProperties.threadPriority);
        // JobStore配置
        prop.put("org.quartz.jobStore.class", ScheduleProperties.jobStoreClass);
        // 集群配置
        prop.put("org.quartz.jobStore.isClustered", ScheduleProperties.isClustered);
        prop.put("org.quartz.jobStore.clusterCheckinInterval", ScheduleProperties.clusterCheckinInterval);
        prop.put("org.quartz.jobStore.maxMisfiresToHandleAtATime", ScheduleProperties.maxMisfiresToHandleAtATime);
        prop.put("org.quartz.jobStore.txIsolationLevelSerializable", ScheduleProperties.txIsolationLevelSerializable);

        prop.put("org.quartz.jobStore.misfireThreshold", ScheduleProperties.misfireThreshold);
        prop.put("org.quartz.jobStore.tablePrefix", ScheduleProperties.tablePrefix);
        factory.setQuartzProperties(prop);

        factory.setSchedulerName(ScheduleProperties.instanceName);
        // 延时启动
        factory.setStartupDelay(2);
        factory.setApplicationContextSchedulerContextKey("applicationContextKey");
        // 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
        factory.setOverwriteExistingJobs(true);
        // 设置自动启动，默认为true
        factory.setAutoStartup(true);
        return factory;
    }
}
