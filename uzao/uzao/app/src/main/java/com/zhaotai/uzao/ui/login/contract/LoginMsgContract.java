package com.zhaotai.uzao.ui.login.contract;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;

/**
 * description: 登录管理类
 * author : zp
 * date: 2017/7/17
 */

public interface LoginMsgContract {
    interface View extends BaseView {

        /**
         * 登录成功
         */
        void loginSuccess();

        /**
         * 第三方登录成功 但是未绑定电话
         */
        void showBindPhonePage();

        /**
         * 开始获取验证码
         */
        void startGetSmsCode();

        /**
         * 倒计时结束
         */
        void finishCountDown();

        /**
         * 登录成功
         */
        void success();

        /**
         * 刷新倒计时
         */
        void refreshCountDown(Long time);
    }

    abstract class Presenter extends BasePresenter {


        /**
         * 三方登录
         *
         * @param part 登录途径
         */
        public abstract void thirdPartLogin(String part, String uid);

        /**
         * 友盟授权
         *
         * @param media 授权渠道
         */
        public abstract void uMengLogin(SHARE_MEDIA media);

        /**
         * 获取个人信息接口
         */

        public abstract void getPersonInfo();


        /**
         * 获取验证码
         *
         * @param iphone 手机号
         */
        public abstract void getSmsCode(String iphone);

        /**
         * 验证码登录
         *
         * @param phone   手机号
         * @param smsCode 验证码
         */
        public abstract void loginRequest(String phone, String smsCode);


        /**
         * 倒计时
         */
        public abstract void countDown();


    }
}
