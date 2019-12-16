package com.linzx.framework.web.interceptor;

import com.linzx.framework.web.annotation.SensitiveField;
import com.linzx.framework.web.annotation.SensitiveFields;
import com.linzx.framework.bean.SensitiveBean;
import com.linzx.framework.core.context.ContextManager;
import com.linzx.framework.core.context.ThreadLocalVariable;
import com.linzx.framework.shiro.bean.UserLoginInfo;
import com.linzx.framework.shiro.utils.ShiroUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 敏感字段注解解析，放入线程变量中，SensitivePropSerialize取出解析结果
 */
public class SensAnnotationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            SensitiveFields sensitiveFields = method.getAnnotation(SensitiveFields.class);
            SensitiveField sensitiveField = method.getAnnotation(SensitiveField.class);
            if (sensitiveFields == null && sensitiveField == null) {
                return true;
            }
            UserLoginInfo userLoginInfo = ShiroUtils.getUserLoginInfo();
            if (userLoginInfo == null || userLoginInfo.isAdmin()) {
                // 超级管理员不对敏感字段处理
                return true;
            }
            Map<String, SensitiveBean> sensitiveFieldMap = new HashMap<>();
            if (sensitiveFields != null) {
                SensitiveField[] sensitiveFieldArr = sensitiveFields.value();
                for(SensitiveField item : sensitiveFieldArr) {
                    addSensitiveField(sensitiveFieldMap, item, userLoginInfo);
                }
            }

            addSensitiveField(sensitiveFieldMap, sensitiveField, userLoginInfo);
            ThreadLocalVariable.setSensitiveVariable(sensitiveFieldMap);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalVariable.removeSensitiveVariable();
    }

    private void addSensitiveField(Map<String, SensitiveBean> sensitiveFieldMap, SensitiveField sensitiveField, UserLoginInfo userLoginInfo) {
        if (sensitiveField == null) {
            return;
        }
        String code = sensitiveField.code();
        if (!userLoginInfo.isSensProp(code)) {
            // 字段不是敏感字段
            return;
        }
        SensitiveBean sensitiveBean = ContextManager.getSensitive(code);
        String[] colNames = sensitiveField.colNames();
        for (String colName : colNames) {
            sensitiveFieldMap.put(colName, sensitiveBean);
        }
    }
}
