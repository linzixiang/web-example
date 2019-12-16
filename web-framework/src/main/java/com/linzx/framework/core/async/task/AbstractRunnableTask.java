package com.linzx.framework.core.async.task;

public abstract class AbstractRunnableTask<P> extends BaseTask<P> implements Runnable{

    public AbstractRunnableTask(P param) {
        super(param);
    }

    @Override
    public final void run() {
        this.execute(super.getParam());
    }

    protected abstract void execute(P param);
}
