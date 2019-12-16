package com.linzx.common.constant;

import com.linzx.utils.CharsetKit;

public class Constants {

    /**
     * 字符集
     */
    public static final String CHARSET_UTF8 = CharsetKit.UTF_8;

    /**
     *  布尔值字符串常量
     */
    public static final String BOOLEAN_TRUE = "true";
    public static final String BOOLEAN_FALSE = "false";

    /**
     * 通用请求常量
     */
    public static final String PAGE_NUM = "pageNum"; // 第几页
    public static final String PAGE_SIZE = "pageSize"; // 每页大小
    public static final String ORDER_BY_COLUMN = "orderByColumn"; // 排序字段
    public static final String IS_ASC = "isAsc"; // 排序方向
    public static final int PAGE_SIZE_DEFAULT = 20; // 默认分页大小
    public static final String ORDER_DIRECTION_DEFAULT = "asc"; // 默认排序方向

    /**
     * 用户角色数据权限
     */
    public static final Integer ROLE_DATA_SCOPE_ALL = 1; // 全部数据权限
    public static final Integer ROLE_DATA_SCOPE_CUSTOM = 2; // 自定数据权限
    /**
     * 用户角色菜单权限
     */
    public static final int ROLE_MENU_SCOPE_ALL = 1; // 全部菜单权限
    public static final int ROLE_MENU_SCOPE_CUSTOM = 2; // 自定菜单权限
    /**
     * 菜单分类
     */
    public static final String MENU_CATALOGUE = "M";
    public static final String MENU_NORMAL = "C";
    public static final String MENU_BUTTON ="F";

    /**
     * 停用/删除 标识
     */
    public static final int STATUS_NORMAL = 0; // 正常
    public static final int STATUS_STOP = 1; // 停用
    public static final int STATUS_DELETE = 2; // 删除

    public static final String IMAGE_ORIGINAL = "original";
    public static final String IMAGE_THUMBNAIL_800 = "thumbnail_800x800";

}
