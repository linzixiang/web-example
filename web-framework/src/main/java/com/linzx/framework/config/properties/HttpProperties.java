package com.linzx.framework.config.properties;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@DependsOn({"globalProperties"})
public class HttpProperties implements InitializingBean {

    /** 读取超时时间 **/
    public static int readTimeout;

    /** 连接超时时间 **/
    public static int connectionTimeout;

    /** 是否开启网络代理 **/
    public static boolean proxyEnable;

    /** 代理的主机名称 **/
    public static String proxyHostname;

    /** 代理的主机端口 **/
    public  static int proxyPort;

    @Override
    public void afterPropertiesSet() throws Exception {
        readTimeout = GlobalProperties.getConfigInt("http.read.timeout", 5000);
        connectionTimeout = GlobalProperties.getConfigInt("http.connection.timeout", 15000);
        proxyEnable = GlobalProperties.getConfigBool("http.proxy.enable.", false);
        proxyHostname = GlobalProperties.getConfigStr("http.proxy.hostname", "");
        proxyPort = GlobalProperties.getConfigInt("http.proxy.port", 0);
    }
}
