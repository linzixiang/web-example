package com.linzx.framework.shiro.core.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;

import java.util.Date;

public class OnlineSessionManager extends DefaultSessionManager {

    @Override
    protected void onStop(Session session) {
        if (session instanceof OnlineSession) {
            OnlineSession ss = (OnlineSession) session;
            Date stopTs = ss.getStopTimestamp();
            ss.setLastAccessTime(stopTs);
        }
    }

    @Override
    protected void onExpiration(Session session) {
        if (session instanceof OnlineSession) {
            ((OnlineSession) session).setExpired(true);
        }
        onChange(session);
    }



}
