package com.linzx.framework.shiro.core.session;

import com.linzx.utils.DateUtils;
import com.linzx.utils.MapUtils;
import com.linzx.utils.ObjectUtils;
import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.StoppedSessionException;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.redisson.RedissonScript;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.linzx.framework.shiro.core.session.OnlineRedisScript.*;

/**
 * redis集群会话
 */
public class OnlineRedisSession extends AbstractOnlineSession {

    private Logger logger = LoggerFactory.getLogger(OnlineRedisSession.class);

    public static final String ID = "id";
    public static final String START_TIME_STAMP = "startTimestamp";
    public static final String STOP_TIME_STAMP = "stopTimestamp";
    public static final String LAST_ACCESS_TIME = "lastAccessTime";
    public static final String TIMEOUT = "timeout";
    public static final String HOST = "host";
    public static final String USER_ID = "userId";
    public static final String LOGIN_NAME = "loginName";
    public static final String BROWSER = "browser";
    public static final String OS = "os";

    /** redis会话id **/
    private Serializable id;

    /**
     * 在sessionId没有生成之前临时保存
     **/
    private Map<String, Object> temp = new ConcurrentHashMap<>();

    private Date lastTouchDate;

    private RedissonClient redissonClient;

    public OnlineRedisSession(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
        this.setTimeout(DefaultSessionManager.DEFAULT_GLOBAL_SESSION_TIMEOUT);
        Date nowDate = DateUtils.getNowDate();
        this.setStartTimestamp(nowDate);
        this.setLastAccessTime(nowDate);
    }

    @Override
    public void setId(Serializable id) {
        this.id = id;
        saveToCache(id.toString());
    }

    @Override
    public boolean isExpired() {
        return false;
    }

    @Override
    public void setExpired(boolean expired) {
    }

    @Override
    public Long getUserId() {
        if (isFromTempMap()) {
            return MapUtils.getLong(temp, USER_ID);
        }
        String sessionId = getId().toString();
        RedissonScript script = (RedissonScript) redissonClient.getScript();
        List<Object> keys = new ArrayList<>(1);
        keys.add(sessionId);
        try {
            Long res = script.eval(sessionId, RScript.Mode.READ_ONLY, GET_USER_ID,
                    RScript.ReturnType.MAPVALUE, keys);
            return res;
        } catch (RedisException e) {
            logger.error("获取userId失败：" , e);
        }
        return null;
    }

    @Override
    public void setUserId(Long userId) {
        if (isFromTempMap()) {
            temp.put(USER_ID, userId);
            return;
        }
        String sessionId = getId().toString();
        RedissonScript script = (RedissonScript) redissonClient.getScript();
        List<Object> keys = new ArrayList<>(1);
        keys.add(sessionId);
        try {
            script.eval(sessionId, RScript.Mode.READ_WRITE, USER_ID_SCRIPT, RScript.ReturnType.VALUE, keys, userId);
        } catch (RedisException e) {
            logger.error("更新userId失败：", e);
            convertException(e);
        }
    }

    @Override
    public String getLoginName() {
        if (isFromTempMap()) {
            return MapUtils.getStringValue(temp, LOGIN_NAME);
        }
        String sessionId = getId().toString();
        RedissonScript script = (RedissonScript) redissonClient.getScript();
        List<Object> keys = new ArrayList<>(1);
        keys.add(sessionId);
        try {
            String res = script.eval(sessionId, RScript.Mode.READ_ONLY, GET_LOGIN_NAME,
                    RScript.ReturnType.MAPVALUE, keys);
            return res;
        } catch (RedisException e) {
            logger.error("获取loginName属性失败", e);
        }
        return null;
    }

    @Override
    public void setLoginName(String loginName) {
        if (ObjectUtils.isEmpty(getId())) {
            temp.put(LOGIN_NAME, loginName);
            return;
        }
        String sessionId = getId().toString();
        RedissonScript script = (RedissonScript) redissonClient.getScript();
        List<Object> keys = new ArrayList<>(1);
        keys.add(sessionId);
        try {
            script.eval(sessionId, RScript.Mode.READ_WRITE, LOGIN_NAME_SCRIPT, RScript.ReturnType.VALUE, keys, loginName);
        } catch (RedisException e) {
            logger.error("更新loginName失败：", e);
            convertException(e);
        }
    }

    @Override
    public String getBrowser() {
        if (ObjectUtils.isEmpty(getId())) {
            return MapUtils.getStringValue(temp, BROWSER);
        }
        String sessionId = getId().toString();
        RedissonScript script = (RedissonScript) redissonClient.getScript();
        List<Object> keys = new ArrayList<>(1);
        keys.add(sessionId);
        try {
            String res = script.eval(sessionId, RScript.Mode.READ_ONLY, GET_BROWSER,
                    RScript.ReturnType.MAPVALUE, keys);
            return res;
        } catch (Exception e) {
            logger.error("获取browser属性失败", e);
        }
        return "";
    }

