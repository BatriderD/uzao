package com.zhaotai.uzao.ui.person.address.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.AddressBean;

/**
 * description:
 * author : zp
 * date: 2017/7/13
 */

public interface AddAddressContract {

    interface View extends BaseView {
        /**
         * 上传地址成功
         *
         * @param addressBean
         */
        void uploadAddressSuccess(AddressBean addressBean);

        /**
         * 上传地址失败
         *
         * @param message 错误信息
         */
        void uploadAddressError(String message);
    }

    abstract class Presenter extends BasePresenter {
        /**
         * 更新地址信息
         *
         * @param addressBean
         */
        public abstract void uploadAddress(AddressBean addressBean);
    }
}
