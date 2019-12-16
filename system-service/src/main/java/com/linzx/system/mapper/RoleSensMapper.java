package com.linzx.system.mapper;

import com.linzx.framework.mapper.BaseMapper;
import com.linzx.system.domain.RoleSens;
import java.util.List;	

/**
 * 敏感字段 角色敏感字段 数据层
 * 
 * @author linzixiang
 * @date 2019_08_216
 */
public interface RoleSensMapper extends BaseMapper<Long, RoleSens> {

	/**
	 * 批量新增敏感字段
	 */
	int insertRoleSensBatch(List<RoleSens> roleSensList);
	
}