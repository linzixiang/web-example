package com.linzx.common.domain;

import com.linzx.common.base.BaseEntity;
import com.linzx.common.utils.CronUtils;
import com.linzx.utils.StringUtils;

import java.util.Date;

/**
 * 定时任务调度 表 common_quartz_job
 *
 * @author linzixiang
 * @date 2019_07_208
 */
public class QuartzJob extends BaseEntity<Long> {
    private static final long serialVersionUID = 1L;

    public static final int CONCURRENT_FORBID = 0;
    public static final int CONCURRENT_ALLOW = 1;

    public static final String JOB_GROUP_DICTCODE = "quartz_job_group";

    /**
     * 主键
     */
    private Long jobId;
    /**
     * 任务名称
     */
    private String jobName;
    /**
     * 任务组名 （1默认 2系统）
     */
    private Integer jobGroup;
    /**
     * 调用目标对象 （beanName.method）
     */
    private String invokeTarget;
    /**
     * 执行表达式 （cron执行表达式）
     */
    private String cronExpression;
    /**
     * 计划策略 （0=默认,1=立即触发执行,2=触发一次执行,3=不触发立即执行）
     */
    private Integer misfirePolicy;
    /**
     * 并发执行 （0=禁止,1=允许）
     */
    private Integer concurrent;
    /**
     * 下次执行时间
     **/
    private Date nextExecuteTime;

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobGroup(Integer jobGroup) {
        this.jobGroup = jobGroup;
    }

    public Integer getJobGroup() {
        return jobGroup;
    }

    public void setInvokeTarget(String invokeTarget) {
        this.invokeTarget = invokeTarget;
    }

    public String getInvokeTarget() {
        return invokeTarget;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setMisfirePolicy(Integer misfirePolicy) {
        this.misfirePolicy = misfirePolicy;
    }

    public Integer getMisfirePolicy() {
        return misfirePolicy;
    }

    public void setConcurrent(Integer concurrent) {
        this.concurrent = concurrent;
    }

    public Integer getConcurrent() {
        return concurrent;
    }

    public Date getNextExecuteTime() {
        if (StringUtils.isNotEmpty(cronExpression)) {
            return CronUtils.getNextExecution(cronExpression);
        }
        return null;
    }

    @Override
    public void setId(Long id) {
        this.setJobId(id);
    }
}
