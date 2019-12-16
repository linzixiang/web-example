package com.linzx.framework.shiro.core.cache.redis;

import com.linzx.framework.config.support.FastjsonCodec;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.CacheConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 使用redis作为shiro的cache
 */
public class RedissonShiroCacheManager implements CacheManager {

    private Map<String, CacheConfig> configMap = new ConcurrentHashMap<>();
    private ConcurrentMap<String, Cache> instanceMap = new ConcurrentHashMap<>();

    private RedissonClient redisClient;

    public RedissonShiroCacheManager(RedissonClient redisson) {
       this.redisClient = redisson;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        Cache<K, V> cache = this.instanceMap.get(name);
        if (cache != null) {
            return cache;
        }
        CacheConfig config = this.configMap.get(name);
        if (config == null) {
            config = new CacheConfig();
            configMap.put(name, config);
        }
        // 永久保存的缓存
        if (config.getMaxIdleTime() == 0 && config.getTTL() == 0 && config.getMaxSize() == 0) {
            RMap<K, Object> map =  redisClient.getMap(name, FastjsonCodec.instance);
            cache = new RedissonShiroCache<>(map);
            Cache<K, V> oldCache = this.instanceMap.putIfAbsent(name, cache);
            if (oldCache != null) {
                cache = oldCache;
            }
            return cache;
        }
        // 带有效期的缓存
        RMapCache<K, Object> mapCache = redisClient.getMapCache(name, FastjsonCodec.instance);
        cache = new RedissonShiroCache<>(mapCache);
        Cache<K, V> oldCache = this.instanceMap.putIfAbsent(name, cache);
        if (oldCache != null) {
            cache = oldCache;
        } else {
            mapCache.setMaxSize(config.getMaxSize());
        }
        return cache;
    }

}
