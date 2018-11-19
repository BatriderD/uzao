package com.zhaotai.uzao.ui.person.collection.presenter;

import android.content.Context;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.person.collection.contract.CollectionMaterialSearchContract;
import com.zhaotai.uzao.ui.person.collection.contract.CollectionThemeSearchContract;

import java.util.HashMap;
import java.util.Map;

/**
 * Time: 2017/5/22
 * Created by LiYou
 * Description :
 */

public class CollectionThemeSearchPresenter extends CollectionThemeSearchContract.Presenter {

    private CollectionThemeSearchContract.View view;

    public CollectionThemeSearchPresenter(CollectionThemeSearchContract.View view, Context context) {
        this.view = view;
        this.mContext = context;
    }

    @Override
    public void getCollectThemeList(final int start, String name) {
        Map<String, String> params = new HashMap<>();
        params.put("start", String.valueOf(start));
        params.put("themeName", name);
        Api.getDefault().getCollectionThemeList(params)
                .compose(RxHandleResult.<PageInfo<ThemeBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<ThemeBean>>(mContext, true) {
                    @Override
                    public void _onNext(PageInfo<ThemeBean> goodsBeanPageInfo) {
                        if (goodsBeanPageInfo.totalRows > 0) {
                            view.showContent();
                            view.showCollectionThemeList(goodsBeanPageInfo);
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
