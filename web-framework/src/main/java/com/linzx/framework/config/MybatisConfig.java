package com.linzx.framework.config;

import com.linzx.framework.config.properties.MybatisProperties;
import com.linzx.framework.core.mybatis.pagehelper.PageInterceptor;
import com.linzx.framework.core.mybatis.versionlock.interceptor.OptimisticLocker;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * mybatis在原先自动配置的基础上增加配置
 */
@Configuration
@DependsOn({"mybatisProperties"})
@AutoConfigureAfter({MybatisAutoConfiguration.class})
public class MybatisConfig {

    @Autowired
    private List<SqlSessionFactory> sqlSessionFactoryList;

    /**
     * 乐观锁,分页拦截器
     */
    @PostConstruct
    public void addOptimisticLockerInterceptor() {
//        OptimisticLocker optimisticLocker = new OptimisticLocker();
//        Properties optimisticLockerProperties = new Properties();
//        optimisticLockerProperties.put("versionColumn", MybatisProperties.versionColumn);
//        optimisticLockerProperties.put("versionField", MybatisProperties.versionField);
//        optimisticLocker.setProperties(optimisticLockerProperties);
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties pageProperties = new Properties();
        pageProperties.put("supportMethodsArguments", MybatisProperties.helperSupportMethodsArguments);
        pageProperties.put("support-methods-arguments", MybatisProperties.helperSupportMethodsArguments);
        pageProperties.put("helperDialect", MybatisProperties.helperDialect);
        pageProperties.put("helper-dialect", MybatisProperties.helperDialect);
        pageProperties.put("pageSizeZero", MybatisProperties.helperPageSizeZero);
        pageProperties.put("page-size-zero", MybatisProperties.helperPageSizeZero);
        pageProperties.put("reasonable", MybatisProperties.helperReasonable);
        pageProperties.put("params", MybatisProperties.helperParams);
        pageInterceptor.setProperties(pageProperties);
        Iterator<SqlSessionFactory> iterator = this.sqlSessionFactoryList.iterator();
        while(iterator.hasNext()) {
            SqlSessionFactory sqlSessionFactory = iterator.next();
            sqlSessionFactory.getConfiguration().addInterceptor(pageInterceptor);
//            sqlSessionFactory.getConfiguration().addInterceptor(optimisticLocker);
        }
    }

}
