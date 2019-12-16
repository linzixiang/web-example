package com.linzx.integrate.oauth.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthConfig {

    private String clientId;

    private String clientSecret;

    private String redirectUri;

    /** 支付宝公钥：当选择支付宝登录时，该值可用 **/
    private String alipayPublicKey;

}
