package com.linzx.framework.shiro.core.session;

import com.linzx.framework.config.properties.ShiroProperties;
import com.linzx.framework.core.async.factory.AsyncTask;
import com.linzx.framework.shiro.bean.UserLoginInfo;
import com.linzx.framework.shiro.realm.UserRealm;
import com.linzx.framework.shiro.service.LoginService;
import com.linzx.framework.shiro.web.filter.LoginAuthFilter;
import com.linzx.framework.web.service.OnlineSessionService;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OnlineSessionDAO extends EnterpriseCacheSessionDAO {

    private static final Logger log = LoggerFactory.getLogger(LoginAuthFilter.class);

    @Autowired(required = false)
    private OnlineSessionService onlineSessionService;

    @Autowired(required = false)
    private LoginService loginService;

    /**
     * 上次同步数据库的时间戳
     */
    private static final String LAST_SYNC_DB_TIMESTAMP = OnlineSessionDAO.class.getName() + "LAST_SYNC_DB_TIMESTAMP";

    /**
     * 创建session，保存到数据库
     */
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = super.doCreate(session);
        return sessionId;
    }

    @Override
    protected void assignSessionId(Session session, Serializable sessionId) {
        OnlineSession onlineSession = (OnlineSession) session;
        onlineSession.setId(sessionId);
    }

    /**
     * 先从缓存中获取session，如果没有再去数据库中获取
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        Session session = super.doReadSession(sessionId);
        if (session == null) {
            if (onlineSessionService != null) {
                OnlineSession onlineSession = onlineSessionService.readSession(String.valueOf(sessionId));
                if (onlineSession == null || OnlineSession.STATUS_ONFFLINE.equals(onlineSession.getOnlineStatus())) {
                    return null;
                }
                try {
                    onlineSession.validate();
                } catch (InvalidSessionException e) {
                    log.info("会话已失效 ", e);
                    return null;
                }
                UserLoginInfo userLoginInfo = loginService.loginByAccount(onlineSession.getLoginName());
                SimplePrincipalCollection simplePrincipalCollection = new SimplePrincipalCollection(userLoginInfo, UserRealm.class.getName());
                onlineSession.setAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY, simplePrincipalCollection);
                super.cache(onlineSession, sessionId);
                return onlineSession;
            }
        }
        return null;
    }

    @Override
    protected void doUpdate(Session session) {
        super.doUpdate(session);
    }

    @Override
    protected void doDelete(Session session) {
        OnlineSession onlineSession = (OnlineSession) session;
        if (onlineSession == null || onlineSessionService == null) {
            return;
        }
        List<OnlineSession> offlineSessionList = new ArrayList<>();
        offlineSessionList.add(onlineSession);
        onlineSessionService.batchOfflineSession(offlineSessionList);
    }

    public void syncToDb(OnlineSession onlineSession) {
        Date lastSyncTimestamp = (Date) onlineSession.getAttribute(LAST_SYNC_DB_TIMESTAMP);
        if (lastSyncTimestamp != null) {
            boolean needSync = true;
            long deltaTime = onlineSession.getLastAccessTime().getTime() - lastSyncTimestamp.getTime();
            if (deltaTime < ShiroProperties.dbSyncPeriod * 60 * 1000) {
                // 时间差不足 无需同步
                needSync = false;
            }
            boolean isGuest = onlineSession.getUserId() == null || onlineSession.getUserId() == 0L;
            // session 数据变更了 同步
            if (!isGuest && onlineSession.isAttributeChanged()) {
                needSync = true;
            }
            if (!needSync) {
                return;
            }
        }
        onlineSession.setAttribute(LAST_SYNC_DB_TIMESTAMP, onlineSession.getLastAccessTime());
        if (onlineSession.isAttributeChanged()) {
            onlineSession.resetAttributeChanged();
        }
       AsyncTask.syncSessionToDb(onlineSession);
    }

}
