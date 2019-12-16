package com.linzx.common.utils;

import com.linzx.utils.ResourceLoaderUtils;
import com.linzx.utils.StringUtils;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 配置处理工具类
 */
public class YamlUtils {

    public static Map<String, ?> loadYaml(String fileName) throws IOException {
        InputStream in = YamlUtils.class.getClassLoader().getResourceAsStream(fileName);
        return StringUtils.isNotEmpty(fileName) ? (LinkedHashMap<String, ?>) new Yaml().load(in) : null;
    }

}
