package com.zhaotai.uzao.bean;

/**
 * description: 账号密码登录成功解析类
 * author : zp
 * date: 2017/7/20
 */

public class PsdAndAccLoginSuccessBean {

    /**
     * expire : 2592000
     * loginId : 18966601631
     * token : 223e50c4-5c8b-45be-933a-63b37601bd71
     * userId : 887595293435027456
     */

    private String expire;
    private String loginId;
    private String token;
    private String userId;

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
