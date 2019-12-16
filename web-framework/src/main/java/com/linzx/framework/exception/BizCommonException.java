package com.linzx.framework.exception;

public class BizCommonException extends BaseException{

    public BizCommonException(String module, String code, Object[] args) {
        super(module, code, args);
    }

    public BizCommonException(String code, Object[] args) {
        super(code, args);
    }

    public BizCommonException(String code) {
        super(code);
    }
}
