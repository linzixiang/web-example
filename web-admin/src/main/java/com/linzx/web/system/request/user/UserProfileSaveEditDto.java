package com.linzx.web.system.request.user;

/**
 * 个人中心修改保存
 */
public class UserProfileSaveEditDto {

    /** 用户昵称 */
    private String userNickname;
    /** 手机号码 */
    private String phoneNumber;
    /** 用户邮箱 */
    private String email;
    /** 用户性别 （0男 1女 2未知） */
    private Integer sex;
    /** 头像图片id **/
    private String avatarImgId;

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getAvatarImgId() {
        return avatarImgId;
    }

    public void setAvatarImgId(String avatarImgId) {
        this.avatarImgId = avatarImgId;
    }
}
