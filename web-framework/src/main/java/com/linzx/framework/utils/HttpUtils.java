package com.linzx.framework.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 网络请求工具类，默认是Content-Type:application/x-www-form-urlencoded
 */
@Component
@DependsOn({"restTemplateConfig"})
public class HttpUtils {

    private static RestTemplate restTemplate;

    @Autowired
    public HttpUtils(RestTemplate restTemplate) {
        HttpUtils.restTemplate = restTemplate;
    }

    /**
     * 发送get请求，返回json
     */
    public static JSONObject getRetJson(String url, Map<String, String> param, HttpHeaders headers) {
        return get(url, param, headers, JSONObject.class);
    }

    /**
     * 发送get请求
     */
    public static <T> T get(String url, Map<String, String> param, HttpHeaders headers, Class<T> responseType) {
        return execute(url, HttpMethod.GET, param, headers, responseType);
    }

    /**
     * 发送post请求，返回json
     */
    public static JSONObject postRetJson(String url, Map<String, String> param, HttpHeaders headers){
        return post(url, param, headers, JSONObject.class);
    }

    /**
     * 发送post请求
     */
    public static <T> T post(String url, Map<String, String> param, HttpHeaders headers, Class<T> responseType) {
        return execute(url, HttpMethod.POST, param, headers, responseType);
    }

    /**
     * 发送post/get请求
     * @param url 请求地址
     * @param httpMethod 请求方式
     * @param param 请求参数
     * @param headers 请求头
     * @param responseType 响应返回类型
     * @return
     */
    public static  <T> T execute(String url, HttpMethod httpMethod, Map<String, String> param, HttpHeaders headers, Class<T> responseType) {
        MultiValueMap multiValueMap = new LinkedMultiValueMap();
        multiValueMap.setAll(param);
        if (headers == null) {
            headers = new HttpHeaders();
        }
        if (headers.getContentType() == null) {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(multiValueMap, headers);
        RequestCallback requestCallback = restTemplate.httpEntityCallback(requestEntity, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        ResponseEntity<T> responseEntity = restTemplate.execute(url, httpMethod, requestCallback, responseExtractor);
        return responseEntity.getBody();
    }

}
