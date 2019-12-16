package com.linzx.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MapUtils extends org.apache.commons.collections.MapUtils {


    public static String getStringValue(Map<String, ?> map ,String name){
        return org.apache.commons.collections4.MapUtils.getString(map, name);
    }

    public static String getStringValue(Map<String, ?> map ,String name, String defaultValue){
        return org.apache.commons.collections4.MapUtils.getString(map, name, defaultValue);
    }

    public static Date getDate(Map<String, ?> map, String name) {
        Object value = map.get(name);
        if (value == null) {
            return null;
        }
        if (value instanceof Date) {
            return (Date) value;
        }
        if (value instanceof String) {
            return DateUtils.parseDate(value);
        }
        return null;
    }


    /**
     * 根据键值对生成map集合
     * @param keysAndValues
     * @return
     */
    public static Map<String, Object> genHashMap(Object... keysAndValues) {
        Map<String, Object> ret = new HashMap<String, Object>();
        int len = keysAndValues.length;
        if (len == 0) {
            return ret;
        }
        checkLength(keysAndValues);
        for (int i = 0; i < len; i += 2) {
            String key = String.valueOf(keysAndValues[i]);
            Object val = keysAndValues[i + 1];
            if (val != null)
                ret.put(key, val);
        }
        return ret;
    }

    public static JSONObject getJSONObject(Map<String, Object> map, String key) {
        String json = MapUtils.getStringValue(map, key);
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        return JSONObject.parseObject(json);
    }

    private static void checkLength(Object...keysAndValues){
        if (keysAndValues.length % 2 != 0)
            throw new IllegalArgumentException("传入的参数必须成对!");
    }

}
