package com.linzx.framework.exception.handle;

import com.alibaba.fastjson.JSONObject;
import com.linzx.framework.config.properties.ShiroProperties;
import com.linzx.framework.shiro.realm.UserRealm;
import com.linzx.framework.utils.MessageUtils;
import com.linzx.framework.utils.ServletUtils;
import com.linzx.framework.web.vo.AjaxResult;
import com.linzx.framework.exception.BizCommonException;
import com.linzx.utils.MapUtils;
import com.linzx.utils.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GroupExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(UserRealm.class);

    /**
     * shiro 登陆认证抛出的异常处理
     */
    @ExceptionHandler(AuthenticationException.class)
    public String shiroAuthenticationExceptionHandle(Exception e, HttpServletRequest request, HttpServletResponse response) {
        if (ServletUtils.isAjaxRequest(request)) {
            ServletUtils.renderString(response, JSONObject.toJSONString(AjaxResult.error(e.getMessage())));
            return null;
        }
        return "redirect:" + ShiroProperties.loginUrl;
    }

    /**
     *  菜单(接口)没有权限，异常捕获。如果是相应的是json则返回json数据，否则响应html页面
     * @param request
     * @return
     */
    @ExceptionHandler(UnauthorizedException.class)
    public String shiroUnauthorizedExceptionHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (ServletUtils.isAjaxRequest(request)) {
            String message = MessageUtils.message("user.cannot.allow.operate");
            AjaxResult result = new AjaxResult(AjaxResult.Type.PERMISSION_DENY, message, null);
            ServletUtils.renderString(response, JSONObject.toJSONString(result));
            return null;
        }
        return "redirect:" + ShiroProperties.unauthorizedUrl;
    }

    /**
     * 业务异常统一处理
     */
    @ExceptionHandler(BizCommonException.class)
    public String frmExceptionHandle(Exception e, HttpServletRequest request, HttpServletResponse response) {
        log.error("frmExceptionHandle", e);
        if (ServletUtils.isAjaxRequest(request)) {
            ServletUtils.renderString(response, JSONObject.toJSONString(AjaxResult.error(e.getMessage())));
            return null;
        }
        return "forward:error/500";
    }

    /**
     * 参数校验异常
     */
    @ExceptionHandler(BindException.class)
    public String bindException(BindException e, HttpServletRequest request, HttpServletResponse response) {
        log.error("bindException", e);
        if (ServletUtils.isAjaxRequest(request)) {
            StringBuilder errorMsg = new StringBuilder();
            JSONObject retError = new JSONObject();
            List<Map> errorList = new ArrayList<>();
            for (ObjectError error : e.getAllErrors()) {
                if (error instanceof FieldError) {
                    errorList.add(MapUtils.genHashMap("field", ((FieldError) error).getField(),
                            "message", error.getDefaultMessage()));
                } else {
//                    error.get
//                    retError.put("")
                }
                errorMsg.append(error.getDefaultMessage()).append(",");
            }
            String errStr = StringUtils.removeEnd(errorMsg.toString(), ",");
            retError.put("errors", errStr);
            retError.put("errorDetail", errorList);
            ServletUtils.renderString(response, JSONObject.toJSONString(AjaxResult.validError(retError)));
            return null;
        }
        return "forward:error/500";
    }

}
