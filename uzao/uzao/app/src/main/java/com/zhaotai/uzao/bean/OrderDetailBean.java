package com.zhaotai.uzao.bean;

/**
 * Time: 2017/6/9
 * Created by LiYou
 * Description : 订单详情界面
 */

public class OrderDetailBean {

    public OrderDate orderDates;

    public OrderBean orderModel;

    public DeliveryInfo deliveryInfo;

    public TradeInfo tradeInfo;

    public class DeliveryInfo {
        public String transportNo;
        public String transportInfo;
        public String transportName;
    }

    public class OrderDate {
        public String submit;
        public String pay;
        public String confirmReceive; //确认收货时间
        public String close;//关闭时间
        public String cancel;//关闭时间
        public String unapproved;//关闭时间
        public String finishProduce;//完成生产
        public String autoClose;//自动关闭
    }

    public class TradeInfo {
        public String tradeNo;
        public String tradeTime;
        public String tradeChannel;
    }

}
