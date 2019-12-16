package com.linzx.framework.web.support.serialize;

import com.linzx.utils.StringUtils;
import org.springframework.stereotype.Component;

@Component("sensitiveFormat")
public class FieldFormatUtils {

    /**
     * 格式化手机号
     * @param phoneNumber
     * @return
     */
    public String mobilePhone(String phoneNumber) {
       if (StringUtils.isPhoneNumber(phoneNumber)) {
           return phoneNumber.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
       }
        return phoneNumber;
    }

    public static void main(String[] args) {
        FieldFormatUtils fieldFormatUtils = new FieldFormatUtils();
        System.out.println(fieldFormatUtils.mobilePhone("15868144632"));
    }

}
