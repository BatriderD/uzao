package com.zhaotai.uzao.ui.person.material.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.MyMaterialBean;
import com.zhaotai.uzao.bean.MyUploadMaterialBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.search.contract.SimpleBaseSearchContract;
import com.zhaotai.uzao.ui.search.presenter.SimpleBaseSearchPresenter;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.HashMap;

/**
 * description:新增商品到主题的搜索 presenter
 * author : ZP
 * date: 2018/1/30 0030.
 */

public class MyMaterialUploadSearchPresenter extends SimpleBaseSearchPresenter {
    public String type;

    public MyMaterialUploadSearchPresenter(SimpleBaseSearchContract.View view, Context context) {
        super(view, context);
    }

    @Override
    public void getCommodityList(final int start, HashMap<String, String> params) {
        Api.getDefault().getUploadMaterial(params)
                .compose(RxHandleResult.<PageInfo<MyUploadMaterialBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<MyUploadMaterialBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<MyUploadMaterialBean> info) {
                        mView.showCommodityList(info);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            ToastUtil.showShort("网络请求失败");
                        } else {
                            mView.loadingMoreFail();
                        }
                    }
                });
    }
}
