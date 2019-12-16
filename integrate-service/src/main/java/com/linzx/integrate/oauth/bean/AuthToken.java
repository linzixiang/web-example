package com.linzx.integrate.oauth.bean;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 授权所需的token
 */
@Getter
@Setter
@Builder
public class AuthToken {

    private String accessToken;
    private int expireIn;
    private String refreshToken;
    private String uid;
    private String openId;
    private String accessCode;
    private String unionId;

}
