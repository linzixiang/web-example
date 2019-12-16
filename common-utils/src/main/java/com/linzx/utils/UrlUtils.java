package com.linzx.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * url构建工具
 */
public class UrlUtils {

    /**
     * 向url链接追加参数
     *
     * @param url 基础地址
     * @param params Map<String, String> 参数
     * @return
     */
    public static String appendParams(String url, Map<String, String> params) {
        if (StringUtils.isEmpty(url)) {
            return "";
        } else if (MapUtils.isEmpty(params)) {
            return url.trim();
        } else {
            StringBuffer sb = new StringBuffer();
            for (String key : params.keySet()) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            url = url.trim();
            int length = url.length();
            int index = url.indexOf("?");
            if (index > -1) { //url说明有问号
                if ((length - 1) == index) { //url最后一个符号为？，如：http://wwww.baidu.com?
                    url += sb.toString();
                } else { //情况为：http://wwww.baidu.com?aa=11
                    url += "&" + sb.toString();
                }
            } else { //url后面没有问号，如：http://wwww.baidu.com
                url += "?" + sb.toString();
            }
            return url;
        }
    }

    /**
     * 向url链接追加参数(单个)
     * @param url 基础地址
     * @param name String 参数key
     * @param value String 参数value
     * @return
     */
    public static String appendSingleParam(String url, String name, String value){
        if(StringUtils.isEmpty(url)){
            return "";
        }else if(StringUtils.isEmpty(name)){
            return url.trim();
        }else{
            Map<String, String> params = new LinkedHashMap<>();
            params.put(name, value);
            return appendParams(url, params);
        }
    }

    /**
     * Map转化为对应得参数字符串,同时对参数编码
     * @param paramMap 参数集合, 例如：{'key1': 'value1', 'key2': ['value2', 'value3']}
     * @param urlEncode 是否对参数值编码，例如：key1=value1&key2=value2,value3 或 key1=value1&key2=value2%2Cvalue3&key3=value4%2Cvalue5
     * @return
     */
    public static String mapToParametersStr(Map<String, Object> paramMap, boolean urlEncode) throws Exception {
        StringBuilder builder = new StringBuilder();
        for(Map.Entry<String, Object> entrySet : paramMap.entrySet()) {
            String key = entrySet.getKey();
            Object value = entrySet.getValue();
            if (ObjectUtils.isEmpty(value)) {
                continue;
            }
            if (value instanceof List) {
                value = ((List)value).toArray();
            }
            if (value instanceof Object[]) {
                value = Convert.toStrConcat((Object[]) value, ",");
            }
            if (urlEncode) {
                value = URLEncoder.encode(value.toString(), "utf-8");
            }
            builder.append(key).append("=").append(value).append("&");
        }
        if (builder.length() > 1) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    public static void main(String[] args) throws Exception {
        Map<String, Object> paramMap = new LinkedHashMap<>();
        paramMap.put("key1", "value1");
        List<String> list = new ArrayList<>();
        list.add("value2");
        list.add("value3");
        paramMap.put("key2", list);
        String[] strArr = new String[]{"value4", "value5"};
        paramMap.put("key3", strArr);
        System.out.println(mapToParametersStr(paramMap, true));
    }

}
