package com.linzx.integrate.thirdpay.bean.outbuilder;

import com.linzx.integrate.thirdpay.bean.MsgType;
import com.linzx.integrate.thirdpay.bean.PayOutMessage;

/**
 * @author egan
 *  <pre>
 *      email egzosn@gmail.com
 *      date 2016-6-1 11:40:30
 *   </pre>
 */
public class PayJsonOutMessage extends PayOutMessage {

    public PayJsonOutMessage() {
        this.msgType = MsgType.json.name();
    }

    @Override
    public String toMessage() {
        return getContent();
    }


}
