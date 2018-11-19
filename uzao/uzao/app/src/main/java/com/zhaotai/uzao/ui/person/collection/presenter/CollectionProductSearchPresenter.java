package com.zhaotai.uzao.ui.person.collection.presenter;

import android.content.Context;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.person.collection.contract.CollectionProductSearchContract;

import java.util.HashMap;
import java.util.Map;

/**
 * Time: 2017/5/22
 * Created by LiYou
 * Description : 收藏的商品
 */

public class CollectionProductSearchPresenter extends CollectionProductSearchContract.Presenter {

    private CollectionProductSearchContract.View view;

    public CollectionProductSearchPresenter(CollectionProductSearchContract.View view, Context context) {
        this.view = view;
        this.mContext = context;
    }

    /**
     * 搜索自己收藏的商品
     *
     * @param start 开始位置
     * @param name  关键字
     */
    @Override
    public void getCollectProductList(final int start, String name) {
        Map<String, String> params = new HashMap<>();
        params.put("start", String.valueOf(start));
        params.put("spuName", name);
        Api.getDefault().getCollectionProductList(params)
                .compose(RxHandleResult.<PageInfo<GoodsBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<GoodsBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<GoodsBean> goodsBeanPageInfo) {
                        if (goodsBeanPageInfo.totalRows > 0) {
                            view.showContent();
                            view.showCollectionProductList(goodsBeanPageInfo);
                        } else {
                            view.showEmpty(mContext.getString(R.string.empty_search));
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            view.showNetworkFail(message);
                        } else {
                            view.loadingFail();
                        }
                    }
                });
    }

}
