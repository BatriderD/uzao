package com.zhaotai.uzao.bean;

/**
 * Time: 2017/8/22
 * Created by LiYou
 * Description :  生产订单
 */

public class ProductOrderBean {
    //订单号
    public String orderNo;
    //订单状态
    public String orderStatus;
    //订单类型
    public String orderType;
    //商品信息
    public String orderProfile;
    //订单总额
    public String totalAmountY;
    //运费
    public String freightAmountY;

    //数量
    public String count;

    //pay 在线支付 collectFreight到付
    public String freightType;
}
