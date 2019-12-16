package com.linzx.framework.es.core;

import com.linzx.framework.es.annotation.EnableESTools;
import com.linzx.utils.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ElasticProcessor implements BeanFactoryPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;
    /**
     * 扫描ESMetaData注解的类托管给spring
     * @param beanFactory
     * @throws BeansException
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Map<String,Object> beans = applicationContext.getBeansWithAnnotation(EnableESTools.class);
        beans.forEach((beanName,bean) -> {
                //获取启动注解信息
                Class<?> mainClass = bean.getClass();
                String[] eps = mainClass.getAnnotation(EnableESTools.class).entityPath();
                List<String> pathList = new ArrayList<>();
                for (int i = 0; i < eps.length; i++) {
                    if(!StringUtils.isEmpty(eps[i])){
                        pathList.add(eps[i]);
                    }
                }
                if(pathList.size() == 0){
                    eps = new String[1];
                    pathList.add(mainClass.getPackage().getName());
                }
                eps = pathList.toArray(new String[pathList.size()]);
                //扫描entity
                ESEntityScanner scanner = new ESEntityScanner((BeanDefinitionRegistry) beanFactory);
                scanner.setResourceLoader(this.applicationContext);
                scanner.scan(eps);
            }
        );
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
