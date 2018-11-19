package com.zhaotai.uzao.ui.login.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.ForgetRequestBean;
import com.zhaotai.uzao.bean.RegisterRequestBean;
import com.zhaotai.uzao.bean.RequestLoginBean;

/**
 * description: 注册管理类
 * author : zp
 * date: 2017/7/17
 */

public interface RegisterContract {
    interface View extends BaseView {
        /**
         * 刷新倒计时界面
         *
         * @param time 时间
         */
        void refreshCountDown(Long time);

        /**
         * 登录成功
         */
        void loginSuccess();

        /**
         * 修改密码成功
         */
        void changePasswordSuccess();
    }

    abstract class Presenter extends BasePresenter {
        /**
         * 开启倒计时
         */
        public abstract void countDown();

        /**
         * 获取验证码
         *
         * @param iphone 手机号
         */
        public abstract void getSmsCode(String iphone);

        /**
         * 注册
         *
         * @param bean 注册信息bean类
         */
        public abstract void register(RegisterRequestBean bean);

        /**
         * 找回密码
         *
         * @param bean 找回密码bean
         */
        public abstract void foregetPassword(ForgetRequestBean bean);

        /**
         * 注册成功 就登录
         *
         * @param bean 登录实体类
         */
        public abstract void login(RequestLoginBean bean);
    }
}
