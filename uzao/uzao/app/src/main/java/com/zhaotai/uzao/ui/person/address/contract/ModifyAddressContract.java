package com.zhaotai.uzao.ui.person.address.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.AddressBean;


/**
 * description: 修改地址信息
 * author : zp
 * date: 2017/7/14
 */

public interface ModifyAddressContract {
    interface View extends BaseView {
        /**
         * 上传地址成功
         */
        void upDataSuccess();

        /**
         * 上传地址失败
         *
         * @param msg 错误信息
         */
        void upDataError(String msg);


    }

    abstract class Presenter extends BasePresenter {
        /**
         * 上传地址信息
         *
         * @param addressBean 上传的地址信息
         */
        public abstract void upDataAddress(AddressBean addressBean);
    }
}
