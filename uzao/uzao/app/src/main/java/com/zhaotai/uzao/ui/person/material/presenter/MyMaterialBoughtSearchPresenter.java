package com.zhaotai.uzao.ui.person.material.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.MyMaterialBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.person.material.contract.BoughtSearchSearchView;
import com.zhaotai.uzao.ui.search.contract.SimpleBaseSearchContract;
import com.zhaotai.uzao.ui.search.presenter.SimpleBaseSearchPresenter;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.HashMap;

/**
 * description:新增商品到主题的搜索 presenter
 * author : ZP
 * date: 2018/1/30 0030.
 */

public class MyMaterialBoughtSearchPresenter extends SimpleBaseSearchPresenter {
    public String type;

    public MyMaterialBoughtSearchPresenter(SimpleBaseSearchContract.View view, Context context) {
        super(view, context);
    }

    @Override
    public void getCommodityList(final int start, HashMap<String, String> params) {
        Api.getDefault().searchBoughtMaterial(params)
                .compose(RxHandleResult.<PageInfo<MyMaterialBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<MyMaterialBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<MyMaterialBean> data) {

                        mView.showCommodityList(data);
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

    public void delMaterial(String sequenceNBR, final int position) {
        Api.getDefault().DelMyBoughtMaterial(sequenceNBR)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext) {
                    @Override
                    public void _onNext(String s) {
                        if (mView instanceof BoughtSearchSearchView) {
                            BoughtSearchSearchView v = (BoughtSearchSearchView) mView;
                            v.showDelSuccess(position);
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("删除失败");
                    }
                });

    }
}
