package com.zhaotai.uzao.ui.login.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.login.contract.Register1Contract;

/**
 * description:
 * author : zp
 * date: 2017/8/2
 */

public class Register1Presenter extends Register1Contract.Presenter {
    private Register1Contract.View mView;

    public Register1Presenter(Context context, Register1Contract.View view) {
        mContext = context;
        mView = view;

    }

    /**
     * 获取验证码
     */
    @Override
    public void getSmsCode(String iphone) {
        //检查手机号码
        Api.getDefault().getSmsCode(iphone)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, false) {
                    @Override
                    public void _onNext(String s) {
                        mView.getSmsSuccess();
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }
}
