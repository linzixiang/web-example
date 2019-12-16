package com.linzx.framework.shiro.core.cache.spring;

import com.linzx.framework.shiro.core.cache.redis.RedissonShiroCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;

/**
 * 使用Spring的CacheManage接管shiro
 */
public class ShiroSpringCacheManager implements CacheManager, Destroyable {

    private static final Logger log = LoggerFactory.getLogger(ShiroSpringCache.class);

    private org.springframework.cache.CacheManager cacheManager;

    private RedissonShiroCacheManager redissonShiroCacheManager;

    public ShiroSpringCacheManager(org.springframework.cache.CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public ShiroSpringCacheManager(RedissonShiroCacheManager redissonShiroCacheManager) {
        this.redissonShiroCacheManager = redissonShiroCacheManager;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String key) throws CacheException {
        if (this.redissonShiroCacheManager == null) {
            org.springframework.cache.Cache cache = cacheManager.getCache(key);
            return new ShiroSpringCache(cache);
        }
        return redissonShiroCacheManager.getCache(key);
    }

    @Override
    public void destroy() throws DestroyFailedException {
        cacheManager = null;
    }
}
