package com.zhaotai.uzao.ui.order.presenter;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.pingplusplus.android.Pingpp;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.ChargeBean;
import com.zhaotai.uzao.bean.EventBean.RefreshAllOrderEvent;
import com.zhaotai.uzao.bean.EventBean.RefreshWaitPayOrderEvent;
import com.zhaotai.uzao.bean.MaterialDetailBean;
import com.zhaotai.uzao.bean.OrderGoodsBean;
import com.zhaotai.uzao.bean.PayCallBackBean;
import com.zhaotai.uzao.bean.PayInfo;
import com.zhaotai.uzao.bean.post.RewardInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.order.contract.MaterialPayConfirmContract;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Time: 2018/1/9
 * Created by LiYou
 * Description :
 */

public class MaterialPayConfirmPresenter extends MaterialPayConfirmContract.Presenter {

    private MaterialPayConfirmContract.View mView;
    private String tradeId;
    private String orderId;

    public MaterialPayConfirmPresenter(MaterialPayConfirmContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    /**
     * 从详情页 立即购买
     *
     * @param way  支付方式
     * @param data 素材信息
     */
    public void getPayInfoFromBuyNow(final String way, List<MaterialDetailBean> data) {
        if (data == null) return;
        List<String> materialIds = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            materialIds.add(data.get(i).sequenceNBR);
        }
        mView.showProgress();
        Api.getDefault().createMaterialOrder(materialIds)
                .compose(RxHandleResult.<List<OrderGoodsBean>>handleResult())
                .subscribe(new RxSubscriber<List<OrderGoodsBean>>(mContext) {
                    @Override
                    public void _onNext(List<OrderGoodsBean> orderGoodsBeen) {
                        List<String> orderIds = new ArrayList<>();
                        for (int i = 0; i < orderGoodsBeen.size(); i++) {
                            orderIds.add(orderGoodsBeen.get(i).orderNo);
                        }
                        if (orderGoodsBeen.size() > 0) {
                            setOrderId(orderGoodsBeen.get(0).orderNo);
                        }
                        getPayInfoFromOrder(way, orderIds);
                    }

                    @Override
                    public void _onError(String message) {
                        mView.stopProgress();
                        ToastUtil.showShort("支付失败");
                    }
                });
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public ArrayList<String> getOrderId() {
        ArrayList<String> orderIds = new ArrayList<>();
        if (orderId != null) {
            orderIds.add(this.orderId);
        }
        return orderIds;
    }

    /**
     * 从订单页支付
     */
    public void getPayInfoFromOrder(String way, List<String> orderIds) {
        PayInfo payInfo = new PayInfo();
        payInfo.orderNos = orderIds;
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
                        mView.stopProgress();
                    }
                });
    }

    /**
     * 打赏
     */
    public void getPayInfoReward(String way, RewardInfo materialInfo) {
        mView.showProgress();
        Api.getDefault().getAwardPayInfo(way, materialInfo)
                .compose(RxHandleResult.<ChargeBean>handleResult())
                .subscribe(new RxSubscriber<ChargeBean>(mContext) {
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
                        mView.stopProgress();
                        ToastUtil.showShort("打赏失败");
                    }
                });
    }

    public void pay(String data) {
        mView.stopProgress();
        //根据返回值 用ping++拉起支付页面
        Pingpp.createPayment((Activity) mContext, data);
    }

    public void callback() {
        if (StringUtil.isEmpty(tradeId)) return;
        Api.getDefault().callbackPay(tradeId, "")
                .compose(RxHandleResult.<PayCallBackBean>handleResult())
                .subscribe(new RxSubscriber<PayCallBackBean>(mContext, false) {
                    @Override
                    public void _onNext(PayCallBackBean s) {
                        mView.finishView();
                    }

                    @Override
                    public void _onError(String message) {
                    }
                });
    }

    public void callback(final boolean type) {
        if (StringUtil.isEmpty(tradeId)) return;
        Api.getDefault().callbackPay(tradeId, "")
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
                        mView.finishView();
                    }

                    @Override
                    public void _onError(String message) {
                    }
                });
    }
}
