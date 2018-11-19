package com.zhaotai.uzao.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Time: 2017/7/27
 * Created by LiYou
 * Description : 订单id
 */

public class OrderBean implements Serializable{
    public String orderNo;
    public String orderStatus;
    public String payAmount;
    public String payAmountY;
    public String totalAmount;
    public String totalAmountY;
    public String transportNo;
    public String extend1;
    public String receiverAddress;
    public String receiverMobile;
    public String receiverName;
    public String orderType;
    public OrderInvoiceBean orderInvoiceModel;
    public String isComment;
    public String activityAmountY;//立减金额
    public String couponAmountY;//优惠金额
    public String remainingTime;//倒计时
    public List<OrderGoodsBean> orderDetailModels;
    public List<OrderMaterialBean> materialOrderDetailModels;

    public OrderPackage orderPackage;

    public boolean isSelectd;

    public class OrderPackage {

        @SerializedName("package")
        public List<PackageBean> packageBig;

        public List<OrderItemBean> unDelivery;

    }
}
