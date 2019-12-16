package com.linzx.integrate.oauth.core.impl;

import com.alibaba.fastjson.JSONObject;
import com.linzx.framework.utils.HttpUtils;
import com.linzx.integrate.oauth.bean.AuthCallback;
import com.linzx.integrate.oauth.bean.AuthToken;
import com.linzx.integrate.oauth.bean.AuthUserInfo;
import com.linzx.integrate.oauth.config.AuthConfig;
import com.linzx.integrate.oauth.config.AuthSource;
import com.linzx.integrate.oauth.core.AuthDefaultRequest;
import com.linzx.utils.UrlUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 微信登录
 */
public class AuthWeChatRequest extends AuthDefaultRequest {

    @Setter
    @Getter
    public static class Config {
        private String appid;
        private String secret;
        private String redirectUri;
    }

    public AuthWeChatRequest(Config weChatConfig) {
        super(AuthSource.WECHAT, toAuthConfig(weChatConfig));
    }

    private static AuthConfig toAuthConfig(Config weChatConfig) {
        AuthConfig authConfig = AuthConfig.builder()
                .clientId(weChatConfig.appid)
                .clientSecret(weChatConfig.secret)
                .redirectUri(weChatConfig.redirectUri)
                .build();
        return authConfig;
    }

    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
        Map<String, String> param = new LinkedHashMap<>();
        param.put("code", authCallback.getCode());
        param.put("openid", config.getClientId());
        param.put("secret", config.getClientSecret());
        param.put("grant_type", "authorization_code");
        JSONObject response = HttpUtils.getRetJson(source.accessToken(), param, null);
        return AuthToken.builder()
                .accessToken(response.getString("access_token"))
                .refreshToken(response.getString("refresh_token"))
                .expireIn(response.getIntValue("expires_in"))
                .openId(response.getString("openid"))
                .build();
    }

    @Override
    protected AuthUserInfo getUserInfo(AuthToken authToken) {
        String openId = authToken.getOpenId();
        Map<String, String> param = new LinkedHashMap<>();
        param.put("access_token", authToken.getAccessToken());
        param.put("openid", openId);
        param.put("lang", "zh_CN");
        JSONObject response = HttpUtils.getRetJson(source.userInfo(), param, null);
        return AuthUserInfo.builder().avatar(response.getString("headimgurl")).build();
    }

    /**
     * 第三方使用网站应用授权登录
     * @param state state 验证授权流程的参数，可以防止csrf
     * @return
     */
    @Override
    public String authorize(String state) {
        Map<String, String> param = new LinkedHashMap<>();
        param.put("response_type", "code");
        param.put("appid", config.getClientId());
        param.put("redirect_uri", config.getRedirectUri());
        param.put("scope", "snsapi_login");
        param.put("state", getState());
        return UrlUtils.appendParams(source.authorize(), param);
    }

    @Override
    public void refresh(AuthToken oldAuthToken) {
        Map<String, String> param = new LinkedHashMap();
        param.put("appid", config.getClientId());
        param.put("refresh_token", oldAuthToken.getRefreshToken());
        param.put("grant_type", "refresh_token");
        JSONObject response = HttpUtils.getRetJson(source.refresh(), param, null);
        if (response.containsKey("errcode")) {
            // 刷新失败
        }
    }
}
