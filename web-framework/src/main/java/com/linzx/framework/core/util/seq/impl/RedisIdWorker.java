package com.linzx.framework.core.util.seq.impl;

import com.linzx.framework.core.util.seq.SequenceIdWorker;
import com.linzx.utils.Convert;
import com.linzx.utils.StringUtils;
import org.redisson.RedissonScript;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;

import java.util.ArrayList;
import java.util.List;

/**
 * redis自增id
 */
public class RedisIdWorker implements SequenceIdWorker<Long> {

    private RedissonClient redissonClient;

    /**
     * 序列号唯一key，用于生成相应的表结构
     **/
    private String sequenceName;

    /**
     * 步长
     **/
    private int step = 1;

    /**
     * 开始值
     **/
    private int start = 1;

    private String ID_WORKER_PREFIX = "ID_WORKER";

    public RedisIdWorker(RedissonClient redissonClient, String sequenceName) {
        this(redissonClient, sequenceName, 1, 1);
    }

    /**
     * @param redissonClient  操作redis的客户端
     * @param sequenceName
     * @param start 当sequenceName不存在时，该参数生效
     * @param step
     */
    public RedisIdWorker(RedissonClient redissonClient, String sequenceName, int start, int step) {
        this.redissonClient = redissonClient;
        this.sequenceName = sequenceName;
        this.start = start;
        this.step = step;
    }

    @Override
    public synchronized Long getNextSeqValue() {
        RedissonScript script = (RedissonScript) redissonClient.getScript();
        List<Object> keys = new ArrayList<>(1);
        keys.add(getIdWorkerKey());
        Object redisId = script.eval(RScript.Mode.READ_WRITE, REDIS_ID_INCR_SCRIPT,
                RScript.ReturnType.VALUE, keys, start, step);
        return Long.parseLong(String.valueOf(redisId));
    }

    /**
     * 生成redis的唯一id
     * @return
     */
    private String getIdWorkerKey() {
        return ID_WORKER_PREFIX + ":" + this.sequenceName;
    }

    private final String REDIS_ID_INCR_SCRIPT = new StringBuilder()
            .append("if redis.call('exists', KEYS[1]) == 0\n") // 如果主键不存在
            .append(" then redis.call('set', KEYS[1], ARGV[1]) return ARGV[1]\n") // 初始化值
            .append("else return redis.call('incrby', KEYS[1], ARGV[2])\n")
            .append("end")
            .toString();

}
