package com.linzx.system.domain;

import com.linzx.common.base.BaseEntity;
import com.linzx.common.constant.Constants;

/**
 * 菜单权限 表 sys_menu
 * 
 * @author linzixiang
 * @date 2019_05_122
 */
public class Menu extends BaseEntity<Long>{
	private static final long serialVersionUID = 1L;

	public static final String CATALOGUE = Constants.MENU_CATALOGUE;
	public static final String MENU = Constants.MENU_NORMAL;
	public static final String BUTTON= Constants.MENU_BUTTON;

	/*** 菜单状态 */
	public static final int INVISIBLE_FALSE = 0;
	public static final int INVISIBLE_TRUE = 1;
	
	/** 菜单ID */
	private Long menuId;
	/** 菜单名称 */
	private String menuName;
	/** 父菜单ID */
	private Long parentId;
	/** 显示顺序 */
	private Integer orderNum;
	/** 菜单层级 */
	private String ancestors;
	/** 层级深度 */
	private Integer depth;
	/** 请求地址 */
	private String url;
	/** 菜单类型 （M目录 C菜单 F按钮） */
	private String menuType;
	/** 菜单状态 （0显示 1隐藏） */
	private Integer invisible;
	/** 权限标识 */
	private String perms;
	/** 菜单图标 */
	private String icon;
	/** 父菜单名称 */
	private String parentIdShow;

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
	public Long getMenuId() {
		return menuId;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getMenuName() {
		return menuName;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public Long getParentId() {
		return parentId;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public Integer getOrderNum() {
		return orderNum;
	}

	public void setAncestors(String ancestors) {
		this.ancestors = ancestors;
	}
	public String getAncestors() {
		return ancestors;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}
	public Integer getDepth() {
		return depth;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrl() {
		return url;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}
	public String getMenuType() {
		return menuType;
	}

	public void setInvisible(Integer invisible) {
		this.invisible = invisible;
	}
	public Integer getInvisible() {
		return invisible;
	}

	public void setPerms(String perms) {
		this.perms = perms;
	}
	public String getPerms() {
		return perms;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getIcon() {
		return icon;
	}

	public String getParentIdShow() {
		return parentIdShow;
	}

	public void setParentIdShow(String parentIdShow) {
		this.parentIdShow = parentIdShow;
	}

	@Override
	public void setId(Long id) {
		this.setMenuId(id);
	}
}
