package com.linzx.framework.core.runner;

import com.linzx.framework.bean.CodeBean;
import com.linzx.framework.core.context.ContextManager;
import com.linzx.utils.ResourceLoaderUtils;
import com.linzx.utils.StringUtils;
import com.linzx.utils.xml.Dom4jUtils;
import org.dom4j.Element;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;

import java.util.List;

/**
 * 启动解析Code.xml配置
 */
@Configuration
@Order(1000)
public class CodeXmlResolverRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        Resource[] resources = ResourceLoaderUtils.getResources("classpath*:config/code/*.xml");
        for(Resource resource : resources) {
            Element rootElement = Dom4jUtils.getRootElement(resource.getInputStream());
            List<Element> elements = rootElement.elements();
            for(Element element : elements) {
                CodeBean codeBean = Dom4jUtils.elementToObject(CodeBean.class, element);
                if (StringUtils.isEmpty(codeBean.getLikeField())) {
                    // 默认用nameField属性搜索
                    codeBean.setLikeField(codeBean.getNameField());
                }
                String keyName = element.attributeValue(CodeBean.UNIQUE_KEYNAME);
                ContextManager.addCode(keyName, codeBean);
            }
        }
    }

}
