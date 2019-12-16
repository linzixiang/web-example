package com.linzx.utils.http.convert;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import org.apache.http.HttpEntity;

public class FastJsonHttpMessageConverter implements HttpMessageConverter<Object>{

    private FastJsonConfig fastJsonConfig = new FastJsonConfig();

    @Override
    public boolean canRead(Class<Object> responseClass, String contentType) {
        return false;
    }

    @Override
    public Object read(Class<Object> responseClass, HttpEntity entity) {
        return null;
    }

}
