package com.linzx.web.system;

import com.linzx.common.constant.Constants;
import com.linzx.framework.utils.PageHelper;
import com.linzx.framework.web.BaseController;
import com.linzx.framework.web.vo.AjaxResult;
import com.linzx.system.domain.Dept;
import com.linzx.system.dto.DeptTreeNode;
import com.linzx.system.service.impl.DeptService;
import com.linzx.utils.Convert;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 部门  信息操作处理
 *
 * @author linzixiang
 * @date 2019_06_180
 */
@Controller
@RequestMapping(DeptController.prefix)
public class DeptController extends BaseController {

    public static final String prefix = "system/dept";

    @Autowired
    private DeptService deptService;

    @RequiresPermissions("system:dept:view")
    @GetMapping("/index")
    public String index() {
        return prefix + "/index";
    }

    /**
    * 列表查询
    */
    @RequiresPermissions("system:dept:list")
    @GetMapping("/list")
    @ResponseBody
    public AjaxResult list(Dept dept, @RequestParam(value = "excludeRoot", required = false, defaultValue = "false") Boolean excludeRoot) {
        if (excludeRoot) {
            dept.getParams().put("excludeRoot", "false");
        }
        dept.setDelFlag(Constants.STATUS_NORMAL);
        Map<String, Object> params = dept.getParams();
        params.put("orderBy", "parent_id, order_num DESC");
        PageHelper.startPage();
        List<Dept> deptList = deptService.selectList(dept).get();
        return AjaxResult.success(deptList);
    }

    /**
     * 新增
     */
    @GetMapping("/preAdd/{parentId}")
    public String preAdd(@PathVariable("parentId") Long parentId, ModelMap mmap) {
        Dept dept = null;
        if (parentId > 0) {
            dept = deptService.getById(parentId).get();
        } else {
            dept = deptService.getRootDept();
        }
        mmap.put("dept", dept);
        return prefix + "/add";
    }

    @RequiresPermissions("system:dept:add")
    @PostMapping("/saveAdd")
    @ResponseBody
    public AjaxResult saveAdd(Dept dept) {
        Long deptId = deptService.insertDept(dept);
        return AjaxResult.success(deptId);
    }

    /**
     * 修改
     */
    @GetMapping("/preEdit/{deptId}")
    public String preEdit(@PathVariable("deptId") Long deptId, ModelMap mmap) {
        Dept dept = deptService.getById(deptId).get();
        if (dept.getParentId() > 0) {
            Dept parent = deptService.getById(dept.getParentId()).get();
            dept.setParentIdShow(parent.getDeptName());
        }
        mmap.put("dept", dept);
        return prefix + "/edit";
    }

    @RequiresPermissions("system:dept:edit")
    @PostMapping("/saveEdit")
    @ResponseBody
    public AjaxResult saveEdit(Dept dept) {
        deptService.updateDept(dept);
        return AjaxResult.success();
    }

    /**
     * 删除
     */
    @RequiresPermissions("system:dept:remove")
    @PostMapping("/remove/{deptId}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("deptId") Long deptId) {
        deptService.deleteByIds(Convert.toLongArray(deptId.toString()));
        return AjaxResult.success();
    }

    @PostMapping("/checkDeptNameUnique")
    @ResponseBody
    public AjaxResult checkDeptNameUnique(@RequestParam("deptName") String deptName,
                                          @RequestParam(value = "selfId",required = false) Long selfId) {
        return AjaxResult.success(true);
    }

    /**
     * 选择菜单树
     */
    @GetMapping("/selectDeptTree/{deptId}")
    public String selectDeptTree(@PathVariable("deptId") Long deptId, ModelMap mmap) {
        if (deptId == 0) {
            mmap.put("dept", deptService.getRootDept());
        } else {
            mmap.put("dept", deptService.getById(deptId));
        }
        return prefix + "/tree";
    }

    /**
     * 加载所有菜单列表树
     */
    @GetMapping("/loadAllDeptTree")
    @ResponseBody
    public AjaxResult loadAllMenuTree() {
        DeptTreeNode deptTree = deptService.findDeptTree();
        return AjaxResult.success(deptTree);
    }

}