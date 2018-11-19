package com.zhaotai.uzao.ui.login.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.EventBean.MaterialLoginSuccessEvent;
import com.zhaotai.uzao.bean.LoginInfo;
import com.zhaotai.uzao.bean.PersonBean;
import com.zhaotai.uzao.bean.PsdAndAccLoginSuccessBean;
import com.zhaotai.uzao.bean.RequestThirdPartLoginBean;
import com.zhaotai.uzao.bean.TokenInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.login.activity.LoginMsgActivity;
import com.zhaotai.uzao.ui.login.contract.LoginMsgContract;
import com.zhaotai.uzao.utils.LogUtils;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * description: 登录页面Presenter
 * author : zp
 * date: 2017/7/17
 */

public class LoginMsgPresenter extends LoginMsgContract.Presenter {
    private LoginMsgContract.View mView;

    public LoginMsgPresenter(Context context, LoginMsgContract.View view) {
        mContext = context;
        mView = view;
    }


    /**
     * 三方登录
     *
     * @param part 登录途径
     * @param uid  uid
     */
    @Override
    public void thirdPartLogin(String part, String uid) {
        RequestThirdPartLoginBean requestThirdPartLoginBean = new RequestThirdPartLoginBean(uid);
        Api.getDefault().third_part_login(part, requestThirdPartLoginBean)
                .compose(RxHandleResult.<PsdAndAccLoginSuccessBean>handleResult())
                .subscribe(new RxSubscriber<PsdAndAccLoginSuccessBean>(mContext, true) {
                    @Override
                    public void _onNext(PsdAndAccLoginSuccessBean bean) {
                        LoginHelper.setLoginInfo(bean.getUserId(), bean.getToken(), bean.getLoginId());
                        //获取登录信息
                        getPersonInfo();
                    }

                    @Override
                    public void _onError(String message) {
                        LogUtils.logd(message);
                    }
                });
    }


    /**
     * 第三方登录
     */
    public void uMengLogin(SHARE_MEDIA media) {

        UMShareAPI mShareAPI = UMShareAPI.get(mContext);

        mShareAPI.doOauthVerify((LoginMsgActivity) mContext, media, authListener);
    }

    /**
     * 获取用户信息
     */
    @Override
    public void getPersonInfo() {
        Api.getDefault().getPersonMobileInfo()
                .compose(RxHandleResult.<PersonBean>handleResult())
                .subscribe(new RxSubscriber<PersonBean>(mContext, false) {
                    @Override
                    public void _onNext(PersonBean personBean) {
                        LogUtils.logd("啦啦啦啦 " + personBean.aboutMe);
                        if (StringUtil.isEmpty(personBean.mobile)) {
//                            去绑定页面
                            mView.showBindPhonePage();
                        } else {
                            //通知素材详情 登录成功
                            EventBus.getDefault().post(new MaterialLoginSuccessEvent());
//                            三方登录成功
                            mView.loginSuccess();
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        LogUtils.logd("啦啦啦啦 " + message);
                    }
                });

    }


    /**
     * 获取验证码
     */
    @Override
    public void getSmsCode(String iphone) {
        //检查手机号码
        if (StringUtil.checkPhoneNumber(iphone)) {
            mView.startGetSmsCode();
            countDown();
            Api.getDefault().getSmsCode(iphone)
                    .compose(RxHandleResult.<String>handleResult())
                    .subscribe(new RxSubscriber<String>(mContext, false) {
                        @Override
                        public void _onNext(String s) {
                            ToastUtil.showShort("发送验证码成功");
                        }

                        @Override
                        public void _onError(String message) {
                            mView.finishCountDown();
                            Log.d("getSmsCode", "_onError: " + message);
                            ToastUtil.showShort("发送验证码失败");
                            if (countDownDisposable != null) {
                                countDownDisposable.dispose();
                            }
                        }
                    });
        } else {
            ToastUtil.showShort("请输入正确的手机号码");
        }


    }


    /**
     * 登录请求
     *
     * @param phone   手机号码
     * @param smsCode 验证码
     */
    @Override
    public void loginRequest(String phone, String smsCode) {
        if (StringUtil.checkPhoneNumber(phone)) {
            Api.getDefault().loginSms(new LoginInfo(phone, smsCode))
                    .compose(RxHandleResult.<TokenInfo>handleResult())
                    .subscribe(new RxSubscriber<TokenInfo>(mContext, true) {
                        @Override
                        public void _onNext(TokenInfo tokenInfo) {
                            PushAgent mPushAgent = PushAgent.getInstance(mContext);
                            mPushAgent.setAlias(tokenInfo.userId, "userId", new UTrack.ICallBack() {
                                @Override
                                public void onMessage(boolean isSuccess, String message) {
                                    LogUtils.logd("DeviceToken_isSuccess:" + isSuccess + "//" + message);
                                }
                            });

                            LoginHelper.setLoginInfo(tokenInfo.userId, tokenInfo.token, tokenInfo.loginId);
                            //通知素材详情 登录成功
                            EventBus.getDefault().post(new MaterialLoginSuccessEvent());
                            mView.success();
                        }

                        @Override
                        public void _onError(String message) {
                            if (message.equals("SMSCODE_ERROR--验证码错误,请重新获取")) {
                                ToastUtil.showShort("验证码错误,请重新获取");
                            }
                        }
                    });
        } else {
            ToastUtil.showShort("请输入正确的手机号码");
        }

    }

    private Disposable countDownDisposable;

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
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        countDownDisposable = d;
                    }

                    @Override
                    public void _onNext(Long aLong) {
                        mView.refreshCountDown(aLong);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });

    }


    private UMAuthListener authListener = new UMAuthListener() {
        /**
         *  授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
            LogUtils.logd("第三方登录onStart");
        }

        /**
         *  授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

            LogUtils.logd("第三方登录成功");
            Toast.makeText(mContext, "成功了", Toast.LENGTH_LONG).show();
            String channel = "";
            String uid = data.get("uid");

            if (SHARE_MEDIA.QQ.equals(platform)) {
                channel = GlobalVariable.CHANNEL_QQ;
            } else if (SHARE_MEDIA.SINA.equals(platform)) {
                channel = GlobalVariable.CHANNEL_SINA;
            } else if (SHARE_MEDIA.WEIXIN.equals(platform)) {
                channel = GlobalVariable.CHANNEL_WEICHAT;
            }
            thirdPartLogin(channel, uid);

        }

        /**
         *  授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            LogUtils.logd("第三方登录失败");
            Toast.makeText(mContext, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         *  授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            LogUtils.logd("第三方登录取消");
            Toast.makeText(mContext, "取消了", Toast.LENGTH_LONG).show();
        }
    };


}
