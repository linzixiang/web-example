package com.linzx.framework.core.queue.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 普通消息消费者
 */
public abstract class AbsConcurrentConsumer extends AbsBasePushConsumer {

    @Override
    protected DefaultMQPushConsumer registerMessageListener(DefaultMQPushConsumer consumer) throws MQClientException {
        // 开启内部类实现监听
        MessageListenerConcurrently messageListenerConcurrently = getMessageListenerConcurrently();
        if (messageListenerConcurrently == null) {
            messageListenerConcurrently = new MessageListenerConcurrentlyImpl();
        }
        consumer.registerMessageListener(messageListenerConcurrently);
        consumer.start();
        return consumer;
    }

    /**
     * 默认回调监听
     */
    private class MessageListenerConcurrentlyImpl implements MessageListenerConcurrently {

        @Override
        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messageExtList,
                                                        ConsumeConcurrentlyContext context) {
            for (MessageExt messageExt : messageExtList) {
                if (!AbsConcurrentConsumer.this.consumeMessage(messageExt, context)) {
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }

    }

    /**
     * 使用默认回调监听时，回调该方法处理业务
     * @param messageExt
     * @param context
     * @return true 表示消费成功
     */
    protected abstract boolean consumeMessage(MessageExt messageExt, ConsumeConcurrentlyContext context);

    /**
     * 允许自定义消费回调监听
     * @return
     */
    public MessageListenerConcurrently getMessageListenerConcurrently() {
        return null;
    }
}
