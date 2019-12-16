package com.linzx.web.system;

import com.linzx.framework.exception.BizCommonException;
import com.linzx.framework.shiro.bean.UserLoginInfo;
import com.linzx.framework.shiro.utils.PasswordUtils;
import com.linzx.framework.shiro.utils.ShiroUtils;
import com.linzx.framework.web.BaseController;
import com.linzx.framework.web.controller.FileOperateController;
import com.linzx.framework.web.vo.AjaxResult;
import com.linzx.system.domain.Dept;
import com.linzx.system.domain.User;
import com.linzx.system.service.UserService;
import com.linzx.system.service.impl.DeptService;
import com.linzx.utils.StrFormatter;
import com.linzx.utils.StringUtils;
import com.linzx.web.system.request.user.UserProfileSaveEditDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * 个人基础信息操作
 */
@Controller
@RequestMapping("/system/user/profile")
public class UserProfileController extends BaseController {

    private String prefix = "system/user/profile";

    @Autowired
    private UserService userService;

    @Autowired
    private DeptService deptService;

    /**
     * 个人信息
     */
    @GetMapping("/index")
    public String index(ModelMap mmap) {
        UserLoginInfo userLoginInfo = ShiroUtils.getUserLoginInfo();
        User user = userService.selectUserById(userLoginInfo.getUserId());
        Long deptId = user.getDeptId();
        Dept dept = null;
        if (deptId > 0) {
            dept = deptService.getById(deptId).get();
        }
        if (dept == null) {
            dept = new Dept();
            dept.setDeptName("未知");
        }
        if (StringUtils.isNotEmpty(user.getAvatar())) {
            String imageUrl = StrFormatter.format(FileOperateController.IMAGE_DOWNLOAD_URL, user.getAvatar());
            user.setAvatar(imageUrl);
        }
        mmap.put("dept", dept);
        mmap.put("user", user);
        return prefix + "/index";
    }

    /**
     * 修改用户头像
     */
    @GetMapping("/avatar")
    public String avatar(ModelMap mmap) {
        UserLoginInfo userLoginInfo = ShiroUtils.getUserLoginInfo();
        User user = userService.selectUserById(userLoginInfo.getUserId());
        if (StringUtils.isNotEmpty(user.getAvatar())) {
            String imageUrl = StrFormatter.format(FileOperateController.IMAGE_DOWNLOAD_URL, user.getAvatar());
            user.setAvatar(imageUrl);
        }
        mmap.put("user", user);
        return prefix + "/avatar";
    }

    /**
     * 保存用户信息
     */
    @PostMapping("/saveEdit")
    @ResponseBody
    public AjaxResult profileSaveEdit(UserProfileSaveEditDto saveEditDto) {
        UserLoginInfo userLoginInfo = ShiroUtils.getUserLoginInfo();
        User user = new User();
        user.setUserId(userLoginInfo.getUserId());
        user.setUserNickname(saveEditDto.getUserNickname());
        user.setSex(saveEditDto.getSex());
        user.setPhoneNumber(saveEditDto.getPhoneNumber());
        user.setEmail(saveEditDto.getEmail());
        user.setAvatar(saveEditDto.getAvatarImgId());
        userService.updateUser(user);
        return AjaxResult.success();
    }

    /**
     * 检查密码是否正确
     */
    @PostMapping("/checkPassword")
    @ResponseBody
    public AjaxResult checkPassword(@RequestParam("password") String password) {
        UserLoginInfo userLoginInfo = ShiroUtils.getUserLoginInfo();
        User user = userService.selectUserById(userLoginInfo.getUserId());
        boolean matches = PasswordUtils.matches(user.getLoginAccount(), user.getSalt(), user.getPassword(), password);
        return AjaxResult.success(matches);
    }

    /**
     * 重置密码
     */
    @PostMapping("/resetPassword")
    @ResponseBody
    public AjaxResult resetPassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword) {
        UserLoginInfo userLoginInfo = ShiroUtils.getUserLoginInfo();
        User user = userService.selectUserById(userLoginInfo.getUserId());
        boolean matches = PasswordUtils.matches(user.getLoginAccount(), user.getSalt(), user.getPassword(), oldPassword);
        if (!matches) {
            throw new BizCommonException("user.origin.password.error");
        }
        user.setSalt(PasswordUtils.randomSalt());
        user.setPassword(PasswordUtils.encryptPassword(user.getLoginAccount(), newPassword, user.getSalt()));
        userService.updateUser(user);
        return AjaxResult.success();
    }

}
