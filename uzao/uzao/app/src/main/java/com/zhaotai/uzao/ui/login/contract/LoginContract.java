package com.zhaotai.uzao.ui.login.contract;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.RequestLoginBean;
import com.zhaotai.uzao.bean.ThirdPartLoginIconBean;

import java.util.List;

/**
 * description: 登录管理类
 * author : zp
 * date: 2017/7/17
 */

public interface LoginContract {
    interface View extends BaseView {

        /**
         * 登录成功
         */
        void loginSuccess();

        /**
         * 第三方登录成功 但是未绑定电话
         */
        void showBindPhonePage();
    }

    abstract class Presenter extends BasePresenter {


        /**
         * 登录
         *
         * @param bean 登录
         */
        public abstract void login(RequestLoginBean bean);

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

    }
}
