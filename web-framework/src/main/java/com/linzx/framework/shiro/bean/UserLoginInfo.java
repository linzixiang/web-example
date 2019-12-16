package com.linzx.framework.shiro.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 数据库登录后返回信息
 */
public class UserLoginInfo implements Serializable {

    /** 全部数据权限 **/
    public static final int DATA_SCOPE_ALL = 1; // 所有数据权限
    public static final int DATA_SCOPE_SELF_DEPT = 2; // 本部门数据权限
    public static final int DATA_SCOPE_SELF_CHILDREN_DEPT = 3; // 本部门及其以及部门
    public static final int DATA_SCOPE_CUSTOMIZE_DEPT = 4; // 自定义部门数据权限

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 部门id
     */
    private Long deptId;
    /**
     * 当前部门的层级id
     */
    private String deptAncestors;
    /**
     * 帐号
     */
    private String loginAccount;
    /**
     * 头像地址
     */
    private String avatarUrl;
    /**
     * 用户昵称
     */
    private String userNickname;
    /**
     * 手机号码
     */
    private String phoneNumber;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 用户类型 （00系统用户）
     */
    private String userType;

    /** 数据权限范围 */
    private Integer dataScope;

    /** 菜单权限范围 */
    private Integer menuScope;

    /** 用户角色信息 */
    private List<RoleBean> roleList;

    /** 用户菜单权限*/
    private Set<String> menuPermsSet;

    /** 敏感字段集合，不在集合范围内表示为敏感字段 **/
    private Set<String> sensProp;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<RoleBean> getRoleList() {
        if (this.roleList == null) {
            this.roleList = new ArrayList<>();
        }
        return roleList;
    }

    public void setRoleList(List<RoleBean> roleList) {
        this.roleList = roleList;
    }

    public Integer getDataScope() {
        return dataScope;
    }

    public void setDataScope(Integer dataScope) {
        this.dataScope = dataScope;
    }

    public Integer getMenuScope() {
        return menuScope;
    }

    public void setMenuScope(Integer menuScope) {
        this.menuScope = menuScope;
    }

    public Set<String> getMenuPermsSet() {
        return menuPermsSet;
    }

    public void setMenuPermsSet(Set<String> menuPermsSet) {
        this.menuPermsSet = menuPermsSet;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Set<String> getSensProp() {
        if (sensProp == null) {
            this.sensProp = new HashSet<>();
        }
        return sensProp;
    }

    public void setSensProp(Set<String> sensProp) {
        this.sensProp = sensProp;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptAncestors() {
        return deptAncestors;
    }

    public void setDeptAncestors(String deptAncestors) {
        this.deptAncestors = deptAncestors;
    }

    @Override
    public String toString() {
        return this.getUserId().toString();
    }

    /**
     * 是否是管理员
     */
    public boolean isAdmin() {
        for(RoleBean roleBean : this.getRoleList()) {
            if (RoleBean.ROLE_CODE_ADMIN.equals(roleBean.getRoleCode())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否为敏感字段
     */
    public boolean isSensProp(String sensPop) {
        if (this.getSensProp().size() == 0) {
            // 敏感字段集合为空，则认为不是敏感字段
            return false;
        }
        if (!this.getSensProp().contains(sensPop)) {
            // 集合不包含字段，则认为是敏感字段
            return true;
        }
        return false;
    }
}
