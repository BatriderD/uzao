package com.zhaotai.uzao.bean.EventBean;

/**
 * Time: 2017/7/29
 * Created by LiYou
 * Description : 取消订单-- 用于全部订单
 */

public class CancelOrderAllEvent {

    public CancelOrderAllEvent(String orderNo) {
        this.orderNo = orderNo;
    }

    public String orderNo;
}
