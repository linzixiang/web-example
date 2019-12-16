package com.linzx.web.system;

import com.linzx.framework.web.vo.AjaxResult;
import com.linzx.utils.MapUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class SysLoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @PostMapping("/login")
    @ResponseBody
    public AjaxResult login(@RequestParam("userName") String userName,
                            @RequestParam(value = "password") String password) {
        UsernamePasswordToken token  = new UsernamePasswordToken(userName, password, false);
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
        Map<String, Object> retMap = MapUtils.genHashMap("token", subject.getSession().getId());
        return AjaxResult.success(retMap);
    }

}
