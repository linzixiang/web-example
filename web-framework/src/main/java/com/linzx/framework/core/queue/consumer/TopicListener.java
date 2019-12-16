package com.linzx.framework.core.queue.consumer;


public interface TopicListener {

    void listener(String topic, String[] tags) throws Exception;

    String getTopic();

    String[] getTags();
}
