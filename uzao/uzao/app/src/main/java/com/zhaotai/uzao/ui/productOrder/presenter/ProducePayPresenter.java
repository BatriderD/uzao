package com.zhaotai.uzao.ui.productOrder.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.ChargeBean;
import com.zhaotai.uzao.bean.PayCallBackBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.productOrder.contract.ProducePayContract;

/**
 * Time: 2017/8/23
 * Created by LiYou
 * Description :
 */

public class ProducePayPresenter extends ProducePayContract.Presenter {

    private ProducePayContract.View view;
    private Gson gson;
    private String tradeId;

    public ProducePayPresenter(Context context,ProducePayContract.View view){
        mContext = context;
        this.view = view;
        gson = new Gson();
    }

    /**
     *
     * @param source 首款（firstPay）,尾款（lastPay）、全部(pay)
     */
    @Override
    public void getProducePayInfo(String payWay,String orderNo, String source) {
        Api.getDefault().getProducePayInfo(payWay,new PayInfo(orderNo,source))
                .compose(RxHandleResult.<ChargeBean>handleResult())
                .subscribe(new RxSubscriber<ChargeBean>(mContext) {
                    @Override
                    public void _onNext(ChargeBean chargeBean) {
                        tradeId = chargeBean.order_no;
                        String payInfo = gson.toJson(chargeBean);
                        view.pay(payInfo);
                    }

                    @Override
                    public void _onError(String message) {
                        view.dismissProgress();
                    }
                });
    }

    @Override
    public void callBack(boolean type) {
        Api.getDefault().callbackPay(tradeId,"")
                .compose(RxHandleResult.<PayCallBackBean>handleResult())
                .subscribe(new RxSubscriber<PayCallBackBean>(mContext, false) {
                    @Override
                    public void _onNext(PayCallBackBean s) {
                        view.finishView();
                    }

                    @Override
                    public void _onError(String message) {
                    }
                });
    }

    public class PayInfo {
         PayInfo (String orderNo,String source){
            this.orderNo = orderNo;
            this.source = source;
        }
        public String orderNo;
        public String source;
    }
}
