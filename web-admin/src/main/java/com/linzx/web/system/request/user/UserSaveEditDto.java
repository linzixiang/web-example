package com.linzx.web.system.request.user;

import com.linzx.common.constant.RegexpConstants;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;

/**
 * 用户修改保存
 */
public class UserSaveEditDto {

    @NotNull(message = "userId不能为空")
    private Long userId;
    /** 部门ID */
    private Long deptId;
    /** 用户昵称 */
    @NotBlank(message = "用户昵称不允许为空")
    private String userNickname;
    /** 用户邮箱 */
    @NotBlank(message = "用户邮箱不允许为空")
    @Email(regexp = RegexpConstants.email , message = "用户邮箱格式不合法")
    private String email;
    /** 手机号码 */
    @NotBlank(message = "手机号码不允许为空")
    @Pattern(regexp = RegexpConstants.phoneNumber, message = "手机号码格式不合法")
    private String phoneNumber;
    /** 用户性别 （0男 1女 2未知） */
    private Integer sex;
    /** 是否停用 （0正常 1停用） */
    private Integer status;
    /** 备注 */
    private String remark;
    /** 角色id **/
    private Set<String> roleIds;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Set<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<String> roleIds) {
        this.roleIds = roleIds;
    }
}
