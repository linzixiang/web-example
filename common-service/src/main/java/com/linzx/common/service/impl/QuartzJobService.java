package com.linzx.common.service.impl;

import java.util.List;

import com.linzx.common.constant.Constants;
import com.linzx.common.core.quartz.ScheduleJobService;
import com.linzx.common.enums.ScheduleConstants;
import com.linzx.common.utils.CronUtils;
import com.linzx.framework.exception.BizCommonException;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linzx.framework.web.BaseService;
import com.linzx.common.mapper.QuartzJobMapper;
import com.linzx.common.domain.QuartzJob;
import com.linzx.common.service.IQuartzJobService;
import com.linzx.utils.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 定时任务调度  服务层实现
 *
 * @author linzixiang
 * @date 2019_07_208
 */
@Service
public class QuartzJobService extends BaseService<Long, QuartzJob, QuartzJobMapper> implements IQuartzJobService {

    private Logger logger = LoggerFactory.getLogger(QuartzJobService.class);

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private QuartzJobMapper quartzJobMapper;

    @Autowired
    private ScheduleJobService scheduleJobService;

    /**
     * 新增定时任务调度
     *
     * @param quartzJob 定时任务调度 信息
     * @return 结果
     */
    @Override
    public Long insertQuartzJob(QuartzJob quartzJob) {
        Long jobId = super.insert(quartzJob).get();
        updateSchedulerJob(quartzJob, quartzJob.getJobGroup());
        return jobId;
    }

    /**
     * 删除定时任务调度 对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteQuartzJobByIds(String ids) {
        Long[] jobIds = Convert.toLongArray(ids);
        for (Long jobId : jobIds) {
            QuartzJob job = super.getById(jobId).get();
            deleteJob(job);
        }
        return super.deleteByIds(Convert.toLongArray(ids));
    }

    /**
     * 暂停任务
     */
    @Override
    @Transactional
    public int pauseJob(Long jobId, Integer jobGroup) throws Exception {
        QuartzJob quartzJob = new QuartzJob();
        quartzJob.setJobId(jobId);
        quartzJob.setJobGroup(jobGroup);
        quartzJob.setStatus(Constants.STATUS_STOP);
        int rows = super.update(quartzJob);
        if (rows > 0) {
            scheduler.pauseJob(scheduleJobService.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * 恢复任务
     */
    @Override
    @Transactional
    public int resumeJob(Long jobId, Integer jobGroup) throws Exception {
        QuartzJob quartzJob = new QuartzJob();
        quartzJob.setJobId(jobId);
        quartzJob.setJobGroup(jobGroup);
        quartzJob.setStatus(Constants.STATUS_NORMAL);
        int rows = super.update(quartzJob);
        if (rows > 0) {
            scheduler.resumeJob(scheduleJobService.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    @Override
    public int changeJobStatus(Long jobId, Integer jobGroup, Integer status) throws Exception {
        int rows = 0;
        if (status == Constants.STATUS_NORMAL) {
            rows = this.resumeJob(jobId, jobGroup);
        } else if (status == Constants.STATUS_STOP) {
            rows = this.pauseJob(jobId, jobGroup);
        }
        return rows;
    }

    @Override
    @Transactional
    public void runImmediate(Long jobId) throws Exception {
        QuartzJob quartzJob = super.getById(jobId).get();
        // 参数
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ScheduleConstants.TASK_PROPERTIES, quartzJob);
        scheduler.triggerJob(scheduleJobService.getJobKey(jobId, quartzJob.getJobGroup()), dataMap);
    }

    @Transactional
    public int deleteJob(QuartzJob job) {
        try {
            Long jobId = job.getJobId();
            int rows = quartzJobMapper.deleteQuartzJobByIds(new Long[]{jobId});
            if (rows > 0) {
                scheduler.deleteJob(scheduleJobService.getJobKey(jobId, job.getJobGroup()));
            }
            return rows;
        } catch (SchedulerException e) {
            logger.error("定时任务删除异常：", e);
            throw new BizCommonException("common.quartz.scheduler.exception");
        }

    }

    @Override
    public boolean checkCronExpression(String cronExpression) {
        return CronUtils.isValid(cronExpression);
    }

    @Override
    public QuartzJobMapper setMapper() {
        return this.quartzJobMapper;
    }

    @Override
    public void afterBeanInit() {
        super.afterBeanInit();
        // 项目启动时，初始化定时器
        List<QuartzJob> quartzJobList = super.selectList(new QuartzJob()).get();
        for (QuartzJob quartzJob : quartzJobList) {
            updateSchedulerJob(quartzJob, quartzJob.getJobGroup());
        }
    }

    /**
     * 更新任务
     */
    public void updateSchedulerJob(QuartzJob job, Integer jobGroup) {
        try {
            Long jobId = job.getJobId();
            // 判断是否存在
            JobKey jobKey = scheduleJobService.getJobKey(jobId, jobGroup);
            if (scheduler.checkExists(jobKey)) {
                // 防止创建时存在数据问题 先移除，然后在执行创建操作
                scheduler.deleteJob(jobKey);
            }
            if (job.getDelFlag() == Constants.STATUS_NORMAL && job.getStatus() == Constants.STATUS_NORMAL) {
                scheduleJobService.createScheduleJob(scheduler, job);
            }
        } catch (SchedulerException e) {
            logger.error("定时任务更新异常：", e);
            throw new BizCommonException("common.quartz.scheduler.exception");
        }

    }
}
