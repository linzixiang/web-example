package com.linzx.framework.core.queue.consumer;

import com.linzx.framework.config.properties.RocketMqProperties;
import com.linzx.utils.CollectionUtils;
import com.linzx.utils.Convert;
import com.linzx.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class AbsBasePushConsumer implements TopicListener, InitializingBean, DisposableBean {

    @Autowired
    protected RocketMqProperties mqProperties;

    private DefaultMQPushConsumer consumer;

    @Override
    public void listener(String topic, String[] tags) throws Exception {
        RocketMqProperties.Consumer consumerProperties = mqProperties.getConsumer();
        if (!consumerProperties.isEnable()) {
            return;
        }
        if (StringUtils.isEmpty(topic)) {
            throw new RuntimeException("消费者topic不允许为空");
        }
        String tagsStr = "*";
        if (CollectionUtils.isNotEmpty(tags)) {
            tagsStr = Convert.toStrConcat(tags, "||");
        }
        log.info("开始：开启" + topic + ":" + tagsStr + "消费者-------------------");
        consumer = new DefaultMQPushConsumer(mqProperties.getGroupName());
        consumer.setNamesrvAddr(mqProperties.getNamesrvAddr());
        consumer.subscribe(topic, tagsStr);
        consumer.setMessageModel(MessageModel.CLUSTERING);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setVipChannelEnabled(consumerProperties.isVipChannelEnabled());
        consumer.setAdjustThreadPoolNumsThreshold(consumerProperties.getAdjustThreadPoolNumsThreshold());
        consumer.setConsumeThreadMax(consumerProperties.getConsumeThreadMax());
        consumer.setConsumeThreadMin(consumerProperties.getConsumeThreadMin());
        consumer.setConsumeTimeout(consumerProperties.getConsumeTimeout());
        consumer.setSuspendCurrentQueueTimeMillis(consumerProperties.getSuspendCurrentQueueTimeMillis());
        consumer.setMaxReconsumeTimes(consumerProperties.getMaxReconsumeTimes());
        consumer.setConsumeMessageBatchMaxSize(consumerProperties.getConsumeMessageBatchMaxSize());
        consumer = registerMessageListener(consumer);
        log.info("结束：开启" + topic + ":" + tagsStr + "消费者-------------------");
    }

    @Override
    public void destroy() {
        if (consumer != null) {
            consumer.shutdown();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.listener(getTopic(), getTags());
    }

    public String[] getTags(){
        return new String[]{ "*" };
    }

    protected abstract DefaultMQPushConsumer registerMessageListener(DefaultMQPushConsumer consumer) throws MQClientException;
}
