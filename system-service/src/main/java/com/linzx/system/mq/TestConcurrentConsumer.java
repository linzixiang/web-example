package com.linzx.system.mq;

import com.linzx.framework.core.queue.consumer.AbsConcurrentConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestConcurrentConsumer extends AbsConcurrentConsumer {

    @Override
    protected boolean consumeMessage(MessageExt messageExt, ConsumeConcurrentlyContext context) {
        log.info("收到消息：" + messageExt.toString());
        return true;
    }

    @Override
    public String getTopic() {
        return "TopicTest";
    }
}
