package com.linzx.framework.core.runner;

import com.linzx.framework.bean.ExcelExportBean;
import com.linzx.framework.bean.ExcelExportContent;
import com.linzx.framework.core.context.ContextManager;
import com.linzx.utils.ResourceLoaderUtils;
import com.linzx.utils.xml.Dom4jUtils;
import org.dom4j.Element;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@Order(996)
public class ExcelXmlResolverRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        Resource[] resources = ResourceLoaderUtils.getResources("classpath*:config/excel/export/*.xml");
        for(Resource resource : resources) {
            String filename = resource.getFilename().split("\\.")[0];
            Element rootElement = Dom4jUtils.getRootElement(resource.getInputStream());
            Element header = rootElement.element("header");
            ExcelExportBean excelExportBean = Dom4jUtils.elementToObject(ExcelExportBean.class, header);
            Element content = rootElement.element("content");
            List<Element> elements = content.elements();
            List<ExcelExportContent> columns = new ArrayList<>();
            if (excelExportBean.isShowSerialNumber()) {
                ExcelExportContent excelExportContent = new ExcelExportContent();
                excelExportContent.setIndex(0);
                excelExportContent.setField(ExcelExportContent.SERIAL_NUMBER);
                excelExportContent.setName("序号");
                columns.add(excelExportContent);
            }
            for(Element element : elements) {
                ExcelExportContent column = Dom4jUtils.elementToObject(ExcelExportContent.class, element);
                columns.add(column);
            }
            Collections.sort(columns);
            excelExportBean.setContent(columns);
            ContextManager.addExcelExportBean(filename, excelExportBean);
        }
    }
}
