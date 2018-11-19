package com.zhaotai.uzao.ui.login.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;

/**
 * description: 注册页面1的管理类
 * author : zp
 * date: 2017/8/2
 */

public interface Register1Contract {
    interface View extends BaseView {

        /**
         * 获得验证码成功
         */
        void getSmsSuccess();
    }

    abstract class Presenter extends BasePresenter {

        /**
         * 获取验证码
         *
         * @param iphone 手机号
         */
        public abstract void getSmsCode(String iphone);

    }
}
