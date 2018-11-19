package com.zhaotai.uzao.bean;

/**
 * description: 注册请求实体类
 * author : zp
 * date: 2017/7/18
 */

public class RegisterRequestBean {

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdentifyingCode() {
        return identifyingCode;
    }

    public void setIdentifyingCode(String identifyingCode) {
        this.identifyingCode = identifyingCode;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public RegisterRequestBean(String loginId, String password, String identifyingCode) {
        this.loginId = loginId;
        this.password = password;
        this.identifyingCode = identifyingCode;
        this.agency = "SUPER_ADMIN";
    }

    private String loginId;
    private String password;
    private String identifyingCode;
    private String agency;


}
