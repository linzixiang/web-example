package com.linzx.framework.core.util.concurrent;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.EvictionListener;
import com.linzx.framework.core.util.concurrent.lock.LockAdapter;
import com.linzx.framework.core.util.concurrent.lock.ProLock;
import com.linzx.framework.utils.SpringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 并发操作工厂类<br/>
 * 1、锁，适配单机锁和分布式锁
 * 2、信号量（Semaphore），待扩展
 * 3、闭锁（CountDownLatch），待扩展
 */
@Component
public class ConcurrentOpFactory implements InitializingBean {

    private static ConcurrentOpFactory me;

    /** 如果redissonClient为空，则用单机锁 **/
    @Autowired(required = false)
    private RedissonClient redissonClient;

    public static ConcurrentOpFactory getInstance() {
        return me;
    }

    /**
     * 单机可重入锁缓存,设置允许缓存的最大容量
     */
    private final Map<String, ProLock> reentrantLockMap = new ConcurrentLinkedHashMap.Builder<String, ProLock>()
            .maximumWeightedCapacity(1000).listener(new EvictionListener<String, ProLock>() {

                @Override
                public void onEviction(String key, ProLock lock) {

                }

            }).build();

    /**
     * 单机读写锁缓存,设置允许缓存的最大容量
     */
    private final Map<String, ReadWriteLock> readWriteLockMap = new ConcurrentLinkedHashMap.Builder<String, ReadWriteLock>()
            .maximumWeightedCapacity(1000).listener(new EvictionListener<String, ReadWriteLock>() {

                @Override
                public void onEviction(String key, ReadWriteLock lock) {

                }

            }).build();

    /**
     * 获取可重入锁
     */
    public ProLock getReentrantLock(String key) {
        key = "reentrantLock" + "-"  + key;
        ProLock lock = null;
        if (redissonClient != null) {
            RLock rLock = redissonClient.getLock(key);
            lock = new LockAdapter(rLock);
        } else {
            lock = reentrantLockMap.get(key);
            if (lock == null) {
                lock = new LockAdapter(new ReentrantLock());
                reentrantLockMap.put(key, lock);
            }
        }
        return lock;
    }

    /**
     * 获取读写锁
     */
    public ReadWriteLock  getReadWriteLock(String key) {
        key = "readWriteLock" + "-"  + key;
        ReadWriteLock lock = null;
        if (redissonClient != null) {
            lock = redissonClient.getReadWriteLock(key);
        } else {
            lock = readWriteLockMap.get(key);
            if (lock == null) {
                lock = new ReentrantReadWriteLock();
                readWriteLockMap.put(key, lock);
            }
        }
        return lock;
    }

//    public Semaphore getSemaphore(String key) throws InterruptedException {
//        key = "semaphore" + "-"  + key;
//        Semaphore semaphore = new Semaphore(1);
//        if (redissonClient != null) {
//            RSemaphore rSemaphore = redissonClient.getSemaphore(key);
//            rSemaphore.acquire(9);
//            rSemaphore.availablePermits();
//            rSemaphore.reducePermits(9);
//            rSemaphore.tryAcquire(1, TimeUnit.HOURS);
//
//        }
//        return null;
//    }

//    public CountDownLatch getCountDownLatch(String key) {
//        RCountDownLatch countDownLatch = redissonClient.getCountDownLatch(key);
//        return null;
//    }

    @Override
    public void afterPropertiesSet() throws Exception {
        me = SpringUtils.getBean(ConcurrentOpFactory.class);
    }

}
