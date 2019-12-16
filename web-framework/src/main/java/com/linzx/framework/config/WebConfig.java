package com.linzx.framework.config;

import com.linzx.framework.web.listener.LogbackListener;
import com.linzx.framework.web.servlet.ApiGateWayServlet;
import com.linzx.framework.web.filter.XssFilter;
import com.linzx.utils.StringUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class WebConfig {

//    @Value("${xss.enabled}")
    private String enabled;

//    @Value("${xss.excludes}")
    private String excludes;

//    @Value("${xss.urlPatterns}")
    private String urlPatterns;

//    @Bean
    public FilterRegistrationBean xssFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new XssFilter());
        registration.addUrlPatterns(StringUtils.split(urlPatterns, ","));
        registration.setName("xssFilter");
        registration.setOrder(Integer.MAX_VALUE);
        Map<String, String> initParameters = new HashMap<String, String>();
        initParameters.put("excludes", excludes);
        initParameters.put("enabled", enabled);
        registration.setInitParameters(initParameters);
        return registration;
    }

    @Bean
    public ServletRegistrationBean dispatchServletRegistration() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        servletRegistrationBean.setServlet(new ApiGateWayServlet());
        List<String> urlMappingList = new ArrayList<>();
        urlMappingList.add("/api/*");
        servletRegistrationBean.setUrlMappings(urlMappingList);
        return servletRegistrationBean;
    }

//    @Bean
    public ServletListenerRegistrationBean servletListenerRegistrationBean() {
        ServletListenerRegistrationBean  servletListenerRegistrationBean = new ServletListenerRegistrationBean();
        servletListenerRegistrationBean.setListener(new LogbackListener());
        return servletListenerRegistrationBean;
    }

}
