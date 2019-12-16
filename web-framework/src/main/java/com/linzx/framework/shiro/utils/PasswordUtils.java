package com.linzx.framework.shiro.utils;

import com.linzx.framework.shiro.bean.UserLoginInfo;
import com.linzx.utils.StringUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * 密码工具
 */
public class PasswordUtils {

    /**
     * 检查密码是否正确
     * @param loginAccount 登录帐号
     * @param salt 加密随机数
     * @param dbPasswod 数据库中的摩玛
     * @param inputPassword 客户输入的密码
     * @return
     */
    public static boolean matches(String loginAccount, String salt, String dbPasswod, String inputPassword) {
        String encryptPassword = encryptPassword(loginAccount, inputPassword, salt);
        return StringUtils.equals(dbPasswod, encryptPassword);
    }

    /**
     * 密码MD5加密后返回
     * @param username 用户名
     * @param password 密码
     * @param salt 随机数
     * @return 加密后的结果
     */
    public static String encryptPassword(String username, String password, String salt) {
        return new Md5Hash(username + password + salt).toHex().toString();
    }

    /**
     * 生成随机数，用于加密
     */
    public static String randomSalt() {
        SecureRandomNumberGenerator secureRandom = new SecureRandomNumberGenerator();
        String hex = secureRandom.nextBytes(3).toHex();
        return hex;
    }

    public static void main(String[] args) {
        System.out.println(randomSalt());
    }
}
