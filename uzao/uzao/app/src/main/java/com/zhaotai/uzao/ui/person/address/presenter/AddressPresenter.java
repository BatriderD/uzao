package com.zhaotai.uzao.ui.person.address.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.AddressBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.person.address.contract.AddressContract;

import java.util.ArrayList;
import java.util.List;

/**
 * time:2017/5/4
 * description: 地址操作类
 * author: LiYou
 */

public class AddressPresenter extends AddressContract.Presenter {

    private AddressContract.View view;


    public AddressPresenter(AddressContract.View view, Context context) {
        mContext = context;
        this.view = view;
    }

    @Override
    public void onStart() {
        getAddressList();
    }

    /**
     * 获取地址列表
     */
    @Override
    public void getAddressList() {
        view.showLoading();
        Api.getDefault().getAddressData()
                .compose(RxHandleResult.<List<AddressBean>>handleResult())
                .subscribe(new RxSubscriber<List<AddressBean>>(mContext, false) {
                    @Override
                    public void _onNext(List<AddressBean> addressBeen) {
                        view.showContent();
                        view.showAddressList(addressBeen);
                    }

                    @Override
                    public void _onError(String message) {
                        view.showNetworkFail(message);
                    }
                });


    }

    /**
     * 删除指定条目
     *
     * @param id  数据列表
     * @param pos 指定条目位置
     */
    @Override
    public void delAddressItem(String id, final int pos) {
        List<String> list = new ArrayList<>();
        list.add(id);
        Api.getDefault().deleteAddress(list)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String strings) {
                        view.delItemSuccess(pos);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 判断条目是否为默认条目 如果不是就请求网络设置为默认条目
     *
     * @param addressBeen 地址数据
     * @param pos         位置
     */
    @Override
    public void setItemDefault(AddressBean addressBeen, final int pos) {
        String isDefault = addressBeen.isDefault;
        if (!"Y".equals(isDefault)) {
//            如果不是默认就上传
            Api.getDefault().changeDefaultAddress(addressBeen.sequenceNBR)
                    .compose(RxHandleResult.<String>handleResult())
                    .subscribe(new RxSubscriber<String>(mContext, true) {
                        @Override
                        public void _onNext(String addressBean) {
                            view.setItemDefault(pos);
                        }

                        @Override
                        public void _onError(String message) {

                        }
                    });
        }
    }
}
