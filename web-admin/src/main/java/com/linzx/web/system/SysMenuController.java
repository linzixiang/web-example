package com.linzx.web.system;

import com.linzx.common.constant.Constants;
import com.linzx.framework.exception.BizCommonException;
import com.linzx.framework.web.BaseController;
import com.linzx.framework.web.vo.AjaxResult;
import com.linzx.system.domain.Menu;
import com.linzx.system.dto.MenuTreeNode;
import com.linzx.system.service.impl.MenuService;
import com.linzx.utils.MapUtils;
import com.linzx.utils.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController {

    private String prefix = "system/menu";

    @Autowired
    private MenuService menuService;

    @RequiresPermissions("system:menu:view")
    @GetMapping("/index")
    public String menu(){
        return prefix + "/index";
    }

    /**
     * 列表查询
     */
    @GetMapping("/list")
    @ResponseBody
    public AjaxResult list(@RequestParam Map<String, Object> searchParam) {
        Menu menu = new Menu();
        menu.setInvisible(MapUtils.getInteger(searchParam, "invisible"));
        Map<String, Object> params = menu.getParams();
        params.put("menuName", MapUtils.getStringValue(searchParam, "menuName"));
        List<Menu> menuList = menuService.selectMenuList(menu);
        return AjaxResult.success(menuList);
    }

    /**
     * 新增
     */
    @GetMapping("/preAdd/{parentId}")
    public String preAdd(@PathVariable("parentId") Long parentId, ModelMap mmap) {
        Menu menu = null;
        if (0L != parentId) {
            menu = menuService.getById(parentId).get();
        } else {
            menu = new Menu();
            menu.setMenuId(0L);
            menu.setMenuName("主目录");
        }
        mmap.put("menu", menu);
        return prefix + "/add";
    }

    @RequiresPermissions("system:menu:add")
    @PostMapping("/saveAdd")
    @ResponseBody
    public AjaxResult saveAdd(Menu menu) {
        Long menuId = menuService.insertMenu(menu);
        return AjaxResult.success(menuId);
    }

    /**
     * 修改
     */
    @GetMapping("/preEdit/{menuId}")
    public String preEdit(@PathVariable("menuId") Long menuId, ModelMap mmap) {
        Menu menu = menuService.getById(menuId).get();
        if (menu.getParentId() > 0) {
            Menu menuParent = menuService.getById(menu.getParentId()).get();
            menu.setParentIdShow(menuParent.getMenuName());
        }
        mmap.put("menu", menu);
        return prefix + "/edit";
    }

    @RequiresPermissions("system:menu:edit")
    @PostMapping("/saveEdit")
    @ResponseBody
    public AjaxResult saveEdit(Menu menu) {
        menu.setDelFlag(Constants.STATUS_NORMAL);        Menu menuOld = new Menu();
        int countChildren = menuService.countChildren(menu.getMenuId());
        if (countChildren > 0) {
            menuOld = menuService.getById(menu.getMenuId()).get();
        }
        if (StringUtils.isNotEmpty(menu.getMenuType()) && countChildren > 0 && !(menu.getMenuType().equals(menuOld.getMenuType()))) {
           // 当菜单存在子节点，不允许修改菜单类型
            throw new BizCommonException("system.menu.type.edit.disable");
        }
        if (menu.getInvisible() != null && countChildren > 0 && menu.getInvisible() == Menu.INVISIBLE_TRUE) {
            // 当菜单存在子节点，不允许隐藏
            throw new BizCommonException("system.menu.invisible.hidden.disable");
        }
        menuService.updateMenu(menu);
        return AjaxResult.success();
    }

    /**
     * 删除
     */
    @RequiresPermissions("system:menu:remove")
    @PostMapping("/remove/{menuId}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("menuId") Long menuId) {
        menuService.deleteByIds(new Long[] {menuId});
        return AjaxResult.success();
    }

    /**
     * 菜单
     */
    @GetMapping("/icon")
    public String icon() {
        return prefix + "/icon";
    }

    /**
     * 加载角色关联的菜单
     * @return
     */
//    @PostMapping("/loadRoleMenuTree")
//    @ResponseBody
//    public AjaxResult loadRoleMenuTree() {
//        MenuTreeNode menuTree = menuService.findMenuTree(false);
//        return  AjaxResult.success(menuTree);
//    }

    /**
     * 加载所有菜单列表树
     */
    @GetMapping("/loadAllMenuTree")
    @ResponseBody
    public AjaxResult loadAllMenuTree(@RequestParam(value = "roleId", required = false) Long roleId) {
        MenuTreeNode menuTree = menuService.findAllMenuRelateRole(roleId);
        return AjaxResult.success(menuTree);
    }

    /**
     * 选择菜单树
     */
    @GetMapping("/selectMenuTree/{menuId}")
    public String selectMenuTree(@PathVariable("menuId") Long menuId, ModelMap mmap) {
        mmap.put("menu", menuService.getById(menuId));
        return prefix + "/tree";
    }

    /**
     * 检查菜单节点是否唯一
     * @param menuName 菜单名称
     */
    @PostMapping("/checkMenuNameUnique")
    @ResponseBody
    public AjaxResult checkMenuNameUnique(@RequestParam("menuName") String menuName,
                                          @RequestParam(value = "selfId",required = false) Long selfId) {
        Boolean nameExist = menuService.isMenuNameExist(menuName, selfId);
        return AjaxResult.success(!nameExist);
    }


}
