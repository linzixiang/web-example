package com.linzx.framework.shiro.bean;

import java.io.Serializable;

/**
 * 用户角色信息
 */
public class RoleBean implements Serializable {

    public static final String ROLE_CODE_ADMIN = "admin";

    /** 角色ID */
    private Long roleId;
    /** 角色名称 */
    private String roleName;
    /** 角色代码 */
    private String roleCode;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

}
