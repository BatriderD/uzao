package com.zhaotai.uzao.ui.theme.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.GoodsBean;
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

public class AddProductToThemeSearchPresenter extends SimpleBaseSearchPresenter {

    public AddProductToThemeSearchPresenter(SimpleBaseSearchContract.View view, Context context) {
        super(view, context);
    }

    @Override
    public void getCommodityList(final int start, HashMap<String, String> params) {
        Api.getDefault().getThemeProductList(params)
                .compose(RxHandleResult.<PageInfo<GoodsBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<GoodsBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<GoodsBean> data) {

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
