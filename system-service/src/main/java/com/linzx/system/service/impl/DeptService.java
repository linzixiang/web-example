package com.linzx.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.linzx.framework.exception.BizCommonException;
import com.linzx.framework.web.BaseService;
import com.linzx.system.domain.RoleDept;
import com.linzx.system.dto.DeptTreeNode;
import com.linzx.system.mapper.RoleDeptMapper;
import com.linzx.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linzx.system.mapper.DeptMapper;
import com.linzx.system.domain.Dept;
import com.linzx.system.service.IDeptService;

/**
 * 部门  服务层实现
 * 
 * @author linzixiang
 * @date 2019_06_180
 */
@Service("deptService")
public class DeptService extends BaseService<Long, Dept, DeptMapper> implements IDeptService {

	@Autowired
	private DeptMapper deptMapper;

	@Autowired
	private RoleDeptMapper roleDeptMapper;
	
    /**
     * 新增部门 
     * 
     * @param dept 部门 信息
     * @return 结果
     */
	@Override
	public Long insertDept(Dept dept) {
		if (dept.getParentId() == null || dept.getParentId() == 0) {
			throw new BizCommonException("system.dept.parent.empty");
		}
		super.insert(dept);
		Dept parent = deptMapper.getById(dept.getParentId());
		dept.setAncestors(parent.getAncestors() + "," + dept.getDeptId());
		deptMapper.update(dept);
	    return dept.getDeptId();
	}
	
	/**
     * 修改部门 
     * 
     * @param dept 部门 信息
     * @return 结果
     */
	@Override
	public int updateDept(Dept dept) {
		// 如果父级菜单发生修改
		if (dept.getParentId() != null) {
			Dept deptOld = deptMapper.getById(dept.getDeptId());
			if (!dept.getParentId().equals(deptOld.getParentId())) {
				Dept parent = deptMapper.getById(dept.getParentId());
				dept.setAncestors(parent.getAncestors() + "," + dept.getDeptId());
			}
		}
	    return super.update(dept);
	}

	@Override
	public Dept getRootDept() {
		Dept deptParam = new Dept();
		deptParam.setParentId(new Long(0));
		List<Dept> deptList = deptMapper.selectList(deptParam);
		if (deptList.size() == 0) {
			throw new BizCommonException("system.dept.not.init");
		}
		if (deptList.size() > 1) {
			throw new BizCommonException("system.dept.root.error");
		}
		return deptList.get(0);
	}

	@Override
	public DeptTreeNode findDeptTree() {
		Dept rootDept = this.getRootDept();
		List<Dept> deptList = findDeptListExcludeRoot();
		DeptTreeNode parentDept = new DeptTreeNode();
		parentDept.setName(rootDept.getDeptName());
		parentDept.setId(rootDept.getDeptId());
		parentDept.setChecked(true);
		parentDept.setUrl("");
		parentDept.setChildren(new ArrayList<>());
		initDeptTree(parentDept, deptList, "", new ArrayList<>());
		return parentDept;
	}

	@Override
	public DeptTreeNode findDeptRelateRoleTree(Long roleId) {
		// 角色关联的部门
		List<RoleDept> roleDeptList = new ArrayList<>();
		StringBuilder deptIdBuilder = new StringBuilder();
		if (roleId != null) {
			RoleDept searchParam = new RoleDept();
			searchParam.setRoleId(roleId);
			roleDeptList = roleDeptMapper.selectList(searchParam);
			if (roleDeptList.size() > 0) {
				deptIdBuilder.append(",");
			}
			for (RoleDept roleDept : roleDeptList) {
				Long deptId = roleDept.getDeptId();
				deptIdBuilder.append(deptId).append(",");
			}
		}
		String deptIdChecked = deptIdBuilder.toString();
		Dept rootDept = getRootDept();
		List<Dept> deptList = findDeptListExcludeRoot();
		DeptTreeNode parentDept = new DeptTreeNode();
		parentDept.setName(rootDept.getDeptName());
		setTreeNodeCheckedId(parentDept, deptIdChecked, rootDept.getDeptId().toString(), roleDeptList);
		parentDept.setUrl("");
		parentDept.setId(rootDept.getDeptId());
		parentDept.setChildren(new ArrayList<>());
		initDeptTree(parentDept, deptList, deptIdChecked, roleDeptList);
		return parentDept;
	}

	private void initDeptTree(DeptTreeNode parentDept, List<Dept> deptList, String deptIds, List<RoleDept> roleDeptList) {
		for (Dept dept : deptList) {
			if (dept.getParentId().equals(parentDept.getId())) {
				DeptTreeNode deptTreeNode = new DeptTreeNode();
				deptTreeNode.setId(dept.getDeptId());
				deptTreeNode.setName(dept.getDeptName());
				if (StringUtils.isEmpty(deptIds)) {
					deptTreeNode.setChecked(false);
				} else {
					setTreeNodeCheckedId(deptTreeNode, deptIds, dept.getDeptId().toString(), roleDeptList);
				}
				parentDept.getChildren().add(deptTreeNode);
				initDeptTree(deptTreeNode, deptList, deptIds, roleDeptList);
			}
		}
	}

	private void setTreeNodeCheckedId (DeptTreeNode deptTreeNode, String deptIdsChecked, String curDeptId, List<RoleDept> roleDeptList) {
		int index = deptIdsChecked.indexOf("," + curDeptId + ",");
		if (index != -1) {
			deptTreeNode.setChecked(true);
			for (int i = 0; i < roleDeptList.size(); i++) {
				RoleDept roleDept = roleDeptList.get(i);
				if (roleDept.getDeptId().equals(curDeptId)) {
					deptTreeNode.setCheckId(roleDept.getRoleDeptId());
					break;
				}
			}
		} else {
			deptTreeNode.setChecked(false);
		}
	}

	/**
	 * 获取部门，不包含根部门
	 */
	private List<Dept> findDeptListExcludeRoot () {
		Dept deptParam = new Dept();
		Map<String, Object> params = deptParam.getParams();
		params.put("orderBy", "parent_id, order_num DESC");
		params.put("delFlag", "0");
		params.put("status", "0");
		params.put("excludeRoot", "true");
		List<Dept> deptList = deptMapper.selectList(deptParam);
		return deptList;
	}


	@Override
	public DeptMapper setMapper() {
		return this.deptMapper;
	}
}
