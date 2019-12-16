package com.linzx.system.service;

import com.linzx.system.domain.UserOnline;
import java.util.List;

/**
 * 在线用户 服务层
 * 
 * @author linzixiang
 * @date 2019_06_172
 */
public interface UserOnlineService {
	/**
     * 查询在线用户信息
     * 
     * @param sessionId 在线用户ID
     * @return 在线用户信息
     */
	public UserOnline getUserOnlineById(Long sessionId);
	
	/**
     * 查询在线用户列表
     * 
     * @param userOnline 在线用户信息
     * @return 在线用户集合
     */
	public List<UserOnline> selectUserOnlineList(UserOnline userOnline);
	
	/**
     * 新增在线用户
     * 
     * @param userOnline 在线用户信息
     * @return 结果
     */
	public Long insertUserOnline(UserOnline userOnline);
	
	/**
     * 修改在线用户
     * 
     * @param userOnline 在线用户信息
     * @return 结果
     */
	public int updateUserOnline(UserOnline userOnline);
		
	/**
     * 删除在线用户信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteUserOnlineByIds(String ids);
	
}
