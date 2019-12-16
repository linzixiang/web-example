package com.linzx.framework.core.util.concurrent.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * 实现了单据锁的功能，同时对单机锁功能扩展
 */
public interface ProLock extends Lock {

    /**
     * 尝试加锁
     * @param waitTime 最多等待时间
     * @param leaseTime 上锁后没有解锁，多少时间后自动解锁
     * @param unit 时间单位
     * @return
     * @throws InterruptedException
     */
    boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException;

    /**
     * 加锁
     * @param leaseTime  上锁后没有解锁，多少时间后自动解锁
     * @param unit  时间单位
     */
    void lock(long leaseTime, TimeUnit unit);

    /**
     * 判断是否已上锁
     */
    boolean isLocked();

}
