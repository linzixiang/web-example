package com.linzx.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.linzx.framework.shiro.core.session.OnlineLocalSession;
import com.linzx.framework.shiro.core.session.OnlineSession;
import com.linzx.framework.shiro.core.session.ShiroSessionFactory;
import com.linzx.framework.web.service.OnlineSessionService;
import com.linzx.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linzx.system.mapper.UserOnlineMapper;
import com.linzx.system.domain.UserOnline;
import com.linzx.system.service.UserOnlineService;
import com.linzx.utils.Convert;

/**
 * 在线用户 服务层实现
 * 
 * @author linzixiang
 * @date 2019_06_172
 */
@Service
public class UserOnlineServiceImpl implements UserOnlineService, OnlineSessionService {

	@Autowired
	private UserOnlineMapper userOnlineMapper;

	@Autowired
	private ShiroSessionFactory sessionFactory;

	/**
     * 查询在线用户信息
     * 
     * @param sessionId 在线用户ID
     * @return 在线用户信息
     */
    @Override
	public UserOnline getUserOnlineById(Long sessionId) {
	    return userOnlineMapper.getUserOnlineById(sessionId);
	}
	
	/**
     * 查询在线用户列表
     * 
     * @param userOnline 在线用户信息
     * @return 在线用户集合
     */
	@Override
	public List<UserOnline> selectUserOnlineList(UserOnline userOnline) {
	    return userOnlineMapper.selectUserOnlineList(userOnline);
	}
	
    /**
     * 新增在线用户
     * 
     * @param userOnline 在线用户信息
     * @return 结果
     */
	@Override
	public Long insertUserOnline(UserOnline userOnline) {
	    return userOnlineMapper.insertUserOnline(userOnline);
	}
	
	/**
     * 修改在线用户
     * 
     * @param userOnline 在线用户信息
     * @return 结果
     */
	@Override
	public int updateUserOnline(UserOnline userOnline) {
	    return userOnlineMapper.updateUserOnline(userOnline);
	}

	/**
     * 删除在线用户对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteUserOnlineByIds(String ids) {
		return userOnlineMapper.deleteUserOnlineByIds(Convert.toLongArray(ids));
	}

	@Override
	public void batchOfflineSession(List<OnlineSession> onlineSessionList) {
		for (OnlineSession onlineSession : onlineSessionList) {
			UserOnline userOnline = new UserOnline();
			UserOnline dbUserOnline = userOnlineMapper.getUserOnlineBySessionKey(onlineSession.getId().toString());
			Long sessionId = dbUserOnline.getSessionId();
			userOnline.setOnlineStatus(OnlineSession.STATUS_ONFFLINE);
			userOnline.setSessionId(sessionId);
			userOnlineMapper.updateUserOnline(userOnline);
		}
	}

	@Override
	public void batchDeleteOnline(List<OnlineSession> onlineSessionList) {
		for (OnlineSession onlineSession : onlineSessionList) {
			UserOnline userOnline = userOnlineMapper.getUserOnlineByUserId(onlineSession.getUserId());
			userOnlineMapper.deleteUserOnlineById(userOnline.getSessionId());
		}
	}

	/**
	 * 读取数据库中保存的会话信息
	 */
	@Override
	public OnlineSession readSession(String sessionKey) {
		UserOnline userOnline = userOnlineMapper.getUserOnlineBySessionKey(sessionKey);
		if (userOnline == null) {
			return null;
		}
		OnlineSession onlineSession = sessionFactory.createOnlineSession();
		onlineSession.setUserId(userOnline.getUserId());
		onlineSession.setHost(userOnline.getIpaddr());
		onlineSession.setBrowser(userOnline.getBrowser());
		onlineSession.setOs(userOnline.getOs());
		onlineSession.setLoginName(userOnline.getLoginName());
		onlineSession.setStartTimestamp(userOnline.getCreatedTime());
		onlineSession.setLastAccessTime(userOnline.getUpdatedTime());
		onlineSession.setTimeout(userOnline.getTimeOut());
		onlineSession.setId(sessionKey);
		return onlineSession;
	}

	/**
	 * 保存会话信息到数据库中
	 * @param userOnlineInfo
	 * @param onlineStatus
	 * @return
	 */
	@Override
	public Long saveOnlineSession(OnlineSession userOnlineInfo, Integer onlineStatus) {
		UserOnline userOnline =  userOnlineMapper.getUserOnlineByUserId(userOnlineInfo.getUserId());
		if (userOnline == null) {
			// 新增操作
			userOnline = new UserOnline();
			userOnline.setUserId(userOnlineInfo.getUserId());
			setUserOnlineAttr(userOnlineInfo, userOnline);
			userOnlineMapper.insertUserOnline(userOnline);
			return userOnline.getSessionId();
		}
		setUserOnlineAttr(userOnlineInfo, userOnline);
		userOnlineMapper.updateUserOnline(userOnline);
		return userOnline.getSessionId();

	}

	/**
	 * 将会话信息更新到数据库
	 * @param userOnlineInfo 缓存的session信息
	 * @param userOnline 数据库保存的session信息
	 * @return
	 */
	private void setUserOnlineAttr(OnlineSession userOnlineInfo, UserOnline userOnline) {
		userOnline.setSessionKey(userOnlineInfo.getId().toString());
		userOnline.setBrowser(userOnlineInfo.getBrowser());
		userOnline.setIpaddr(userOnlineInfo.getHost());
		userOnline.setOs(userOnlineInfo.getOs());
		userOnline.setLoginName(userOnlineInfo.getLoginName());
		userOnline.setOnlineStatus(userOnlineInfo.getOnlineStatus());
		userOnline.setCreatedTime(userOnlineInfo.getStartTimestamp());
		userOnline.setUpdatedTime(userOnlineInfo.getLastAccessTime());
		userOnline.setTimeOut(userOnlineInfo.getTimeout());
	}

	@Override
	public List<OnlineSession> selectOnlineByExpired(Date expiredDate) {
		String lastAccessTime = DateUtils.parseDateToStr(DateUtils.DateFormatStrEnum.YYYY_MM_DD_HH_MM_SS, expiredDate);
		List<UserOnline> onlineSessionList = userOnlineMapper.selectOnlineByExpired(lastAccessTime);
		List<OnlineSession> expiredSessionList = new ArrayList<>();
		for (UserOnline userOnline : onlineSessionList) {
			// 此处直接用单机的session，因为后面要要对已失效会话判断是否保留，如果是redis的session，已失效的会话已删除无法保留已失效的会话
			OnlineLocalSession onlineSession = new OnlineLocalSession();
			onlineSession.setId(userOnline.getSessionKey());
			onlineSession.setLastAccessTime(userOnline.getUpdatedTime());
			onlineSession.setStartTimestamp(userOnline.getCreatedTime());
			onlineSession.setTimeout(userOnline.getTimeOut());
			onlineSession.setUserId(userOnline.getUserId());
			onlineSession.setOnlineStatus(userOnline.getOnlineStatus());
			expiredSessionList.add(onlineSession);
		}
		return expiredSessionList;
	}
}
