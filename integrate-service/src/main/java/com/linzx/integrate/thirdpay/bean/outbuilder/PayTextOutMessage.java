package com.linzx.integrate.thirdpay.bean.outbuilder;

import com.linzx.integrate.thirdpay.bean.MsgType;
import com.linzx.integrate.thirdpay.bean.PayOutMessage;

public class PayTextOutMessage extends PayOutMessage {

    public PayTextOutMessage() {
        this.msgType = MsgType.text.name();
    }

    @Override
    public String toMessage() {
        return getContent();
    }
}
