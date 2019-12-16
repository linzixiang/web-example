package com.linzx.framework.shiro.web.filter;

import com.alibaba.fastjson.JSONObject;
import com.linzx.common.constant.SecurityConstants;
import com.linzx.framework.shiro.bean.UserLoginInfo;
import com.linzx.framework.shiro.core.session.OnlineLocalSession;
import com.linzx.framework.shiro.core.session.OnlineSession;
import com.linzx.framework.shiro.core.session.OnlineSessionDAO;
import com.linzx.framework.shiro.utils.ShiroUtils;
import com.linzx.framework.utils.MessageUtils;
import com.linzx.framework.utils.ServletUtils;
import com.linzx.framework.web.vo.AjaxResult;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OnlineSessionFilter extends AccessControlFilter {

    private OnlineSessionDAO onlineSessionDAO;

    public OnlineSessionFilter(OnlineSessionDAO onlineSessionDAO) {
        this.onlineSessionDAO = onlineSessionDAO;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = getSubject(request, response);
        if (subject == null || subject.getSession() == null) {
            return true;
        }
        Session session = onlineSessionDAO.readSession(subject.getSession().getId());
        if (session != null && session instanceof OnlineSession) {
            OnlineSession onlineSession = (OnlineSession) session;
            request.setAttribute(SecurityConstants.ONLINE_SESSION, onlineSession);
            boolean isGuest = onlineSession.getUserId() == null || onlineSession.getUserId() == 0L;
            if (isGuest == true) {
                UserLoginInfo userLoginInfo = ShiroUtils.getUserLoginInfo();
                if (userLoginInfo != null) {
                    onlineSession.setUserId(userLoginInfo.getUserId());
                    onlineSession.setLoginName(userLoginInfo.getLoginAccount());
                    onlineSession.markAttributeChanged();
                }
            }
            if (OnlineLocalSession.STATUS_ONFFLINE.equals(onlineSession.getOnlineStatus())) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if (subject != null) {
            subject.logout();
        }
        boolean isAjaxRequest = ServletUtils.isAjaxRequest((HttpServletRequest) request);
        if (isAjaxRequest) {
            AjaxResult ajaxResult = new AjaxResult(AjaxResult.Type.LOGIN_ERROR, MessageUtils.message("user.login.auth.invalid"), null);
            ServletUtils.renderString((HttpServletResponse) response, JSONObject.toJSONString(ajaxResult));
            return false;
        } else {
            saveRequestAndRedirectToLogin(request, response);
        }
        return false;
    }
}
