package com.zhaotai.uzao.ui.login.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.RequestNewPasswordBean;

/**
 * description: 设置新密码管理类
 * author : zp
 * date: 2017/7/17
 */

public interface SetNewPasswordContract {
    interface View extends BaseView {

        /**
         * 登录成功
         */
        void setNewSuccess();
    }

    abstract class Presenter extends BasePresenter {


        /**
         * 修改密码
         *
         * @param bean 登录
         */
        public abstract void setNewPassword(RequestNewPasswordBean bean);

        /**
         * 检查密码
         *
         * @param psd1 新密码1
         * @param psd2 新密码2
         * @return 密码是否合适
         */
        public abstract boolean checkData(String psd1, String psd2);
    }
}
