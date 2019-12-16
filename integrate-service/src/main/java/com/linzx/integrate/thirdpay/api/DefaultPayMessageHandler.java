package com.linzx.integrate.thirdpay.api;

import com.alibaba.fastjson.JSON;
import com.linzx.integrate.thirdpay.bean.PayMessage;
import com.linzx.integrate.thirdpay.bean.PayOutMessage;
import com.linzx.integrate.thirdpay.exception.PayErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 默认处理支付回调消息的处理器接口
 *
 * 主要用来处理支付相关的业务
 */
public class DefaultPayMessageHandler implements PayMessageHandler<PayMessage, PayService>  {

    protected final Logger logger = LoggerFactory.getLogger(DefaultPayMessageHandler.class);

    @Override
    public PayOutMessage handle(PayMessage payMessage, Map<String, Object> context, PayService payService) throws PayErrorException {
        if (logger.isInfoEnabled()) {
            logger.info("回调支付消息处理器，回调消息：" + JSON.toJSONString(payMessage));
        }
        return payService.successPayOutMessage(payMessage);
    }

}
