package com.linzx.framework.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.linzx.framework.web.interceptor.FrmManageInterceptor;
import com.linzx.framework.web.interceptor.RepeatSubmitInterceptor;
import com.linzx.framework.web.interceptor.SensAnnotationInterceptor;
import com.linzx.framework.web.support.serialize.SensitivePropSerialize;
import com.linzx.framework.web.support.ReqHeaderMethodArgumentResolver;
import com.linzx.framework.web.support.serialize.ValueToStrFilter;
import com.linzx.utils.CharsetKit;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    /**
     * 修改StringHttpMessageConverter返回字符为utf-8
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 添加FastJson转换器
        //1.需要定义一个convert转换消息的对象;
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        //2:添加fastJson的配置信息;
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.WriteNullListAsEmpty, // List字段如果为null,输出为[],而非null
                SerializerFeature.WriteNullStringAsEmpty, // 字符类型字段如果为null,输出为"",而非null
                SerializerFeature.DisableCircularReferenceDetect, // 消除对同一对象循环引用的问题
                SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null,输出为false
                SerializerFeature.WriteDateUseDateFormat
        );
        SensitivePropSerialize propSerialize = new SensitivePropSerialize();
        ValueToStrFilter valueToStrFilter = new ValueToStrFilter();
        fastJsonConfig.setSerializeFilters(valueToStrFilter, propSerialize);
        //3处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        //4.在convert中添加配置信息.
        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(0, fastJsonHttpMessageConverter); // 放在首位，优先级最高
        // 修改字符串编码
        for (HttpMessageConverter<?> messageConverter : converters) {
            if (messageConverter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) messageConverter).setDefaultCharset(CharsetKit.CHARSET_UTF_8);
            }
        }
        super.extendMessageConverters(converters);
    }

    /**
     * 自定义请求参数解析器
     */
    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(new ReqHeaderMethodArgumentResolver());
    }

    /**
     * 配置访问静态资源
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        if(!registry.hasMappingForPattern("/static/**")){
            registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        }
        super.addResourceHandlers(registry);
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RepeatSubmitInterceptor());
        registry.addInterceptor(new SensAnnotationInterceptor());
        registry.addInterceptor(new FrmManageInterceptor());
        super.addInterceptors(registry);
    }


}
