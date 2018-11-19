package com.zhaotai.uzao.ui.person.address.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.AddressBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.person.address.contract.AddAddressContract;

/**
 * description: 增加地址页面presenter
 * author : zp
 * date: 2017/7/13
 */

public class AddAddressPresenter extends AddAddressContract.Presenter {
    private Context mContext;
    private AddAddressContract.View mView;


    public AddAddressPresenter(Context context, AddAddressContract.View view) {
        mContext = context;
        mView = view;
    }

    /**
     * 上传地址
     *
     * @param addressBean 地址
     */
    @Override
    public void uploadAddress(AddressBean addressBean) {
        Api.getDefault().addAddress(addressBean)
                .compose(RxHandleResult.<AddressBean>handleResult())
                .subscribe(new RxSubscriber<AddressBean>(mContext, true) {
                    @Override
                    public void _onNext(AddressBean addressBean) {

                        mView.uploadAddressSuccess(addressBean);

                    }

                    @Override
                    public void _onError(String message) {
                        mView.uploadAddressError(message);
                    }
                });
    }
}
