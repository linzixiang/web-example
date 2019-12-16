package com.linzx.framework.shiro.core.session;

import org.apache.shiro.session.mgt.ValidatingSession;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public interface OnlineSession extends ValidatingSession {

    public static final Integer STATUS_ONLINE = 1;
    public static final Integer STATUS_ONFFLINE = 0;

    void setId(Serializable sessionId);

    boolean isExpired();

    void setExpired(boolean expired);

    void setStartTimestamp(Date startTimestamp);

    Date getStartTimestamp();

    void setStopTimestamp(Date stopTimestamp);

    Date getStopTimestamp();

    void setLastAccessTime(Date lastAccessTime);

    Long getUserId();

    void setUserId(Long userId);

    String getLoginName();

    void setLoginName(String loginName);

    void setHost(String host);

    String getBrowser();

    void setBrowser(String browser);

    String getOs();

    void setOs(String os);

    Integer getOnlineStatus();

    void setOnlineStatus(Integer onlineStatus);

    Map<Object, Object> getAttributes();

    void setAttributes(Map<Object, Object> attributes);

    boolean isAttributeChanged();

    void markAttributeChanged();

    void resetAttributeChanged();

}
