package com.linzx.framework.config.properties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@DependsOn({"globalProperties"})
public class ElasticSearchProperties implements InitializingBean {

    /**
     * 例如：192.168.0.100:9200,192.168.0.101:9200
     */
    public static String host;
    public static int connectTimeoutMillis = 1000;
    public static int socketTimeoutMillis = 30000;
    public static int connectionRequestTimeoutMillis = 500;
    public static int maxConnectPerRoute = 10;
    public static int maxConnectTotal = 30;

    @Override
    public void afterPropertiesSet() throws Exception {

    }

}
