package com.linzx.framework.shiro.core.cache.spring;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ShiroSpringCache<K, V> implements Cache<K, V> {

    private static final Logger log = LoggerFactory.getLogger(ShiroSpringCache.class);

    private org.springframework.cache.Cache cache;

    public ShiroSpringCache(org.springframework.cache.Cache cache) {
        if (cache == null) {
            throw new IllegalArgumentException("Cache argument cannot be null.");
        }
        this.cache = cache;
    }


    @Override
    public V get(K key) throws CacheException {
        if(key == null){
            return null;
        }
        org.springframework.cache.Cache.ValueWrapper valueWrapper = cache.get(key);
        if(valueWrapper == null){
            return null;
        }
        Object o = valueWrapper.get();
        return (V) o;
    }

    @Override
    public V put(K key, V value) throws CacheException {
        V preValue = get(key);
        cache.put(key, value);
        return preValue;
    }

    @Override
    public V remove(K key) throws CacheException {
        V v = get(key);
        cache.evict(key);//干掉这个名字为key的缓存
        return v;
    }

    @Override
    public void clear() throws CacheException {
        cache.clear();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set<K> keys() {
        return new HashSet<>();
    }

    @Override
    public Collection<V> values() {
        return new ArrayList<>();
    }
}
