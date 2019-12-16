package com.linzx.framework.shiro.core.session;

import static com.linzx.framework.shiro.core.session.OnlineRedisSession.*;

/**
 * redis的session操作lua脚本
 */
public class OnlineRedisScript {

    /**
     * 初始化会话
     **/
    public static final String INIT_SCRIPT = new StringBuilder()
            .append("redis.call('HMSET',KEYS[1],'")
            .append(ID + "',ARGV[1],'")
            .append(START_TIME_STAMP + "',ARGV[2],'")
            .append(LAST_ACCESS_TIME + "',ARGV[2],'")
            .append(TIMEOUT + "',ARGV[3],'")
            .append(HOST + "',ARGV[4],'")
            .append(USER_ID + "',ARGV[5],'")
            .append(LOGIN_NAME + "',ARGV[6],'")
            .append(BROWSER + "',ARGV[7],'")
            .append(OS + "',ARGV[8])\n")
            .append("redis.call('PEXPIRE', KEYS[1], ARGV[3])")
            .toString();

    /**
     * 设置userId属性
     */
    public static final String USER_ID_SCRIPT = new StringBuilder()
            .append("if redis.call('PTTL', KEYS[1]) <= 0 then return " + makeError(RETURN_CODE_EXPIRED) + " end \n")
//            .append("if redis.call('HEXISTS', KEYS[1], '" + USER_ID + "') == 1 then return " + makeError(RETURN_CODE_STOPPED) + " end \n")
            .append("redis.call('HSET', KEYS[1], '" + USER_ID + "', ARGV[1])")
            .toString();

    /**
     * 设置loginName属性
     */
    public static final String LOGIN_NAME_SCRIPT = new StringBuilder()
            .append("if redis.call('PTTL', KEYS[1]) <= 0 then return " + makeError(RETURN_CODE_EXPIRED) + " end \n")
//            .append("if redis.call('HEXISTS', KEYS[1], '" + LOGIN_NAME + "') == 1 then return " + makeError(RETURN_CODE_STOPPED) + " end \n")
            .append("redis.call('HSET', KEYS[1], '" + LOGIN_NAME + "', ARGV[1])")
            .toString();

    /**
     * 设置会话stop属性值
     **/
    public static final String STOP_SCRIPT = new StringBuilder()
            .append("if redis.call('PTTL', KEYS[1]) <= 0 then return " + makeError(RETURN_CODE_EXPIRED) + " end \n")
            .append("if redis.call('HEXISTS', KEYS[1], '" + STOP_TIME_STAMP + "') == 1 then return " + makeError(RETURN_CODE_STOPPED) + " end \n")
            .append("redis.call('HSET', KEYS[1], '" + STOP_TIME_STAMP + "', ARGV[1])")
            .toString();

    /**
     * 设置开始访问时间
     */
    public static final String START_TIME_STAMP_SCRIPT = new StringBuilder()
            .append("if redis.call('PTTL', KEYS[1]) <= 0 then return " + makeError(RETURN_CODE_EXPIRED) + " end \n")
//            .append("if redis.call('HEXISTS', KEYS[1], '" + START_TIME_STAMP + "') == 1 then return " + makeError(RETURN_CODE_STOPPED) + " end \n")
            .append("redis.call('HSET', KEYS[1], '" + START_TIME_STAMP + "', ARGV[1])")
            .toString();

    /**
     * 更新最后访问时间，重置session的有效期
     **/
    public static final String TOUCH_SCRIPT = new StringBuilder()
            .append("if redis.call('PTTL', KEYS[1]) <= 0 then return " + makeError(RETURN_CODE_EXPIRED) + " end \n")
            .append("if redis.call('HEXISTS', KEYS[1], '" + STOP_TIME_STAMP + "') == 1 then return " + makeError(RETURN_CODE_STOPPED) + " end \n")
            .append("local timeout = redis.call('HGET', KEYS[1], '" + TIMEOUT + "')\n")
            .append("if timeout == nil then return " + makeError(RETURN_CODE_INVALID) + " end \n")
            .append("redis.call('HSET', KEYS[1], '" + LAST_ACCESS_TIME + "', ARGV[1])\n")
            .append("redis.call('PEXPIRE', KEYS[1], timeout)")
            .toString();

