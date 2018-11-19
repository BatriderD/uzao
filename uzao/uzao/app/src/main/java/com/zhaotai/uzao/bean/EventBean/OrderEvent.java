package com.zhaotai.uzao.bean.EventBean;

/**
 * Time: 2017/6/14
 * Created by LiYou
 * Description : 用于订单列表刷新
 */

public class OrderEvent {
    public static final int ALL_ORDER = 1;//全部订单
    public static final int WAIT_PAY = 2;//待付款
    public static final int WAIT_RECEIVE = 3;//待付款
    public int refreshAllOrder ;
    public int refreshWaitPay ;
    public int refreshWaitReceive ;
}
