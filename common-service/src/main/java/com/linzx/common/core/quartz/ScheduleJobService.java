package com.linzx.common.core.quartz;

import com.linzx.common.constant.Constants;
import com.linzx.common.core.quartz.execution.QuartzConcurrentExecution;
import com.linzx.common.core.quartz.execution.QuartzSyncExecution;
import com.linzx.common.domain.QuartzJob;
import com.linzx.common.enums.ScheduleConstants;
import com.linzx.framework.exception.BizCommonException;
import org.quartz.*;
import org.springframework.stereotype.Component;

@Component
public class ScheduleJobService {

    /**
     * 得到quartz任务类
     *
     * @param isConcurrent 是否并发执行
     * @return
     */
    private Class<? extends Job> getQuartzJobClass(boolean isConcurrent) {
        if (isConcurrent) {
            return QuartzConcurrentExecution.class;
        }
        return QuartzSyncExecution.class;
    }

    /**
     * 构建任务触发对象
     */
    public TriggerKey getTriggerKey(Long jobId, Integer jobGroup) {
        String jobGroupStr =  QuartzJob.JOB_GROUP_DICTCODE  + "_" + jobGroup;
        return TriggerKey.triggerKey(ScheduleConstants.TASK_CLASS_NAME + jobId, jobGroupStr);
    }

    /**
     * 构建任务键对象
     */
    public JobKey getJobKey(Long jobId, Integer jobGroup) {
        String jobGroupStr =  QuartzJob.JOB_GROUP_DICTCODE  + "_" + jobGroup;
        return JobKey.jobKey(ScheduleConstants.TASK_CLASS_NAME + jobId, jobGroupStr);
    }

    /**
     * 创建定时任务
     */
    public void createScheduleJob(Scheduler scheduler, QuartzJob job) throws SchedulerException, BizCommonException {
        Class<? extends Job> jobClass = getQuartzJobClass(QuartzJob.CONCURRENT_ALLOW == job.getConcurrent());
        // 构建job信息
        Long jobId = job.getJobId();
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(getJobKey(jobId, job.getJobGroup())).build();

        // 表达式调度构建器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
        cronScheduleBuilder = handleCronScheduleMisfirePolicy(job, cronScheduleBuilder);

        // 按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(jobId, job.getJobGroup()))
                .withSchedule(cronScheduleBuilder).build();

        // 放入参数，运行时的方法可以获取
        jobDetail.getJobDataMap().put(ScheduleConstants.TASK_PROPERTIES, job);

        // 判断是否存在
        if (scheduler.checkExists(getJobKey(jobId, job.getJobGroup()))) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(getJobKey(jobId, job.getJobGroup()));
        }

        scheduler.scheduleJob(jobDetail, trigger);

        // 暂停任务
        if (job.getStatus() == Constants.STATUS_STOP) {
            scheduler.pauseJob(getJobKey(jobId, job.getJobGroup()));
        }
    }

    /**
     * 设置定时任务策略
     */
    public CronScheduleBuilder handleCronScheduleMisfirePolicy(QuartzJob job, CronScheduleBuilder cb)
            throws BizCommonException {
        switch (job.getMisfirePolicy().toString()) {
            case ScheduleConstants.MISFIRE_DEFAULT:
                return cb;
            case ScheduleConstants.MISFIRE_IGNORE_MISFIRES:
                return cb.withMisfireHandlingInstructionIgnoreMisfires();
            case ScheduleConstants.MISFIRE_FIRE_AND_PROCEED:
                return cb.withMisfireHandlingInstructionFireAndProceed();
            case ScheduleConstants.MISFIRE_DO_NOTHING:
                return cb.withMisfireHandlingInstructionDoNothing();
            default:
                throw new BizCommonException("common.task.misfire.policy.error");
        }
    }

}
