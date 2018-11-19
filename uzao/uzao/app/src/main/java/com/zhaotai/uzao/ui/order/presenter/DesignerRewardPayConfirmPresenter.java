package com.zhaotai.uzao.ui.order.presenter;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.pingplusplus.android.Pingpp;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.ChargeBean;
import com.zhaotai.uzao.bean.EventBean.RefreshAllOrderEvent;
import com.zhaotai.uzao.bean.EventBean.RefreshWaitPayOrderEvent;
import com.zhaotai.uzao.bean.PayCallBackBean;
import com.zhaotai.uzao.bean.post.RewardInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.order.contract.DesignerPayConfirmContract;
import com.zhaotai.uzao.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Time: 2018/1/9
 * Created by LiYou
 * Description : 设计师打赏确认订单
 */

public class DesignerRewardPayConfirmPresenter extends DesignerPayConfirmContract.Presenter {

    private DesignerPayConfirmContract.View mView;
    private final RewardInfo info;
    private String tradeId;

    public DesignerRewardPayConfirmPresenter(DesignerPayConfirmContract.View view, Context context) {
        mView = view;
        mContext = context;
        info = new RewardInfo();
        info.materialIds = new ArrayList<>();
    }


    /**
     * 打赏
     */
    public void getPayInfoReward(String way, RewardInfo materialInfo) {
        mView.showProgress();
        Api.getDefault().getDesignerAwardPayInfo(way, materialInfo)
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
