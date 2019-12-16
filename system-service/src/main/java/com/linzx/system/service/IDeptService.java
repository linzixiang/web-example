package com.linzx.system.service;

import com.linzx.system.domain.Dept;
import com.linzx.system.dto.DeptTreeNode;

import java.util.List;

/**
 * 部门  服务层
 * 
 * @author linzixiang
 * @date 2019_06_180
 */
public interface IDeptService {

	/**
     * 新增部门
     *
     * @param dept 部门 信息
     * @return 结果
     */
	public Long insertDept(Dept dept);

	/**
     * 修改部门
     *
     * @param dept 部门 信息
     * @return 结果
     */
	public int updateDept(Dept dept);

	/**
	 * 获取最顶层的部门
	 * @return
	 */
	public Dept getRootDept();

	/**
	 * 获取部门树
	 * @return
	 */
	public DeptTreeNode findDeptTree();

	/**
	 * 获取部门树关联角色
	 */
	DeptTreeNode findDeptRelateRoleTree(Long roleId);
	
}
