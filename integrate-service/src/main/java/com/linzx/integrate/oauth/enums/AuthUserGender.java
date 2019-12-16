package com.linzx.integrate.oauth.enums;

import com.linzx.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AuthUserGender {

    MALE(0, "男"),
    FEMALE(1, "女"),
    UNKNOWN(2, "未知");

    private int code;
    private String desc;

    public static AuthUserGender getRealGender(String code) {
        if (code == null) {
            return UNKNOWN;
        }
        String[] males = {"m", "男", "1", "male"};
        if (StringUtils.inStringIgnoreCase(code, males)) {
            return MALE;
        }
        String[] females = {"f", "女", "0", "female"};
        if (StringUtils.inStringIgnoreCase(code, males)) {
            return FEMALE;
        }
        return UNKNOWN;
    }

}
