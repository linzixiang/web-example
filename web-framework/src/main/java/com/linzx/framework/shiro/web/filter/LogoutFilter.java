package com.linzx.framework.shiro.web.filter;

import com.alibaba.fastjson.JSONObject;
import com.linzx.framework.web.vo.AjaxResult;
import com.linzx.framework.utils.ServletUtils;
import com.linzx.utils.StringUtils;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 退出登录过滤器
 */
public class LogoutFilter extends org.apache.shiro.web.filter.authc.LogoutFilter {

    /**
     * 退出后重定向的地址
     */
    private String loginUrl;

    public LogoutFilter(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        boolean ajaxRequest = ServletUtils.isAjaxRequest((HttpServletRequest) request);
        if (ajaxRequest) { // 如果是ajax请求，则返回json数据，而不是重定向到登录页面
            Subject subject = this.getSubject(request, response);
            try {
                subject.logout();
            } catch (SessionException var6) {

            }
            ServletUtils.renderString((HttpServletResponse) response, JSONObject.toJSONString(AjaxResult.success()));
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * 退出跳转URL
     */
    @Override
    protected String getRedirectUrl(ServletRequest request, ServletResponse response, Subject subject) {
        if (StringUtils.isNotEmpty(loginUrl)) {
            return loginUrl;
        }
        return super.getRedirectUrl(request, response, subject);
    }

}
