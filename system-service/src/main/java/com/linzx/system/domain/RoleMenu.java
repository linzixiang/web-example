package com.linzx.system.domain;

import com.linzx.common.base.BaseEntity;

/**
 * 角色和菜单关联 角色1-N菜单表 sys_role_menu
 * 
 * @author linzixiang
 * @date 2019_05_122
 */
public class RoleMenu extends BaseEntity<Long>{
	private static final long serialVersionUID = 1L;
	
	/** 主键 */
	private Long roleMenuId;
	/** 角色ID */
	private Long roleId;
	/** 菜单ID */
	private Long menuId;

	public void setRoleMenuId(Long roleMenuId) {
		this.roleMenuId = roleMenuId;
	}
	public Long getRoleMenuId() {
		return roleMenuId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public Long getRoleId() {
		return roleId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
	public Long getMenuId() {
		return menuId;
	}

	@Override
	public void setId(Long id) {
		this.setRoleMenuId(id);
	}
}
