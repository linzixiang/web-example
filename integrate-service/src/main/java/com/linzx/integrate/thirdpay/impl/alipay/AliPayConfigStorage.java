package com.linzx.integrate.thirdpay.impl.alipay;

import com.linzx.integrate.thirdpay.api.BasePayConfigStorage;
import lombok.Setter;

@Setter
public class AliPayConfigStorage extends BasePayConfigStorage {

    /**
     * 商户应用id
     */
    private String appid;
    /**
     * 商户签约拿到的pid,partner_id的简称，合作伙伴身份等同于 partner
     */
    private String pid;

    /**
     * 商户收款账号
     */
    private String seller;

    @Override
    public String getAppid() {
        return appid;
    }

    @Override
    public String getPid() {
        return pid;
    }

    @Override
    public String getSeller() {
        return seller;
    }

}
