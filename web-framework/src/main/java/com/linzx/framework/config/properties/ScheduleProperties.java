package com.linzx.framework.config.properties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * 定时任务配置
 */
@Configuration
@DependsOn({"globalProperties"})
public class ScheduleProperties implements InitializingBean {

    /** quartz参数 **/
    public static String instanceName;
    public static String instanceId;
    /** 线程池配置 **/
    public static String threadPoolClass;
    public static String threadCount;
    public static String threadPriority;
    /** JobStore配置 **/
    public static String jobStoreClass;
    /** 集群配置 **/
    public static String isClustered;
    public static String clusterCheckinInterval;
    public static String maxMisfiresToHandleAtATime;
    public static String txIsolationLevelSerializable;
    public static String misfireThreshold;
    public static String tablePrefix;

    @Override
    public void afterPropertiesSet() throws Exception {
        instanceName = GlobalProperties.getConfigStr("org.quartz.scheduler.instanceName", "CommonScheduler");
        instanceId = GlobalProperties.getConfigStr("org.quartz.scheduler.instanceId", "AUTO");
        threadPoolClass = GlobalProperties.getConfigStr("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        threadCount = GlobalProperties.getConfigStr("org.org.quartz.threadPool.threadCount", "20");
        threadPriority = GlobalProperties.getConfigStr("org.quartz.threadPool.threadPriority", "5");
        jobStoreClass = GlobalProperties.getConfigStr("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
        isClustered = GlobalProperties.getConfigStr("org.quartz.jobStore.isClustered", "true");
        clusterCheckinInterval = GlobalProperties.getConfigStr("org.quartz.jobStore.clusterCheckinInterval", "15000");
        maxMisfiresToHandleAtATime = GlobalProperties.getConfigStr("org.quartz.jobStore.maxMisfiresToHandleAtATime", "1");
        txIsolationLevelSerializable = GlobalProperties.getConfigStr("org.quartz.jobStore.txIsolationLevelSerializable", "true");
        misfireThreshold = GlobalProperties.getConfigStr("org.quartz.jobStore.misfireThreshold", "12000");
        tablePrefix = GlobalProperties.getConfigStr("org.quartz.jobStore.tablePrefix", "QUARTZ_FRM_");
    }
}
