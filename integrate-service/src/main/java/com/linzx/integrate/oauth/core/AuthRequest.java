package com.linzx.integrate.oauth.core;

import com.linzx.integrate.oauth.bean.AuthCallback;
import com.linzx.integrate.oauth.bean.AuthToken;
import com.linzx.integrate.oauth.bean.AuthUserInfo;

public interface AuthRequest {

    /**
     * 返回带{@code state}参数的授权url，授权回调时会带上这个{@code state}
     * @param state state 验证授权流程的参数，可以防止csrf
     * @return 返回授权地址
     */
    String authorize(String state);

    /**
     * 第三方登录
     * @param authCallback 用于接收回调参数的实体
     * @return 返回登录成功后的用户信息
     */
    AuthUserInfo login(AuthCallback authCallback);

    /**
     * 撤销授权
     * @param authToken 登录成功后返回的Token信息
     * @return AuthResponse
     */
    default void revoke(AuthToken authToken) {

    };

    /**
     * 刷新access token （续期）
     * @param authToken 登录成功后返回的Token信息
     * @return AuthResponse
     */
    default void refresh(AuthToken authToken){

    };

}
