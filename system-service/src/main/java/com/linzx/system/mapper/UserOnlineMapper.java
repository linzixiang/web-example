package com.linzx.system.mapper;

import com.linzx.system.domain.UserOnline;
import java.util.List;	

/**
 * 在线用户 数据层
 * 
 * @author linzixiang
 * @date 2019_06_172
 */
public interface UserOnlineMapper {
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
     * 删除在线用户
     * 
     * @param sessionId 在线用户ID
     * @return 结果
     */
	public int deleteUserOnlineById(Long sessionId);
	
	/**
     * 批量删除在线用户
     * 
     * @param sessionIds 需要删除的数据ID
     * @return 结果
     */
	public int deleteUserOnlineByIds(Long[] sessionIds);

	/**
	 * 根据session唯一标识读取session信息
	 * @param sessionKey
	 * @return
	 */
	public UserOnline getUserOnlineBySessionKey(String sessionKey);

	/**
	 * 根据用户id获取会话信息
	 * @param userId
	 * @return
	 */
	UserOnline getUserOnlineByUserId(Long userId);

	/**
	 * 获取登陆超时的会话
	 * @param lastAccessTime
	 * @return
	 */
    List<UserOnline> selectOnlineByExpired(String lastAccessTime);
}