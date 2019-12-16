package com.linzx.framework.config.properties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.net.URL;

@Configuration
@DependsOn({"globalProperties"})
public class RedissonProperties implements InitializingBean {

    public static String mode;

    public static class SingleServer {

        public static String address;

        public static Integer connectionPoolSize = 500;

        public static Integer idleConnectionTimeout = 10000;

        public static Integer connectTimeout = 30000;

        public static Integer timeout = 3000;

        public static Integer database = 0;
    }



    @Override
    public void afterPropertiesSet() throws Exception {
        mode = GlobalProperties.getConfigStr("spring.redis.mode", "");
        switch (mode) {
            case "single":
                String host = GlobalProperties.getConfigStr("spring.redis.host", "");
                Integer port = GlobalProperties.getConfigInt("spring.redis.port", 6379);
                Integer database = GlobalProperties.getConfigInt("spring.redis.database", 0);
                SingleServer.address = "redis://" + host + ":" + port;
                SingleServer.database = database;
                break;
        }
    }

}
