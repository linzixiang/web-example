package com.linzx.system.domain;

import com.linzx.common.base.BaseEntity;

/**
 * 用户和角色关联 用户N-1角色表 sys_user_role
 * 
 * @author linzixiang
 * @date 2019_05_122
 */
public class UserRole extends BaseEntity<Long>{
	private static final long serialVersionUID = 1L;
	
	/** 主键 */
	private Long userRoleId;
	/** 用户ID */
	private Long userId;
	/** 角色ID */
	private Long roleId;

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}
	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getUserId() {
		return userId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public Long getRoleId() {
		return roleId;
	}

	@Override
	public void setId(Long id) {
		this.setUserRoleId(id);
	}
}
