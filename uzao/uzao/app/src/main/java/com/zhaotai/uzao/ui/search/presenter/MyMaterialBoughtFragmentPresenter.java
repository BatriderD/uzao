package com.zhaotai.uzao.ui.search.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.MyMaterialBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.search.contract.MyMaterialBoughtFragmentContract;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.HashMap;

/**
 * description: 我的素材管理类
 * author : ZP
 * date: 2018/1/9 0009.
 */

public class MyMaterialBoughtFragmentPresenter extends MyMaterialBoughtFragmentContract.Presenter {
    MyMaterialBoughtFragmentContract.View mView;

    public MyMaterialBoughtFragmentPresenter(Context context, MyMaterialBoughtFragmentContract.View view) {
        mView = view;
        mContext = context;
    }

    @Override
    public void getMyBoughtMaterial(int start, String word) {
        HashMap<String, String> params = new HashMap<>();
        params.put("start", String.valueOf(start));
        params.put("length", String.valueOf(15));
        params.put("status", "inExpired");
        params.put("name", word);
        Api.getDefault().searchBoughtMaterial(params)
                .compose(RxHandleResult.<PageInfo<MyMaterialBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<MyMaterialBean>>(mContext, true) {
                    @Override
                    public void _onNext(PageInfo<MyMaterialBean> data) {
                        mView.showMaterialList(data);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("网络请求错误");
                    }
                });

    }

}
