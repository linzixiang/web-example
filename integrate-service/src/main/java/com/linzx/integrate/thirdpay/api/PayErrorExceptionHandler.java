package com.linzx.integrate.thirdpay.api;

import com.linzx.integrate.thirdpay.exception.PayErrorException;

/**
 *   PayErrorExceptionHandler处理器
 */
public interface PayErrorExceptionHandler {

    /**
     * 异常统一处理器
     * @param e 支付异常
     */
     void handle(PayErrorException e);

}
