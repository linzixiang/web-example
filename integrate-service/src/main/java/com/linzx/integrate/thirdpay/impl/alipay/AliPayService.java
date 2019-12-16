package com.linzx.integrate.thirdpay.impl.alipay;

import com.linzx.integrate.thirdpay.bean.*;
import com.linzx.integrate.thirdpay.api.BasePayService;
import com.linzx.integrate.thirdpay.api.Callback;
import com.linzx.integrate.thirdpay.http.HttpConfigStorage;
import com.linzx.utils.http.MethodType;

import java.util.Date;
import java.util.Map;

/**
 * 支付宝支付服务
 */
public class AliPayService extends BasePayService<AliPayConfigStorage> {

    /**
     * 正式测试环境
     */
    private static final String HTTPS_REQ_URL = "https://openapi.alipay.com/gateway.do";
    /**
     * 沙箱测试环境账号
     */
    private static final String DEV_REQ_URL = "https://openapi.alipaydev.com/gateway.do";

    public static final String SIGN = "sign";

    public static final String SUCCESS_CODE = "10000";

    public static final String CODE = "code";
    /**
     * 附加参数
     */
    public static final String PASSBACK_PARAMS = "passback_params";
    /**
     * 产品代码
     */
    public static final String PRODUCT_CODE = "product_code";
    /**
     * 返回地址
     */
    public static final String RETURN_URL = "return_url";

    /**
     * 请求内容
     */
    public static final String BIZ_CONTENT = "biz_content";

    public AliPayService(AliPayConfigStorage payConfigStorage) {
        super(payConfigStorage);
    }

    public AliPayService(AliPayConfigStorage payConfigStorage, HttpConfigStorage configStorage) {
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
