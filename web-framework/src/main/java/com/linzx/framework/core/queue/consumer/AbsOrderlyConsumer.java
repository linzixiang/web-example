package com.linzx.framework.core.queue.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 有序消息消费者
 */
public abstract class AbsOrderlyConsumer extends AbsBasePushConsumer {

    @Override
    protected DefaultMQPushConsumer registerMessageListener(DefaultMQPushConsumer consumer) throws MQClientException {
        // 开启内部类实现监听
        MessageListenerOrderly messageListenerOrderly = getMessageListenerOrderly();
        if (messageListenerOrderly == null) {
            messageListenerOrderly = new AbsOrderlyConsumer.MessageListenerOrderlyImpl();
        }
        consumer.registerMessageListener(messageListenerOrderly);
        consumer.start();
        return consumer;
    }

    /**
     * 默认回调监听
     */
    private class MessageListenerOrderlyImpl implements MessageListenerOrderly {

        @Override
        public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
            consumeOrderlyContext.setAutoCommit(false);
            for (MessageExt messageExt : list) {
                if (AbsOrderlyConsumer.this.consumeMessage(messageExt, consumeOrderlyContext)) {
                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }
            }
            return ConsumeOrderlyStatus.SUCCESS;
        }

    }

    /**
     * 使用默认回调监听时，回调该方法处理业务
     * @param messageExt
     * @param context
     * @return true 表示消费成功
     */
    protected abstract boolean consumeMessage(MessageExt messageExt, ConsumeOrderlyContext context);

    protected MessageListenerOrderly getMessageListenerOrderly() {
        return null;
    }
}
