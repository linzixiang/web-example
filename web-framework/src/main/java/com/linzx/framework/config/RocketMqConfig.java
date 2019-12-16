package com.linzx.framework.config;

import com.linzx.framework.config.properties.RocketMqProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties(RocketMqProperties.class)
public class RocketMqConfig {

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnProperty(prefix = "rocketmq.producer", value = "enable", havingValue = "true")
    public DefaultMQProducer defaultProducer(RocketMqProperties mqProperties) throws MQClientException {
        log.info("defaultProducer config：" + mqProperties.toString());
        log.info("defaultProducer 正在创建---------------------------------------");
        DefaultMQProducer producer = new DefaultMQProducer(mqProperties.getGroupName());
        producer.setNamesrvAddr(mqProperties.getNamesrvAddr());
        producer.setVipChannelEnabled(mqProperties.getProducer().isVipChannelEnabled());
        producer.setRetryTimesWhenSendAsyncFailed(mqProperties.getProducer().getRetryTimesWhenSendAsyncFailed());
        producer.start();
        log.info("rocketmq producer server开启成功---------------------------------.");
        return producer;
    }

}
