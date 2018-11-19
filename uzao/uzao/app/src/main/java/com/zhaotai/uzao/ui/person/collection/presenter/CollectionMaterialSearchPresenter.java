package com.zhaotai.uzao.ui.person.collection.presenter;

import android.content.Context;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.person.collection.contract.CollectionMaterialSearchContract;
import com.zhaotai.uzao.ui.person.collection.contract.CollectionProductSearchContract;

import java.util.HashMap;
import java.util.Map;

/**
 * Time: 2017/5/22
 * Created by LiYou
 * Description :
 */

public class CollectionMaterialSearchPresenter extends CollectionMaterialSearchContract.Presenter {

    private CollectionMaterialSearchContract.View view;

    public CollectionMaterialSearchPresenter(CollectionMaterialSearchContract.View view, Context context) {
        this.view = view;
        this.mContext = context;
    }

    @Override
    public void getCollectMaterialList(final int start, String name) {
        Map<String, String> params = new HashMap<>();
        params.put("start", String.valueOf(start));
        params.put("materialName", name);
        Api.getDefault().getCollectionMaterialList(params)
                .compose(RxHandleResult.<PageInfo<MaterialListBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<MaterialListBean>>(mContext, false) {
                    @Override
                    public void _onNext(PageInfo<MaterialListBean> goodsBeanPageInfo) {
                        if (goodsBeanPageInfo.totalRows > 0) {
                            view.showContent();
                            view.showCollectionMaterialList(goodsBeanPageInfo);
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
