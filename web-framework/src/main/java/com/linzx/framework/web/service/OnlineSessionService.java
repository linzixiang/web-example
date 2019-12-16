package com.linzx.framework.web.service;

import com.linzx.framework.shiro.core.session.OnlineLocalSession;
import com.linzx.framework.shiro.core.session.OnlineSession;

import java.util.Date;
import java.util.List;

public interface OnlineSessionService {

    /**
     * 当会话过期/停止（如用户退出时）属性等会调用，更新状态为下线
     */
    public void batchOfflineSession(List<OnlineSession> onlineSession);

    /**
     * 批量删除session
     */
    public void batchDeleteOnline(List<OnlineSession> sessions);

    /**
     * 根据会话ID获取会话
     */
    public OnlineSession readSession(String sessionId);

    /**
     * 保存会话
     */
    public Long saveOnlineSession(OnlineSession userOnlineInfo, Integer onlineStatus);

    /**
     * 获取所有过期的会话，根据最后进入时间比较
     */
    public List<OnlineSession> selectOnlineByExpired(Date expiredDate);

}
