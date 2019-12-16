package com.linzx.framework.web.interceptor;

import com.linzx.framework.core.context.ThreadLocalVariable;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 框架的管理
 */
public class FrmManageInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalVariable.clear();
    }

}
