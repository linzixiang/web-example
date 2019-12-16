package com.linzx.framework.core.context;

import com.linzx.framework.bean.SensitiveBean;

import java.util.HashMap;
import java.util.Map;

/**
 * 线程变量管理
 */
public class ThreadLocalVariable {

    /** 通用线程变量 **/
    private static ThreadLocal<Map<String, Object>> threadVariables = new ThreadLocal<>();

    /** 需要处理的敏感字段 **/
    private static ThreadLocal<Map<String, SensitiveBean>> sensitiveVariable = new ThreadLocal<>();

    public static Map<String, SensitiveBean> getSensitiveVariable() {
        return sensitiveVariable.get();
    }

    public static void setVariable(String key, Object value) {
        Map<String, Object> mapVariables = threadVariables.get();
        if (mapVariables == null) {
            mapVariables = new HashMap<>();
            threadVariables.set(mapVariables);
        }
        mapVariables.put(key, value);
    }

    public static Object getVariable(String key) {
        Map<String, Object> mapVariables = threadVariables.get();
        if (mapVariables != null) {
            return mapVariables.get(key);
        }
        return null;
    }

    public static void removeVariable(String key) {
        Map<String, Object> mapVariables = threadVariables.get();
        if (mapVariables != null && mapVariables.containsKey(key)) {
            mapVariables.remove(key);
        }
    }

    public static void clear() {
        Map<String, Object> mapVariables = threadVariables.get();
        if (mapVariables != null) {
            mapVariables.clear();
        }
        threadVariables.remove();
        threadVariables.set(null);
    }

    public static void setSensitiveVariable(Map<String, SensitiveBean> sensitiveMap) {
        sensitiveVariable.set(sensitiveMap);
    }

    public static void removeSensitiveVariable() {
        Map<String, SensitiveBean> sensitiveBeanMap = sensitiveVariable.get();
        if (sensitiveBeanMap != null) {
            sensitiveBeanMap.clear();
        }
        sensitiveVariable.remove();
        sensitiveVariable.set(null);
    }
}
