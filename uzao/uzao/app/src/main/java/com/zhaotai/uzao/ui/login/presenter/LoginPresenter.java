package com.zhaotai.uzao.ui.login.presenter;

import android.content.Context;
import android.widget.Toast;

import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.EventBean.MaterialLoginSuccessEvent;
import com.zhaotai.uzao.bean.PersonBean;
import com.zhaotai.uzao.bean.PsdAndAccLoginSuccessBean;
import com.zhaotai.uzao.bean.RequestLoginBean;
import com.zhaotai.uzao.bean.RequestThirdPartLoginBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.login.activity.LoginActivity;
import com.zhaotai.uzao.ui.login.contract.LoginContract;
import com.zhaotai.uzao.utils.LogUtils;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.MD5Utils;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

/**
 * description: 登录页面Presenter
 * author : zp
 * date: 2017/7/17
 */

public class LoginPresenter extends LoginContract.Presenter {
    private LoginContract.View mView;

    public LoginPresenter(Context context, LoginContract.View view) {
        mContext = context;
        mView = view;
    }

    /**
     * 登录
     *
     * @param bean {@link RequestLoginBean}登录实体类
     */
    @Override
    public void login(RequestLoginBean bean) {
        if (checkData(bean)) {
            bean.setPassword(MD5Utils.getMD5(bean.getPassword()));
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
                            if ("PASSWORD_ERROR--密码错误,输入密码不匹配".equals(message))
                            message = "密码错误";
                            ToastUtil.showShort(message);
                        }
                    });
        }
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
     * 检查用户名密码是否和规范
     *
     * @return 检查密码格式
     */
    private boolean checkData(RequestLoginBean bean) {
        String phoneNumber = bean.getLoginId();
        String password = bean.getPassword();
        if (!StringUtil.checkPhoneNumber(phoneNumber)) {
            //手机号正则
            ToastUtil.showShort(mContext.getString(R.string.please_input_correct_phone_number));
            return false;
        }
        if (StringUtil.isEmpty(password) || StringUtil.isEmpty(phoneNumber)) {
            //账号密码为空
            ToastUtil.showShort(mContext.getString(R.string.psd_and_num_cant_empty));
            return false;
        }
        if (password.length() > GlobalVariable.PSD_MAX || password.length() < GlobalVariable.PSD_MIN) {
            //密码长度错误
            ToastUtil.showShort(mContext.getString(R.string.psd_length_error));
            return false;
        }
        if (!StringUtil.isPassWord(password)) {
            ToastUtil.showShort(mContext.getString(R.string.psd_error));
            return false;
        }
        return true;
    }

    /**
     * 第三方登录
     */
    public void uMengLogin(SHARE_MEDIA media) {

        UMShareAPI mShareAPI = UMShareAPI.get(mContext);

        mShareAPI.doOauthVerify((LoginActivity) mContext, media, authListener);
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
                        LogUtils.logd("获取个人电话信息 " + message);
                    }
                });

    }


    /**
     * uMeng登录回调监听
     */
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
