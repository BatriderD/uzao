package com.zhaotai.uzao.bean;

/**
 * Time: 2017/8/26
 * Created by LiYou
 * Description : 汇款识别码
 */

public class CompanyRemitBean {
    public String orderNo;
    public String source;
    public String tradeNo;
    public String tradeId;
    public String userId;
    public String mobile;
    public BankInfo bankInfo;

    public class BankInfo {
        public String openingBank;
        public String userName;
        public String account;
    }

}
