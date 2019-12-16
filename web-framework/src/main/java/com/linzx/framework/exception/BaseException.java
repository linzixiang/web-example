package com.linzx.framework.exception;

import com.linzx.framework.utils.MessageUtils;
import com.linzx.utils.StringUtils;

public class BaseException extends RuntimeException {

    /**
     * 所属模块
     */
    private String module;

    /**
     * 错误码
     */
    private String code;

    /**
     * 错误码对应的参数
     */
    private Object[] args;

    public BaseException(String module, String code, Object[] args) {
        this.module = module;
        this.code = code;
        this.args = args;
    }

    public BaseException(String code, Object[] args) {
        this(null, code, args);
    }

    public BaseException(String code) {
        this(null, code, null);
    }

    @Override
    public String getMessage() {
        String message = null;
        if (!StringUtils.isEmpty(code)) {
            message = MessageUtils.message(code, args);
        }
        if (message == null) {
            message = "unknown message";
        }
        return message;
    }
}
