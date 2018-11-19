package com.zhaotai.uzao.ui.theme.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.theme.contract.CollectionToThemeContract;

import java.util.HashMap;

/**
 * Time: 2018/1/19
 * Created by LiYou
 * Description :  新增收藏的主题的presenter
 */

public class CollectionToThemePresenter extends CollectionToThemeContract.Presenter {
    private CollectionToThemeContract.MaterialView mMaterialView;
    private CollectionToThemeContract.ProductView mProductView;

    public CollectionToThemePresenter(Context context, CollectionToThemeContract.MaterialView mView) {
        this.mMaterialView = mView;
        mContext = context;
    }

    public CollectionToThemePresenter(Context context, CollectionToThemeContract.ProductView mView) {
        this.mProductView = mView;
        mContext = context;
    }

    @Override
    public void getCollectMaterialList(final int start, final boolean isLoading, final HashMap<String, String> params) {
        Api.getDefault().getCollectionMaterialList(params)
                .compose(RxHandleResult.<PageInfo<MaterialListBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<MaterialListBean>>(mContext, true) {
                    @Override
                    public void _onNext(PageInfo<MaterialListBean> materialInfo) {
                        if (isLoading && start == Constant.PAGING_HOME) {
                            mMaterialView.showContent();
                            mMaterialView.stopLoadingMore();
                        } else {
                            mMaterialView.stopLoadingMore();
                        }
                        mMaterialView.showMaterialList(materialInfo);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            mMaterialView.showNetworkFail(message);
                        } else {
                            mMaterialView.stopLoadingMore();
                        }
                    }
                });
    }

    @Override
    public void getCollectProductList(final int start, final boolean isLoading, HashMap<String, String> params) {
        params.put("start", String.valueOf(start));
        Api.getDefault().getCollectionProductList(params)
                .compose(RxHandleResult.<PageInfo<GoodsBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<GoodsBean>>(mContext, true) {
                    @Override
                    public void _onNext(PageInfo<GoodsBean> goodsInfo) {
                        if (isLoading && start == Constant.PAGING_HOME) {
                            mProductView.showContent();
                            mProductView.stopLoadingMore();
                        } else {
                            mProductView.stopLoadingMore();
                        }
                        mProductView.showProductList(goodsInfo);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            mProductView.showNetworkFail(message);
                        } else {
                            mProductView.stopLoadingMore();
                        }
                    }
                });
    }
}
