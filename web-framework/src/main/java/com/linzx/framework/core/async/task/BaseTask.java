package com.linzx.framework.core.async.task;

public abstract class BaseTask<P> {

    private P param;

    public BaseTask(P param) {
        this.param = param;
    }

    public P getParam() {
        return param;
    }
}
