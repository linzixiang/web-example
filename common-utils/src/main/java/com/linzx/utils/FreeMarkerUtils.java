package com.linzx.utils;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * freemarker模板引擎工具
 */
public class FreeMarkerUtils {

    private static Map<String, Configuration> configurationCache = new ConcurrentHashMap<String, Configuration>();

    /**
     * 获取配置
     */
    public static Configuration getConfiguration(String templateFilePath) throws IOException {
        if (null != configurationCache.get(templateFilePath)) {
            return configurationCache.get(templateFilePath);
        }
        Configuration config = new Configuration(Configuration.VERSION_2_3_25);
        config.setDefaultEncoding("UTF-8");
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        config.setLogTemplateExceptions(false);
        FileTemplateLoader fileTemplateLoader = new FileTemplateLoader(new File(templateFilePath));
        config.setTemplateLoader(fileTemplateLoader);
        configurationCache.put(templateFilePath, config);
        return config;
    }

    /**
     * 获取转换后的html字符串
     */
    public static String getContent(String templateAbsPath, Object data) throws Exception {
        File file = new File(templateAbsPath);
        String templateFilePath = file.getParentFile().getAbsolutePath();
        String templateFileName = file.getName(); // 模板文件名
        Template template = getConfiguration(templateFilePath).getTemplate(templateFileName);
        StringWriter writer = new StringWriter();
        template.process(data, writer);
        writer.flush();
        return writer.toString();
    }

}