    /**
     * 获取最后访问时间
     */
    public static final String GET_LAST_ACCESS_TIME = new StringBuilder()
            .append("if redis.call('PTTL', KEYS[1]) <= 0 then return " + makeError(RETURN_CODE_EXPIRED) + " end \n")
            .append("local lastTime = redis.call('HGET', KEYS[1], '" + LAST_ACCESS_TIME + "')\n")
            .append("if lastTime == nil then return " + makeError(RETURN_CODE_INVALID) + " end \n")
            .append("return lastTime")
            .toString();
    /**
     * 获取开始访问时间
     */
    public static final String GET_START_TIME_STAMP = new StringBuilder()
            .append("if redis.call('PTTL', KEYS[1]) <= 0 then return " + makeError(RETURN_CODE_EXPIRED) + " end \n")
            .append("local startTime = redis.call('HGET', KEYS[1], '" + START_TIME_STAMP + "')\n")
            .append("if startTime == nil then return " + makeError(RETURN_CODE_INVALID) + " end \n")
            .append("return startTime")
            .toString();

    /**
     * 获取会话的timeout属性
     */
    public static final String GET_TIMEOUT_SCRIPT = new StringBuilder()
            .append("if redis.call('PTTL', KEYS[1]) <= 0 then return " + makeError(RETURN_CODE_EXPIRED) + " end\n")
            .append("if redis.call('HEXISTS', KEYS[1], '" + STOP_TIME_STAMP + "') == 1 then return " + makeError(RETURN_CODE_STOPPED) + " end\n")
            .append("local timeout = redis.call('HGET', KEYS[1], '" + TIMEOUT + "')\n")
            .append("if timeout == nil then return " + makeError(RETURN_CODE_INVALID) + " end\n")
            .append("return timeout")
            .toString();

    /**
     * 判断会话是否过期
     **/
    public static final String IS_TIMEOUT = new StringBuilder()
            .append("if redis.call('PTTL', KEYS[1]) <= 0 then return " + makeError(RETURN_CODE_EXPIRED) + " end\n")
            .append("if redis.call('HEXISTS', KEYS[1], '" + STOP_TIME_STAMP + "') == 1 then return " + makeError(RETURN_CODE_STOPPED) + " end\n")
            .toString();

    /**
     * 获取用户id
     */
    public static final String GET_USER_ID = new StringBuilder()
            .append("local userId = redis.call('HGET', KEYS[1], '" + USER_ID + "')\n")
            .append("return userId")
            .toString();

    /**
     * 获取用户登录帐号，不能返回空
     */
    public static final String GET_LOGIN_NAME = new StringBuilder()
            .append("local loginName = redis.call('HGET', KEYS[1], '" + LOGIN_NAME + "')\n")
            .append("return loginName")
            .toString();

    /**
     * 获取操作系统，允许为空
     */
    public static final String GET_OS = new StringBuilder()
            .append("local os = redis.call('HGET', KEYS[1], '" + OS + "')\n")
            .append("return os")
            .toString();

    /**
     * 获取浏览器类型，允许为空
     */
    public static final String GET_BROWSER = new StringBuilder()
            .append("local browser = redis.call('HGET', KEYS[1], '" + BROWSER + "')\n")
            .append("return browser")
            .toString();

    /**
     * 获取登录主机地址
     */
    public static final String GET_HOST = new StringBuilder()
            .append("local host = redis.call('HGET', KEYS[1], '" + HOST + "')\n")
            .append("return host")
            .toString();

    /**
     * 获取会话终止时间
     */
    public static final String GET_STOP_TIME_STAMP = new StringBuilder()
            .append("local stopTimeStamp = redis.call('HGET', KEYS[1], '" + STOP_TIME_STAMP + "')\n")
            .append("return stopTimeStamp")
            .toString();

    private static String makeError(String errMsg) {
        return "redis.error_reply('" + errMsg + "')";
    }

    public static void main(String[] args) {
        System.out.println("--------------初始化会话--------------------");
        System.out.println(INIT_SCRIPT);
        System.out.println("--------------设置会话终止时间--------------------");
        System.out.println(STOP_SCRIPT);
        System.out.println("--------------重置会话有效期--------------------");
        System.out.println(TOUCH_SCRIPT);
        System.out.println("--------------获取最后访问时间--------------------");
        System.out.println(GET_LAST_ACCESS_TIME);
        System.out.println("--------------获取会话有效期--------------------");
        System.out.println(GET_TIMEOUT_SCRIPT);
        System.out.println("--------------判断会话是否过期--------------------");
        System.out.println(IS_TIMEOUT);

    }

}
