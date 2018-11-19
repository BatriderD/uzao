package com.zhaotai.uzao.ui.productOrder.contract;

import android.content.Context;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ProductOrderBean;
import com.zhaotai.uzao.bean.ProductOrderDetailBean;

/**
 * Time: 2017/6/6
 * Created by LiYou
 * Description :
 */

public interface ProductOrderContract {

    interface AllView extends BaseSwipeView {
        //显示订单列表
        void showAllProductOrderList(PageInfo<ProductOrderBean> data);
    }

    interface CompleteAndCloseView extends BaseSwipeView {
        void showProductOrderList(PageInfo<ProductOrderBean> data);
    }

    interface ProductOrderDetailView extends BaseView {
        void showProductOrderDetail(ProductOrderDetailBean productOrderDetail);

        void finishView();
    }

    abstract class AllPresenter extends BasePresenter {
        //获取全部生产订单
        public abstract void getAllProductOrderList(int start, boolean isLoading);

        //获取待处理生产订单
        public abstract void getWaitHandleProductOrderList(int start, boolean isLoading);

        //确认收货 -- 样品的确认收货
        public abstract void receiveSampleProduct(String orderNo, String type, int position, boolean source, String orderStatus);

        //确认收货 -- 打样订单收货、大货和大货打样批量生产收货
        public abstract void receiveProduct(String orderNo, int position, boolean source, String orderStatus);
    }

    abstract class CompleteAndClosePresenter extends BasePresenter {
        //获取完成生产订单
        public abstract void getCompleteProductOrderList(int start, boolean isLoading);

        //获取关闭生产订单
        public abstract void getCloseProductOrderList(int start, boolean isLoading);
    }

    abstract class ProductOrderDetailPresenter extends BasePresenter {
        //获取订单详情
        public abstract void getProductOrderDetail(String status);

        //确认收货 --样品
        public abstract void receiveSampleProduct(String orderNo, String type, int position, boolean source, String orderStatus);

        //确认收货
        public abstract void receiveProduct(String orderNo, int position, boolean source, String orderStatus);

        //IM聊天
        public abstract void goToIM();

        //获取支付信息
        public abstract void getPayInfo(Context context, ProductOrderDetailBean data);

        //首付支付信息
        public abstract void firstPayInfo(Context context, ProductOrderDetailBean data);

        //尾款支付信息
        public abstract void lastPayInfo(Context context, ProductOrderDetailBean data);

        //样品物流信息
        public abstract void sampleLogistics(Context context, ProductOrderDetailBean data);

        //大货样品物流信息
        public abstract void sampleProduceLogistics(Context context, ProductOrderDetailBean data);

        //物流信息
        public abstract void logistics(Context context, String orderNo);

        //确认汇款
        public abstract void confirmCallback(String tradeId, String orderStatus, int position,boolean source);

        public abstract void showProgress();

        public abstract void stopProgress();
    }
}
