package com.linzx.web.system;

import com.linzx.framework.bean.SensitiveBean;
import com.linzx.framework.core.context.ContextManager;
import com.linzx.framework.shiro.bean.UserLoginInfo;
import com.linzx.framework.utils.PageHelper;
import com.linzx.framework.web.BaseController;
import com.linzx.framework.web.vo.AjaxResult;
import com.linzx.system.domain.Role;
import com.linzx.system.domain.RoleDept;
import com.linzx.system.domain.RoleSens;
import com.linzx.system.dto.DeptTreeNode;
import com.linzx.system.service.impl.DeptService;
import com.linzx.system.service.impl.RoleService;
import com.linzx.utils.Convert;
import com.linzx.utils.ObjectUtils;
import com.linzx.utils.StringUtils;
import com.linzx.web.system.request.role.DataAccessEditDto;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 角色  信息操作处理
 *
 * @author linzixiang
 * @date 2019_05_132
 */
@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController {

    private String prefix = "system/role";

    @Autowired
    private RoleService roleService;

    @Autowired
    private DeptService deptService;

    @RequiresPermissions("system:role:view")
    @GetMapping("/index")
    public String index() {
        return prefix + "/index";
    }

    /**
    * 列表查询
    */
    @RequiresPermissions("system:role:list")
    @GetMapping("/list")
    @ResponseBody
    public AjaxResult list(Role role) {
        PageHelper.startPage();
        List<Role> roleList = roleService.selectList(role).get();
        return AjaxResult.success(roleList);
    }

    /**
     * 新增
     */
    @GetMapping("/preAdd/{roleId}")
    public String preAdd(@PathVariable("roleId") Long roleId, ModelMap mmap) {
        Role role = null;
        if (0L != roleId) {
            role = roleService.getById(roleId).get();
        } else {
            role = new Role();
            role.setRoleId(new Long(0));
            role.setRoleName("无");
        }
        mmap.put("role", role);
        return prefix + "/add";
    }

    @RequiresPermissions("system:role:add")
    @PostMapping("/saveAdd")
    @ResponseBody
    public AjaxResult saveAdd(Role role, Long[] menuIds) {
        Long roleId = roleService.insertRoleWithMenu(role, menuIds);
        return AjaxResult.success(roleId);
    }

    /**
     * 修改
     */
    @GetMapping("/preEdit/{roleId}")
    public String preEdit(@PathVariable("roleId") Long roleId, ModelMap mmap) {
        Role role = roleService.getById(roleId).get();
        mmap.put("role", role);
        return prefix + "/edit";
    }

    @RequiresPermissions("system:role:edit")
    @PostMapping("/saveEdit")
    @ResponseBody
    public AjaxResult saveEdit(@RequestBody Role role) {
        roleService.updateRoleWithMenu(role);
        return AjaxResult.success();
    }

    /**
     * 删除
     */
    @RequiresPermissions("system:role:remove")
    @PostMapping("/remove/{roleId}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("roleId") Long roleId) {
        roleService.deleteByIds(new Long[]{ roleId });
        return AjaxResult.success();
    }

    /**
     * 检查角色名称是否已存在
     */
    @PostMapping("/checkRoleNameUnique")
    @ResponseBody
    public AjaxResult checkRoleNameUnique(@RequestParam("roleName") String roleName,
                                          @RequestParam(value = "selfId",required = false) Long roleId) {
        Boolean roleNameExist = roleService.isRoleNameExist(roleName, roleId);
        return AjaxResult.success(!roleNameExist);
    }

    /**
     * 检查角色编码是否已存在
     */
    @PostMapping("/checkRoleCodeUnique")
    @ResponseBody
    public AjaxResult checkRoleCodeUnique(@RequestParam("roleCode") String roleCode,
                                          @RequestParam(value = "selfId",required = false) Long roleId) {
        Boolean roleCodeExist = roleService.isRoleCodeExist(roleCode, roleId);
        return AjaxResult.success(!roleCodeExist);
    }

    @RequiresPermissions("system:role:edit")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(@RequestParam("roleId") Long roleId, @RequestParam("status") Integer status) {
        Role param = new Role();
        param.setRoleId(roleId);
        param.setStatus(status);
        int count = roleService.update(param);
        return AjaxResult.success(count);
    }

    /**
     * 数据权限页面
     * @return
     */
    @RequiresPermissions("system:role:edit")
    @GetMapping("/data/{roleId}")
    public String dataAccessPri(@PathVariable("roleId") Long roleId, ModelMap mmap) {
        Role role = roleService.getById(roleId).get();
        List<RoleSens> roleSensList = roleService.findRoleSensByRoleId(roleId);
        Set<String> sensCodeSet = new HashSet<>();
        for (RoleSens roleSens : roleSensList) {
            sensCodeSet.add(roleSens.getSensCode());
        }
        List<SensitiveBean> allSensitive = ContextManager.getAllSensitive();
        List<Map<String, Object>> retSensList = new ArrayList<>();
        for (SensitiveBean sensitiveBean : allSensitive) {
            Map<String, Object> sensitiveMap = new HashMap<>();
            sensitiveMap.put("code", sensitiveBean.getCode());
            sensitiveMap.put("name", sensitiveBean.getName());
            if (sensCodeSet.size() == 0) {
                sensitiveMap.put("checked", true);
            } else {
                sensitiveMap.put("checked", sensCodeSet.contains(sensitiveBean.getCode()));
            }
            retSensList.add(sensitiveMap);
        }
        mmap.put("roleSens", retSensList);
        mmap.put("role", role);
        return prefix + "/data";
    }

    /**
     * 保存数据权限编辑
     */
    @PostMapping("/saveDataAccessEdit")
    @ResponseBody
    public AjaxResult saveDataAccessEdit(DataAccessEditDto dataAccessEditDto) {
        Role role = new Role();
        BeanUtils.copyProperties(dataAccessEditDto, role);
        List<RoleDept> roleDeptAdd = new ArrayList<>();
        List<Long> roleDeptDel = new ArrayList<>();
        // 选择自定义部门数据权限时
        if (ObjectUtils.equals(UserLoginInfo.DATA_SCOPE_CUSTOMIZE_DEPT, dataAccessEditDto.getDataScope())) {
            String[] deptIdNewArr = Convert.toStrArray(dataAccessEditDto.getDeptIds());
            List<RoleDept> roleDeptOld = roleService.findRoleDeptByRoleId(dataAccessEditDto.getRoleId());
            String[] deptIdOldArr = new String[roleDeptOld.size()];
            for (int i = 0; i< roleDeptOld.size(); i++) {
                RoleDept roleDept = roleDeptOld.get(i);
                deptIdOldArr[i] = roleDept.getDeptId().toString();
                // 数据库中有数据，新上传的数据没有，则删除
                if (!StringUtils.inStringIgnoreCase(roleDept.getDeptId().toString(), deptIdNewArr)) {
                    roleDeptDel.add(roleDept.getDeptId());
                }
            }
            for (String deptId : deptIdNewArr) {
                // 数据库中没有，新上传的数据中有，则新增
                if (!StringUtils.inStringIgnoreCase(deptId, deptIdOldArr)) {
                    RoleDept roleDept = new RoleDept();
                    roleDept.setRoleId(dataAccessEditDto.getRoleId());
                    roleDept.setDeptId(Convert.toLong(deptId));
                    roleDeptAdd.add(roleDept);
                }
            }
        }
        List<RoleSens> roleSensAdd = new ArrayList<>();
        List<Long> roleSensDel = new ArrayList<>();
        String[] sensCodeNewArr = Convert.toStrArray(dataAccessEditDto.getSensCodes());
        List<RoleSens> roleSensList = roleService.findRoleSensByRoleId(dataAccessEditDto.getRoleId());
        String[] deptIdOldArr = new String[roleSensList.size()];
        for (int i = 0; i < roleSensList.size(); i++) {
            RoleSens roleSens = roleSensList.get(i);
            deptIdOldArr[i] = roleSens.getSensCode();
            if (!StringUtils.inStringIgnoreCase(roleSens.getSensCode(), sensCodeNewArr)) {
                roleSensDel.add(roleSens.getSensId());
            }
        }
        for (String sensCode : sensCodeNewArr) {
            if (!StringUtils.inStringIgnoreCase(sensCode, deptIdOldArr)) {
                RoleSens roleSens = new RoleSens();
                roleSens.setRoleId(dataAccessEditDto.getRoleId());
                roleSens.setSensCode(sensCode);
                roleSensAdd.add(roleSens);
            }
        }
        roleService.updateRoleWithDataAccessScope(role, roleDeptAdd, roleDeptDel, roleSensAdd, roleSensDel);
        return AjaxResult.success();
    }

    /**
     *  角色关联的部门数据
     * @return
     */
    @GetMapping("/roleDeptTree")
    @ResponseBody
    public AjaxResult roleDeptTreeData(@RequestParam("roleId") Long roleId) {
        DeptTreeNode deptTree = deptService.findDeptRelateRoleTree(roleId);
        return AjaxResult.success(deptTree);
    }

}