package com.linzx.system.mapper;

import com.linzx.framework.mapper.BaseMapper;
import com.linzx.system.domain.Role;
import com.linzx.system.domain.User;
import com.linzx.system.domain.UserRole;
import java.util.List;	

/**
 * 用户和角色关联 用户N-1角色 数据层
 * 
 * @author linzixiang
 * @date 2019_05_122
 */
public interface UserRoleMapper extends BaseMapper<Long, UserRole> {

	/**
	 * 批量新增
	 * @param userRoleList
	 * @return
	 */
	public int insertUserRoleBatch(List<UserRole> userRoleList);

}