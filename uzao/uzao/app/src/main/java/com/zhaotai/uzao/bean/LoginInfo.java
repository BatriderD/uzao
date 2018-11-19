package com.zhaotai.uzao.bean;

/**
 * Time: 2017/5/10
 * Created by LiYou
 * Description :
 */

public class LoginInfo {

    public LoginInfo(String loginId,String identifyingCode){
        this.loginId = loginId;
        this.identifyingCode = identifyingCode;
    }

    public String loginId;
    public String identifyingCode;
}
