package com.linzx.framework.shiro.utils;

import com.linzx.common.utils.BeanUtils;
import com.linzx.framework.shiro.bean.RoleBean;
import com.linzx.framework.shiro.bean.UserLoginInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShiroUtils {

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    public static void logout() {
        getSubject().logout();
    }

    public static UserLoginInfo getUserLoginInfo() {
        Object principal = getSubject().getPrincipal();
        if (principal != null) {
            UserLoginInfo userLoginInfo = new UserLoginInfo();
            BeanUtils.copyBeanProp(userLoginInfo, principal);
            return userLoginInfo;
        }
        return  null;
    }

    /**
     * 重新设置登录信息
     */
    public static void setUserLoginInfo(UserLoginInfo userLoginInfo) {
        Subject subject = getSubject();
        PrincipalCollection principalCollection = subject.getPrincipals();
        String realmName = principalCollection.getRealmNames().iterator().next();
        PrincipalCollection newPrincipalCollection = new SimplePrincipalCollection(userLoginInfo, realmName);
        // 重新加载Principal
        subject.runAs(newPrincipalCollection);
    }

    /**
     * 获取角色编码集合
     */
    public static Set<String> getRoleCodeSet() {
        UserLoginInfo userLoginInfo = getUserLoginInfo();
        Set<String> roleCodeSet = new HashSet<>();
        for (RoleBean roleBean : userLoginInfo.getRoleList()) {
            roleCodeSet.add(roleBean.getRoleCode());
        }
        return  roleCodeSet;
    }

    /**
     * 获取角色id集合
     */
    public static Set<Long> getRoleIdSet() {
        UserLoginInfo userLoginInfo = getUserLoginInfo();
        Set<Long> roleIdSet = new HashSet<>();
        for (RoleBean roleBean : userLoginInfo.getRoleList()) {
            roleIdSet.add(roleBean.getRoleId());
        }
        return roleIdSet;
    }

    /**
     * 判断用户是否拥有某个权限
     */
    public static boolean isPermittedOperator(String permission) {
        return getSubject().isPermitted(permission);
    }

    /**
     * 判断用户是否拥有某个角色
     */
    public static boolean hasRoleOperator(String role)
    {
        return getSubject().hasRole(role);
    }

}
