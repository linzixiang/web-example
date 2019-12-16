package com.linzx.integrate.oauth.core.impl;


import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.linzx.integrate.oauth.AuthException;
import com.linzx.integrate.oauth.bean.AuthCallback;
import com.linzx.integrate.oauth.bean.AuthToken;
import com.linzx.integrate.oauth.bean.AuthUserInfo;
import com.linzx.integrate.oauth.config.AuthConfig;
import com.linzx.integrate.oauth.config.AuthSource;
import com.linzx.integrate.oauth.core.AuthDefaultRequest;
import com.linzx.integrate.oauth.enums.AuthUserGender;
import com.linzx.utils.StringUtils;
import com.linzx.utils.UrlUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 支付宝登录
 */
public class AuthAlipayRequest extends AuthDefaultRequest {

    private AlipayClient alipayClient;

    @Getter
    @Setter
    public static class Config {
        private String appid;
        private String secret;
        private String redirectUri;
        private String publicKey;
    }

    public AuthAlipayRequest(Config authConfig) {
        super(AuthSource.ALIPAY, toAuthConfig(authConfig));
        this.alipayClient = new DefaultAlipayClient(AuthSource.ALIPAY.accessToken(), config.getClientId(),
                config.getClientSecret(), "json", "UTF-8",
                config.getAlipayPublicKey(), "RSA2");
    }

    private static AuthConfig toAuthConfig(Config weChatConfig) {
        AuthConfig authConfig = AuthConfig.builder()
                .clientId(weChatConfig.appid)
                .clientSecret(weChatConfig.secret)
                .redirectUri(weChatConfig.redirectUri)
                .alipayPublicKey(weChatConfig.publicKey)
                .build();
        return authConfig;
    }

    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setGrantType("authorization_code");
        request.setCode(authCallback.getAuthCode());
        AlipaySystemOauthTokenResponse response = null;
        try {
            response = this.alipayClient.execute(request);
        } catch (Exception e) {
            throw new AuthException(e);
        }
        if (!response.isSuccess()) {
            throw new AuthException(response.getSubMsg());
        }
        return AuthToken.builder()
                .accessToken(response.getAccessToken())
                .uid(response.getUserId())
                .expireIn(Integer.parseInt(response.getExpiresIn()))
                .refreshToken(response.getRefreshToken())
                .build();
    }

    @Override
    protected AuthUserInfo getUserInfo(AuthToken authToken) {
        String accessToken = authToken.getAccessToken();
        AlipayUserInfoShareRequest request = new AlipayUserInfoShareRequest();
        AlipayUserInfoShareResponse response = null;
        try {
            response = this.alipayClient.execute(request, accessToken);
        } catch (AlipayApiException e) {
            throw new AuthException(e.getErrMsg(), e);
        }
        if (!response.isSuccess()) {
            throw new AuthException(response.getSubMsg());
        }
        String province = response.getProvince();
        String city = response.getCity();
        String location = "";
        if (StringUtils.isNotEmpty(province)) {
            location = province;
            if (StringUtils.isNotEmpty(city)) {
                location = location + "-" + city;
            }
        }
        return AuthUserInfo.builder()
                .uuid(response.getUserId())
                .username(StringUtils.isEmpty(response.getUserName()) ? response.getNickName() : response.getUserName())
                .nickname(response.getNickName())
                .avatar(response.getAvatar())
                .location(location)
                .gender(AuthUserGender.getRealGender(response.getGender()))
                .token(authToken)
                .source(source)
                .build();
    }

    @Override
    public String authorize(String state) {
        Map<String, String> param = new LinkedHashMap<>();
        param.put("app_id", config.getClientId());
        param.put("scope", "auth_user");
        param.put("redirect_uri", config.getRedirectUri());
        param.put("state", getState());
        return UrlUtils.appendParams(source.authorize(), param);
    }
}
