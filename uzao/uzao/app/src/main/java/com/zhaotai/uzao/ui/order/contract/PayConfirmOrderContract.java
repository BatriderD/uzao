package com.zhaotai.uzao.ui.order.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.AddressBean;
import com.zhaotai.uzao.bean.CreateOrderBean;
import com.zhaotai.uzao.bean.DiscountCouponBean;
import com.zhaotai.uzao.bean.PayOrderBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Time: 2017/5/31
 * Created by LiYou
 * Description :
 */

public interface PayConfirmOrderContract {

    interface View extends BaseView {
        //显示默认地址
        void showDefaultAddress(AddressBean addressBeen);

        //显示添加地址
        void showAddAddress();

        //订单详情
        void showOrderDetail(PayOrderBean payOrderBean);

        //创建订单失败
        void createOrderFail();

        //有无可用优惠券
        void showDiscount(List<DiscountCouponBean> discountCouponList);

        //关闭界面
        void finishView();

        void showProgress();

        void stopProgress();
    }

    abstract class Presenter extends BasePresenter {

        //修改订单地址
        public abstract void modifyOrderAddress(AddressBean addressBean, String orderId);

        //获取订单详情
        public abstract void getOrderDetail(ArrayList<String> orderId);

        //获取支付信息
        public abstract void getPayInfo(String way, List<String> orderIds, String couponId);

        //支付回调
        public abstract void callback();

        public abstract void callback(boolean type);

        //检查数据完成性
        public abstract boolean checkData(CreateOrderBean orderInfo);

        //获取可用的优惠券
        public abstract void getCoupons(String money);

        //支付
        public abstract void pay(String data);
    }
}
