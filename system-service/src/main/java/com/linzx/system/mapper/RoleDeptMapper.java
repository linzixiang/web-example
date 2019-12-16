package com.linzx.system.mapper;

import com.linzx.framework.mapper.BaseMapper;
import com.linzx.system.domain.RoleDept;
import java.util.List;	

/**
 * 角色和部门关联  数据层
 * 
 * @author linzixiang
 * @date 2019_05_122
 */
public interface RoleDeptMapper extends BaseMapper<Long, RoleDept> {

	/**
	 * 批量新增角色和部门关联
	 * @param roleDeptList
	 * @return
	 */
	int insertRoleDeptBatch(List<RoleDept> roleDeptList);
	
}