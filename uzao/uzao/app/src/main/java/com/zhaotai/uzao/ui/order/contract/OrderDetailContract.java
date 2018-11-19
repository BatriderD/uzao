package com.zhaotai.uzao.ui.order.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.OrderDetailBean;

/**
 * Time: 2018/7/16 0016
 * Created by LiYou
 * Description :
 */
public interface OrderDetailContract {

    interface View extends BaseView {
        //显示详情
        void showDetail(OrderDetailBean orderDetailBean);

        //关闭界面
        void finishView();
    }

    abstract class Presenter extends BasePresenter {
        //获取订单详情
        public abstract void getOrderDetail(String orderId);
    }
}
