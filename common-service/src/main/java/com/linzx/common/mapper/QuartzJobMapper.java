package com.linzx.common.mapper;

import com.linzx.common.domain.QuartzJob;
import com.linzx.framework.mapper.BaseMapper;

import java.util.List;

/**
 * 定时任务调度  数据层
 * 
 * @author linzixiang
 * @date 2019_07_208
 */
public interface QuartzJobMapper extends BaseMapper<Long, QuartzJob> {
	/**
     * 查询定时任务调度 信息
     * 
     * @param jobId 定时任务调度 ID
     * @return 定时任务调度 信息
     */
	public QuartzJob getQuartzJobById(Long jobId);
	
	/**
     * 查询定时任务调度 列表
     * 
     * @param quartzJob 定时任务调度 信息
     * @return 定时任务调度 集合
     */
	public List<QuartzJob> selectQuartzJobList(QuartzJob quartzJob);
	
	/**
     * 新增定时任务调度 
     * 
     * @param quartzJob 定时任务调度 信息
     * @return 结果
     */
	public Long insertQuartzJob(QuartzJob quartzJob);
	
	/**
     * 修改定时任务调度 
     * 
     * @param quartzJob 定时任务调度 信息
     * @return 结果
     */
	public int updateQuartzJob(QuartzJob quartzJob);
	
	/**
     * 删除定时任务调度 
     * 
     * @param jobId 定时任务调度 ID
     * @return 结果
     */
	public int deleteQuartzJobById(Long jobId);
	
	/**
     * 批量删除定时任务调度 
     * 
     * @param jobIds 需要删除的数据ID
     * @return 结果
     */
	public int deleteQuartzJobByIds(Long[] jobIds);
	
}