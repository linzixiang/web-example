package com.linzx.framework.core.runner;

import com.linzx.framework.bean.SensitiveBean;
import com.linzx.framework.core.context.ContextManager;
import com.linzx.utils.ResourceLoaderUtils;
import com.linzx.utils.xml.Dom4jUtils;
import org.dom4j.Element;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;

import java.util.List;

@Configuration
@Order(997)
public class SensitiveResolverRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        Resource resource = ResourceLoaderUtils.getResource("classpath:config/role-sensitive.xml");
        Element rootElement = Dom4jUtils.getRootElement(resource.getInputStream());
        List<Element> elements = rootElement.elements();
        for(Element element : elements) {
            String queryCode = element.attributeValue(SensitiveBean.UNIQUE_KEYNAME);
            SensitiveBean sensitiveBean = Dom4jUtils.elementToObject(SensitiveBean.class, element);
            ContextManager.addSensitive(queryCode, sensitiveBean);
        }
    }

}
