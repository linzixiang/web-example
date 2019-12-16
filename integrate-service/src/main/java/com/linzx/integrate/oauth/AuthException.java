package com.linzx.integrate.oauth;

import com.linzx.integrate.oauth.enums.AuthResponseStatus;

public class AuthException extends RuntimeException {

    private int errorCode;

    private String errorMsg;

    public AuthException(int errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public AuthException(AuthResponseStatus status) {
        super(status.getMsg());
        this.errorCode = status.getCode();
        this.errorMsg = status.getMsg();
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthException(Throwable cause) {
        super(cause);
    }

    public AuthException(String errorMsg) {
        this(AuthResponseStatus.FAILURE.getCode(), errorMsg);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

}
