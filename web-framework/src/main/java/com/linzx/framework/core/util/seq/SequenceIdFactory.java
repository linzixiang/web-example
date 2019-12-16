package com.linzx.framework.core.util.seq;


import com.linzx.framework.core.util.seq.impl.DbAutoIdWorker;
import com.linzx.framework.core.util.seq.impl.RedisIdWorker;
import com.linzx.framework.core.util.seq.impl.SnowflakeIdWorker;
import com.linzx.framework.core.util.seq.impl.UUIDWorker;
import com.linzx.framework.utils.SqlSessionUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 只需要指定唯一key，自动生成序列
 */
@Component
public class SequenceIdFactory extends AbstractSequenceIdFactory {

    @Autowired(required = false)
    private static RedissonClient redissonClient;

    private static Map<String, SequenceIdWorker> sequenceIdWorkerMap = new ConcurrentHashMap<>(256);

    @Override
    public Long getRedisNextSeqValue(String sequenceName, int start, int step) throws Exception {
        SequenceIdWorker sequenceIdWorker = sequenceIdWorkerMap.get(sequenceName);
        if (sequenceIdWorker == null) {
            synchronized (RedisIdWorker.class) {
                if (sequenceIdWorkerMap.get(sequenceName) == null) {
                    if (redissonClient == null) {
                        throw new RuntimeException("redissonClient not support!");
                    }
                    sequenceIdWorker = new RedisIdWorker(redissonClient, sequenceName, start, step);
                    sequenceIdWorkerMap.put(sequenceName, sequenceIdWorker);
                }
            }
        }
        return (Long) sequenceIdWorker.getNextSeqValue();
    }

    public Long getRedisNextSeqValue(String sequenceName) throws Exception {
        return this.getRedisNextSeqValue(sequenceName, 1, 1);
    }

    @Override
    public Long getDbNextSeqValue(String sequenceName, int start) throws Exception {
        SequenceIdWorker sequenceIdWorker = sequenceIdWorkerMap.get(sequenceName);
        if (sequenceIdWorker == null) {
            synchronized (DbAutoIdWorker.class) {
                if (sequenceIdWorkerMap.get(sequenceName) == null) {
                    Connection connection = SqlSessionUtils.getConnection();
                    sequenceIdWorker = new DbAutoIdWorker(connection, sequenceName, start);
                    sequenceIdWorkerMap.put(sequenceName, sequenceIdWorker);
                }
            }
        }
        return (Long) sequenceIdWorker.getNextSeqValue();
    }

    public Long getDbNextSeqValue(String sequenceName) throws Exception {
        return  this.getDbNextSeqValue(sequenceName, 1);
    }

    @Override
    public Long getSnowflakeNextSeqValue(String sequenceName, long workerId, long datacenterId) throws Exception {
        SequenceIdWorker sequenceIdWorker = sequenceIdWorkerMap.get(sequenceName);
        if (sequenceIdWorker == null) {
            synchronized (SnowflakeIdWorker.class) {
                if (sequenceIdWorkerMap.get(sequenceName) == null) {
                    sequenceIdWorker = new SnowflakeIdWorker(workerId, datacenterId);
                    sequenceIdWorkerMap.put(sequenceName, sequenceIdWorker);
                }
            }
        }
        return (Long) sequenceIdWorker.getNextSeqValue();
    }

    public Long getSnowflakeNextSeqValue(String sequenceName) throws Exception {
        return this.getSnowflakeNextSeqValue(sequenceName, 0, 0);
    }

    @Override
    public String getUUIDNextSeqValue(String sequenceName) throws Exception {
        SequenceIdWorker sequenceIdWorker = sequenceIdWorkerMap.get(sequenceName);
        if (sequenceIdWorker == null) {
            synchronized (UUIDWorker.class) {
                if (sequenceIdWorkerMap.get(sequenceName) == null) {
                    sequenceIdWorker = new UUIDWorker();
                    sequenceIdWorkerMap.put(sequenceName, sequenceIdWorker);
                }
            }
        }
        return (String) sequenceIdWorker.getNextSeqValue();
    }

    public static void main(String[] args) {
        SequenceIdFactory sequenceIdFactory = new SequenceIdFactory();
        try {
            System.out.println("uuid:" + sequenceIdFactory.getUUIDNextSeqValue("test1"));
            System.out.println("snowflakeId:" + sequenceIdFactory.getSnowflakeNextSeqValue("test2"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
