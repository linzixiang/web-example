package com.linzx.system.mapper;

import com.linzx.framework.mapper.BaseMapper;
import com.linzx.framework.mapper.SimpleCountMapper;
import com.linzx.system.domain.User;
import com.linzx.system.pojo.user.UserManage;

import java.util.List;
import java.util.Map;

/**
 * 用户 数据层
 * 
 * @author linzixiang
 * @date 2019_04_118
 */
public interface UserMapper extends SimpleCountMapper, BaseMapper<Long, User> {

	/**
	 * 根据业务唯一键查询用户
	 * @param paramMap 参数
	 * @return 结果
	 */
	public User getUserByBizKey(Map<String, Object> paramMap);

	/**
	 * 用户部门信息列表查询
	 * @param user
	 * @return
	 */
	public List<UserManage> findUserManage(User user);
	
}