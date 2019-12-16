package com.linzx.integrate.oauth.config;

import com.linzx.integrate.oauth.AuthException;
import com.linzx.integrate.oauth.enums.AuthResponseStatus;

public enum AuthSource {

    /** 阿里 **/
    ALIPAY {

        @Override
        public String authorize() {
            return "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm";
        }

        @Override
        public String accessToken() {
            return "https://openapi.alipay.com/gateway.do";
        }

        @Override
        public String userInfo() {
            return "https://openapi.alipay.com/gateway.do";
        }

    },
    /** 微信 **/
    WECHAT {

        @Override
        public String authorize() {
            return "https://open.weixin.qq.com/connect/qrconnect";
        }

        @Override
        public String accessToken() {
            return "https://api.weixin.qq.com/sns/oauth2/access_token";
        }

        @Override
        public String userInfo() {
            return "https://api.weixin.qq.com/sns/userinfo";
        }

        @Override
        public String refresh() {
            return "https://api.weixin.qq.com/sns/oauth2/refresh_token";
        }

    };

    /**
     * 授权的api
     * @return url
     */
    public abstract String authorize();

    /**
     * 获取accessToken的api
     * @return url
     */
    public abstract String accessToken();

    /**
     * 获取用户信息的api
     * @return url
     */
    public abstract String userInfo();

    /**
     * 取消授权的api
     * @return url
     */
    public String revoke() {
        throw new AuthException(AuthResponseStatus.UNSUPPORTED);
    }

    /**
     * 刷新授权的api
     * @return url
     */
    public String refresh() {
        throw new AuthException(AuthResponseStatus.UNSUPPORTED);
    }
    }
