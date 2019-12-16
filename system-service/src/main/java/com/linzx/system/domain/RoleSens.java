package com.linzx.system.domain;

import com.linzx.common.base.BaseEntity;

/**
 * 敏感字段 角色敏感字段表 sys_role_sens
 * 
 * @author linzixiang
 * @date 2019_08_216
 */
public class RoleSens extends BaseEntity<Long>{
	private static final long serialVersionUID = 1L;
	
	/** 主键 */
	private Long sensId;
	/** 角色id */
	private Long roleId;
	/** 唯一编码 */
	private String sensCode;

	public void setSensId(Long sensId) {
		this.sensId = sensId;
	}
	public Long getSensId() {
		return sensId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public Long getRoleId() {
		return roleId;
	}

	public void setSensCode(String sensCode) {
		this.sensCode = sensCode;
	}
	public String getSensCode() {
		return sensCode;
	}

	@Override
	public void setId(Long id) {
		this.setSensId(id);
	}
}
