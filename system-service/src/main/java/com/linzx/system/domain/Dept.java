package com.linzx.system.domain;

import com.linzx.common.base.BaseEntity;

/**
 * 部门 表 sys_dept
 * 
 * @author linzixiang
 * @date 2019_06_180
 */
public class Dept extends BaseEntity<Long>{
	private static final long serialVersionUID = 1L;
	
	/** 部门id */
	private Long deptId;
	/** 部门名称 */
	private String deptName;
	/** 父部门id */
	private Long parentId;
	/** 部门层级 */
	private String ancestors;
	/** 显示顺序 (降序) */
	private Integer orderNum;
	/** 联系电话 */
	private String phone;
	/** 邮箱 */
	private String email;

	/** 父菜单名称 */
	private String parentIdShow;

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public Long getDeptId() {
		return deptId;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDeptName() {
		return deptName;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public Long getParentId() {
		return parentId;
	}

	public void setAncestors(String ancestors) {
		this.ancestors = ancestors;
	}
	public String getAncestors() {
		return ancestors;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public Integer getOrderNum() {
		return orderNum;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPhone() {
		return phone;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}

	public String getParentIdShow() {
		return parentIdShow;
	}

	public void setParentIdShow(String parentIdShow) {
		this.parentIdShow = parentIdShow;
	}

	@Override
	public void setId(Long id) {
		this.setDeptId(id);
	}
}
