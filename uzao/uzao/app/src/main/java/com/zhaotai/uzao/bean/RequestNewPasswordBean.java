package com.zhaotai.uzao.bean;

/**
 * description: 设置新密码请求实体类
 * author : zp
 * date: 2017/7/18
 */

public class RequestNewPasswordBean {


    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    private String newPassword;

    public RequestNewPasswordBean(String newPassword) {
        this.newPassword = newPassword;
    }
}
