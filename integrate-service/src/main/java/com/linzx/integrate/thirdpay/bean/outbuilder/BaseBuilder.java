package com.linzx.integrate.thirdpay.bean.outbuilder;

import com.linzx.integrate.thirdpay.bean.PayOutMessage;

public abstract class BaseBuilder<BuilderType, ValueType> {


    public abstract ValueType build();

    public void setCommon(PayOutMessage m) {

    }

}