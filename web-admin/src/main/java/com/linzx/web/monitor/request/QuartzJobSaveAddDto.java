package com.linzx.web.monitor.request;

public class QuartzJobSaveAddDto {

    /** 任务名称 */
    private String jobName;
    /** 任务组名 （1默认 2系统） */
    private Integer jobGroup;
    /** 调用目标对象 （beanName.method） */
    private String invokeTarget;
    /** 执行表达式 （cron执行表达式） */
    private String cronExpression;
    /** 计划策略 （0=默认,1=立即触发执行,2=触发一次执行,3=不触发立即执行） */
    private Integer misfirePolicy;
    /** 并发执行 （0=禁止,1=允许） */
    private Integer concurrent;
    /** 是否停用 （0正常 1停用） */
    private Integer status;
    private String remark;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Integer getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(Integer jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getInvokeTarget() {
        return invokeTarget;
    }

    public void setInvokeTarget(String invokeTarget) {
        this.invokeTarget = invokeTarget;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Integer getMisfirePolicy() {
        return misfirePolicy;
    }

    public void setMisfirePolicy(Integer misfirePolicy) {
        this.misfirePolicy = misfirePolicy;
    }

    public Integer getConcurrent() {
        return concurrent;
    }

    public void setConcurrent(Integer concurrent) {
        this.concurrent = concurrent;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
