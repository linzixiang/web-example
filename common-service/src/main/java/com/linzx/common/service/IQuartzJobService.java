package com.linzx.common.service;

import com.linzx.common.domain.QuartzJob;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * 定时任务调度  服务层
 * 
 * @author linzixiang
 * @date 2019_07_208
 */
public interface IQuartzJobService {
	
	/**
     * 新增定时任务调度 
     * 
     * @param quartzJob 定时任务调度 信息
     * @return 结果
     */
	public Long insertQuartzJob(QuartzJob quartzJob);
		
	/**
     * 删除定时任务调度 信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteQuartzJobByIds(String ids);

	/**
	 * 暂停任务
	 */
	public int pauseJob(Long jobId, Integer jobGroup) throws Exception;

	/**
	 * 恢复任务
	 */
	int resumeJob(Long jobId, Integer jobGroup) throws Exception;

	int changeJobStatus(Long jobId, Integer jobGroup, Integer status) throws Exception;

	/**
	 * 立即执行
	 */
	void runImmediate(Long jobId) throws Exception;

	/**
	 * 检查cronExpression表达式
	 * @param cronExpression
	 * @return
	 */
	boolean checkCronExpression(String cronExpression);

}
