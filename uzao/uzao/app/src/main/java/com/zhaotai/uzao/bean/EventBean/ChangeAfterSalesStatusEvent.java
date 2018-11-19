package com.zhaotai.uzao.bean.EventBean;

/**
 * Time: 2018/8/7 0007
 * Created by LiYou
 * Description : 改变申请售后状态
 */
public class ChangeAfterSalesStatusEvent {

    public ChangeAfterSalesStatusEvent(String applyNo) {
        this.applyNo = applyNo;
    }

    public String applyNo;
}
