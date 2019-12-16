package com.linzx.web.system.request.user;

import java.util.List;

/**
 * 用户新增保存
 */
public class UserSaveAddDto {

    /** 部门ID */
    private Long deptId;
    /** 登录账号 */
    private String loginAccount;
    /** 用户昵称 */
    private String userNickname;
    /** 用户邮箱 */
    private String email;
    /** 密码 */
    private String password;
    /** 手机号码 */
    private String phoneNumber;
    /** 用户性别 （0男 1女 2未知） */
    private Integer sex;
    /** 是否停用 （0正常 1停用） */
    private Integer status;
    /** 备注 */
    private String remark;
    /** 角色id **/
    private List<Long> roleIds;

    public Long getDeptId() {
        return deptId;
    }
    public void setDeptId(Long deptId) {
        this.deptId = deptId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getSex() {
        return sex;
    }
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }
    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}
