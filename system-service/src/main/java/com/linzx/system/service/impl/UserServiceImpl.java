package com.linzx.system.service.impl;

import java.util.*;

import com.linzx.common.constant.Constants;
import com.linzx.framework.core.context.ContextManager;
import com.linzx.framework.utils.CacheUtils;
import com.linzx.framework.exception.UnknownAccountException;
import com.linzx.framework.exception.UserPasswordNotMatchException;
import com.linzx.framework.shiro.bean.UserLoginInfo;
import com.linzx.framework.shiro.bean.RoleBean;
import com.linzx.framework.shiro.service.LoginService;
import com.linzx.framework.shiro.utils.PasswordUtils;
import com.linzx.framework.web.BaseService;
import com.linzx.framework.web.controller.FileOperateController;
import com.linzx.system.domain.*;
import com.linzx.system.mapper.*;
import com.linzx.system.pojo.user.UserManage;
import com.linzx.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;
import com.linzx.system.service.UserService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户 服务层实现
 *
 * @author linzixiang
 * @date 2019_04_118
 */
@Service("userService")
public class UserServiceImpl extends BaseService<Long, User, UserMapper> implements UserService, LoginService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private DeptMapper deptMapper;

    /**
     * 查询用户 信息
     *
     * @param userId 用户 ID
     * @return 用户 信息
     */
    @Override
    public User selectUserById(java.lang.Long userId) {
        return userMapper.getById(userId);
    }

    /**
     * 查询用户 列表
     *
     * @param user 用户 信息
     * @return 用户 集合
     */
    @Override
    public List<User> selectUserList(User user) {
        return userMapper.selectList(user);
    }

    /**
     * 新增用户
     *
     * @param user 用户 信息
     * @return 结果
     */
    @Override
    public Long insertUser(User user) {
        super.initDefaultBeforeCreated(user);
        return userMapper.insert(user);
    }

    /**
     * 修改用户
     *
     * @param user 用户 信息
     * @return 结果
     */
    @Override
    public int updateUser(User user) {
        super.initDefaultBeforeUpdated(user);
        return userMapper.update(user);
    }

    /**
     * 删除用户 对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteUserByIds(String ids) {
        return userMapper.deleteByIds(Convert.toLongArray(ids));
    }

    @Override
    public List<UserManage> queryUserManageList(User user) {
        List<UserManage> userManageList = userMapper.findUserManage(user);
        return userManageList;
    }

    @Override
    @Transactional
    public Long saveAddUserWithRole(User user, List<Long> roleIds) {
        this.insertUser(user);
        addUserRole(user, roleIds);
        return user.getUserId();
    }

    @Override
    @Transactional
    public Long saveEditUserWithRole(User user, List<Long> roleIdsAddList, List<UserRole> delRoleList) {
        this.updateUser(user);
        addUserRole(user, roleIdsAddList);
        for(UserRole userRole : delRoleList) {
            userRoleMapper.deleteById(userRole.getUserRoleId());
        }
        return user.getUserId();
    }

    @Override
    public boolean isLoginAccountExist(String loginAccount, Long excludeId) {
        Map<String, Object> searchMap = MapUtils.genHashMap("loginAccount", loginAccount, "excludeId", excludeId);
        Long count = userMapper.countSimple(searchMap);
        return count > 0;
    }

    @Override
    public boolean isEmailExist(String email, Long excludeId) {
        Map<String, Object> searchMap = MapUtils.genHashMap("email", email, "excludeId", excludeId);
        Long count = userMapper.countSimple(searchMap);
        return count > 0;
    }

    @Override
    public boolean isPhoneNumberExist(String phoneNumber, Long excludeId) {
        Map<String, Object> searchMap = MapUtils.genHashMap("phoneNumber", phoneNumber, "excludeId", excludeId);
        Long count = userMapper.countSimple(searchMap);
        return count > 0;
    }

    @Override
    public List<UserRole> findUserRoleList(Long userId) {
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        List<UserRole> userRoles = userRoleMapper.selectList(userRole);
        return userRoles;
    }

    /**
     * 用户登录
     *
     * @param username
     * @param password
     */
    @Override
    public UserLoginInfo loginByAccountAndPwd(String username, String password) throws UnknownAccountException,
            UserPasswordNotMatchException {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("loginAccount", username);
        User user = userMapper.getUserByBizKey(paramMap);
        if (user == null) { // 用户账号不存在
            throw new UnknownAccountException();
        }
        // 如果没有传入密码，这跳过密码验证
        if (StringUtils.isNotEmpty(password) && !PasswordUtils.matches(user.getLoginAccount(), user.getSalt(),user.getPassword(), password)) {
            throw new UserPasswordNotMatchException();
        }
        /** 用户登录基础信息 */
        UserLoginInfo userLoginInfo = new UserLoginInfo();
        userLoginInfo.setUserId(user.getUserId());
        userLoginInfo.setLoginAccount(user.getLoginAccount());
        userLoginInfo.setUserNickname(user.getUserNickname());
        userLoginInfo.setPhoneNumber(user.getPhoneNumber());
        userLoginInfo.setEmail(user.getEmail());
        userLoginInfo.setUserType(user.getUserType());
        if (StringUtils.isNotEmpty(user.getAvatar())) {
            userLoginInfo.setAvatarUrl(StrFormatter.format(FileOperateController.IMAGE_DOWNLOAD_URL, user.getAvatar()));
        } else {
            userLoginInfo.setAvatarUrl("");
        }
        Dept dept = deptMapper.getById(user.getDeptId());
        /** 用户部门信息 */
        userLoginInfo.setDeptId(user.getDeptId());
        userLoginInfo.setDeptAncestors(dept.getAncestors());
        /** 用户角色信息 */
        List<Role> roleList = roleMapper.selectRoleListByUserId(user.getUserId());
        List<RoleBean> roleBeanList = new ArrayList<>();
        List<Long> roleIds = new ArrayList<>();
        Integer dataScope = Integer.MAX_VALUE, menuScope = Integer.MAX_VALUE;
        for (Role  role: roleList) {
            RoleBean roleBean = new RoleBean();
            roleBean.setRoleCode(role.getRoleCode());
            roleBean.setRoleId(role.getRoleId());
            roleBean.setRoleName(role.getRoleName());
            roleBeanList.add(roleBean);
            dataScope = Integer.min(dataScope, role.getDataScope());
            menuScope = Integer.min(menuScope, role.getMenuScope());
            roleIds.add(role.getRoleId());
        }
        userLoginInfo.setDataScope(dataScope);
        userLoginInfo.setMenuScope(menuScope);
        userLoginInfo.setRoleList(roleBeanList);

        /**
         * 用户菜单权限信息
         */
        if (Constants.ROLE_MENU_SCOPE_CUSTOM == menuScope) {
            Map<String, Object> param = new HashMap<>();
            param.put("roleIds", roleIds);
            List<String> menuType = new ArrayList<>();
            menuType.add(Menu.CATALOGUE);
            menuType.add(Menu.BUTTON);
            menuType.add(Menu.MENU);
            param.put("menuTypes", menuType);
            Set<String> menuPermsSet = menuMapper.selectNormalPermsByRoleIds(param);
            userLoginInfo.setMenuPermsSet(menuPermsSet);
        }
        return userLoginInfo;
    }

    @Override
    public UserLoginInfo loginByAccount(String account) throws UnknownAccountException {
        return this.loginByAccountAndPwd(account, null);
    }

    private void addUserRole(User user, List<Long> roleIdList) {
        if (CollectionUtils.isNotEmpty(roleIdList)) {
            List<UserRole> userRoleList = new ArrayList<>();
            for(Long roleId : roleIdList) {
                UserRole userRole = new UserRole();
                userRole.setUserId(user.getUserId());
                userRole.setRoleId(roleId);
                userRoleList.add(userRole);
            }
            userRoleMapper.insertUserRoleBatch(userRoleList);
        }
    }

    @Override
    public UserMapper setMapper() {
        return this.userMapper;
    }
}