    @Override
    public void setBrowser(String browser) {
        if (ObjectUtils.isEmpty(getId())) {
            temp.put(BROWSER, browser);
            return;
        }
    }

    @Override
    public String getOs() {
        if (ObjectUtils.isEmpty(getId())) {
            return MapUtils.getStringValue(temp, OS);
        }
        String sessionId = getId().toString();
        RedissonScript script = (RedissonScript) redissonClient.getScript();
        List<Object> keys = new ArrayList<>(1);
        keys.add(sessionId);
        try {
            String res = script.eval(sessionId, RScript.Mode.READ_ONLY, GET_OS,
                    RScript.ReturnType.MAPVALUE, keys);
            return res;
        } catch (Exception e) {
            logger.error("获取os属性失败", e);
        }
        return "";
    }

    @Override
    public void setOs(String os) {
        if (ObjectUtils.isEmpty(getId())) {
            temp.put(OS, os);
            return;
        }
        // 会话初始化时固定，不允许修改
    }

    @Override
    public Serializable getId() {
        return this.id;
    }

    @Override
    public Date getStartTimestamp() {
        if (ObjectUtils.isEmpty(getId())) {
            return MapUtils.getDate(temp, START_TIME_STAMP);
        }
        String sessionId = getId().toString();
        RedissonScript script = (RedissonScript) redissonClient.getScript();
        List<Object> keys = new ArrayList<>(1);
        keys.add(sessionId);
        Date startTime = null;
        try {
            startTime = script.eval(sessionId, RScript.Mode.READ_ONLY, GET_START_TIME_STAMP, RScript.ReturnType.MAPVALUE, keys);
        } catch (RedisException e) {
            logger.error("获取startTimestamp失败：", e);
            convertException(e);
        }
        if (startTime == null) {
            throw new InvalidSessionException();
        }
        return startTime;
    }

    @Override
    public void setLastAccessTime(Date lastAccessTime) {
        if (ObjectUtils.isEmpty(getId())) {
            temp.put(LAST_ACCESS_TIME, lastAccessTime);
            return;
        }
        // 只有在初始化之前会调用，后面更新lastAccessTime走touch方法
    }

    @Override
    public Date getLastAccessTime() {
        if (ObjectUtils.isEmpty(getId())) {
            return MapUtils.getDate(temp, LAST_ACCESS_TIME);
        }
        String sessionId = getId().toString();
        RedissonScript script = (RedissonScript) redissonClient.getScript();
        List<Object> keys = new ArrayList<>(1);
        keys.add(sessionId);
        Date lastAccessTime = null;
        try {
            lastAccessTime = script.eval(sessionId, RScript.Mode.READ_ONLY, GET_LAST_ACCESS_TIME, RScript.ReturnType.MAPVALUE, keys);
        } catch (RedisException e) {
            logger.error("获取lastAccessTime失败：", e);
        }
        return lastAccessTime;
    }

    @Override
    public void setTimeout(long timeout) {
        if (ObjectUtils.isEmpty(getId())) {
            temp.put(TIMEOUT, timeout);
            return;
        }
    }

    @Override
    public long getTimeout() {
        if (ObjectUtils.isEmpty(getId())) {
            return MapUtils.getLongValue(temp, TIMEOUT);
        }
        String sessionId = getId().toString();
        RedissonScript script = (RedissonScript) redissonClient.getScript();
        List<Object> keys = new ArrayList<>(1);
        keys.add(sessionId);
        Integer res = null;
        try {
            res = script.eval(sessionId, RScript.Mode.READ_ONLY, GET_TIMEOUT_SCRIPT,
                    RScript.ReturnType.MAPVALUE, keys);
        } catch (RedisException e) {
            logger.error("获取timeout属性失败", e);
            convertException(e);
        }
        if (res == null) {
            throw new InvalidSessionException();
        }
        return new Long(res);
    }

    @Override
    public void setHost(String host) {
        if (ObjectUtils.isEmpty(getId())) {
            temp.put(HOST, host);
            return;
        }
    }

    @Override
    public String getHost() {
        if (ObjectUtils.isEmpty(getId())) {
            return MapUtils.getStringValue(temp, HOST);
        }
        String sessionId = getId().toString();
        RedissonScript script = (RedissonScript) redissonClient.getScript();
        List<Object> keys = new ArrayList<>(1);
        keys.add(sessionId);
        try {
            String res = script.eval(sessionId, RScript.Mode.READ_ONLY, GET_HOST,
                    RScript.ReturnType.MAPVALUE, keys);
            return res;
        } catch (Exception e) {
            logger.error("获取host属性失败", e);
        }
        return "";
    }

    @Override
    public void setStopTimestamp(Date stopTimestamp) {
        stop();
    }

