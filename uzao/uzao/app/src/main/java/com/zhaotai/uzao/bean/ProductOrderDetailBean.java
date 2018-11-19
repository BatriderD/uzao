package com.zhaotai.uzao.bean;

import java.util.List;

/**
 * Time: 2017/8/22
 * Created by LiYou
 * Description :
 */

public class ProductOrderDetailBean {

    public String orderStatus;
    public String orderType;

    //pay 在线支付  collectFreight 到付
    public String freightType;

    //打样数量
    public String sampleCount;
    //生产数量
    public String count;

    //批量地址
    public List<BatchAddress> batchAddress;

    //地址
    public String receiverAddress;
    public String receiverMobile;
    public String receiverName;

    //关闭理由
    public String closeReason;

    //订单时间
    public OrderDate orderDates;

    //订单价格
    public OrderPrices orderPrices;
    //支付方式
    public String extend2;
    public String extend3;

    //发票信息
    public String invoiceType;
    public String invoiceTitle;
    public String invoiceContent;
    public String invoiceNumber;

    //生产信息
    public ProduceInfo produceInfos;

    //支付信息
    public List<TradeInfo> tradeInfos;

    //物流信息
    public List<TransportInfo> transportInfos;

    public class BatchAddress {
        public String receiverAddress;
        public String receiverMobile;
        public String receiverName;
        public String transportId;
    }

    public class TransportInfo {
        public String transportId;
        public String transportTypeName;
        public String transportType;
    }

    public class TradeInfo {
        public String tradeNo;
        public String tradeTime;
        public String tradeChannel;
        public String tradeChannelName;
        public String payType;
    }

    public class ProduceInfo {
        public Produce produce;
        public String productPeriodic;
    }

    public class Produce {
        //生产数量
        public String produceCount;
        //打样数量
        public String sampleCount;
        //生产类型
        public String produceType;
        //设计信息
        public CustomDesignModel customDesignModel;
    }

    public class CustomDesignModel {
        public String designName;
        public List<String> effectImages;//效果图

    }


    public class OrderDate {
        public String submit;
        public String firstPay;
        public String lastPay;
        public String pay;
        public String samplingConfirmReceive;//打样确认时间
        public String produceConfirmReceive;//大货样确认时间
        public String confirmReceive; //确认收货时间
        public String produceDeliver;
        public String close;//关闭时间
        public String cancel;//关闭时间
        public String unapproved;//关闭时间
        public String finishProduce;//完成生产
        public String autoClose;//自动关闭
    }

    public class OrderPrices {
        public String total;
        public String freightY;
        public String firstPay;
        public String totalY;
        public String lastPay;
        public String freight;
        public String firstPayY;
        public String lastPayY;
    }

    //周期
    public class Periodic {
        // 打样周期
        public String samplePeriodic;
        // 生产周期
        public String productPeriodic ;
        // 大货样生产周期
        public String produceSmaplePeriodic ;
    }
}
