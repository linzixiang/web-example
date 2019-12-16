package com.linzx.integrate.thirdpay.bean.outbuilder;

import com.linzx.integrate.thirdpay.bean.PayOutMessage;

public class XmlBuilder extends BaseBuilder<XmlBuilder, PayOutMessage> {

    private String content;
    private String code;

    public XmlBuilder content(String content) {
        this.content = content;
        return this;
    }

    public XmlBuilder code(String code) {
        this.code = code;
        return this;
    }


    @Override
    public PayOutMessage build() {
        PayXmlOutMessage message = new PayXmlOutMessage();
        setCommon(message);
        message.setContent(content);
        message.setCode(code);
        return message;
    }
}
