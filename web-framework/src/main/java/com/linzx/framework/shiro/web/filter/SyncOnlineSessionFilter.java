package com.linzx.framework.shiro.web.filter;

import com.linzx.common.constant.SecurityConstants;
import com.linzx.framework.shiro.core.session.OnlineLocalSession;
import com.linzx.framework.shiro.core.session.OnlineSession;
import com.linzx.framework.shiro.core.session.OnlineSessionDAO;
import org.apache.shiro.web.filter.PathMatchingFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 同步Session数据到Db
 */
public class SyncOnlineSessionFilter extends PathMatchingFilter {

    private OnlineSessionDAO onlineSessionDAO;

    public SyncOnlineSessionFilter(OnlineSessionDAO onlineSessionDAO) {
        this.onlineSessionDAO = onlineSessionDAO;
    }

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        OnlineSession onlineSession = (OnlineSession) request.getAttribute(SecurityConstants.ONLINE_SESSION);
        // session停止时间，如果stopTimestamp不为null，则代表已停止，不同步
        if (onlineSession != null && onlineSession.isValid()) {
            onlineSessionDAO.syncToDb(onlineSession);
        }
        return true;
    }
}
