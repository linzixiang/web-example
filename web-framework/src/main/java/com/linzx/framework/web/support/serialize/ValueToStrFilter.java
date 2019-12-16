package com.linzx.framework.web.support.serialize;

import com.alibaba.fastjson.serializer.ValueFilter;

import java.math.BigDecimal;

/**
 * 将基础类型转字符串
 */
public class ValueToStrFilter implements ValueFilter {

    @Override
    public Object process(Object object, String name, Object value) {
        if (value == null) return null;
        if (value instanceof Integer || value instanceof Long ||
            value instanceof Double || value instanceof BigDecimal ||
            value instanceof Float) {
            return value.toString();
        }
        return value;
    }

}
