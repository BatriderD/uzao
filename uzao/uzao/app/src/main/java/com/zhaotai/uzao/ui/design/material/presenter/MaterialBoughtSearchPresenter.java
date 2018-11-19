package com.zhaotai.uzao.ui.design.material.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.MyMaterialBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.search.contract.SimpleBaseSearchContract;
import com.zhaotai.uzao.ui.search.presenter.SimpleBaseSearchPresenter;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.HashMap;

/**
 * description: 已购素材的搜索presenter
 * author : ZP
 * date: 2018/3/9 0009.
 */

public class MaterialBoughtSearchPresenter extends SimpleBaseSearchPresenter {
    public MaterialBoughtSearchPresenter(SimpleBaseSearchContract.View view, Context context) {
        super(view, context);
    }

    /**
     * 获得已购素材列表
     * @param start 起始的位置
     * @param map 请求的素材参数
     */
    @Override
    public void getCommodityList(final int start, HashMap<String, String> map) {
        Api.getDefault().searchBoughtMaterial(map)
                .compose(RxHandleResult.<PageInfo<MyMaterialBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<MyMaterialBean>>(mContext, true) {
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
}
