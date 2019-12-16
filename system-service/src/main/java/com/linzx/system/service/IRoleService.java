package com.linzx.system.service;

import com.linzx.system.domain.Role;
import com.linzx.system.domain.RoleDept;
import com.linzx.system.domain.RoleSens;

import java.util.List;

/**
 * 角色  服务层
 * 
 * @author linzixiang
 * @date 2019_05_122
 */
public interface IRoleService {

	/**
	 * 新增角色以及关联的菜单
	 * @param role
	 * @return
	 */
	public Long insertRoleWithMenu(Role role, Long[] menuIds);

	/**
	 * 更新角色，以及关联的菜单
	 */
	void updateRoleWithMenu(Role role);

	/**
	 * 检查名称是否重复
	 * @param roleName 角色名称
	 * @param excludedId 排除的id
	 * @return
	 */
	public Boolean isRoleNameExist(String roleName, Long excludedId);

	/**
	 * 检查名称是否重复
	 * @param roleCode 角色编码
	 * @param excludedId 排除的id
	 * @return
	 */
	public Boolean isRoleCodeExist(String roleCode, Long excludedId);

	/**
	 * 获取角色关联的部门
	 */
	List<RoleDept> findRoleDeptByRoleId(Long roleId);

	/**
	 * 获取角色的敏感字段
	 */
	List<RoleSens> findRoleSensByRoleId(Long roleId);

	/**
	 * 更新角色，同时更新角色关联的数据权限（比如部门，敏感字段）
	 */
	void updateRoleWithDataAccessScope(Role role, List<RoleDept> roleDeptAdd, List<Long> roleDeptDel, List<RoleSens> roleSensAdd, List<Long> roleSensDel);
}
