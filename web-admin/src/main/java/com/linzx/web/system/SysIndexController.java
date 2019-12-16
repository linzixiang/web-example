package com.linzx.web.system;

import com.linzx.framework.shiro.bean.UserLoginInfo;
import com.linzx.framework.shiro.utils.ShiroUtils;
import com.linzx.system.dto.MenuTreeNode;
import com.linzx.system.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SysIndexController {

    @Autowired
    private IMenuService menuService;

    @GetMapping("/index")
    public String index(ModelMap modelMap){
        UserLoginInfo loginInfo = ShiroUtils.getUserLoginInfo();
        MenuTreeNode menuTreeNode = menuService.findMenuTree(false);
        modelMap.put("menuList", menuTreeNode.getChildren());
        modelMap.put("loginInfo", loginInfo);
        modelMap.put("demoEnabled", true);
        return "index";
    }

    @GetMapping("/system/main")
    public String systemMain() {
        return "/system/main";
    }

}
