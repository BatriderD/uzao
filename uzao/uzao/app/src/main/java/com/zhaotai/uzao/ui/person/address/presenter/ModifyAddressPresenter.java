package com.zhaotai.uzao.ui.person.address.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.AddressBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.person.address.contract.ModifyAddressContract;


/**
 * description: 修改地址presenter
 * author : zp
 * date: 2017/7/14
 */

public class ModifyAddressPresenter extends ModifyAddressContract.Presenter {

    private Context mContext;
    private ModifyAddressContract.View mView;

    public ModifyAddressPresenter(Context context, ModifyAddressContract.View view) {
        super();
        mContext = context;
        mView = view;
    }

    /**
     * 修改地址
     *
     * @param addressBean 上传的商品信息
     */
    @Override
    public void upDataAddress(AddressBean addressBean) {
        Api.getDefault().modifyAddress(addressBean.sequenceNBR, addressBean)
                .compose(RxHandleResult.<AddressBean>handleResult())
                .subscribe(new RxSubscriber<AddressBean>(mContext, true) {
                    @Override
                    public void _onNext(AddressBean ddd) {
                        mView.upDataSuccess();

                    }

                    @Override
                    public void _onError(String message) {
                        mView.upDataError(message);
                    }
                });
    }
}
