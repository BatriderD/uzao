package com.zhaotai.uzao.bean;

/**
 * description:
 * author : zp
 * date: 2017/7/24
 */

public class RequestBindPhoneBean {
    //    {@"oldMobile":@"",
//        @"mobile":self.textFieldPhone.text,
//        @"identifyingCode":self.textFieldSMSCode.text,
//        @"password":@""};
    private String oldMobile;
    private String mobile;
    private String identifyingCode;
    private String password;

    @Override
    public String toString() {
        return "RequestBindPhoneBean{" +
                "oldMobile='" + oldMobile + '\'' +
                ", mobile='" + mobile + '\'' +
                ", identifyingCode='" + identifyingCode + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getOldMobile() {
        return oldMobile;
    }

    public void setOldMobile(String oldMobile) {
        this.oldMobile = oldMobile;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RequestBindPhoneBean(String mobile, String identifyingCode) {

        this.oldMobile = "";
        this.mobile = mobile;
        this.identifyingCode = identifyingCode;
        this.password = "";
    }
}
