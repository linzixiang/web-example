package com.linzx.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 对象工具类型
 */
public class ObjectUtils extends org.springframework.util.ObjectUtils {

    /**
     * 比较是否相等，可以避免空指针报错
     */
    public static boolean equals(Object obj1, Object obj2) {
        return nullSafeEquals(obj1, obj2);
    }

    /**
     * bean对象转map集合
     */
    public static Map<String, Object> beanToMap(Object obj, Field[] fields) {
        HashMap<String, Object> retMap = new HashMap<>();
        if (obj == null) {
            return retMap;
        }
        for (Field field : fields) {
            Object value = ReflectionUtils.getFieldValue(obj, obj.getClass(), field.getName());
            retMap.put(field.getName(), value);
        }
        return retMap;
    }

}
