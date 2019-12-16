package com.linzx.framework.shiro.core.session;

import com.linzx.framework.utils.IpUtils;
import com.linzx.framework.utils.SpringUtils;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SimpleSessionFactory;
import org.apache.shiro.web.session.mgt.WebSessionContext;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class ShiroSessionFactory extends SimpleSessionFactory {

    @Autowired(required = false)
    private RedissonClient redissonClient;

    @Override
    public Session createSession(SessionContext initData) {
        OnlineSession onlineSession = createOnlineSession();
        if (initData != null && initData instanceof WebSessionContext) {
            WebSessionContext sessionContext = (WebSessionContext) initData;
            HttpServletRequest request = (HttpServletRequest) sessionContext.getServletRequest();
            if (request != null) {
                UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
                // 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                onlineSession.setOs(os);
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                onlineSession.setBrowser(browser);
                // 获取ip地址
                String ipAddr = IpUtils.getIpAddr(request);
                onlineSession.setHost(ipAddr);
            }
        }
        return onlineSession;
    }

    public OnlineSession createOnlineSession() {
        if (redissonClient != null) {
            return new OnlineRedisSession(redissonClient);
        }
        return new OnlineLocalSession();
    }

}
