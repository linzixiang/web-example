package com.linzx.system.service.impl;

import java.util.*;

import com.linzx.common.constant.Constants;
import com.linzx.framework.shiro.bean.RoleBean;
import com.linzx.framework.shiro.bean.UserLoginInfo;
import com.linzx.framework.shiro.utils.ShiroUtils;
import com.linzx.framework.web.BaseService;
import com.linzx.system.domain.RoleMenu;
import com.linzx.system.dto.MenuTreeNode;
import com.linzx.system.mapper.RoleMenuMapper;
import com.linzx.utils.MapUtils;
import com.linzx.utils.SqlUtils;
import com.linzx.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linzx.system.mapper.MenuMapper;
import com.linzx.system.domain.Menu;
import com.linzx.system.service.IMenuService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 菜单权限  服务层实现
 * 
 * @author linzixiang
 * @date 2019_05_122
 */
@Service("menuService")
public class MenuService extends BaseService<Long, Menu, MenuMapper> implements IMenuService {

	@Autowired
	private MenuMapper menuMapper;

	@Autowired
	private RoleMenuMapper roleMenuMapper;
	
	/**
     * 查询菜单权限 列表
     * 
     * @param menu 菜单权限 信息
     * @return 菜单权限 集合
     */
	@Override
	public List<Menu> selectMenuList(Menu menu) {
//		Map<String, Object> param = BeanUtils.beanToHashMap(menu);
		List<String> menuTypes = new ArrayList<>();
		menuTypes.add(Constants.MENU_CATALOGUE);
		menuTypes.add(Constants.MENU_NORMAL);
		LinkedHashMap<String,String> orderFieldMap = new LinkedHashMap();
		orderFieldMap.put("order_num", "desc");
		String sqlOrderBy = SqlUtils.genSqlOrderBy(orderFieldMap, false);
		menu.getParams().put("menuTypes", menuTypes);
		menu.getParams().put("orderBy", sqlOrderBy);
		return super.selectList(menu).get();
	}
	
    /**
     * 新增菜单权限 
     * 
     * @param menu 菜单权限 信息
     * @return 结果
     */
	@Override
	@Transactional
	public Long insertMenu(Menu menu) {
		Long parentId = menu.getParentId();
		if (parentId == null) {
			parentId = new Long(0);
		}
		menu.setParentId(parentId);
		super.insert(menu);
		if (parentId == 0) {
			// 新增根节点
			menu.setAncestors(menu.getMenuId().toString());
			menu.setDepth(1);
		} else {
			Menu parent = menuMapper.getById(parentId);
			menu.setAncestors(parent.getAncestors() + "," + menu.getMenuId());
			menu.setDepth(new Integer(parent.getDepth() + 1));
		}
		super.update(menu);
	    return menu.getMenuId();
	}
	
	/**
     * 修改菜单权限 
     * 
     * @param menu 菜单权限 信息
     * @return 结果
     */
	@Override
	public int updateMenu(Menu menu) {
		super.initDefaultBeforeUpdated(menu);
		// 如果父级菜单发生修改
		if (menu.getParentId() != null) {
			Menu menuOld = menuMapper.getById(menu.getMenuId());
			if (!menu.getParentId().equals(menuOld.getParentId())) {
				Menu parent = menuMapper.getById(menu.getParentId());
				menu.setAncestors(parent.getAncestors() + "," + menu.getMenuId());
				menu.setDepth(new Integer(parent.getDepth() + 1));
			}
		}
		return menuMapper.update(menu);
	}

	@Override
	public MenuTreeNode findMenuTree(boolean isAllMenu) {
		Map<String, Object> param = new HashMap<>();
		UserLoginInfo userLoginInfo = ShiroUtils.getUserLoginInfo();
		List<Long> roleIdList = new ArrayList<>();
		for (RoleBean roleBean : userLoginInfo.getRoleList()) {
			roleIdList.add(roleBean.getRoleId());
		}
		param.put("roleIds", roleIdList);
		List<String> menuType = new ArrayList<>();
		menuType.add(Menu.CATALOGUE);
		menuType.add(Menu.MENU);
		param.put("menuTypes", menuType);
		// 只展示显示的菜单
		param.put("invisible", "0");
		List<Menu> menuList;
		// 如果是角色菜单包含所有菜单权限，或isAllMenu= true
		if (isAllMenu || Constants.ROLE_MENU_SCOPE_ALL == userLoginInfo.getMenuScope()) {
			menuList = menuMapper.selectAllNormalMenuList(param);
		} else {
			menuList = menuMapper.selectNormalMenuList(param);
		}
		MenuTreeNode rootMenu = new MenuTreeNode();
		rootMenu.setMenuId(new Long(0));
		rootMenu.setMenuName("根目录");
		rootMenu.setChildren(new ArrayList<>());
		initMenuTree(rootMenu, menuList, "", new ArrayList<>());
		return rootMenu;
	}

