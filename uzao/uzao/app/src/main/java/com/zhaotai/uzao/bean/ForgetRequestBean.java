package com.zhaotai.uzao.bean;

/**
 * description: 找回密码请求实体类
 * author : zp
 * date: 2017/7/18
 */

public class ForgetRequestBean {

    public ForgetRequestBean(String loginId, String newPassword, String identifyingCode) {
        this.loginId = loginId;
        this.newPassword = newPassword;
        this.mobile = loginId;
        this.identifyingCode = identifyingCode;
    }

    private String loginId;
    private String newPassword;
    private String mobile;
    private String identifyingCode;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdentifyingCode() {
        return identifyingCode;
    }

    public void setIdentifyingCode(String identifyingCode) {
        this.identifyingCode = identifyingCode;
    }
}
