package com.linzx.framework.shiro.core.cache.redis;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.NullValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * redis缓存
 */
public class RedissonShiroCache<K, V> implements Cache<K, V> {

    private RMapCache<K, Object> mapCache;
    private final RMap<K, Object> map;
    private CacheConfig config;
    private final AtomicLong hits = new AtomicLong();
    private final AtomicLong misses = new AtomicLong();
    private final boolean allowNullValues = false;

    public RedissonShiroCache(RMapCache<K, Object> mapCache, CacheConfig config) {
        this.mapCache = mapCache;
        this.map = mapCache;
        this.config = config;
    }

    public RedissonShiroCache(RMap<K, Object> map) {
        this.map = map;
    }

    @Override
    public V get(K key) throws CacheException {
        Object value = this.map.get(key);
        if (value == null) {
            addCacheMiss();
        } else {
            addCacheHit();
        }
        return fromStoreValue(value);
    }

    @Override
    public V put(K key, V value) throws CacheException {
        Object previous;
        if (!allowNullValues && value == null) {
            if (mapCache != null) {
                previous = mapCache.remove(key);
            } else {
                previous = map.remove(key);
            }
            return fromStoreValue(previous);
        }

        Object val = toStoreValue(value);
        if (mapCache != null) {
            previous = mapCache.put(key, val, config.getTTL(), TimeUnit.MILLISECONDS,
                    config.getMaxIdleTime(), TimeUnit.MILLISECONDS);
        } else {
            previous = map.put(key, val);
        }
        return fromStoreValue(previous);
    }

    @Override
    public V remove(K key) throws CacheException {
        Object previous = this.map.remove(key);
        return fromStoreValue(previous);
    }

    @Override
    public void clear() throws CacheException {
        this.map.clear();
    }

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public Set<K> keys() {
        return this.map.readAllKeySet();
    }

    @Override
    public Collection<V> values() {
        Collection<Object> innerValues = this.map.readAllValues();
        Collection<V> res = new ArrayList<>(innerValues.size());
        for (Object val : innerValues) {
            res.add(fromStoreValue(val));
        }
        return res;
    }

    public long fastRemove(K... keys) {
        return this.map.fastRemove(keys);
    }

    public void fastPut(K key, V value) throws CacheException {
        if (!allowNullValues && value == null) {
            if (mapCache != null) {
                mapCache.fastRemove(key);
            } else {
                map.fastRemove(key);
            }
            return;
        }

        Object val = toStoreValue(value);
        if (mapCache != null) {
            mapCache.fastPut(key, val, config.getTTL(), TimeUnit.MILLISECONDS,
                    config.getMaxIdleTime(), TimeUnit.MILLISECONDS);
        } else {
            map.fastPut(key, val);
        }
    }

    /**
     * 如果传入key对应的value已经存在，就返回存在的value，不进行替换。如果不存在，就添加key和value，返回null
     */
    public V putIfAbsent(K key, V value) throws CacheException {
        Object previous;
        if (!allowNullValues && value == null) {
            if (mapCache != null) {
                previous = mapCache.get(key);
            } else {
                previous = map.get(key);
            }
            return fromStoreValue(previous);
        }

        Object val = toStoreValue(value);
        if (mapCache != null) {
            previous = mapCache.putIfAbsent(key, val, config.getTTL(), TimeUnit.MILLISECONDS,
                    config.getMaxIdleTime(), TimeUnit.MILLISECONDS);
        } else {
            previous = map.putIfAbsent(key, val);
        }
        return fromStoreValue(previous);
    }

    public boolean fastPutIfAbsent(K key, V value) throws CacheException {
        if (!allowNullValues && value == null) {
            return false;
        }

        Object val = toStoreValue(value);
        if (mapCache != null) {
            return mapCache.fastPutIfAbsent(key, val, config.getTTL(), TimeUnit.MILLISECONDS,
                    config.getMaxIdleTime(), TimeUnit.MILLISECONDS);
        } else {
            return map.fastPutIfAbsent(key, val);
        }
    }

    protected V fromStoreValue(Object storeValue) {
        if (storeValue instanceof NullValue) {
            return null;
        }
        return (V) storeValue;
    }

    protected Object toStoreValue(V userValue) {
        if (userValue == null) {
            return NullValue.INSTANCE;
        }
        return userValue;
    }

    private void addCacheHit() {
        this.hits.incrementAndGet();
    }

    private void addCacheMiss() {
        this.misses.incrementAndGet();
    }

}