    @Override
    public void stop() {
        String sessionId = getId().toString();
        RedissonScript script = (RedissonScript) redissonClient.getScript();
        List<Object> keys = new ArrayList<>(1);
        keys.add(sessionId);
        try {
            script.eval(sessionId, RScript.Mode.READ_WRITE, STOP_SCRIPT, RScript.ReturnType.VALUE, keys, DateUtils.getNowDate());
        } catch (RedisException e) {
            logger.error("更新stopTimestamp失败：", e);
        }
    }

    @Override
    public void setStartTimestamp(Date startTimestamp) {
        if (ObjectUtils.isEmpty(getId())) {
            temp.put(START_TIME_STAMP, startTimestamp);
            return;
        }
        String sessionId = getId().toString();
        RedissonScript script = (RedissonScript) redissonClient.getScript();
        List<Object> keys = new ArrayList<>(1);
        keys.add(sessionId);
        try {
            script.eval(sessionId, RScript.Mode.READ_WRITE, START_TIME_STAMP_SCRIPT, RScript.ReturnType.VALUE, keys, startTimestamp);
        } catch (RedisException e) {
            logger.error("更新startTimestamp失败：", e);
        }
    }

    @Override
    public Date getStopTimestamp() {
        if (ObjectUtils.isEmpty(getId())) {
            return MapUtils.getDate(temp, STOP_TIME_STAMP);
        }
        String sessionId = getId().toString();
        RedissonScript script = (RedissonScript) redissonClient.getScript();
        List<Object> keys = new ArrayList<>(1);
        keys.add(sessionId);
        try {
            Date stopTimestamp = script.eval(sessionId, RScript.Mode.READ_ONLY, GET_STOP_TIME_STAMP, RScript.ReturnType.MAPVALUE, keys);
            return stopTimestamp;
        } catch (RedisException e) {
            logger.error("获取stopTimestamp失败：", e);
        }
        return null;
    }

    protected boolean isTimedOut() {
        String sessionId = getId().toString();
        RedissonScript script = (RedissonScript) redissonClient.getScript();
        List<Object> keys = new ArrayList<>(1);
        keys.add(sessionId);
        try {
            script.eval(sessionId, RScript.Mode.READ_ONLY, IS_TIMEOUT,
                    RScript.ReturnType.MAPVALUE, keys);
        } catch (RedisException e) {
            return true;
        }
        return false;
    }

    @Override
    public void touch() {
        if (lastTouchDate != null && DateUtils.getNowDate().before(DateUtils.addMinutes(lastTouchDate, 1))) {
            // 控制刷新频率，减轻redis压力
            return;
        }
        lastTouchDate = DateUtils.getNowDate();
        String sessionId = getId().toString();
        RedissonScript script = (RedissonScript) redissonClient.getScript();
        List<Object> keys = new ArrayList<>(1);
        keys.add(sessionId);
        try {
            script.evalAsync(sessionId, RScript.Mode.READ_WRITE, TOUCH_SCRIPT, RScript.ReturnType.VALUE, keys, lastTouchDate);
        } catch (RedisException e) {
            logger.error("重置会话有效期：", e);
            convertException(e);
        }
    }

    /**
     * 保存到缓存
     **/
    private void saveToCache(String sessionId) {
        Long userId = MapUtils.getLong(temp, USER_ID);
        RedissonScript script = (RedissonScript) redissonClient.getScript();
        Date startTimestamp = MapUtils.getDate(temp, START_TIME_STAMP);
        Integer timeout = MapUtils.getInteger(temp, TIMEOUT);
        String host = MapUtils.getStringValue(temp, HOST);
        String loginName = MapUtils.getStringValue(temp, LOGIN_NAME);
        String browser = MapUtils.getStringValue(temp, BROWSER, "");
        String os = MapUtils.getStringValue(temp, OS, "");
        List<Object> keys = new ArrayList<>(1);
        keys.add(sessionId);
        script.eval(sessionId, RScript.Mode.READ_WRITE, INIT_SCRIPT, RScript.ReturnType.VALUE, keys,
                sessionId, startTimestamp, timeout, host, userId, loginName,
                browser, os);
        temp.clear();
    }

    private boolean isFromTempMap() {
        return ObjectUtils.isEmpty(getId()) || MapUtils.isNotEmpty(temp);
    }

    public static final String RETURN_CODE_EXPIRED = "-1";

    public static final String RETURN_CODE_STOPPED = "-2";

    public static final String RETURN_CODE_INVALID = "-3";


    private void convertException(RedisException e) {
        String errMsg = e.getMessage();
        if (RETURN_CODE_EXPIRED.equals(errMsg)) {
            throw new ExpiredSessionException();
        } else if (RETURN_CODE_STOPPED.equals(errMsg)) {
            throw new StoppedSessionException();
        } else if (RETURN_CODE_INVALID.equals(errMsg)) {
            throw new InvalidSessionException();
        } else {
            throw e;
        }
    }

}
