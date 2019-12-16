package com.linzx.web.monitor;

import com.linzx.framework.utils.PageHelper;
import com.linzx.framework.web.BaseController;
import com.linzx.framework.web.vo.AjaxResult;
import com.linzx.system.domain.UserOnline;
import com.linzx.system.service.UserOnlineService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 在线用户 信息操作处理
 *
 * @author linzixiang
 * @date 2019_06_172
 */
@Controller
@RequestMapping("/monitor/userOnline")
public class UserOnlineController extends BaseController {

    private String prefix = "monitor/userOnline";

    @Autowired
    private UserOnlineService userOnlineService;

    @RequiresPermissions("monitor:userOnline:view")
    @GetMapping("/index")
    public String index() {
        return prefix + "/index";
    }

    /**
    * 列表查询
    */
    @RequiresPermissions("monitor:userOnline:list")
    @GetMapping("/list")
    @ResponseBody
    public AjaxResult list(UserOnline userOnline) {
        PageHelper.startPage();
        List<UserOnline> userOnlineList = userOnlineService.selectUserOnlineList(userOnline);
        return AjaxResult.success(userOnlineList);
    }

    /**
     * 删除
     */
    @RequiresPermissions("monitor:userOnline:remove")
    @PostMapping("/remove/{userOnlineId}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("userOnlineId") Long userOnlineId) {
        userOnlineService.deleteUserOnlineByIds(userOnlineId.toString());
        return AjaxResult.success();
    }


    @RequiresPermissions("monitor:userOnline:forceLogout")
    @PostMapping("/forceLogout")
    @ResponseBody
    public AjaxResult forceLogout(String[] sessionIds) {
        return AjaxResult.success();
    }

}