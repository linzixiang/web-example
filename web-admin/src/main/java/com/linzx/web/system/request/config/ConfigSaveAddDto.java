package com.linzx.web.system.request.config;

public class ConfigSaveAddDto {

    /** 参数名称 */
    private String configName;
    /** 参数键 */
    private String configCode;
    /** 参数值 */
    private String configValue;
    /** 备注 **/
    private String remark;

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigCode() {
        return configCode;
    }

    public void setConfigCode(String configCode) {
        this.configCode = configCode;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
