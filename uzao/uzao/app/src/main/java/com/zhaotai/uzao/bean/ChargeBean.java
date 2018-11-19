package com.zhaotai.uzao.bean;

import java.util.List;

/**
 * Time: 2017/6/13
 * Created by LiYou
 * Description : ping++支付
 */

public class ChargeBean {

    public String body;
    public String subject;
    public String object;
    public String order_no;
    public String client_ip;
    public String currency;
    public String failure_code;
    public String id;
    public String amount;
    public String paid;
    public String refunded;
    public String created;
    public String description;
    public String amount_refunded;
    public String livemode;
    public String amount_settle;
    public Metadata metadata;
    public String time_paid;
    public String app;
    public Extra extra;
    public Credential credential;
    public String time_settle;
    public String time_expire;
    public String failure_msg;
    public Refunds refunds;
    public String transaction_no;
    public String channel;

    public class Metadata {
        public String orderNo;
        public String type;
        public String tradeItems;
    }

    private class Extra {}

    private class Credential {
        public Alipay alipay;
        public Wx wx;
        public String object;
    }

    private class Alipay {
        public String orderInfo;
    }

    private class Wx {
        public String sign;
        public String appId;
        public String timeStamp;
        public String partnerId;
        public String packageValue;
        public String nonceStr;
        public String prepayId;
    }

    private class Refunds {
        public List data;
        public boolean has_more;
        public String object;
        public String url;
    }
}
