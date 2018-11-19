package com.zhaotai.uzao.bean;

/**
 * description: 修改密码请求实体类
 * author : zp
 * date: 2017/7/18
 */

public class ChangePasswordRequestBean {

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public ChangePasswordRequestBean(String oldPassword, String newPassword) {

        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    private String oldPassword;
    private String newPassword;


}
