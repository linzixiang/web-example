package com.linzx.framework.core.util.seq.impl;

import com.linzx.framework.core.util.seq.SequenceIdWorker;

import java.util.UUID;

/**
 * UUID生成器
 *
 */
public class UUIDWorker implements SequenceIdWorker<String> {

    @Override
    public String getNextSeqValue() {
        return UUID.randomUUID().toString();
    }

}
