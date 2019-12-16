package com.linzx.framework.config.properties;

import com.linzx.common.utils.YamlUtils;
import com.linzx.utils.MapUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 初始化yml配置
 */
@Configuration
public class GlobalProperties implements InitializingBean {

    /** 配置文件配置缓存 **/
    private static final Map<String, Object> configMap = new ConcurrentHashMap<>();

    public void initConfig() throws Exception {
        Map<String, ?> yamlConfig = YamlUtils.loadYaml("application.yml");
        Map<String, Object> yamlConfigTiledMap = new LinkedHashMap<>();
        tiled(yamlConfigTiledMap, yamlConfig, "");
        if (yamlConfigTiledMap.containsKey("spring.profiles.active")) {
            String activeYml = MapUtils.getString(yamlConfigTiledMap, "spring.profiles.active");
            Map<String, ?> yamlConfig2 = YamlUtils.loadYaml("application-" +activeYml+ ".yml");
            tiled(yamlConfigTiledMap, yamlConfig2, "");
        }
        for(String key : yamlConfigTiledMap.keySet()) {
            configMap.put(key, yamlConfigTiledMap.get(key));
        }
    }

    /**
     * 将层次结构的配置平铺
     */
    private void tiled(Map<String, Object> yamlConfigNew, Map<String, ?> yamlConfig, String parentKey) {
        if (yamlConfig == null) {
            return;
        }
        for (String key : yamlConfig.keySet()) {
            Object value = yamlConfig.get(key);
            if (value instanceof Map) {
                tiled(yamlConfigNew, (Map<String,?>)value, parentKey + "." + key);
            } else {
                if (parentKey.startsWith(".")) {
                    parentKey = parentKey.substring(1);
                }
                yamlConfigNew.put(parentKey + "." + key, value);
                if (key.contains("-")) {
                    String[] keyArr = key.split("-");
                    StringBuilder keyBuilder = new StringBuilder(keyArr[0]);
                    for (int i = 1; i < keyArr.length; i++) {
                        keyBuilder.append(keyArr[i].substring(0, 1).toUpperCase())
                                .append(keyArr[i].substring(1));
                    }
                    yamlConfigNew.put(parentKey + "." + keyBuilder.toString(), value);
                }
            }
        }
    }

    /** 获取参数配置 **/
    public static Integer getConfigInt(String key,Integer defaultVal) {
        return MapUtils.getInteger(configMap, key, defaultVal);
    }
    public static Long getConfigLong(String key, Long defaultVal) {
        return MapUtils.getLong(configMap, key, defaultVal);
    }
    public static String getConfigStr(String key, String defaultVal) {
        return MapUtils.getString(configMap, key, defaultVal);
    }
    public static Boolean getConfigBool(String key, Boolean defaultVal) {
        return MapUtils.getBoolean(configMap, key, defaultVal);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.initConfig();
    }
}
