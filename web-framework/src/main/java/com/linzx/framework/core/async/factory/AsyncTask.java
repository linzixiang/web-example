package com.linzx.framework.core.async.factory;

import com.linzx.framework.core.async.AsyncManager;
import com.linzx.framework.shiro.core.session.OnlineLocalSession;
import com.linzx.framework.shiro.core.session.OnlineSession;
import com.linzx.framework.utils.SpringUtils;
import com.linzx.framework.web.service.OnlineSessionService;

import java.util.TimerTask;

public class AsyncTask {

    /**
     * 会话同步到数据库
     */
    public static void syncSessionToDb(final OnlineSession session) {
        TimerTask sessionToDbTask = new TimerTask() {
            @Override
            public void run() {
                OnlineSessionService onlineSessionService = SpringUtils.getBeanByType(OnlineSessionService.class);
                if (onlineSessionService != null) {
                    onlineSessionService.saveOnlineSession(session, session.getOnlineStatus());
                }
            }
        };
        AsyncManager.me().execute(sessionToDbTask);
    }

}
