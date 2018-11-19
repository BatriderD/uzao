package com.zhaotai.uzao.ui.login.presenter;

import android.content.Context;
import android.util.Log;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.RequestBindPhoneBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.login.contract.BindPhoneContract;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * description:
 * author : zp
 * date: 2017/7/19
 */

public class BindPhonePresenter extends BindPhoneContract.Presenter {
    private BindPhoneContract.View mView;
    private static final String TAG = "BindPhonePresenter";

    public BindPhonePresenter(Context context, BindPhoneContract.View view) {
        mContext = context;
        mView = view;
    }

    /**
     * 倒计时
     */
    @Override
    public void countDown() {
        final int countTime = 60;
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(countTime + 1)
                .map(new Function<Long, Long>() {

                    @Override
                    public Long apply(@NonNull Long aLong) throws Exception {
                        return countTime - aLong;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<Long>(mContext, false) {
                    @Override
                    public void _onNext(Long aLong) {
                        mView.refreshCountDown(aLong);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });

    }

    /**
     * 获取验证码
     */
    @Override
    public void getSmsCode(String iphone) {
        //检查手机号码
        countDown();
        Api.getDefault().getSmsCode(iphone)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, false) {
                    @Override
                    public void _onNext(String s) {
                        Log.d(TAG, "获取_onNext: 验证码获取成功");
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    @Override
    public void bindPhone(RequestBindPhoneBean bean) {
        checkData(bean);
        Api.getDefault().bindPhoneNumber(bean)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String s) {
                        System.out.println(s);
                        mView.bindSuccess();
                        ToastUtil.showShort(s);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort(message);
                        System.out.println(message);
                    }
                });
    }

    @Override
    public boolean checkData(RequestBindPhoneBean bean) {

        if (!StringUtil.checkPhoneNumber(bean.getMobile())) {
            ToastUtil.showShort(mContext.getString(R.string.please_input_correct_phone_number));
            return false;
        }
        if (StringUtil.isEmpty(bean.getPassword())) {
            ToastUtil.showShort(mContext.getString(R.string.please_input_verification));
            return false;
        }
        return true;
    }
}
