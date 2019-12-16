package com.linzx.utils.xml;

import com.linzx.utils.ReflectionUtils;
import com.linzx.utils.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.Field;

/**
 * dom4j解析xml
 */
public class Dom4jUtils {

    /**
     * 获取xml的根元素
     */
    public static Element getRootElement(InputStream is) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(is);
        Element element = document.getRootElement();
        return element;
    }

    /**
     * 将Element的属性赋值给clazz对象
     */
    public static <T> T elementToObject(Class<T> clazz, Element xmlStr) throws Exception {
        T bean = clazz.newInstance();
        Field[] declareFields = ReflectionUtils.getAllDeclareFields(clazz);
        for(Field field : declareFields) {
            String value = xmlStr.attributeValue(field.getName());
            if (StringUtils.isEmpty(value)) {
                continue;
            }
            Object fieldValue = null;
            String typeName = field.getType().toString();
            if (field.getType() == String.class || "string".equals(typeName)) {
                fieldValue = value;
            } else if (field.getType() ==  Long.class || "long".equals(typeName)) {
                fieldValue = Long.parseLong(value);
            } else if (field.getType() ==  Integer.class || "int".equals(typeName)) {
                fieldValue = Integer.parseInt(value);
            } else if (field.getType() == Double.class || "double".equals(typeName)) {
                fieldValue = Double.parseDouble(value);
            } else if (field.getType() == Boolean.class || "boolean".equals(typeName)) {
                fieldValue = Boolean.parseBoolean(value);
            } else {
                throw new RuntimeException(String.format("暂不支持该类型设置值：%s -> %s" , value, field.getType().getName()));
            }
            ReflectionUtils.setFieldValue(bean, field.getName(), fieldValue);
        }
        return bean;
    }

}