	@Override
	public MenuTreeNode findAllMenuRelateRole(Long roleId) {
		// 查询所有菜单
		Map<String, Object> param = new HashMap<>();
		List<String> menuType = new ArrayList<>();
		menuType.add(Menu.MENU);
		menuType.add(Menu.CATALOGUE);
		menuType.add(Menu.BUTTON);
		param.put("menuTypes", menuType);
		List<Menu> menuList = menuMapper.selectAllNormalMenuList(param);
		// 查询角色拥有的菜单
		StringBuilder menuIdBuilder = new StringBuilder();
		List<RoleMenu> roleMenuList = new ArrayList<>();
		if (roleId != null) {
			RoleMenu searchParam = new RoleMenu();
			searchParam.setRoleId(roleId);
			roleMenuList = roleMenuMapper.selectList(searchParam);
			if (roleMenuList.size() > 0) {
				menuIdBuilder.append(",");
			}
			for(RoleMenu roleMenu : roleMenuList) {
				menuIdBuilder.append(roleMenu.getMenuId()).append(",");
			}
		}
		MenuTreeNode rootMenu = new MenuTreeNode();
		rootMenu.setMenuId(new Long(0));
		rootMenu.setMenuName("根目录");
		rootMenu.setChildren(new ArrayList<>());
		initMenuTree(rootMenu, menuList, menuIdBuilder.toString(), roleMenuList);
		return rootMenu;
	}

	@Override
	public Boolean isMenuNameExist(String menuName, Long excludeId) {
		Menu param = new Menu();
		param.setMenuName(menuName);
		if (excludeId != null && excludeId != 0) {
			param.getParams().put("excludeId", excludeId);
		}
		Integer count = menuMapper.countMenuByCondition(param);
		return count > 0 ;
	}

	@Override
	public int countChildren(Long menuId) {
		Map<String, Object> searchParam = MapUtils.genHashMap("parentId", menuId, "delFlag", Constants.STATUS_NORMAL);
		return menuMapper.countSimple(searchParam).intValue();
	}

	private void initMenuTree(MenuTreeNode parentMenu, List<Menu> menuTreeNodeList, String menuIds, List<RoleMenu> roleMenuList) {
		for (Menu menu : menuTreeNodeList) {
			if (menu.getParentId().equals(parentMenu.getMenuId())) {
				MenuTreeNode menuTreeNode = new MenuTreeNode();
				menuTreeNode.setMenuId(menu.getMenuId());
				menuTreeNode.setAncestors(menu.getAncestors());
				menuTreeNode.setIcon(menu.getIcon());
				menuTreeNode.setMenuName(menu.getMenuName());
				menuTreeNode.setOrderNum(menu.getOrderNum());
				menuTreeNode.setMenuType(menu.getMenuType());
				menuTreeNode.setPerms(menu.getPerms());
				menuTreeNode.setUrl(menu.getUrl());
				menuTreeNode.setChildren(new ArrayList<>());
				if (StringUtils.isEmpty(menuIds)) {
					menuTreeNode.setChecked(false);
				} else {
					int index = menuIds.indexOf("," + menu.getMenuId() + ",");
					if (index != -1) {
						menuTreeNode.setChecked(true);
						for (int i = 0; i < roleMenuList.size(); i++) {
							RoleMenu roleMenu = roleMenuList.get(i);
							if (roleMenu.getMenuId().equals(menu.getMenuId())) {
								menuTreeNode.setCheckId(roleMenu.getRoleMenuId());
								break;
							}
						}
					} else {
						menuTreeNode.setChecked(false);
					}
				}
				parentMenu.getChildren().add(menuTreeNode);
				initMenuTree(menuTreeNode, menuTreeNodeList, menuIds, roleMenuList);
			}
		}
	}

	@Override
	public MenuMapper setMapper() {
		return this.menuMapper;
	}
}
