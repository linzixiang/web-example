package com.linzx.framework.shiro.web.filter;

import com.alibaba.fastjson.JSONObject;
import com.linzx.framework.web.vo.AjaxResult;
import com.linzx.framework.utils.MessageUtils;
import com.linzx.framework.utils.ServletUtils;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 如果客户端接收的是Accept:application/json，则以json格式返回
 */
public class LoginAuthFilter extends UserFilter {

    private static final Logger log = LoggerFactory.getLogger(LoginAuthFilter.class);

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        boolean isAjaxRequest = ServletUtils.isAjaxRequest((HttpServletRequest) request);
        if (isAjaxRequest) {
            AjaxResult ajaxResult = new AjaxResult(AjaxResult.Type.LOGIN_ERROR, MessageUtils.message("user.login.auth.invalid"), null);
            ServletUtils.renderString((HttpServletResponse) response, JSONObject.toJSONString(ajaxResult));
            return false;
        }
        return super.onAccessDenied(request, response);
    }

}
