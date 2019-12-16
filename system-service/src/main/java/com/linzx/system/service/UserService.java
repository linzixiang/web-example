package com.linzx.system.service;

import com.linzx.system.domain.User;
import com.linzx.system.domain.UserRole;
import com.linzx.system.pojo.user.UserManage;

import java.util.List;

/**
 * 用户  服务层
 * 
 * @author linzixiang
 * @date 2019_05_122
 */
public interface UserService {
	/**
     * 查询用户 信息
     * 
     * @param userId 用户 ID
     * @return 用户 信息
     */
	User selectUserById(Long userId);
	
	/**
     * 查询用户 列表
     * 
     * @param user 用户 信息
     * @return 用户 集合
     */
	List<User> selectUserList(User user);
	
	/**
     * 新增用户 
     * 
     * @param user 用户 信息
     * @return 结果
     */
	Long insertUser(User user);
	
	/**
     * 修改用户 
     * 
     * @param user 用户 信息
     * @return 结果
     */
	int updateUser(User user);
		
	/**
     * 删除用户 信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteUserByIds(String ids);

	/**
	 * 用户管理列表查询
	 * @param user
	 * @return
	 */
	List<UserManage> queryUserManageList(User user);

	/**
	 * 新增
	 * @param user
	 * @param roleIds
	 */
	Long saveAddUserWithRole(User user, List<Long> roleIds);

	/**
	 * 修改保存
	 * @param user 用户对象
	 * @param addRoleList 新增的角色id集合
	 * @param delRoleList 删除的角色集合
	 * @return
	 */
	Long saveEditUserWithRole(User user, List<Long> addRoleList, List<UserRole> delRoleList);

	/**
	 * 检查帐号是否已存在
	 */
	boolean isLoginAccountExist(String loginAccount, Long excludeId);

	/**
	 * 检查邮箱是否已存在
	 */
	boolean isEmailExist(String email, Long excludeId);

	/**
	 * 检查手机号是否已存在
	 */
	boolean isPhoneNumberExist(String phoneNumber, Long excludeId);

	/**
	 * 获取用户关联的角色
	 */
	List<UserRole> findUserRoleList(Long userId);

}
