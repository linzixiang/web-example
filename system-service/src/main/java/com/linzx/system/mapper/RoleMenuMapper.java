package com.linzx.system.mapper;

import com.linzx.framework.mapper.BaseMapper;
import com.linzx.system.domain.RoleMenu;
import java.util.List;	

/**
 * 角色和菜单关联 角色1-N菜单 数据层
 * 
 * @author linzixiang
 * @date 2019_05_122
 */
public interface RoleMenuMapper extends BaseMapper<Long, RoleMenu> {

	/**
	 * 批量新增角色菜单
	 */
	public int insertRoleMenuBatch(List<RoleMenu> roleMenuList);
}