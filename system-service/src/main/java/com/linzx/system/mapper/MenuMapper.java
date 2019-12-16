package com.linzx.system.mapper;

import com.linzx.framework.mapper.BaseMapper;
import com.linzx.framework.mapper.SimpleCountMapper;
import com.linzx.system.domain.Menu;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 菜单权限  数据层
 * 
 * @author linzixiang
 * @date 2019_05_122
 */
public interface MenuMapper extends SimpleCountMapper, BaseMapper<Long, Menu> {

	/**
	 * 查询所有正常状态的目录和菜单
	 * @param param
	 * @return
	 */
	public List<Menu> selectAllNormalMenuList(Map<String, Object> param);

	/**
	 * 查询某角色下的可用菜单
	 * @param param
	 * @return
	 */
	public List<Menu> selectNormalMenuList(Map<String, Object> param);

	/**
	 * 查询某角色下的可用菜单权限编码
	 */
	public Set<String> selectNormalPermsByRoleIds(Map<String, Object> param);

	/**
	 * 查询符合条件的数量
	 */
	public Integer countMenuByCondition(Menu menu);
	
}