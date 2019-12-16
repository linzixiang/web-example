package com.linzx.utils.http;

import com.alibaba.fastjson.JSONObject;
import com.linzx.utils.ObjectUtils;
import com.linzx.utils.UrlUtils;
import com.linzx.utils.http.convert.ResponseExtractor;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;

public class ClientHttpRequest<T> extends HttpEntityEnclosingRequestBase implements ResponseHandler<T> {

    public static final ContentType APPLICATION_FORM_URLENCODED_UTF_8 = ContentType.create("application/x-www-form-urlencoded", Consts.UTF_8);
    public static final ContentType APPLICATION_JSON = ContentType.create("application/json", Consts.UTF_8);

    /**
     * http请求方式 get pos
     */
    private MethodType method;
    /**
     * 默认使用的响应编码
     */
    private Charset defaultCharset;
    /**
     * 响应类型
     */
    private Class<T> responseType;
    /**
     * 从响应中抽取结果
     */
    private ResponseExtractor responseExtractor;

    public ClientHttpRequest(URI uri, MethodType method, Map<String, Object> params, ContentType contentType) throws Exception {
        this(uri, method);
        String parametersStr = null;
        if (ObjectUtils.equals(contentType.toString(), APPLICATION_FORM_URLENCODED_UTF_8.toString())) {
            parametersStr = UrlUtils.mapToParametersStr(params, true);
        } else if (ObjectUtils.equals(contentType.toString(), APPLICATION_JSON.toString())) {
            parametersStr = JSONObject.toJSONString(params);
        } else {
            throw new RuntimeException("request contentType error");
        }
        setEntity(new StringEntity(parametersStr, contentType));
    }

    public ClientHttpRequest(String uri, MethodType method, Map<String, Object> params, ContentType contentType) {
        this(uri, method);
        setEntity(new StringEntity("", contentType));
    }

    /**
     * 根据请求地址 请求方法，请求内容对象
     *
     * @param uri            请求地址
     * @param method         请求方法
     * @param params         请求参数
     */
    public ClientHttpRequest(URI uri, MethodType method, Map<String, Object> params) throws Exception {
        this(uri, method, params, APPLICATION_FORM_URLENCODED_UTF_8);
    }

    public ClientHttpRequest(String uri, MethodType method, Map<String, Object> params) {
        this(uri, method, params, APPLICATION_FORM_URLENCODED_UTF_8);
    }

    public ClientHttpRequest(URI uri, MethodType method, String body, ContentType contentType) {
        this(uri, method);
        setEntity(new StringEntity(body, contentType));
    }

    public ClientHttpRequest(String uri, MethodType method, String body, ContentType contentType) {
        this(uri, method);
        setEntity(new StringEntity(body, contentType));
    }

    public ClientHttpRequest(URI uri, MethodType method, String body) {
        this(uri, method, body, APPLICATION_FORM_URLENCODED_UTF_8);
    }

    public ClientHttpRequest(String uri, MethodType method, String body) {
        this(uri, method, body, APPLICATION_FORM_URLENCODED_UTF_8);
    }

    /**
     * 根据请求地址 请求方法
     *
     * @param uri    请求地址
     * @param method 请求方法
     */
    public ClientHttpRequest(URI uri, MethodType method) {
        this.setURI(uri);
        this.method = method;
    }

    public ClientHttpRequest(String uri, MethodType method) {
        this.setURI(URI.create(uri));
        this.method = method;
    }

    /**
     * 请求的类型
     * @param contentType
     * @return
     */
    public ClientHttpRequest<T> contentType(ContentType contentType) {
        HttpEntity httpEntity = getEntity();
        if (httpEntity instanceof AbstractHttpEntity) {
            ((AbstractHttpEntity) httpEntity).setContentType(contentType.toString());
        }
        return this;
    }

    /**
     * 设置请求头
     * @param headers
     * @return
     */
    public ClientHttpRequest<T> httpHeader(HttpHeader headers) {
        for (Header header : headers.getHeaders()) {
            addHeader(header);
        }
        return this;
    }

    /**
     * 响应的编码
     * @param defaultCharset
     * @return
     */
    public ClientHttpRequest<T> defaultCharset(Charset defaultCharset) {
        this.defaultCharset = defaultCharset;
        return this;
    }

    /**
     * 响应类型
     * @param responseType
     * @return
     */
    public ClientHttpRequest<T> responseType(Class<T> responseType) {
        this.responseType = responseType;
        return this;
    }

    /**
     * 设置代理
     * @param httpProxy
     * @return
     */
    public ClientHttpRequest<T> proxy(HttpHost httpProxy) {
        if (httpProxy != null) {
            RequestConfig config = RequestConfig.custom().setProxy(httpProxy).build();
            setConfig(config);
        }
        return this;
    }

    /**
     * 设置抽取结果类
     */
    public ClientHttpRequest<T> responseExtractor(ResponseExtractor responseExtractor) {
        this.responseExtractor = responseExtractor;
        return this;
    }

    public Charset getDefaultCharset() {
        if (null == defaultCharset) {
            defaultCharset = Consts.UTF_8;
        }
        return defaultCharset;
    }

    public Class<T> getResponseType() {
        if (responseType == null) {
            return (Class<T>) String.class;
        }
        return responseType;
    }
    
    public ResponseExtractor getResponseExtractor() {
        if (responseExtractor == null) {
            this.responseExtractor = new ResponseExtractor();
        }
        return responseExtractor;
    }

    @Override
    public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        final StatusLine statusLine = response.getStatusLine();
        final HttpEntity entity = response.getEntity();
        String[] value = null;
        if (null == entity.getContentType()) {
            value = new String[]{"application/x-www-form-urlencoded"};
        } else {
            value = entity.getContentType().getValue().split(";");
        }
        //这里进行特殊处理，如果状态码非正常状态，但内容类型匹配至对应的结果也进行对应的响应类型转换
        if (statusLine.getStatusCode() >= 300 && statusLine.getStatusCode() != 304) {
            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
        }
        return (T) getResponseExtractor().extractData(entity, getClass());
    }

    @Override
    public String getMethod() {
        return method.name();
    }

}
