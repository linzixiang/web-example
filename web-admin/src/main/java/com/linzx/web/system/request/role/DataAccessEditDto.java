package com.linzx.web.system.request.role;

import com.linzx.system.domain.RoleDept;

import java.util.List;

/**
 * 角色数据权限保存
 */
public class DataAccessEditDto {

    private Long roleId;

    private Integer dataScope;

    /** 自定义权限选中的部门id **/
    private String deptIds;

    /** 勾选的敏感字段 **/
    private String sensCodes;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Integer getDataScope() {
        return dataScope;
    }

    public void setDataScope(Integer dataScope) {
        this.dataScope = dataScope;
    }

    public String getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(String deptIds) {
        this.deptIds = deptIds;
    }

    public String getSensCodes() {
        return sensCodes;
    }

    public void setSensCodes(String sensCodes) {
        this.sensCodes = sensCodes;
    }
}
