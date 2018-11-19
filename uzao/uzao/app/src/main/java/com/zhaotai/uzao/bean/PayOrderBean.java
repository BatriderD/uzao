package com.zhaotai.uzao.bean;

import java.util.List;

/**
 * Time: 2017/7/26
 * Created by LiYou
 * Description : 支付确认
 */

public class PayOrderBean {
    public String payPrice;
    public String canUseCouponPrice;
    public List<PayOrderDetailBean> orderList;
    public String activityPrice;
    public String totalPrice;
}
