package com.linzx.framework.config;

import com.linzx.framework.config.condition.RedisOnEnableCondition;
import com.linzx.framework.config.properties.RedissonProperties.SingleServer;
import com.linzx.framework.config.support.FastjsonCodec;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;


/**
 * 参考文章：https://blog.csdn.net/zilong_zilong/article/details/78252037
 * 序列化：https://www.jianshu.com/p/43cfa79e62a5
 */
@Configuration
@DependsOn("redissonProperties")
@Conditional(RedisOnEnableCondition.class)
public class RedissonConfig {

    /**
     * 单节点部署方式
     */
    @Bean(destroyMethod = "shutdown")
    @ConditionalOnProperty(prefix = "spring.redis", name = "mode", havingValue = "single")
    public RedissonClient standaloneClient() {
        Config config = new Config();
        config.setCodec(FastjsonCodec.instance);
        config.useSingleServer().setAddress(SingleServer.address);
        config.useSingleServer()
                // 设置对于master节点的连接池中连接数最大为500
                .setConnectionPoolSize(SingleServer.connectionPoolSize)
                // 如果当前连接池里的连接数量超过了最小空闲连接数，而同时有连接空闲时间超过了该数值，那么这些连接将会自动被关闭，并从连接池里去掉。时间单位是毫秒
                .setIdleConnectionTimeout(SingleServer.idleConnectionTimeout)
                // 同任何节点建立连接时的等待超时。时间单位是毫秒
                .setConnectTimeout(SingleServer.connectTimeout)
                //等待节点回复命令的时间。该时间从命令发送成功时开始计时
                .setTimeout(SingleServer.timeout)
                .setDatabase(SingleServer.database);
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }

    /**
     * 哨兵部署方式
     */
    public RedissonClient sentinelClient() {
        Config config = new Config();
        config.setCodec(FastjsonCodec.instance);
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }

    /**
     * 集群方式
     */
    public RedissonClient clusterClient() {
        Config config = new Config();
        config.setCodec(FastjsonCodec.instance);
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }

}
