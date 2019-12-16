package com.linzx.framework.exception;

/**
 * 用户账户不存在异常
 */
public class UnknownAccountException extends BizCommonException {

    public UnknownAccountException() {
        super("user.login.account.unknown");
    }
}
