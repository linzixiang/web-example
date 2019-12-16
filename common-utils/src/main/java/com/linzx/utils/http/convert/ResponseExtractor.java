package com.linzx.utils.http.convert;

import com.linzx.utils.ObjectUtils;
import org.apache.http.HttpEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 从响应中抽取结果，封装成对象
 */
public class ResponseExtractor<T> {

    private final List<HttpMessageConverter<?>> messageConverters;

    public ResponseExtractor() {
        this.messageConverters = new ArrayList<>();
        this.messageConverters.add(new FastJsonHttpMessageConverter());
        this.messageConverters.add(new XmlHttpMessageConverter());
        this.messageConverters.add(new StringHttpMessageConverter());
    }

    public ResponseExtractor(List<HttpMessageConverter<?>> messageConverters) {
        this.messageConverters = messageConverters;
    }

    public T extractData(HttpEntity entity, Class<T> responseClass) {
        String contentType = entity.getContentType().getValue();
        for (HttpMessageConverter messageConverter : this.messageConverters) {
            if (messageConverter.canRead(responseClass, contentType)) {
                return (T) messageConverter.read(responseClass, entity);
            }
        }
        return null;
    }

}
