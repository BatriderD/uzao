package com.zhaotai.uzao.bean.EventBean;

/**
 * Time: 2017/7/29
 * Created by LiYou
 * Description : 删除订单
 */

public class RemoveOrderEvent {

    public RemoveOrderEvent(String orderNo) {
        this.orderNo = orderNo;
    }

    public String orderNo;
}
