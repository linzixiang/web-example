package com.linzx.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

public class ResourceLoaderUtils {

    private static ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    /**
     * 获取精确指定的单个资源，例如：classpath:META-INF/spring.factories，META-INF/spring.factories
     */
    public static Resource getResource(String location) {
        return resolver.getResource(location);
    }

    /**
     * 获取模糊匹配的多个资源
     */
    public static Resource[] getResources(String locationPattern) throws IOException {
        // 例如：classpath*:com/seasy/**/*.class，classpath*:META-INF/spring.factories
        return resolver.getResources(locationPattern);
    }
}
