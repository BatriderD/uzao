package com.zhaotai.uzao.bean.EventBean.produceOrder;

/**
 * Time: 2017/8/24
 * Created by LiYou
 * Description :待处理订单 大货样 确认收货
 */

public class WaitHandleSampleProduceReceiveEvent {
    public WaitHandleSampleProduceReceiveEvent(int position,String orderStatus) {
        this.position = position;
        this.orderStatus = orderStatus;
    }

    public int position;
    public String orderStatus;
}
