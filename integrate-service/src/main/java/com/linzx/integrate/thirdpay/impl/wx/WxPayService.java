package com.linzx.integrate.thirdpay.impl.wx;

import com.linzx.integrate.thirdpay.bean.*;
import com.linzx.integrate.thirdpay.api.BasePayService;
import com.linzx.integrate.thirdpay.api.Callback;
import com.linzx.integrate.thirdpay.http.HttpConfigStorage;
import com.linzx.utils.http.MethodType;

import java.util.Date;
import java.util.Map;

/**
 * 微信支付服务
 */
public class WxPayService extends BasePayService<WxPayConfigStorage> {

    public WxPayService(WxPayConfigStorage payConfigStorage) {
        super(payConfigStorage);
    }

    public WxPayService(WxPayConfigStorage payConfigStorage, HttpConfigStorage configStorage) {
        super(payConfigStorage, configStorage);
    }

    @Override
    public boolean verify(Map<String, Object> params) {
        return false;
    }

    @Override
    public boolean signVerify(Map<String, Object> params, String sign) {
        return false;
    }

    @Override
    public boolean verifySource(String id) {
        return false;
    }

    @Override
    public Map<String, Object> orderInfo(PayOrder order) {
        return null;
    }

    @Override
    public PayOutMessage getPayOutMessage(String code, String message) {
        return null;
    }

    @Override
    public PayOutMessage successPayOutMessage(PayMessage payMessage) {
        return null;
    }

    @Override
    public String buildRequest(Map<String, Object> orderInfo, MethodType method) {
        return null;
    }

    @Override
    public String getQrPay(PayOrder order) {
        return null;
    }

    @Override
    public Map<String, Object> microPay(PayOrder order) {
        return null;
    }

    @Override
    public Map<String, Object> query(String tradeNo, String outTradeNo) {
        return null;
    }

    @Override
    public Map<String, Object> close(String tradeNo, String outTradeNo) {
        return null;
    }

    @Override
    public Map<String, Object> refund(RefundOrder refundOrder) {
        return null;
    }

    @Override
    public Map<String, Object> refundquery(RefundOrder refundOrder) {
        return null;
    }

    @Override
    public Map<String, Object> downloadbill(Date billDate, String billType) {
        return null;
    }

    @Override
    public Map<String, Object> secondaryInterface(Object tradeNoOrBillDate, String outTradeNoBillType, TransactionType transactionType) {
        return null;
    }

    @Override
    public <T> T secondaryInterface(Object tradeNoOrBillDate, String outTradeNoBillType, TransactionType transactionType, Callback<T> callback) {
        return null;
    }

    @Override
    public Map<String, Object> transfer(TransferOrder order) {
        return null;
    }

    @Override
    public <T> T transfer(TransferOrder order, Callback<T> callback) {
        return null;
    }

    @Override
    public String getReqUrl(TransactionType transactionType) {
        return null;
    }
}
