package com.linzx.framework.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.linzx.framework.web.annotation.RepeatSubmit;
import com.linzx.framework.core.util.concurrent.ConcurrentOpFactory;
import com.linzx.framework.core.util.concurrent.lock.ProLock;
import com.linzx.framework.utils.MessageUtils;
import com.linzx.framework.utils.ServletUtils;
import com.linzx.framework.web.vo.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 通过给会话上锁的方式防止重复提交
 */
public class RepeatSubmitInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(RepeatSubmitInterceptor.class);

    private ThreadLocal<ProLock>  repeatSubmitLock = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
            if (annotation != null) {
                ProLock lock = ConcurrentOpFactory.getInstance().getReentrantLock(getLockKey(request));
                // 加锁，锁存活时间为10秒
                boolean isGetLock = lock.tryLock(0, 10, TimeUnit.SECONDS);
                if (!isGetLock) {
                    // 没有获取到锁，认为请求未完成，给出提示
                    String message = MessageUtils.message("user.request.submit.repeat");
                    AjaxResult result = new AjaxResult(AjaxResult.Type.ERROR, message, null);
                    ServletUtils.renderString(response, JSONObject.toJSONString(result));
                    return false;
                } else {
                    repeatSubmitLock.set(lock);
                }
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ProLock lock = repeatSubmitLock.get();
        if (lock != null && lock.isLocked()) {
            try {
                lock.unlock();
            } catch (Exception e) {
                logger.error("锁解除失败", e);
            }
        }
    }

    private String getLockKey(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        String sessionId = httpSession.getId();
        String uri = request.getRequestURI().replace("/", ".");
        String lockKey = sessionId + uri;
        return lockKey;
    }

}
