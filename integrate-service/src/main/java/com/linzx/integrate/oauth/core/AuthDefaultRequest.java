package com.linzx.integrate.oauth.core;

import com.linzx.integrate.oauth.bean.AuthCallback;
import com.linzx.integrate.oauth.bean.AuthToken;
import com.linzx.integrate.oauth.bean.AuthUserInfo;
import com.linzx.integrate.oauth.config.AuthConfig;
import com.linzx.integrate.oauth.config.AuthSource;

import java.util.UUID;


public abstract class AuthDefaultRequest implements AuthRequest {
    
    protected AuthSource source;

    protected AuthConfig config;

    public AuthDefaultRequest(AuthSource source, AuthConfig authConfig) {
        this.source = source;
        this.config = authConfig;
    }

    @Override
    public AuthUserInfo login(AuthCallback authCallback) {
        AuthToken authToken = this.getAccessToken(authCallback);
        AuthUserInfo user = this.getUserInfo(authToken);
        return user;
    }

    /**
     * 获取access token
     * @param authCallback
     * @return
     */
    protected abstract AuthToken getAccessToken(AuthCallback authCallback);

    /**
     * 使用access token换取用户信息
     * @param authToken
     * @return
     */
    protected abstract AuthUserInfo getUserInfo(AuthToken authToken);

    protected String getState() {
        return UUID.randomUUID().toString();
    }

}
