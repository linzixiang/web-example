package com.linzx.framework.core.runner;

import com.linzx.framework.bean.ApiBean;
import com.linzx.framework.core.context.ContextManager;
import com.linzx.utils.ResourceLoaderUtils;
import com.linzx.utils.xml.Dom4jUtils;
import org.apache.commons.io.IOUtils;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.xpath.DefaultXPath;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.util.List;

@Configuration
@Order(999)
public class ApiXmlResolverRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        Resource[] resources = ResourceLoaderUtils.getResources("classpath*:config/gateway/*.xml");
        for(Resource resource : resources) {
            InputStream inputStream = null;
            try {
                inputStream = resource.getInputStream();
                Element rootElement = Dom4jUtils.getRootElement(inputStream);
                XPath xPath = new DefaultXPath("/apiGateway/module/api");
                List<Element> elements = xPath.selectNodes(rootElement);
                for(Element element : elements) {
                    String apiId = element.attributeValue(ApiBean.UNIQUE_KEYNAME);
                    ApiBean apiBean = Dom4jUtils.elementToObject(ApiBean.class, element);
                    ContextManager.addApiGateWay(apiId, apiBean);
                }
            } finally {
                IOUtils.closeQuietly(inputStream);
            }

        }
    }
}
