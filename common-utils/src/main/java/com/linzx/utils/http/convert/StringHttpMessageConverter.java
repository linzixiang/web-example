package com.linzx.utils.http.convert;

import org.apache.http.HttpEntity;

public class StringHttpMessageConverter implements HttpMessageConverter<String>{

    @Override
    public boolean canRead(Class<String> responseClass, String contentType) {
        return false;
    }

    @Override
    public String read(Class<String> responseClass, HttpEntity entity) {
        return null;
    }

}
