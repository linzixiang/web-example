package com.linzx.framework.core.util.seq;

public interface SequenceIdWorker<T> {

    T getNextSeqValue() throws Exception;

}
