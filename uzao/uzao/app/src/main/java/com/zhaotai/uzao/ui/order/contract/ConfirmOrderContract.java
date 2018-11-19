package com.zhaotai.uzao.ui.order.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.ActivityModelBean;
import com.zhaotai.uzao.bean.AddressBean;
import com.zhaotai.uzao.bean.CreateOrderBean;

/**
 * Time: 2017/5/31
 * Created by LiYou
 * Description :
 */

public interface ConfirmOrderContract {

    interface View extends BaseView {
        //显示默认地址
        void showDefaultAddress(AddressBean addressBeen);

        //显示添加地址
        void showAddAddress();

        //创建订单失败
        void createOrderFail();

        void finishView();
    }

    abstract class Presenter extends BasePresenter {
        //获取地址
        public abstract void getAddress();

        //创建订单
        public abstract void createOrder(CreateOrderBean orderInfo);

        public abstract void createDesignOrder(CreateOrderBean orderInfo);

        //检查数据完成性
        public abstract boolean checkData(CreateOrderBean orderInfo);

    }
}
