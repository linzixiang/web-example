package com.linzx.framework.core.async.task;

import java.util.concurrent.Callable;

public abstract class AbstractCallableTask<P, V> extends BaseTask<P> implements Callable<V> {

    public AbstractCallableTask(P param) {
        super(param);
    }

    @Override
    public final V call() throws Exception {
        return this.execute(super.getParam());
    }

    protected abstract V execute(P param);

}
