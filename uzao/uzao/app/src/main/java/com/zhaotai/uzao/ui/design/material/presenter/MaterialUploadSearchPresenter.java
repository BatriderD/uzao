package com.zhaotai.uzao.ui.design.material.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.MyUploadMaterialBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.search.contract.SimpleBaseSearchContract;
import com.zhaotai.uzao.ui.search.presenter.SimpleBaseSearchPresenter;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.HashMap;

/**
 * description:获得上传的素材presenter
 * author : ZP
 * date: 2018/3/9 0009.
 */

public class MaterialUploadSearchPresenter extends SimpleBaseSearchPresenter {
    public MaterialUploadSearchPresenter(SimpleBaseSearchContract.View view, Context context) {
        super(view, context);
    }

    /**
     * 请求素材内容的
     * @param start 起始位置
     * @param map 请求的参数
     */
    @Override
    public void getCommodityList(final int start, HashMap<String, String> map) {
        Api.getDefault().getUploadMaterial(map)
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
