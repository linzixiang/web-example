package com.linzx.system.domain;

import com.linzx.common.base.BaseEntity;

/**
 * 在线用户 表 sys_user_online
 * 
 * @author linzixiang
 * @date 2019_06_172
 */
public class UserOnline extends BaseEntity<Long>{
	private static final long serialVersionUID = 1L;
	
	/** 会话主键 */
	private Long sessionId;
	/** 会话唯一标识 */
	private String sessionKey;
	/** 用户id */
	private Long userId;
	/** 用户名 */
	private String loginName;
	/** 登录ip地址 */
	private String ipaddr;
	/** 浏览器类型 */
	private String browser;
	/** 在线状态 (0：离线 1：在线) */
	private Integer onlineStatus;
	/** 登录定位 */
	private String loginLocation;
	/** 操作系统 */
	private String os;
	/** 超时时间 */
	private Long timeOut;

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}
	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	public String getSessionKey() {
		return sessionKey;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getUserId() {
		return userId;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getLoginName() {
		return loginName;
	}

	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}
	public String getIpaddr() {
		return ipaddr;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}
	public String getBrowser() {
		return browser;
	}

	public void setOnlineStatus(Integer onlineStatus) {
		this.onlineStatus = onlineStatus;
	}
	public Integer getOnlineStatus() {
		return onlineStatus;
	}

	public void setLoginLocation(String loginLocation) {
		this.loginLocation = loginLocation;
	}
	public String getLoginLocation() {
		return loginLocation;
	}

	public void setOs(String os) {
		this.os = os;
	}
	public String getOs() {
		return os;
	}

	public void setTimeOut(Long timeOut) {
		this.timeOut = timeOut;
	}
	public Long getTimeOut() {
		return timeOut;
	}

	@Override
	public void setId(Long id) {
		this.setSessionId(id);
	}
}
