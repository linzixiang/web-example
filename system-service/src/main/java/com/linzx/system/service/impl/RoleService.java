package com.linzx.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linzx.framework.web.BaseService;
import com.linzx.system.domain.RoleDept;
import com.linzx.system.domain.RoleMenu;
import com.linzx.system.domain.RoleSens;
import com.linzx.system.mapper.RoleDeptMapper;
import com.linzx.system.mapper.RoleMenuMapper;
import com.linzx.system.mapper.RoleSensMapper;
import com.linzx.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linzx.system.mapper.RoleMapper;
import com.linzx.system.domain.Role;
import com.linzx.system.service.IRoleService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色  服务层实现
 * 
 * @author linzixiang
 * @date 2019_05_122
 */
@Service("roleService")
public class RoleService extends BaseService<Long, Role, RoleMapper> implements IRoleService {

	@Autowired
	private RoleMapper roleMapper;

	@Autowired
	private RoleDeptMapper roleDeptMapper;

	@Autowired
	private RoleMenuMapper roleMenuMapper;

	@Autowired
	private RoleSensMapper roleSensMapper;


	/**
     * 新增角色 
     * 
     * @param role 角色 信息
     * @return 结果
     */
	@Override
	@Transactional
	public Long insertRoleWithMenu(Role role, Long[] menuIds) {
		super.insert(role);
		Long roleId = role.getRoleId();
		List<RoleMenu> roleMenuList = new ArrayList<>();
		for(int i = 0; i < menuIds.length; i++) {
			if (menuIds[i] == 0) {
				continue;
			}
			RoleMenu roleMenu = new RoleMenu();
			roleMenu.setRoleId(roleId);
			roleMenu.setMenuId(menuIds[i]);
			roleMenuList.add(roleMenu);
		}
		if (roleMenuList.size() > 0) {
			roleMenuMapper.insertRoleMenuBatch(roleMenuList);
		}
		return new Long(0);
	}

	@Override
	@Transactional
	public void updateRoleWithMenu(Role role) {
		List<RoleMenu> roleMenuNewList = role.getRoleMenuList();
		RoleMenu roleMenuSearchParam = new RoleMenu();
		roleMenuSearchParam.setRoleId(role.getRoleId());
		List<RoleMenu> roleMenuOldList = roleMenuMapper.selectList(roleMenuSearchParam);
		List<RoleMenu> roleMenuAddList = new ArrayList<>();// 需要新增的角色菜单
		Map<String, RoleMenu> roleMenuNewMap = new HashMap<>();
		for (RoleMenu roleMenu : roleMenuNewList) {
			if (roleMenu.getMenuId() == 0) {
				continue;
			}
			String roleMenuKey = roleMenu.getMenuId() + "-" + roleMenu.getRoleId();
			roleMenuNewMap.put(roleMenuKey, roleMenu);
			if (roleMenu.getRoleMenuId() == null) {
				roleMenuAddList.add(roleMenu);
			}
		}
		List<Long> roleMenuDelList = new ArrayList<>(); // 需要删除的角色菜单
		for (RoleMenu roleMenu : roleMenuOldList) {
			String roleMenuKey = roleMenu.getMenuId() + "-" + roleMenu.getRoleId();
			if (!roleMenuNewMap.containsKey(roleMenuKey)) {
				roleMenuDelList.add(roleMenu.getRoleMenuId());
			}
		}
		super.update(role);
		if (roleMenuDelList.size() > 0) {
			Long[] roleMenuIds = new Long[roleMenuDelList.size()];
			roleMenuMapper.deleteByIds(roleMenuDelList.toArray(roleMenuIds));
		}
		if (roleMenuAddList.size() > 0) {
			roleMenuMapper.insertRoleMenuBatch(roleMenuAddList);
		}
	}

	@Override
	public Boolean isRoleNameExist(String roleName, Long excludedId) {
		Role searchParam = new Role();
		searchParam.setRoleName(roleName);
		searchParam.getParams().put("excludedId", excludedId);
		Integer count = roleMapper.countByCondition(searchParam);
		return count > 0;
	}

	@Override
	public Boolean isRoleCodeExist(String roleCode, Long excludedId) {
		Role searchParam = new Role();
		searchParam.setRoleCode(roleCode);
		searchParam.getParams().put("excludedId", excludedId);
		Integer count = roleMapper.countByCondition(searchParam);
		return count > 0;
	}

	@Override
	public List<RoleDept> findRoleDeptByRoleId(Long roleId) {
		RoleDept roleDept = new RoleDept();
		roleDept.setRoleId(roleId);
		List<RoleDept> roleDeptList = roleDeptMapper.selectList(roleDept);
		return roleDeptList;
	}

	@Override
	public List<RoleSens> findRoleSensByRoleId(Long roleId) {
		if (roleId != null && roleId > 0) {
			RoleSens roleSens = new RoleSens();
			roleSens.setRoleId(roleId);
			List<RoleSens> roleSensList = roleSensMapper.selectList(roleSens);
			return roleSensList;
		}
		return new ArrayList<>();
	}

	@Override
	@Transactional
	public void updateRoleWithDataAccessScope(Role role,
											  List<RoleDept> roleDeptAdd, List<Long> roleDeptDel,
											  List<RoleSens> roleSensAdd, List<Long> roleSensDel) {
		super.update(role);
		if (CollectionUtils.isNotEmpty(roleDeptAdd)) {
			roleDeptMapper.insertRoleDeptBatch(roleDeptAdd);
		}
		if (CollectionUtils.isNotEmpty(roleDeptDel)) {
			Long[] roleDeptIdArr = Convert.toLongArrayIgnoreEmpty(roleDeptDel);
			roleDeptMapper.deleteByIds(roleDeptIdArr);
		}
		if (CollectionUtils.isNotEmpty(roleSensAdd)) {
			roleSensMapper.insertRoleSensBatch(roleSensAdd);
		}
		if (CollectionUtils.isNotEmpty(roleSensDel)) {
			Long[] sensIds = Convert.toLongArrayIgnoreEmpty(roleSensDel);
			roleSensMapper.deleteByIds(sensIds);
		}
	}

	@Override
	public RoleMapper setMapper() {
		return this.roleMapper;
	}
}
