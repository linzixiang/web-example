package com.linzx.utils.http.convert;

import org.apache.http.HttpEntity;

public interface HttpMessageConverter<T> {

    boolean canRead(Class<T> responseClass, String contentType);

    T read(Class<T> responseClass, HttpEntity entity);
}
