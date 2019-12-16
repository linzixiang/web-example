package com.linzx.framework.web.support;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.linzx.utils.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 解析请求参数中的reqHeader
 */
public class ReqHeaderMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return ReqHeader.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String reqHeaderStr = webRequest.getParameter("reqHeader");
        if (StringUtils.isNotEmpty(reqHeaderStr)) {
            ReqHeader reqHeader = JSONObject.parseObject(reqHeaderStr, ReqHeader.class);
            return reqHeader;
        }
        return null;
    }

}
