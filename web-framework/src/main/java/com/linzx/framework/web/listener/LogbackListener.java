package com.linzx.framework.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * logback设置环境变量
 */
public class LogbackListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        
    }
}
