package com.linzx.utils.http.convert;

import org.apache.http.HttpEntity;

public class XmlHttpMessageConverter implements HttpMessageConverter<Object>{

    @Override
    public boolean canRead(Class<Object> responseClass, String contentType) {
        return false;
    }

    @Override
    public Object read(Class<Object> responseClass, HttpEntity entity) {
        return null;
    }

}
