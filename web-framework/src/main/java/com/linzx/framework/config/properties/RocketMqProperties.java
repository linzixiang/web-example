package com.linzx.framework.config.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Setter
@Getter
@ToString
@Primary
@ConfigurationProperties(prefix = "rocketmq")
@Configuration
public class RocketMqProperties {

    public String namesrvAddr; // nameServer配置

    public String groupName; // 组名称

    public Producer producer = new Producer();

    public Consumer consumer = new Consumer();

    @Setter
    @Getter
    @ToString
    public class Producer {
        public boolean enable = false;
        public boolean vipChannelEnabled = false;
        int defaultTopicQueueNums = 4;
        int sendMsgTimeout = 3000;
        int compressMsgBodyOverHowmuch = 1024 * 4;
        private int retryTimesWhenSendFailed = 2;
        int retryTimesWhenSendAsyncFailed = 2;
        boolean retryAnotherBrokerWhenNotStoreOK = false;
        int maxMessageSize = 1024 * 1024 * 4;
    }

    @Setter
    @Getter
    @ToString
    public class Consumer {
        public boolean enable = false;
        public boolean vipChannelEnabled = false;
        int consumeThreadMin = 20;
        int consumeThreadMax = 64;
        long adjustThreadPoolNumsThreshold = 100000;
        int pullThresholdForQueue = 1000;
        int pullThresholdSizeForQueue = 100;
        int pullThresholdForTopic = -1;
        int pullThresholdSizeForTopic = -1;
        long pullInterval = 0;
        int consumeMessageBatchMaxSize = 1;
        int pullBatchSize = 32;
        boolean postSubscriptionWhenPull = false;
        boolean unitMode = false;
        int maxReconsumeTimes = -1;
        long suspendCurrentQueueTimeMillis = 1000;
        long consumeTimeout = 15;
    }



}
