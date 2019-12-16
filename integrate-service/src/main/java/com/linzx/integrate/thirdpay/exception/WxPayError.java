package com.linzx.integrate.thirdpay.exception;

/**
 * 微信支付异常
 * @author: egan
 * <pre>
 *
 * email egzosn@gmail.com
 * date 2017/3/6 17:31
 * </pre>
 */
public class WxPayError implements PayError {

    private String errorCode;

    private String errorMsg;
    private String content;


    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    public WxPayError(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public WxPayError(String errorCode, String errorMsg, String content) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.content = content;
    }

    @Override
    public String getString() {
            return "支付错误: errcode=" + errorCode + ", errmsg=" + errorMsg + (null == content ? "" : "\n content:" + content);
    }
}
