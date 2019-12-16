package com.linzx.framework.shiro.realm;

import com.linzx.common.constant.Constants;
import com.linzx.framework.exception.UnknownAccountException;
import com.linzx.framework.exception.UserPasswordNotMatchException;
import com.linzx.framework.shiro.bean.UserLoginInfo;
import com.linzx.framework.shiro.service.LoginService;
import com.linzx.framework.shiro.utils.ShiroUtils;
import com.linzx.framework.utils.SpringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRealm extends AuthorizingRealm {

    private static final Logger log = LoggerFactory.getLogger(UserRealm.class);

    /**
     * 授权，如果开启缓存则只会回调一次；
     * 回调时机：
     * 1、SecurityUtils.getSubject().isPermitted(String str);
     * 2、过滤器中配置了类似/**=roles[user"或者/**=perms[user]
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        UserLoginInfo userLoginInfo = ShiroUtils.getUserLoginInfo();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (Constants.ROLE_MENU_SCOPE_ALL == userLoginInfo.getMenuScope()) {
            // 管理员所有权限
            info.addRole("admin");
            info.addStringPermission("*:*:*");
        } else {
            info.setRoles(ShiroUtils.getRoleCodeSet());
            info.setStringPermissions(userLoginInfo.getMenuPermsSet());
        }
        return info;
    }

    /**
     * 登录认证
     * shiro 常见异常：
     *      CredentitalsException 凭证异常
     *          IncorrectCredentialsException 不正确的凭证
     *          ExpiredCredentialsException 凭证过期
     *      AccountException 账号异常
     *          ConcurrentAccessException 并发访问异常（多个用户同时登录时抛出）
     *          UnknownAccountException 未知的账号
     *          ExcessiveAttemptsException 认证次数超过限制
     *      DisabledAccountException 禁用的账号
     *          LockedAccountException 账号被锁定
     *          UnsupportedTokenException 使用了不支持的Token
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();
        String password = "";
        if (upToken.getPassword() != null) {
            password = new String(upToken.getPassword());
        }
        UserLoginInfo userLoginDbInfo = null;
        try {
            LoginService loginService = SpringUtils.getBeanByType(LoginService.class);
            userLoginDbInfo = loginService.loginByAccountAndPwd(username, password);
        } catch (UnknownAccountException e) {
           throw new org.apache.shiro.authc.UnknownAccountException(e.getMessage(), e);
        } catch (UserPasswordNotMatchException e) {
            throw new org.apache.shiro.authc.UnknownAccountException(e.getMessage(), e);
        } catch (Exception e) {
            log.info("对用户[" + username + "]进行登录验证..验证未通过{}", e);
            throw new AuthenticationException(e.getMessage(), e);
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userLoginDbInfo, password, getName());
        return info;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token.getClass().getName().equals(UsernamePasswordToken.class.getName()) ;
    }

}
