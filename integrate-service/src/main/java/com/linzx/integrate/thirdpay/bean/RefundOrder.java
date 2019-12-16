package com.linzx.integrate.thirdpay.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 退款订单信息
 */
public class RefundOrder {

    public RefundOrder() {
    }

    public RefundOrder(String refundNo, String tradeNo, BigDecimal refundAmount) {
        this.refundNo = refundNo;
        this.tradeNo = tradeNo;
        this.refundAmount = refundAmount;
    }

    public RefundOrder(String tradeNo, String outTradeNo, BigDecimal refundAmount, BigDecimal totalAmount) {
        this.tradeNo = tradeNo;
        this.outTradeNo = outTradeNo;
        this.refundAmount = refundAmount;
        this.totalAmount = totalAmount;
    }

    public RefundOrder(String refundNo, String tradeNo, String outTradeNo, BigDecimal refundAmount, BigDecimal totalAmount) {
        this.refundNo = refundNo;
        this.tradeNo = tradeNo;
        this.outTradeNo = outTradeNo;
        this.refundAmount = refundAmount;
        this.totalAmount = totalAmount;
    }

    /**
     * 退款单号，每次进行退款的单号，此处唯一
     */
    private String refundNo;
    /**
     * 支付平台订单号,交易号
     */
    private String tradeNo;
    /**
     * 商户单号
     */
    private String outTradeNo;
    /**
     * 退款金额
     */
    private BigDecimal refundAmount;
    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 退款交易日期
     */
    private Date orderDate;

    /**
     * 货币
     */
    private CurType curType;
    /**
     * 退款说明
     */
    private String description;

}
