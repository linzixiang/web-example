package com.linzx.framework.core.runner;

import com.linzx.framework.bean.DictBean;
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
@Order(998)
public class DictXmlResolverRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        Resource[] resources = ResourceLoaderUtils.getResources("classpath*:config/dict/*.xml");
        for(Resource resource : resources) {
            Element rootElement = Dom4jUtils.getRootElement(resource.getInputStream());
            List<Element> elements = rootElement.elements();
            for(Element element : elements) {
                String queryCode = element.attributeValue(DictBean.UNIQUE_KEYNAME);
                DictBean dictBean = Dom4jUtils.elementToObject(DictBean.class, element);
                ContextManager.addDict(queryCode, dictBean);
            }
        }
    }
}
