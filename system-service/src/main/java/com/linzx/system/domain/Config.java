package com.linzx.system.domain;

import com.linzx.common.base.BaseEntity;

/**
 * 系统配置表 sys_config
 * 
 * @author linzixiang
 * @date 2019_07_203
 */
public class Config extends BaseEntity<Long>{
	private static final long serialVersionUID = 1L;
	
	/** 主键 */
	private Long configId;
	/** 参数名称 */
	private String configName;
	/** 参数键 */
	private String configCode;
	/** 参数值 */
	private String configValue;
	/** 系统内置 （0表示否，1表示是） */
	private Integer sysInner;

	public void setConfigId(Long configId) {
		this.configId = configId;
	}
	public Long getConfigId() {
		return configId;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}
	public String getConfigName() {
		return configName;
	}

	public void setConfigCode(String configCode) {
		this.configCode = configCode;
	}
	public String getConfigCode() {
		return configCode;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}
	public String getConfigValue() {
		return configValue;
	}

	public void setSysInner(Integer sysInner) {
		this.sysInner = sysInner;
	}
	public Integer getSysInner() {
		return sysInner;
	}

	@Override
	public void setId(Long id) {
		this.setConfigId(id);
	}
}
