package com.zhaotai.uzao.bean;

/**
 * description: 第三方登录请求实体类
 * author : zp
 * date: 2017/7/21
 */

public class RequestThirdPartLoginBean {
    public RequestThirdPartLoginBean(String loginId) {
        this.loginId = loginId;
    }

    String loginId;
}
