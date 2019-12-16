package com.linzx.framework.exception;

/**
 * 用户密码错误异常
 */
public class UserPasswordNotMatchException extends BizCommonException {

    public UserPasswordNotMatchException() {
        super("user.login.password.error");
    }
}
