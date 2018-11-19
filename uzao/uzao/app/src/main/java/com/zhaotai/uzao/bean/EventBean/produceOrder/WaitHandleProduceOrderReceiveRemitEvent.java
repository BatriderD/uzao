package com.zhaotai.uzao.bean.EventBean.produceOrder;

/**
 * Time: 2017/8/29
 * Created by LiYou
 * Description : 待处理生产订单 -- 确认汇款
 */

public class WaitHandleProduceOrderReceiveRemitEvent {

    public WaitHandleProduceOrderReceiveRemitEvent(int position, String orderStatus) {
        this.position = position;
        this.orderStatus = orderStatus;
    }

    public int position;
    public String orderStatus;

}
