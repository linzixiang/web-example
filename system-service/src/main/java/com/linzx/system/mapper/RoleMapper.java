package com.linzx.system.mapper;

import com.linzx.framework.mapper.BaseMapper;
import com.linzx.system.domain.Role;
import java.util.List;
import java.util.Map;

/**
 * 角色  数据层
 * 
 * @author linzixiang
 * @date 2019_05_122
 */
public interface RoleMapper extends BaseMapper<Long, Role> {

	/**
	 * 查询用户的角色
	 */
	public List<Role> selectRoleListByUserId(Long userId);

	/**
	 * 查询符合条件的数量
	 */
	public Integer countByCondition(Role role);
	
}