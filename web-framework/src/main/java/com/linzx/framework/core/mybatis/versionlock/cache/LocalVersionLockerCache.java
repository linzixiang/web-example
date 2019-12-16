package com.linzx.framework.core.mybatis.versionlock.cache;

import com.linzx.framework.core.mybatis.versionlock.annotation.VersionLocker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class LocalVersionLockerCache implements Cache<VersionLocker> {

    private static final Logger logger = LoggerFactory.getLogger(LocalVersionLockerCache.class);

    private ConcurrentHashMap<String, ConcurrentHashMap<MethodSignature, VersionLocker>> caches = new ConcurrentHashMap<>();

    @Override
    public boolean containMethodSignature(MethodSignature vm) {
        String nameSpace = getNameSpace(vm);
        ConcurrentHashMap<MethodSignature, VersionLocker> cache = caches.get(nameSpace);
        if(null == cache || cache.isEmpty()) {
            return false;
        }
        boolean containsMethodSignature = cache.containsKey(vm);
        if(containsMethodSignature && logger.isDebugEnabled()) {
            logger.debug("The method " + nameSpace + vm.getId() + "is hit in cache.");
        }
        return containsMethodSignature;
    }

    @Override
    public void cacheMethod(MethodSignature vm, VersionLocker locker) {
        String nameSpace = getNameSpace(vm);
        ConcurrentHashMap<MethodSignature, VersionLocker> cache = caches.get(nameSpace);
        if(null == cache || cache.isEmpty()) {
            cache = new ConcurrentHashMap<>();
            cache.put(vm, locker);
            caches.put(nameSpace, cache);
            if(logger.isDebugEnabled()) {
                logger.debug("Locker debug info ==> " + nameSpace + ": " + vm.getId() + " is cached.");
            }
        } else {
            cache.put(vm, locker);
        }
    }

    @Override
    public VersionLocker getVersionLocker(MethodSignature vm) {
        String nameSpace = getNameSpace(vm);
        ConcurrentHashMap<MethodSignature, VersionLocker> cache = caches.get(nameSpace);
        if(null == cache || cache.isEmpty()) {
            return null;
        }
        return cache.get(vm);
    }

    private String getNameSpace(MethodSignature vm) {
        String id = vm.getId();
        int pos = id.lastIndexOf(".");
        return id.substring(0, pos);
    }

}
