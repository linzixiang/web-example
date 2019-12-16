package com.linzx.framework.web.vo;

import com.alibaba.fastjson.JSONObject;
import com.linzx.framework.core.mybatis.pagehelper.Page;
import com.linzx.framework.utils.MessageUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 展现层返回对象封装
 * { "bizCode": 0, "bizMsg": "成功", "data": "[] 或 {}"}
 * { "bizCode": 500, "bizMsg": "错误提示"}
 */
public class AjaxResult extends HashMap<String, Object> {

    private final String CODE_TAG = "bizCode";

    private final String MSG_TAG = "bizMsg";

    private final String DATA_TAG = "data";

    public enum Type {
        /**
         * 成功
         */
        SUCCESS(0),
        /**
         * 错误
         */
        ERROR(1000),
        /**
         * 参数校验失败
         */
        VALID_ERROR(1001),
        /**
         * 登录认证失效（会话超时）
         */
        LOGIN_ERROR(2001),
        /**
         * 无权限
         */
        PERMISSION_DENY(2002);

        private final int value;

        Type(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }

    /**
     * 状态类型
     */
    private Type type;

    /**
     * 状态码
     */
    private int code;

    /**
     * 返回内容
     */
    private String msg;

    /**
     * 数据对象
     */
    private Object data;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public AjaxResult(Type type, Object msg, Object data) {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
        if (data != null) {
            super.put(DATA_TAG, data);
        }
    }

    public static AjaxResult success() {
        return success(null);
    }

    public static AjaxResult success (Object data) {
        if (data != null && data instanceof  List) {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("rows", data);
            if (data instanceof Page) {
                Page page = ((Page) data);
                dataMap.put("totalNum", page.getTotal());
                dataMap.put("totalPage",  page.getPages());
                if (page.isNeedSum()) {
                    dataMap.put("colSum", page.getRowSum());
                }
            }
            data = dataMap;
        }
        return new AjaxResult(Type.SUCCESS, MessageUtils.message("operator.success"), data);
    }

    public static AjaxResult error () {
        return new AjaxResult(Type.ERROR, MessageUtils.message("operator.error"), null);
    }

    public static AjaxResult error (String msg) {
        return new AjaxResult(Type.ERROR, msg, null);
    }

    public static AjaxResult validError (Object data) {
        return new AjaxResult(Type.ERROR, data, null);
    }

    public AjaxResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    /**
     * 结果转json字符串
     */
    public String toJSONString(AjaxResult ajaxResult) {
        return  JSONObject.toJSONString(ajaxResult);
    }
}
