package com.linzx.framework.utils;

import com.linzx.framework.bean.CacheBean;
import com.linzx.framework.core.context.ContextManager;
import org.springframework.cache.Cache;

public class CacheUtils {

    /**
     * 检查是否存在key
     * @param cache
     * @param key
     * @return false表示不存在
     */
    public static boolean exists(Cache cache, String key) {
        Cache.ValueWrapper valueWrapper = cache.get(key);
        if (valueWrapper == null) {
            return false;
        }
        Object value = valueWrapper.get();
        if (value == null) {
            return false;
        }
        return true;
    }

    /**
     * 获取缓存的值
     * @param cache
     * @param key
     * @return
     */
    public static Object getValue(Cache cache, Object key) {
        Cache.ValueWrapper valueWrapper = cache.get(key.toString());
        if (valueWrapper == null) {
            return null;
        }
        return valueWrapper.get();
    }

    public static Object getValue(String moduleName, String keyName, Object key) {
        CacheBean.CacheKey cacheKey = new CacheBean.CacheKey(moduleName, keyName);
        Cache cache = ContextManager.getCache(cacheKey);
        return  getValue(cache, key.toString());
    }

}
