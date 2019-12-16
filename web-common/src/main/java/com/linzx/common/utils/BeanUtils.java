package com.linzx.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Bean 工具类
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {

    private static Logger logger = LoggerFactory.getLogger(BeanUtils.class);

    /**
     * 将对象src属性赋值到对象dest
     * @param dest
     * @param src
     */
    public static void copyBeanProp(Object dest, Object src) {
        try {
            copyProperties(src, dest);
        } catch (Exception e) {
            logger.error("BeanUtils.copyBeanProp error", e);
        }
    }

    /**
     * 将bean对象转map，如果map集合中存在某key与bean属性一值，则覆盖key相应的value
     */
    public static <T>  Map<String, Object> beanToHashMap(T bean) {
        Map<String, Object> map = new HashMap<>();
        BeanMap beanMap = BeanMap.create(bean);
        for (Object key : beanMap.keySet()) {
            Object object = beanMap.get(key);
            if (object == null) {
                continue;
            }
            map.put(key.toString(), object);
        }
        return map;
    }
}
