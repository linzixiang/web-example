package com.linzx.framework.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.linzx.framework.bean.CacheBean;
import com.linzx.framework.config.properties.ShiroProperties;
import com.linzx.framework.shiro.core.cache.redis.RedissonShiroCacheManager;
import com.linzx.framework.shiro.core.session.OnlineSessionDAO;
import com.linzx.framework.shiro.core.session.OnlineWebSessionManager;
import com.linzx.framework.shiro.core.session.ShiroSessionFactory;
import com.linzx.framework.shiro.core.session.ShiroSessionValidationScheduler;
import com.linzx.framework.shiro.core.cache.spring.ShiroSpringCacheManager;
import com.linzx.framework.shiro.realm.UserRealm;
import com.linzx.framework.shiro.web.filter.LoginAuthFilter;
import com.linzx.framework.shiro.web.filter.LogoutFilter;
import com.linzx.framework.shiro.web.filter.OnlineSessionFilter;
import com.linzx.framework.shiro.web.filter.SyncOnlineSessionFilter;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@DependsOn({"shiroProperties", "cacheConfig"})
public class ShiroConfig {

    @Autowired
    private CacheManager cacheManager;

    @Autowired(required = false)
    private RedissonClient redissonClient;

    /**
     * 缓存管理器 使用Ehcache实现
     */
    @Bean
    public ShiroSpringCacheManager shiroCacheManager() {
        if (redissonClient != null) {
            RedissonShiroCacheManager redissonShiroCacheManager = new RedissonShiroCacheManager(redissonClient);
            return new ShiroSpringCacheManager(redissonShiroCacheManager);
        }
        return new ShiroSpringCacheManager(cacheManager);
    }

    @Bean("userRealm")
    public UserRealm userRealm() {
        UserRealm userRealm = new UserRealm();
        setRealmCache(shiroCacheManager(), userRealm);
        return userRealm;
    }

    /**
     * 设置realm的缓存管理器，以及缓存key
     */
    private void setRealmCache(ShiroSpringCacheManager cacheManager, AuthorizingRealm authorizingRealm) {
        CacheBean.CacheKey cacheKey = new CacheBean.CacheKey(ShiroProperties.cacheModuleName, ShiroProperties.cacheAuthentication);
        authorizingRealm.setAuthorizationCacheName(cacheKey.get());
        authorizingRealm.setCacheManager(cacheManager);
    }

    @Bean
    public ShiroSessionFactory sessionFactory() {
        ShiroSessionFactory sessionFactory = new ShiroSessionFactory();
        return sessionFactory;
    }

    @Bean
    public ShiroSessionValidationScheduler sessionValidationScheduler() {
        ShiroSessionValidationScheduler sessionValidationScheduler = new ShiroSessionValidationScheduler();
        // 相隔多久检查一次session的有效性，单位毫秒，默认就是10分钟
        sessionValidationScheduler.setSessionValidationInterval(ShiroProperties.validationInterval * 60 * 1000);
        // 设置会话验证调度器进行会话验证时的会话管理器
        sessionValidationScheduler.setSessionManager(sessionValidationManager());
        return sessionValidationScheduler;
    }


    @Bean
    public OnlineSessionDAO onlineSessionDAO() {
        OnlineSessionDAO onlineSessionDAO = new OnlineSessionDAO();
        return onlineSessionDAO;
    }

    @Bean
    public OnlineWebSessionManager sessionValidationManager() {
        OnlineWebSessionManager shiroSessionManager = createSessionManage();
        return shiroSessionManager;
    }

    @Bean
    public OnlineWebSessionManager sessionManager() {
        OnlineWebSessionManager shiroSessionManager = createSessionManage();
        // 定义要使用的无效的Session定时调度器
        shiroSessionManager.setSessionValidationScheduler(sessionValidationScheduler());
        return shiroSessionManager;
    }

