package com.linzx.framework.web.support;

/**
 * 所有请求的公共参数
 */
public class ReqHeader {

    private String clientIp;

    private String macAddress;

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
}
