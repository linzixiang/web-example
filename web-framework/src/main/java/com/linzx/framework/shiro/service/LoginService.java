package com.linzx.framework.shiro.service;

import com.linzx.framework.exception.UnknownAccountException;
import com.linzx.framework.exception.UserPasswordNotMatchException;
import com.linzx.framework.shiro.bean.UserLoginInfo;

import java.util.Set;

public interface LoginService {

    /**
     * 用户登录
     */
    UserLoginInfo loginByAccountAndPwd(String account, String password) throws UnknownAccountException, UserPasswordNotMatchException;

    /**
     * 无密码登陆
     */
    UserLoginInfo loginByAccount(String account) throws UnknownAccountException;

}