    private OnlineWebSessionManager createSessionManage() {
        OnlineWebSessionManager shiroSessionManager = new OnlineWebSessionManager();
        // 删除过期的session
        shiroSessionManager.setDeleteInvalidSessions(true);
        // 设置全局session超时时间
        shiroSessionManager.setGlobalSessionTimeout(ShiroProperties.sessionTimeout * 60 * 1000);
        // 去掉 JSESSIONID
        shiroSessionManager.setSessionIdUrlRewritingEnabled(false);
        // 是否定时检查session
        shiroSessionManager.setSessionValidationSchedulerEnabled(true);
        // 自定义SessionDao
        shiroSessionManager.setSessionDAO(onlineSessionDAO());
        // 自定义sessionFactory
        shiroSessionManager.setSessionFactory(sessionFactory());
        return shiroSessionManager;
    }

    @Bean
    public SecurityManager securityManager(UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm.
        securityManager.setRealms(Arrays.asList(userRealm));
        // 注入缓存管理器;
//        securityManager.setCacheManager(shiroCacheManager());
        // 记住我
        // securityManager.setRememberMeManager(rememberMeManager());
        // session管理器
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    /**
     * shiro过滤器配置
     * /admin/**=anon ：无参，表示可匿名访问
     * /admin/user/**=authc ：无参，表示需要认证才能访问
     * /admin/user/**=authcBasic ：无参，表示需要httpBasic认证才能访问
     * /admin/user/**=ssl ：无参，表示需要安全的URL请求，协议为https
     * /home=user ：表示用户不一定需要通过认证，只要曾被 Shiro 记住过登录状态就可以正常发起 /home 请求
     * /edit=authc,perms[admin:edit]：表示用户必需已通过认证，并拥有 admin:edit 权限才可以正常发起 /edit 请求
     * /admin=authc,roles[admin] ：表示用户必需已通过认证，并拥有 admin 角色才可以正常发起 /admin 请求
     * /admin/user/**=port[8081] ：当请求的URL端口不是8081时，跳转到schemal://serverName:8081?queryString
     * /admin/user/**=rest[user] ：根据请求方式来识别，相当于 /admins/user/**=perms[user:get]或perms[user:post] 等等
     * /admin**=roles["admin,guest"] ：允许多个参数（逗号分隔），此时要全部通过才算通过，相当于hasAllRoles()
     * /admin**=perms["user:add:*,user:del:*"]：允许多个参数（逗号分隔），此时要全部通过才算通过，相当于isPermitedAll()
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager, OnlineSessionDAO onlineSessionDAO) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // Shiro的核心安全接口,这个属性是必须的
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 身份认证失败，则跳转到登录页面的配置
        shiroFilterFactoryBean.setLoginUrl(ShiroProperties.loginUrl);
        // 权限认证失败，则跳转到指定页面
        shiroFilterFactoryBean.setUnauthorizedUrl(ShiroProperties.unauthorizedUrl);
        // Shiro连接约束配置，即过滤链的定义
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 静态资源允许匿名访问
        filterChainDefinitionMap.put("/static/**", "anon");
        // 工具相关允许匿名访问
        filterChainDefinitionMap.put("/tool/**", "anon");
        // 退出 logout地址，shiro去清除session
        filterChainDefinitionMap.put("/logout", "logout");
        // 登陆允许匿名
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/api", "anon");
        // 普通方式认证，需要创建session
        filterChainDefinitionMap.put("/**", "loginAuth,onlineSession,syncOnlineSession");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        // 自定义过滤器
        Map<String, Filter> filters = new LinkedHashMap<>();
        // 注销成功，则跳转到指定页面
        filters.put("logout", new LogoutFilter(ShiroProperties.loginUrl));
        filters.put("loginAuth", new LoginAuthFilter());
        filters.put("onlineSession", new OnlineSessionFilter(onlineSessionDAO));
        filters.put("syncOnlineSession", new SyncOnlineSessionFilter(onlineSessionDAO));
        shiroFilterFactoryBean.setFilters(filters);
        return shiroFilterFactoryBean;
    }

    /**
     * 开启Shiro注解通知器
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
            @Qualifier("securityManager") SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 解决UnavailableSecurityManagerException
     * @return
     */
    @Bean
    public FilterRegistrationBean delegatingFilterProxy(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("shiroFilter");
        filterRegistrationBean.setFilter(proxy);
        return filterRegistrationBean;
    }

    /**
     * thymeleaf模板引擎和shiro框架的整合
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }


}
