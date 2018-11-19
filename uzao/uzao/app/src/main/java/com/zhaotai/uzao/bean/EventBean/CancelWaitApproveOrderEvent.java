package com.zhaotai.uzao.bean.EventBean;

/**
 * Time: 2017/7/29
 * Created by LiYou
 * Description : 取消订单 -- 用于待审核列表
 */

public class CancelWaitApproveOrderEvent {

    public CancelWaitApproveOrderEvent(int position) {
        this.position = position;
    }

    public int position;
}
