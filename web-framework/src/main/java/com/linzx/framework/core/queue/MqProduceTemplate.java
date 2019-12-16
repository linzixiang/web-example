package com.linzx.framework.core.queue;

import com.linzx.utils.CollectionUtils;
import com.linzx.utils.Convert;
import com.linzx.utils.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class MqProduceTemplate {

    @Autowired(required = false)
    private DefaultMQProducer defaultMQProducer;

    /**
     * 同步发送消息，返回发送结果
     * @param topic 消息主题
     * @param tagsArr 消息tag
     * @param body 消息体
     * @param selector 发送到队列选择器
     * @param arg 配置selector使用，以参数形式传入selector
     * @param timeoutSec 发送超时时间
     * @return 发送结果
     * @throws Exception
     */
    public SendResult sendMessage(String topic, String[] tagsArr, String keys, String body, MessageQueueSelector selector, Object arg, int timeoutSec) throws Exception {
        Message message = createMessage(topic, tagsArr, keys, body);
        timeoutSec = timeoutSec != 0 ? timeoutSec * 1000 : defaultMQProducer.getSendMsgTimeout();
        if (selector != null) {
            return defaultMQProducer.send(message, selector, arg, timeoutSec);
        } else {
            return defaultMQProducer.send(message, timeoutSec);
        }
    }

    /**
     * 异步发送消息，回调发送结果
     * @param topic 主题
     * @param tagsArr 消息tag
     * @param body 消息体
     * @param callback
     * @param selector 发送到队列选择器
     * @param arg 配置selector使用，以参数形式传入selector
     * @param timeoutSec 发送超时时间
     * @throws Exception
     */
    public void sendAsyncMessage(String topic, String[] tagsArr, String keys, String body, SendCallback callback,
                                 MessageQueueSelector selector, Object arg,
                                 int timeoutSec) throws Exception {
        Message message = createMessage(topic, tagsArr, keys, body);
        timeoutSec = timeoutSec != 0 ? timeoutSec * 1000 : defaultMQProducer.getSendMsgTimeout();
        if (selector != null) {
            defaultMQProducer.send(message, selector, arg, callback, timeoutSec);
        } else {
            defaultMQProducer.send(message, callback, timeoutSec);
        }
    }

    public SendResult sendMessage(String topic, String[] tagsArr, String keys, String body) throws Exception {
        return this.sendMessage(topic, tagsArr, keys, body, null, null, 0);
    }

    public SendResult sendMessage(String topic, String[] tagsArr, String keys, String body, int timeoutSec) throws Exception {
        return this.sendMessage(topic, tagsArr, keys, body, null, null, timeoutSec);
    }

    public SendResult sendMessage(String topic, String[] tagsArr, String keys, String body, MessageQueueSelector selector, Object arg) throws Exception {
        return this.sendMessage(topic, tagsArr, keys, body, selector, arg, 0);
    }

    public void sendAsyncMessage(String topic, String[] tagsArr, String keys, String body, SendCallback callback) throws Exception {
        this.sendAsyncMessage(topic, tagsArr, keys, body, callback);
    }

    public void sendAsyncMessage(String topic, String[] tagsArr, String keys, String body, SendCallback callback, int timeoutSec) throws Exception {
        this.sendAsyncMessage(topic, tagsArr, keys, body, callback, null, null, timeoutSec);
    }

    public void sendAsyncMessage(String topic, String[] tagsArr, String keys, String body, SendCallback callback,
                                 MessageQueueSelector selector, Object arg) throws Exception {
        this.sendAsyncMessage(topic, tagsArr, keys, body, callback, selector, arg, 0);
    }

    private Message createMessage(String topic, String[] tagsArr, String keys, String body) throws UnsupportedEncodingException {
        if (StringUtils.isEmpty(topic)) {
            throw new RuntimeException("topic not allow empty!");
        }
        String tags = "*";
        if (CollectionUtils.isEmpty(tagsArr)) {
            tags = Convert.toStrConcat(tagsArr, "||");
        }
        return new Message(topic, tags, keys, body.getBytes("UTF-8"));
    }

}
