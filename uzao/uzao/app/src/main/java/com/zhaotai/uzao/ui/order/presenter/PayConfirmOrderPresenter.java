package com.zhaotai.uzao.ui.order.presenter;

import android.app.Activity;

import com.google.gson.Gson;
import com.pingplusplus.android.Pingpp;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.AddressBean;
import com.zhaotai.uzao.bean.ChargeBean;
import com.zhaotai.uzao.bean.CreateOrderBean;
import com.zhaotai.uzao.bean.DiscountCouponBean;
import com.zhaotai.uzao.bean.EventBean.RefreshAllOrderEvent;
import com.zhaotai.uzao.bean.EventBean.RefreshWaitPayOrderEvent;
import com.zhaotai.uzao.bean.OrderAddressBean;
import com.zhaotai.uzao.bean.PayCallBackBean;
import com.zhaotai.uzao.bean.PayInfo;
import com.zhaotai.uzao.bean.PayOrderBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.order.contract.PayConfirmOrderContract;
import com.zhaotai.uzao.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Time: 2017/7/26
 * Created by LiYou
 * Description :
 */

public class PayConfirmOrderPresenter extends PayConfirmOrderContract.Presenter {

    private PayConfirmOrderContract.View view;
    private String tradeId;
    private Activity mActivity;
    private String spuIds = "";

    public PayConfirmOrderPresenter(PayConfirmOrderContract.View view, Activity context) {
        this.view = view;
        mContext = context;
        this.mActivity = context;
    }

    /**
     * 修改订单地址
     *
     * @param addressBean 地址
     * @param orderId     订单Id
     */
    @Override
    public void modifyOrderAddress(AddressBean addressBean, String orderId) {
        OrderAddressBean orderAddressBean = new OrderAddressBean();
        orderAddressBean.orderNo = orderId;
        orderAddressBean.receiverName = addressBean.recieverName;
        orderAddressBean.receiverMobile = addressBean.recieverPhone;
        orderAddressBean.receiverAddress = addressBean.addrAlias;

        Api.getDefault().modifyOrderAddress(orderAddressBean)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String s) {

                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 获取订单列表
     *
     * @param orderIds 订单id
     */
    @Override
    public void getOrderDetail(ArrayList<String> orderIds) {
        String ids = "";
        for (int i = 0; i < orderIds.size(); i++) {
            ids += orderIds.get(i) + ",";
        }
        Api.getDefault().getPayOrderList(ids)
                .compose(RxHandleResult.<PayOrderBean>handleResult())
                .subscribe(new RxSubscriber<PayOrderBean>(mContext, false) {
                    @Override
                    public void _onNext(PayOrderBean payOrderBean) {
                        view.showOrderDetail(payOrderBean);
                        for (int i = 0; i < payOrderBean.orderList.size(); i++) {
                            for (int j = 0; j < payOrderBean.orderList.get(i).orderDetailModels.size(); j++) {
                                spuIds += payOrderBean.orderList.get(i).orderDetailModels.get(j).spuId + ",";
                            }
                        }
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }


    /**
     * 获取支付信息
     *
     * @param way      支付方式
     * @param orderIds 订单id
     * @param couponId 优惠券id
     */
    @Override
    public void getPayInfo(String way, List<String> orderIds, String couponId) {
        view.showProgress();
        PayInfo payInfo = new PayInfo();
        payInfo.orderNos = orderIds;
        payInfo.couponId = couponId;
        Api.getDefault().getPayInfo(way, payInfo)
                .compose(RxHandleResult.<ChargeBean>handleResult())
                .subscribe(new RxSubscriber<ChargeBean>(mContext, false) {
                    @Override
                    public void _onNext(ChargeBean chargeBean) {
                        //支付id
                        tradeId = chargeBean.order_no;
                        Gson gson = new Gson();
                        String data = gson.toJson(chargeBean);
                        pay(data);
                    }

                    @Override
                    public void _onError(String message) {
                        view.stopProgress();
                    }
                });
    }

    @Override
    public void callback() {
        if (StringUtil.isEmpty(tradeId)) return;
        Api.getDefault().callbackPay(tradeId, spuIds)
                .compose(RxHandleResult.<PayCallBackBean>handleResult())
                .subscribe(new RxSubscriber<PayCallBackBean>(mContext, false) {
                    @Override
                    public void _onNext(PayCallBackBean s) {
                        view.finishView();
                    }

                    @Override
                    public void _onError(String message) {
                        view.createOrderFail();
                    }
                });
    }

    @Override
    public void callback(final boolean type) {
        if (StringUtil.isEmpty(tradeId)) return;
        Api.getDefault().callbackPay(tradeId, spuIds)
                .compose(RxHandleResult.<PayCallBackBean>handleResult())
                .subscribe(new RxSubscriber<PayCallBackBean>(mContext, false) {
                    @Override
                    public void _onNext(PayCallBackBean s) {
                        //刷新界面--订单列表
                        if (type) {
                            //刷新全部订单
                            EventBus.getDefault().post(new RefreshAllOrderEvent());
                        } else {
                            //刷新待付款订单
                            EventBus.getDefault().post(new RefreshWaitPayOrderEvent());
                        }
                        view.finishView();
                    }

                    @Override
                    public void _onError(String message) {
                        view.createOrderFail();
                    }
                });
    }

    @Override
    public boolean checkData(CreateOrderBean orderInfo) {
        return false;
    }


    /**
     * 获取优惠券
     */
    @Override
    public void getCoupons(String money) {
        Api.getDefault().getNumDiscount(money)
                .compose(RxHandleResult.<List<DiscountCouponBean>>handleResult())
                .subscribe(new RxSubscriber<List<DiscountCouponBean>>(mContext, false) {
                    @Override
                    public void _onNext(List<DiscountCouponBean> discountCouponBeen) {
                        view.showDiscount(discountCouponBeen);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    @Override
    public void pay(String data) {
        view.stopProgress();
        //根据返回值 用ping++拉起支付页面
        Pingpp.createPayment(mActivity, data);
    }
}
