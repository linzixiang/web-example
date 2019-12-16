package com.linzx.framework.core.util.concurrent.lock;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 锁适配器，可以支持分布式锁或单机锁
 */
public class LockAdapter implements ProLock {

    private Lock lock;

    public LockAdapter(Lock lock) {
        this.lock = lock;
    }

    @Override
    public boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
        if (lock instanceof RLock) {
            RLock rLock = (RLock) lock;
            return rLock.tryLock(waitTime, leaseTime, unit);
        }
        return lock.tryLock(waitTime, unit);
    }

    @Override
    public void lock(long leaseTime, TimeUnit unit) {
        if (lock instanceof RLock) {
            RLock rLock = (RLock) lock;
            rLock.lock(leaseTime, unit);
        } else {
            lock.lock();
        }
    }

    @Override
    public boolean isLocked() {
        if (lock instanceof RLock) {
            RLock rLock = (RLock) lock;
            return rLock.isLocked();
        } else {
            return true;
        }
    }

    @Override
    public void lock() {
        lock.lock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        lock.lockInterruptibly();
    }

    @Override
    public boolean tryLock() {
        return lock.tryLock();
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return lock.tryLock(time, unit);
    }

    @Override
    public void unlock() {
        lock.unlock();
    }

    @Override
    public Condition newCondition() {
        return lock.newCondition();
    }
}
