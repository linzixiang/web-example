package com.linzx.framework.config.properties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;

@Configuration
@DependsOn({"globalProperties"})
public class ShiroProperties implements InitializingBean {

    // 登录页面地址
    public static String loginUrl;
    // 权限认证失败地址
    public static String unauthorizedUrl;
    /**相隔多久检查一次session的有效性，单位分钟 */
    public static int validationInterval;
    /** session超时时间 */
    public static int sessionTimeout;

    /** session同步到数据库时间间隔，单位分钟 **/
    public static int dbSyncPeriod;

    /** 离线会话数据库保存时间，单位小时 **/
    public static int offlineKeepTime;

    // 缓存所属模块
    public static String cacheModuleName;
    // 认证缓存配置
    public static String cacheAuthentication;
    // session缓存key
    public static String cacheActiveSession;

    @Override
    public void afterPropertiesSet() throws Exception {
        loginUrl = GlobalProperties.getConfigStr("shiro.user.loginUrl", "/login");
        unauthorizedUrl = GlobalProperties.getConfigStr("shiro.user.unauthorizedUrl", "/unauth");
        cacheModuleName = GlobalProperties.getConfigStr("shiro.cache.moduleName", "system");
        cacheAuthentication= GlobalProperties.getConfigStr("shiro.cache.authentication", "userRealm");
        cacheActiveSession = GlobalProperties.getConfigStr("shiro.cache.session", "activeSessionCache");
        validationInterval = GlobalProperties.getConfigInt("shiro.session.validationInterval", 1);
        sessionTimeout = GlobalProperties.getConfigInt("shiro.session.timeout", 60);
        dbSyncPeriod = GlobalProperties.getConfigInt("shiro.session.dbSync", 1);
        offlineKeepTime = GlobalProperties.getConfigInt("shiro.session.offline-keep-time",24);
    }
}
