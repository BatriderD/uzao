package com.zhaotai.uzao.ui.person.discount.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.app.AppConfig;
import com.zhaotai.uzao.bean.DiscountCouponBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.person.discount.contract.SelectedCouponContract;
import com.zhaotai.uzao.utils.SPUtils;

import java.util.List;

/**
 * Time: 2017/7/19
 * Created by LiYou
 * Description :优惠券选择页面
 */

public class SelectedCouponPresenter extends SelectedCouponContract.Presenter {

    private SelectedCouponContract.View view;
    private String userId;

    public SelectedCouponPresenter(SelectedCouponContract.View view, Context context) {
        this.view = view;
        mContext = context;
        userId = SPUtils.getSharedStringData(AppConfig.USER_ID);
    }

    @Override
    public void getCouponList(final boolean isLoading,String money) {
        Api.getDefault().getNumDiscount(money)
                .compose(RxHandleResult.<List<DiscountCouponBean>>handleResult())
                .subscribe(new RxSubscriber<List<DiscountCouponBean>>(mContext, false) {
                    @Override
                    public void _onNext(List<DiscountCouponBean> discountCouponBeen) {
                        if (isLoading) {
                            view.showContent();
                        } else {
                            view.stopRefresh();
                        }
                        view.showDiscountList(discountCouponBeen);
                    }

                    @Override
                    public void _onError(String message) {
                        view.showNetworkFail(message);
                    }
                });
    }
}
