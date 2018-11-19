package com.zhaotai.uzao.ui.order.contract;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.DictionaryBean;
import com.zhaotai.uzao.bean.OrderBean;
import com.zhaotai.uzao.bean.OrderDetailBean;
import com.zhaotai.uzao.bean.PageInfo;

import java.util.List;

/**
 * Time: 2017/6/6
 * Created by LiYou
 * Description :
 */

public interface OrderContract {

    interface View extends BaseSwipeView {
        //显示订单列表
        void showOrderList(PageInfo<OrderBean> data);

        //显示订单取消原因
        void showCancelReason(List<DictionaryBean> dictionaryBeanList);
    }

    interface WaitPayView extends BaseView {

        //显示详情
        void showDetail(OrderDetailBean orderDetailBean);

        //取消订单
        void cancelOrder();

        //更新倒计时
        void updateTime(String time);

        //时间到期
        void finishView();

        //显示订单取消原因
        void showCancelReason(List<DictionaryBean> dictionaryBeanList);
    }

    //待审核 待发货
    interface WaitApproveAndDeliveryView extends BaseView {
        //显示详情
        void showDetail(OrderDetailBean orderDetailBean);

        void finishView();
    }

    //待收货
    interface WaitReceiveView extends BaseView {
        //显示详情
        void showDetail(OrderDetailBean orderDetailBean);

        //更新倒计时
        void updateTime(String time);

        //时间到期
        void finishView();
    }

    abstract class Presenter extends BasePresenter {
        //获取全部订单
        public abstract void getAllOrderList(int start, boolean isLoading);

        //获取待付款订单
        public abstract void getWaitPayOrderList(int start, boolean isLoading);

        //获取待审核订单
        public abstract void getWaitApproveOrderList(int start, boolean isLoading);

        //获取待发货订单
        public abstract void getWaitDeliveryOrderList(int start, boolean isLoading);

        //获取待收货订单
        public abstract void getWaitReceiveOrderList(int start, boolean isLoading);

        //订单详情 - 待付款
        public abstract void getOrderDetail(String orderId);

        //订单详情 -待审核 待发货
        public abstract void getWaitApproveAndDeliveryOrderDetail(String orderId);

        //订单详情 - 待收货
        public abstract void getWaitReceiveOrderDetail(String orderId);


        //删除订单
        public abstract void deleteOrder(String orderId, BaseQuickAdapter adapter, int position);


        //取消订单
        public abstract void cancelOrder(String orderId, String cancelReason);

        //待付款详情取消订单
        public abstract void waitPayDetailCancelOrder(String orderId, String cancelReason, int position);

        //确认收货
        public abstract void confirmReceiveGoods(String packageOrderId);

        //倒计时
        public abstract void countDown(int remainingTime);

        //倒计时 -间隔没分钟
        public abstract void countMinDown(int remainingTime);
    }
}
