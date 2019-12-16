package com.linzx.framework.web.support.aspect;

import com.linzx.common.base.BaseEntity;
import com.linzx.framework.web.annotation.DataScope;
import com.linzx.framework.shiro.bean.RoleBean;
import com.linzx.framework.shiro.bean.UserLoginInfo;
import com.linzx.framework.shiro.utils.ShiroUtils;
import com.linzx.utils.StrFormatter;
import com.linzx.utils.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 数据过滤处理
 */
@Aspect
@Component
public class DataScopeAspect {

    public static final String DATA_SCOPE = "dataScope";

    @Pointcut("@annotation(com.linzx.framework.web.annotation.DataScope)")
    public void dataScopePointCut() {
    }

    @Before("dataScopePointCut()")
    public void doBefore(JoinPoint point) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        DataScope dataScopeAnnotation = method.getAnnotation(DataScope.class);
        if (dataScopeAnnotation == null) {
            return ;
        }
        UserLoginInfo userLoginInfo = ShiroUtils.getUserLoginInfo();
        if (userLoginInfo != null && !userLoginInfo.isAdmin()) {
            // 不是管理员，需要控制数据权限
            dataScopeFilter(point, userLoginInfo, dataScopeAnnotation);
        }
    }

    /**
     * 拼接数据权限
     */
    private void dataScopeFilter(JoinPoint joinPoint, UserLoginInfo userLoginInfo, DataScope dataScopeAnnotation) {
        StringBuilder sqlString = new StringBuilder();
        String CONDITION_OR = " or ";
        if (userLoginInfo.getDataScope() == UserLoginInfo.DATA_SCOPE_ALL) {
            sqlString.append(CONDITION_OR).append("1 = 1");
        }
        // 部门权限sql拼接
        if (StringUtils.isNotEmpty(dataScopeAnnotation.deptAlias())) {
            if (userLoginInfo.getDataScope() == UserLoginInfo.DATA_SCOPE_SELF_DEPT) {
                // 本部门
                String sqlSelfDept = StrFormatter.format("{}.dept_id = {}", dataScopeAnnotation.deptAlias(), userLoginInfo.getDeptId());
                sqlString.append(CONDITION_OR).append(sqlSelfDept);
            } else if (userLoginInfo.getDataScope() == UserLoginInfo.DATA_SCOPE_SELF_CHILDREN_DEPT) {
                // 本部门及其以下部门
                String sqlSelfChildren = StringUtils.format(
                        "{}.dept_id in (select dept_id from sys_dept where dept_id = {} or ancestors like '{},%' )",
                        dataScopeAnnotation.deptAlias(), userLoginInfo.getDeptId(), userLoginInfo.getDeptAncestors());
                sqlString.append(CONDITION_OR).append(sqlSelfChildren);
            } else if (userLoginInfo.getDataScope() == UserLoginInfo.DATA_SCOPE_CUSTOMIZE_DEPT) {
                // 自定义部门权限
                for (RoleBean roleBean : userLoginInfo.getRoleList()) {
                    String sqlDept = StrFormatter.format("{}.dept_id in ( select dept_id from sys_role_dept where role_id = {} ) ", dataScopeAnnotation.deptAlias(), roleBean.getRoleId());
                    sqlString.append(CONDITION_OR).append(sqlDept);
                }
            }
        }
        // 用户权限拼接
        if (StringUtils.isNotEmpty(dataScopeAnnotation.userAlias())) {

        }
        if (StringUtils.isNotEmpty(sqlString.toString())) {
            BaseEntity baseEntity = (BaseEntity) joinPoint.getArgs()[0];
            baseEntity.getParams().put(DATA_SCOPE, " AND (" + sqlString.substring(CONDITION_OR.length()) + ")");
        }
    }

}
