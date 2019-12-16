package com.linzx.framework.shiro.core.session;

import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

/**
 * 单机session
 */
public class OnlineLocalSession extends AbstractOnlineSession {

    private Logger logger = LoggerFactory.getLogger(OnlineLocalSession.class);

    public OnlineLocalSession() {
        this.timeout = DefaultSessionManager.DEFAULT_GLOBAL_SESSION_TIMEOUT;
        this.startTimestamp = new Date();
        this.lastAccessTime = this.startTimestamp;
    }

    private Serializable id;

    private Date startTimestamp;

    private Date stopTimestamp;

    private Date lastAccessTime;

    private long timeout;

    private boolean expired;

    /** 用户ID */
    private Long userId;

    /** 用户名称 */
    private String loginName;

    /** 登录IP地址 */
    private String host;

    /** 浏览器类型 */
    private String browser;

    /** 操作系统 */
    private String os;

    @Override
    public Serializable getId() {
        return this.id;
    }

    @Override
    public void setId(Serializable id) {
        this.id = id;
    }

    @Override
    public boolean isExpired() {
        return expired;
    }

    @Override
    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    @Override
    public long getTimeout() {
        return timeout;
    }

    @Override
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    @Override
    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    @Override
    public Date getStartTimestamp() {
        return startTimestamp;
    }

    @Override
    public void setStartTimestamp(Date startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    @Override
    public Date getStopTimestamp() {
        return stopTimestamp;
    }

    @Override
    public void setStopTimestamp(Date stopTimestamp) {
        this.stopTimestamp = stopTimestamp;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String getLoginName() {
        return loginName;
    }

    @Override
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Override
    public String getBrowser() {
        return browser;
    }

    @Override
    public void setBrowser(String browser) {
        this.browser = browser;
    }

    @Override
    public String getOs() {
        return os;
    }

    @Override
    public void setOs(String os) {
        this.os = os;
    }

    @Override
    public void touch() {
        this.lastAccessTime = new Date();
    }

    @Override
    public void stop() {
        if (this.stopTimestamp == null) {
            this.stopTimestamp = new Date();
        }
    }

    protected boolean isStopped() {
        return getStopTimestamp() != null;
    }

    @Override
    protected boolean isTimedOut() {
        if (isExpired()) {
            return true;
        }
        long timeout = getTimeout();
        if (timeout >= 0l) {
            Date lastAccessTime = getLastAccessTime();
            if (lastAccessTime == null) {
                String msg = "session.lastAccessTime for session with id [" +
                        getId() + "] is null.  This value must be set at " +
                        "least once, preferably at least upon instantiation.  Please check the " +
                        getClass().getName() + " implementation and ensure " +
                        "this value will be set (perhaps in the constructor?)";
                throw new IllegalStateException(msg);
            }
            long expireTimeMillis = System.currentTimeMillis() - timeout;
            Date expireTime = new Date(expireTimeMillis);
            return lastAccessTime.before(expireTime);
        } else {
            if (logger.isTraceEnabled()) {
                logger.trace("No timeout for session with id [" + getId() +
                        "].  Session is not considered expired.");
            }
        }
        return false;
    }

}
