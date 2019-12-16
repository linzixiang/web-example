package com.linzx.system.service;

import com.linzx.system.domain.Menu;
import com.linzx.system.dto.MenuTreeNode;

import java.util.List;

/**
 * 菜单权限  服务层
 * 
 * @author linzixiang
 * @date 2019_05_122
 */
public interface IMenuService {
	
	/**
     * 查询菜单权限 列表
     * 
     * @param menu 菜单权限 信息
     * @return 菜单权限 集合
     */
	List<Menu> selectMenuList(Menu menu);
	
	/**
     * 新增菜单权限 
     * 
     * @param menu 菜单权限 信息
     * @return 结果
     */
	Long insertMenu(Menu menu);

	/**
     * 修改菜单权限
     *
     * @param menu 菜单权限 信息
     * @return 结果
     */
	int updateMenu(Menu menu);

	/**
	 * 获取菜单树
	 */
	MenuTreeNode findMenuTree(boolean isAllMenu);

	/**
	 * 获取菜单树，如果属于该角色则标记为选中
	 */
	MenuTreeNode findAllMenuRelateRole(Long roleId);

	/**
	 * 检查菜单节点是否唯一
	 * @return true 表示菜单存在
	 */
	Boolean isMenuNameExist(String menuName, Long excludedId);

	/**
	 * 计算子节点数量
	 * @return
	 */
	int countChildren(Long menuId);
	
}
