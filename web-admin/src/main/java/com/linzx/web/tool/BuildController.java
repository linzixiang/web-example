package com.linzx.web.tool;

import com.linzx.framework.web.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 */
@Controller
@RequestMapping("/tool/build")
public class BuildController extends BaseController {

    private String prefix = "tool/build";

    @RequiresPermissions("tool:build:view")
    @GetMapping("/index")
    public String build() {
        return prefix + "/index";
    }

}
