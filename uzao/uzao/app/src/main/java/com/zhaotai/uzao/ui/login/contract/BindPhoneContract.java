package com.zhaotai.uzao.ui.login.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;

import com.zhaotai.uzao.bean.RequestBindPhoneBean;

/**
 * description: 绑定手机管理类
 * author : zp
 * date: 2017/7/17
 */

public interface BindPhoneContract {
    interface View extends BaseView {
        /**
         * 刷新倒计时界面
         *
         * @param time 时间
         */
        void refreshCountDown(Long time);

        /**
         * 绑定成功
         */
        void bindSuccess();
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
         * 绑定
         *
         * @param bean 注册信息bean类
         */
        public abstract void bindPhone(RequestBindPhoneBean bean);


        /**
         * 检查是否合法
         *
         * @param bean 注册信息bean类
         */
        public abstract boolean checkData(RequestBindPhoneBean bean);

    }
}
