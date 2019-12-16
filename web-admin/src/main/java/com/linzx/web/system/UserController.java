package com.linzx.web.system;

import com.linzx.common.constant.Constants;
import com.linzx.framework.web.annotation.DataScope;
import com.linzx.framework.web.annotation.SensitiveField;
import com.linzx.framework.web.annotation.SensitiveFields;
import com.linzx.framework.shiro.utils.PasswordUtils;
import com.linzx.framework.utils.PageHelper;
import com.linzx.framework.web.vo.AjaxResult;
import com.linzx.framework.web.BaseController;
import com.linzx.system.domain.Dept;
import com.linzx.system.domain.Role;
import com.linzx.system.domain.User;
import com.linzx.system.domain.UserRole;
import com.linzx.system.pojo.user.UserManage;
import com.linzx.system.service.UserService;
import com.linzx.system.service.impl.DeptService;
import com.linzx.system.service.impl.RoleService;
import com.linzx.web.system.request.user.UserSaveAddDto;
import com.linzx.web.system.request.user.UserSaveEditDto;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController {

    private String prefix = "system/user";

    @Autowired
    private UserService userService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private RoleService roleService;

    @RequiresPermissions("system:user:view")
    @GetMapping("/index")
    public String index() {
        return prefix + "/index";
    }

    /**
     * 列表查询
     */
    @RequiresPermissions("system:user:list")
    @GetMapping("/list")
    @ResponseBody
    @SensitiveFields({@SensitiveField(code = "mobilePhone", colNames = "phoneNumber")})
    @DataScope(deptAlias = "test")
    public AjaxResult list(User user) {
        if (user.getDeptId() != null) {
            Dept dept = deptService.getById(user.getDeptId()).get();
            user.getParams().put("deptAncestors", dept.getAncestors());
            user.setDeptId(null);
        }
        PageHelper.startPage(true);
        List<UserManage> userManageList = userService.queryUserManageList(user);
        return AjaxResult.success(userManageList);
    }

    /**
     * 新增
     */
    @GetMapping("/preAdd")
    public String preAdd(ModelMap mmap) {
        mmap.put("roles", findRoleEnableList());
        return prefix + "/add";
    }

    @RequiresPermissions("system:user:add")
    @PostMapping("/saveAdd")
    @ResponseBody
    public AjaxResult saveAdd(UserSaveAddDto userSaveAddDto) {
        User user = new User();
        user.setDeptId(userSaveAddDto.getDeptId());
        user.setLoginAccount(userSaveAddDto.getLoginAccount());
        user.setUserNickname(userSaveAddDto.getUserNickname());
        user.setEmail(userSaveAddDto.getEmail());
        user.setSalt(PasswordUtils.randomSalt());
        user.setPassword(PasswordUtils.encryptPassword(user.getLoginAccount(), userSaveAddDto.getPassword(), user.getSalt()));
        user.setPhoneNumber(userSaveAddDto.getPhoneNumber());
        user.setSex(userSaveAddDto.getSex());
        user.setStatus(userSaveAddDto.getStatus());
        user.setRemark(userSaveAddDto.getRemark());
        userService.saveAddUserWithRole(user, userSaveAddDto.getRoleIds());
        return AjaxResult.success();
    }

    /**
     * 修改
     */
    @GetMapping("/preEdit/{userId}")
    public String preEdit(@PathVariable("userId") Long userId, ModelMap mmap) {
        User user = userService.selectUserById(userId);
        mmap.put("user", user);
        Dept dept = new Dept();
        if (user.getDeptId() != null && user.getDeptId() > 0) {
            dept = deptService.getById(user.getDeptId()).get();
        }
        mmap.put("dept", dept);        // 当前已选中的角色集合
        Set<String> roleIdsCheckedSet = new HashSet<>();
        List<UserRole> userRoleList = userService.findUserRoleList(userId);
        for(UserRole userRole: userRoleList) {
            roleIdsCheckedSet.add(userRole.getRoleId().toString());
        }
        // 设置用户已关联的角色状态为true
        List<Role> roleEnableList = findRoleEnableList();
        for(Role role : roleEnableList) {
            if (roleIdsCheckedSet.contains(role.getRoleId().toString())) {
                role.setOnChecked(true);
            } else {
                role.setOnChecked(false);
            }
        }
        mmap.put("roles", roleEnableList);
        return prefix + "/edit";
    }

    @RequiresPermissions("system:user:edit")
    @PostMapping("/saveEdit")
    @ResponseBody
    public AjaxResult saveEdit(@Validated UserSaveEditDto userSaveEditDto) {
        User user = new User();
        user.setUserId(userSaveEditDto.getUserId());
        user.setDeptId(userSaveEditDto.getDeptId());
        user.setUserNickname(userSaveEditDto.getUserNickname());
        user.setEmail(userSaveEditDto.getEmail());
        user.setPhoneNumber(userSaveEditDto.getPhoneNumber());
        user.setSex(userSaveEditDto.getSex());
        user.setStatus(userSaveEditDto.getStatus());
        user.setRemark(userSaveEditDto.getRemark());
        List<UserRole> delRoleList = new ArrayList<>();
        List<Long> addRoleList = new ArrayList<>();
        Set<String> roleIdsNew = userSaveEditDto.getRoleIds();
        Set<String> roleIdsOld = new HashSet<>();
        // 原先的用户角色
        List<UserRole> userRoleList = userService.findUserRoleList(userSaveEditDto.getUserId());
        for (UserRole userRole : userRoleList) {
            roleIdsOld.add(userRole.getRoleId().toString());
        }
        for (UserRole userRole : userRoleList) {
            // 数据库存在，上传的角色不存在，则删除
            if (!roleIdsNew.contains(userRole.getRoleId().toString())) {
                delRoleList.add(userRole);
            }
        }
        for (String roleId : roleIdsNew) {
            // 上传存在，数据库不存在，则新增
            if (!roleIdsOld.contains(roleId)) {
                addRoleList.add(Long.parseLong(roleId));
            }
        }
        Long userId = userService.saveEditUserWithRole(user, addRoleList, delRoleList);
        return AjaxResult.success(userId);
    }

    /**
     * 删除用户
     */
    @RequiresPermissions("system:user:remove")
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(@RequestParam("ids") String userIds) {
        String[] userIdsArr = userIds.split(",");
        for (String userId : userIdsArr) {
            User user = new User();
            user.setUserId(Long.parseLong(userId));
            user.setDelFlag(Constants.STATUS_DELETE);
            userService.updateUser(user);
        }
        return AjaxResult.success();
    }

    @RequiresPermissions("system:user:edit")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(@RequestParam("userId") Long userId, @RequestParam("status") Integer status) {
        User user = new User();
        user.setUserId(userId);
        user.setStatus(status);
        userService.updateUser(user);
        return AjaxResult.success();
    }

    /**
     * 重置用户密码
     */
    @RequiresPermissions("system:user:resetPwd")
    @PostMapping("/resetPwdDefault")
    @ResponseBody
    public AjaxResult resetPassword(@RequestParam("userId") Long userId) {
        User user = userService.selectUserById(userId);
        user.setSalt(PasswordUtils.randomSalt());
        user.setPassword(PasswordUtils.encryptPassword(user.getLoginAccount(), "123456", user.getSalt()));
        return AjaxResult.success();
    }

    /**
     * 检查用户账户是否存在
     * @return
     */
    @PostMapping("/checkLoginAccountUnique")
    @ResponseBody
    public AjaxResult checkLoginAccountUnique(@RequestParam("loginAccount") String loginAccount,
                                              @RequestParam(value = "selfId",required = false) Long selfId) {
        boolean exist = userService.isLoginAccountExist(loginAccount, selfId);
        return AjaxResult.success(!exist);
    }

    /**
     * 检查手机号是否已存在
     * @return
     */
    @PostMapping("/checkPhoneUnique")
    @ResponseBody
    public AjaxResult checkPhoneUnique(@RequestParam("phoneNumber") String phoneNumber,
                                       @RequestParam(value = "selfId",required = false) Long selfId) {
        boolean exist = userService.isPhoneNumberExist(phoneNumber, selfId);
        return AjaxResult.success(!exist);
    }

    /**
     * 检查邮箱是否存在
     */
    @PostMapping("/checkEmailUnique")
    @ResponseBody
    public AjaxResult checkEmailUnique(@RequestParam("email") String email,
                                       @RequestParam(value = "selfId",required = false) Long selfId) {
        boolean exist = userService.isEmailExist(email, selfId);
        return AjaxResult.success(!exist);
    }

    /**
     * 获取可用角色给页面展示
     * @return
     */
    private List<Role> findRoleEnableList() {
        Role role = new Role();
        role.setStatus(Constants.STATUS_NORMAL);
        role.setDelFlag(Constants.STATUS_NORMAL);
        List<Role> roleList = roleService.selectList(role).get();
        return roleList;
    }


}
