package com.zhaotai.uzao.ui.login.presenter;

import android.content.Context;
import android.util.Log;

import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.EventBean.MaterialLoginSuccessEvent;
import com.zhaotai.uzao.bean.ForgetRequestBean;
import com.zhaotai.uzao.bean.PersonBean;
import com.zhaotai.uzao.bean.PsdAndAccLoginSuccessBean;
import com.zhaotai.uzao.bean.RegisterRequestBean;
import com.zhaotai.uzao.bean.RequestLoginBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.login.contract.RegisterContract;
import com.zhaotai.uzao.utils.LogUtils;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * description:  注册/找回密码
 * author : zp
 * date: 2017/7/17
 */

public class RegisterPresenter extends RegisterContract.Presenter {

    private static final String TAG = "RegisterPresenter";
    private RegisterContract.View mView;

    public RegisterPresenter(Context context, RegisterContract.View view) {
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

    /**
     * 注册
     *
     * @param bean 注册信息bean类
     */
    @Override
    public void register(final RegisterRequestBean bean) {
        Api.getDefault().register(bean)
                .compose(RxHandleResult.<PersonBean>handleResult())
                .subscribe(new RxSubscriber<PersonBean>(mContext, true) {
                    @Override
                    public void _onNext(PersonBean s) {
                        ToastUtil.showShort("注册成功");
//                        用账号密码登录
                        RequestLoginBean requestLoginBean = new RequestLoginBean(bean.getLoginId(), bean.getPassword());
                        login(requestLoginBean);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort(message);
                    }
                });
    }

    /**
     * 找回密码
     *
     * @param bean 注册信息bean类
     */
    @Override
    public void foregetPassword(ForgetRequestBean bean) {
        Api.getDefault().forgetPassword(bean)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String s) {
                        mView.changePasswordSuccess();
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort(message);
                    }
                });
    }

    /**
     * 登录
     *
     * @param bean 登录
     */
    @Override
    public void login(RequestLoginBean bean) {
        Api.getDefault().login(bean)
                .compose(RxHandleResult.<PsdAndAccLoginSuccessBean>handleResult())
                .subscribe(new RxSubscriber<PsdAndAccLoginSuccessBean>(mContext, true) {

                    @Override
                    public void _onNext(PsdAndAccLoginSuccessBean psdAndAccLoginSuccessBean) {
                        PushAgent mPushAgent = PushAgent.getInstance(mContext);
                        mPushAgent.setAlias(psdAndAccLoginSuccessBean.getUserId(), "userId", new UTrack.ICallBack() {
                            @Override
                            public void onMessage(boolean isSuccess, String message) {
                                LogUtils.logd("DeviceToken_isSuccess:" + isSuccess + "//" + message);
                            }
                        });
                        LoginHelper.setLoginInfo(psdAndAccLoginSuccessBean.getUserId(), psdAndAccLoginSuccessBean.getToken(), psdAndAccLoginSuccessBean.getLoginId());
                        ToastUtil.showShort("登录成功");
                        //通知素材详情 登录成功
                        EventBus.getDefault().post(new MaterialLoginSuccessEvent());
                        mView.loginSuccess();
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort(message);
                    }
                });
    }
}
