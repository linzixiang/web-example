package com.lin;

import com.linzx.MainApplication;
import com.linzx.framework.core.queue.MqProduceTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class RocketMqTest {

    @Autowired
    MqProduceTemplate mqProduceTemplate;

    @Test
    public void rocketMqTest() {
        try {
//            mqProduceTemplate.sendMessage("TopicTest", new String[]{"*"}, "RocketMqTest");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
