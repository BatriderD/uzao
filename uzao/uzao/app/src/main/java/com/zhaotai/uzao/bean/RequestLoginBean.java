package com.zhaotai.uzao.bean;

/**
 * description:  登录请求实体类
 * author : zp
 * date: 2017/7/18
 */

public class RequestLoginBean {
    private String loginId;
    private String password;

    public RequestLoginBean(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }

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
}
