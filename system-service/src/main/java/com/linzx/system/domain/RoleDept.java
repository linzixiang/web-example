package com.linzx.system.domain;

import com.linzx.common.base.BaseEntity;

/**
 * 角色和部门关联 表 sys_role_dept
 * 
 * @author linzixiang
 * @date 2019_05_122
 */
public class RoleDept extends BaseEntity<Long>{
	private static final long serialVersionUID = 1L;
	
	/** 主键 */
	private Long roleDeptId;
	/** 角色ID */
	private Long roleId;
	/** 部门ID */
	private Long deptId;

	public void setRoleDeptId(Long roleDeptId) {
		this.roleDeptId = roleDeptId;
	}
	public Long getRoleDeptId() {
		return roleDeptId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public Long getRoleId() {
		return roleId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public Long getDeptId() {
		return deptId;
	}

	@Override
	public void setId(Long id) {
		this.setRoleId(id);
	}
}
