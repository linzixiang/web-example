package com.linzx.framework.exception.handle;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * 自定义错误响应
 */
 @Component
public class CustomizeErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
//        AjaxResult ajaxResult = (AjaxResult) webRequest.getAttribute("ext", RequestAttributes.SCOPE_REQUEST);
//        if (ajaxResult != null) {
//            errorAttributes.putAll(ajaxResult);
//            errorAttributes.remove("status");
//            errorAttributes.remove("error");
//            errorAttributes.remove("trace");
//            errorAttributes.remove("message");
//        }
        return errorAttributes;
    }
}
