package com.linzx.system.domain;

import com.linzx.common.base.BaseEntity;
import com.linzx.common.constant.Constants;

/**
 * 用户 表 sys_user
 * 
 * @author linzixiang
 * @date 2019_05_122
 */
public class User extends BaseEntity<Long>{
	private static final long serialVersionUID = 1L;

	/** 用户ID */
	private Long userId;
	/** 部门ID */
	private Long deptId;
	/** 登录账号 */
	private String loginAccount;
	/** 用户昵称 */
	private String userNickname;
	/** 用户类型 （00 系统用户不允许删除） */
	private String userType;
	/** 用户邮箱 */
	private String email;
	/** 手机号码 */
	private String phoneNumber;
	/** 用户性别 （0男 1女 2未知） */
	private Integer sex;
	/** 头像 */
	private String avatar;
	/** 密码 */
	private String password;
	/** 盐加密 */
	private String salt;
	/** 最后登陆IP */
	private String loginIp;
	/** 最后登陆时间 */
	private java.util.Date lastLoginTime;
	/** 是否停用 （0正常 1停用） */
	private Integer status;
	/** 删除标志 （0代表存在 2代表删除） */
	private Integer delFlag;
	/** 乐观锁 */
	private Integer revision;
	/** 创建人 */
	private Long createdBy;
	/** 创建时间 */
	private java.util.Date createdTime;
	/** 更新人 */
	private Long updatedBy;
	/** 更新时间 */
	private java.util.Date updatedTime;

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getUserId() {
		return userId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public Long getDeptId() {
		return deptId;
	}

	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}
	public String getLoginAccount() {
		return loginAccount;
	}

	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}
	public String getUserNickname() {
		return userNickname;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getUserType() {
		return userType;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Integer getSex() {
		return sex;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getAvatar() {
		return avatar;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getSalt() {
		return salt;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	public String getLoginIp() {
		return loginIp;
	}

	public void setLastLoginTime(java.util.Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public java.util.Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getStatus() {
		return status;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	public Integer getDelFlag() {
		return delFlag;
	}

	public void setRevision(Integer revision) {
		this.revision = revision;
	}
	public Integer getRevision() {
		return revision;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedTime(java.util.Date createdTime) {
		this.createdTime = createdTime;
	}
	public java.util.Date getCreatedTime() {
		return createdTime;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedTime(java.util.Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	public java.util.Date getUpdatedTime() {
		return updatedTime;
	}

	@Override
	public void setId(Long id) {
		this.setUserId(id);
	}
}
