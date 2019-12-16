package com.linzx.integrate.thirdpay.bean;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferOrder {

    /**
     * 转账批次订单单号
     */
    private String batchNo;

    /**
     * 转账订单单号
     */
    private String outNo;

    /**
     * 收款方账户, 用户openid,卡号等等
     */
    private String  payeeAccount ;

    /**
     * 转账金额
     */
    private BigDecimal amount ;

    /**
     * 付款人名称
     */
    private String payerName;

    /**
     * 收款人名称
     */
    private String payeeName;
    /**
     * 收款人地址
     */
    private String payeeAddress;

    /**
     * 备注
     */
    private String remark;

    /**
     * 收款开户行
     */
    private Bank bank;

    /**
     *  收款开户行地址
     */
    private String payeeBankAddress;

    /**
     * 币种
     */
    private CurType curType;
    /**
     * 国家代码
     */
    private CountryCode countryCode;
    /**
     * 转账类型，收款方账户类型，比如支付宝账户或者银行卡
     */
    private TransferType transferType;

    /**
     * 操作者ip，根据支付平台所需进行设置
     */
    private String ip;

}
