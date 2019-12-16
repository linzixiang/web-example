package com.linzx.system.domain;

import com.linzx.common.base.BaseEntity;
import com.linzx.framework.shiro.bean.RoleBean;

import java.util.List;

/**
 * 角色 表 sys_role
 * 
 * @author linzixiang
 * @date 2019_05_123
 */
public class Role extends BaseEntity<Long>{
	private static final long serialVersionUID = 1L;

	public static final String ROLE_CODE_ADMIN = RoleBean.ROLE_CODE_ADMIN;
	
	/** 角色ID */
	private Long roleId;
	/** 角色名称 */
	private String roleName;
	/** 角色代码 */
	private String roleCode;
	/** 数据权限 （1全部数据权限 2本部门数据权限 3本部门及其以下数据权限 4自定义数据权限） */
	private Integer dataScope;
	/** 菜单权限 （1全部菜单权限 2自定义权限） */
	private Integer menuScope;
	/** 显示顺序 （降序） */
	private Integer orderNum;
	/** 角色状态 （0正常 1停用） */
	private Integer status;
	/** 删除标志 （0正常 2删除） */
	private Integer delFlag;
	/*** 角色关联的菜单 **/
	private List<RoleMenu> roleMenuList;
	/*** 是否被选中 **/
	private Boolean onChecked;

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRoleName() {
		return roleName;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	public String getRoleCode() {
		return roleCode;
	}

	public void setDataScope(Integer dataScope) {
		this.dataScope = dataScope;
	}
	public Integer getDataScope() {
		return dataScope;
	}

	public void setMenuScope(Integer menuScope) {
		this.menuScope = menuScope;
	}
	public Integer getMenuScope() {
		return menuScope;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public Integer getOrderNum() {
		return orderNum;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getStatus() {
		return status;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	public Integer getDelFlag() {
		return delFlag;
	}

	public List<RoleMenu> getRoleMenuList() {
		return roleMenuList;
	}

	public void setRoleMenuList(List<RoleMenu> roleMenuList) {
		this.roleMenuList = roleMenuList;
	}

	public Boolean getOnChecked() {
		return onChecked;
	}

	public void setOnChecked(Boolean onChecked) {
		this.onChecked = onChecked;
	}

	@Override
	public void setId(Long id) {
		this.setRoleId(id);
	}
}
