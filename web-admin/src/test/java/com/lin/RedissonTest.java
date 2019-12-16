package com.lin;

import com.linzx.MainApplication;
import com.linzx.framework.config.support.FastjsonCodec;
import com.linzx.framework.shiro.core.session.OnlineRedisSession;
import com.linzx.framework.shiro.core.session.OnlineSession;
import org.apache.shiro.session.mgt.SimpleSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.RedissonScript;
import org.redisson.api.RMap;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class RedissonTest {

    @Autowired(required = false)
    private RedissonClient redissonClient;

    @Test
    public void redisMapTest() {
        RMap<String, Object> test = redissonClient.getMap("test");
        test.put("testKey", "testVal");
        Object testKey = test.get("testKey");
        int i = 0;
    }

    private String sessionId = "9d8ce230-23a7-4608-b67b-332d19b0d266";

    @Test
    public void createRedisSession() {
        OnlineSession onlineSession = createOnlineSession(redissonClient);
    }

    @Test
    public void stopRedisSession() {
        OnlineSession onlineSession = createOnlineSession(redissonClient);
        Date stopTimestamp = onlineSession.getStopTimestamp();
        onlineSession.stop();
        Date stopTimestamp1 = onlineSession.getStopTimestamp();
        onlineSession.validate();
    }

    @Test
    public void touchRedisSession() {
        OnlineSession onlineSession = createOnlineSession(redissonClient);
        onlineSession.touch();
    }

    @Test
    public void expireRedisSession() {
        SimpleSession onlineSession = (SimpleSession) createOnlineSession(redissonClient);
        onlineSession.validate();
    }

    @Test
    public void getSessionAttr() {
        OnlineSession onlineSession = createOnlineSession(redissonClient);
        Long userId = onlineSession.getUserId();
        String loginName = onlineSession.getLoginName();
        String browser = onlineSession.getBrowser();
        Date lastAccessTime = onlineSession.getLastAccessTime();
        String host = onlineSession.getHost();

    }

    @Test
    public void setSessionAttr() {
        OnlineSession onlineSession = createOnlineSession(redissonClient);
        onlineSession.setUserId(new Long(123));
    }

    private OnlineSession createOnlineSession(RedissonClient redissonClient) {
        OnlineSession onlineSession = new OnlineRedisSession(redissonClient);
        onlineSession.setHost("127.0.0.1");
        onlineSession.setTimeout(6000000);
//        onlineSession.setLoginName("admin");
        onlineSession.setBrowser("google");
        onlineSession.setOs("windows");
//        onlineSession.setUserId(new Long(123));
        onlineSession.setId(sessionId);
        return onlineSession;
    }

    @Test
    public void initSessionEvalScript() {
        String sessionId = UUID.randomUUID().toString();
        List<Object> keys = new ArrayList<>(1);
        keys.add(sessionId);

        RedissonScript script = (RedissonScript) this.redissonClient.getScript(FastjsonCodec.instance);
        script.eval(sessionId, RScript.Mode.READ_WRITE, INIT_SCRIPT,
                RScript.ReturnType.VALUE, keys, sessionId, 600000, new Date(),
                "127.0.0.1");
        System.out.println("sessionId:" + sessionId);
    }



    @Test
    public void getStartTimestampEvalScript() {
        List<Object> keys = new ArrayList<>(1);
        keys.add(this.sessionId);

        RedissonScript script = (RedissonScript) this.redissonClient.getScript(FastjsonCodec.instance);
        Date res = script.eval(this.sessionId, RScript.Mode.READ_ONLY, GET_START_SCRIPT,
                    RScript.ReturnType.MAPVALUE, keys);
        int i = 0;
    }

    

    public static final String RETURN_CODE_EXPIRED = "-1";

    public static final String RETURN_CODE_STOPPED = "-2";

    public static final String RETURN_CODE_INVALID = "-3";

    public static final String INFO_ID_KEY = "id";
    public static final String INFO_START_KEY = "startTimestamp";
    public static final String INFO_STOP_KEY = "stopTimestamp";
    public static final String INFO_LAST_KEY = "lastAccessTime";
    public static final String INFO_TIMEOUT_KEY = "timeout";
    public static final String INFO_HOST_KEY = "host";

    public static final String INIT_SCRIPT =
            "redis.call('HMSET', KEYS[1], '\"" + INFO_ID_KEY + "\"', ARGV[1], '\"" + INFO_TIMEOUT_KEY + "\"', ARGV[2],\n" +
                    "  '\"" + INFO_START_KEY + "\"', ARGV[3], '\"" + INFO_LAST_KEY + "\"', ARGV[3],\n" +
                    "  '\"" + INFO_HOST_KEY + "\"', ARGV[4])\n" +
                    "local timeout = cjson.decode(ARGV[2])[2]\n" +
                    "redis.call('PEXPIRE', KEYS[1], timeout)";
    

    public static final String TOUCH_SCRIPT =
            "if redis.call('PTTL', KEYS[1]) <= 0 then\n" +
                    "  return " + makeError(RETURN_CODE_EXPIRED) + "\n" +
                    "end\n" +
                    "\n" +
                    "if redis.call('HEXISTS', KEYS[1], '\"" + INFO_STOP_KEY + "\"') == 1 then\n" +
                    "  return " + makeError(RETURN_CODE_STOPPED) + "\n" +
                    "end\n" +
                    "\n" +
                    "local timeoutEncoded = redis.call('HGET', KEYS[1], '\"" + INFO_TIMEOUT_KEY + "\"')\n" +
                    "if timeoutEncoded == nil then\n" +
                    "  return " + makeError(RETURN_CODE_INVALID) + "\n" +
                    "end\n" +
                    "\n" +
                    "local timeout = cjson.decode(timeoutEncoded)[2]\n" +
                    "\n" +
                    "redis.call('HSET', KEYS[1], '\"" + INFO_LAST_KEY + "\"', ARGV[1])\n" +
                    "redis.call('PEXPIRE', KEYS[1], timeout)\n" +
                    "redis.call('PEXPIRE', KEYS[2], timeout)";
    public static final String GET_START_SCRIPT =
            "if redis.call('PTTL', KEYS[1]) <= 0 then\n" +
                    "  return " + makeError(RETURN_CODE_EXPIRED) + "\n" +
                    "end\n" +
                    "\n" +
                    "if redis.call('HEXISTS', KEYS[1], '\"" + INFO_STOP_KEY + "\"') == 1 then\n" +
                    "  return " + makeError(RETURN_CODE_STOPPED) + "\n" +
                    "end\n" +
                    "\n" +
                    "local startTime = redis.call('HGET', KEYS[1], '\"" + INFO_START_KEY + "\"')\n" +
                    "if startTime == nil then\n" +
                    "  return " + makeError(RETURN_CODE_INVALID) + "\n" +
                    "end\n" +
                    "\n" +
                    "return startTime";

    public static final String GET_LAST_SCRIPT =
            "if redis.call('PTTL', KEYS[1]) <= 0 then\n" +
                    "  return " + makeError(RETURN_CODE_EXPIRED) + "\n" +
                    "end\n" +
                    "\n" +
                    "if redis.call('HEXISTS', KEYS[1], '\"" + INFO_STOP_KEY +"\"') == 1 then\n" +
                    "  return " + makeError(RETURN_CODE_STOPPED) + "\n" +
                    "end\n" +
                    "\n" +
                    "local lastTime = redis.call('HGET', KEYS[1], '\"" + INFO_LAST_KEY + "\"')\n" +
                    "if lastTime == nil then\n" +
                    "  return " + makeError(RETURN_CODE_INVALID) + "\n" +
                    "end\n" +
                    "\n" +
                    "return lastTime";

    public static final String GET_TIMEOUT_SCRIPT =
            "if redis.call('PTTL', KEYS[1]) <= 0 then\n" +
                    "  return " + makeError(RETURN_CODE_EXPIRED) + "\n" +
                    "end\n" +
                    "\n" +
                    "if redis.call('HEXISTS', KEYS[1], '\"" + INFO_STOP_KEY + "\"') == 1 then\n" +
                    "  return " + makeError(RETURN_CODE_STOPPED) + "\n" +
                    "end\n" +
                    "\n" +
                    "local timeout = redis.call('HGET', KEYS[1], '\"" + INFO_TIMEOUT_KEY + "\"')\n" +
                    "if timeout == nil then\n" +
                    "  return " + makeError(RETURN_CODE_INVALID) + "\n" +
                    "end\n" +
                    "\n" +
                    "return timeout";

    public static final String GET_HOST_SCRIPT =
            "if redis.call('PTTL', KEYS[1]) <= 0 then\n" +
                    "  return " + makeError(RETURN_CODE_EXPIRED) + "\n" +
                    "end\n" +
                    "\n" +
                    "if redis.call('HEXISTS', KEYS[1], '\"" + INFO_STOP_KEY + "\"') == 1 then\n" +
                    "  return " + makeError(RETURN_CODE_STOPPED) + "\n" +
                    "end\n" +
                    "\n" +
                    "local host = redis.call('HGET', KEYS[1], '\"" + INFO_HOST_KEY + "\"')\n" +
                    "if host == nil then\n" +
                    "  return " + makeError(RETURN_CODE_INVALID) + "\n" +
                    "end\n" +
                    "\n" +
                    "return host";

    public static final String SET_TIMEOUT_SCRIPT =
            "if redis.call('PTTL', KEYS[1]) <= 0 then\n" +
                    "  return " + makeError(RETURN_CODE_EXPIRED) + "\n" +
                    "end\n" +
                    "\n" +
                    "if redis.call('HEXISTS', KEYS[1], '\"" + INFO_STOP_KEY + "\"') == 1 then\n" +
                    "  return " + makeError(RETURN_CODE_STOPPED) + "\n" +
                    "end\n" +
                    "\n" +
                    "local timeout = redis.call('HGET', KEYS[1], '\"" + INFO_TIMEOUT_KEY + "\"')\n" +
                    "if timeout == nil then\n" +
                    "  return " + makeError(RETURN_CODE_INVALID) + "\n" +
                    "end\n" +
                    "\n" +
                    "redis.call('HSET', KEYS[1], '\"" + INFO_TIMEOUT_KEY + "\"', ARGV[1])\n" +
                    "local newTimeout = cjson.decode(ARGV[1])[2]\n" +
                    "redis.call('PEXPIRE', KEYS[1], newTimeout)\n" +
                    "redis.call('PEXPIRE', KEYS[2], newTimeout)";

    public static final String STOP_SCRIPT =
            "if redis.call('PTTL', KEYS[1]) <= 0 then\n" +
                    "  return " + makeError(RETURN_CODE_EXPIRED) + "\n" +
                    "end\n" +
                    "\n" +
                    "if redis.call('HEXISTS', KEYS[1], '\"" + INFO_STOP_KEY + "\"') == 1 then\n" +
                    "  return " + makeError(RETURN_CODE_STOPPED) + "\n" +
                    "end\n" +
                    "\n" +
                    "redis.call('HSET', KEYS[1], '\"" + INFO_STOP_KEY + "\"', ARGV[1])";

    public static final String GET_ATTRKEYS_SCRIPT =
            "if redis.call('PTTL', KEYS[1]) <= 0 then\n" +
                    "  return " + makeError(RETURN_CODE_EXPIRED) + "\n" +
                    "end\n" +
                    "\n" +
                    "if redis.call('HEXISTS', KEYS[1], '\"" + INFO_STOP_KEY + "\"') == 1 then\n" +
                    "  return " + makeError(RETURN_CODE_STOPPED) + "\n" +
                    "end\n" +
                    "\n" +
                    "return redis.call('HKEYS', KEYS[2])";

    public static final String GET_ATTR_SCRIPT =
            "if redis.call('PTTL', KEYS[1]) <= 0 then\n" +
                    "  return " + makeError(RETURN_CODE_EXPIRED) + "\n" +
                    "end\n" +
                    "\n" +
                    "if redis.call('HEXISTS', KEYS[1], '\"" + INFO_STOP_KEY + "\"') == 1 then\n" +
                    "  return " + makeError(RETURN_CODE_STOPPED) + "\n" +
                    "end\n" +
                    "\n" +
                    "return redis.call('HGET', KEYS[2], ARGV[1])";

    public static final String REMOVE_ATTR_SCRIPT =
            "if redis.call('PTTL', KEYS[1]) <= 0 then\n" +
                    "  return " + makeError(RETURN_CODE_EXPIRED) + "\n" +
                    "end\n" +
                    "\n" +
                    "if redis.call('HEXISTS', KEYS[1], '\"" + INFO_STOP_KEY + "\"') == 1 then\n" +
                    "  return " + makeError(RETURN_CODE_STOPPED) + "\n" +
                    "end\n" +
                    "\n" +
                    "local attr = redis.call('HGET', KEYS[2], ARGV[1])\n" +
                    "if attr ~= nil then\n" +
                    "  redis.call('HDEL', KEYS[2], ARGV[1])\n" +
                    "end\n" +
                    "\n" +
                    "return attr";

    public static final String SET_ATTR_SCRIPT =
            "local pttl = redis.call('PTTL', KEYS[1])\n" +
                    "if pttl <= 0 then\n" +
                    "  return " + makeError(RETURN_CODE_EXPIRED) + "\n" +
                    "end\n" +
                    "\n" +
                    "if redis.call('HEXISTS', KEYS[1], '\"" + INFO_STOP_KEY + "\"') == 1 then\n" +
                    "  return " + makeError(RETURN_CODE_STOPPED) + "\n" +
                    "end\n" +
                    "\n" +
                    "redis.call('HSET', KEYS[2], ARGV[1], ARGV[2])\n" +
                    "-- redis auto delete key of hash when it is empty.\n" +
                    "-- then, expire time of the hash will be lost.\n" +
                    "if redis.call('PTTL', KEYS[2]) <= 0 then\n" +
                    "  redis.call('PEXPIRE', KEYS[2], pttl)\n" +
                    "end";

    public static final String DELETE_SCRIPT =
            "redis.call('UNLINK', KEYS[1], KEYS[2])";

    public static final String READ_SCRIPT =
            "return redis.call('PTTL', KEYS[1])";

    private static String makeError(String errMsg) {
        return "redis.error_reply(\"" + errMsg + "\")";
    }

}
