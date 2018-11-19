package com.zhaotai.uzao.ui.category.goods.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.ProductListBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.category.goods.contract.NavigateProductListContract;

/**
 * Time: 2018/3/23
 * Created by LiYou
 * Description : 导航商品列表
 */

public class NavigateProductListPresenter extends NavigateProductListContract.Presenter {

    private NavigateProductListContract.View mView;

    public NavigateProductListPresenter(Context context, NavigateProductListContract.View view) {
        mContext = context;
        mView = view;
    }

    /**
     * 获取导航商品列表
     */
    @Override
    public void getNavigateData(int start, String code) {
        Api.getDefault().getNavigateProductList(start, code)
                .compose(RxHandleResult.<ProductListBean<GoodsBean>>handleResult())
                .subscribe(new RxSubscriber<ProductListBean<GoodsBean>>(mContext) {
                    @Override
                    public void _onNext(ProductListBean<GoodsBean> goodsBeanPageInfo) {
                        if (mView != null) {
                            if (goodsBeanPageInfo.page.totalRows > 0) {
                                mView.showContent();
                                mView.stopLoadingMore();
                                mView.stopRefresh();
                                mView.bindData(goodsBeanPageInfo.page);
                            } else {
                                mView.showEmpty();
                            }
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        if (mView != null) {
                            mView.stopRefresh();
                            mView.showNetworkFail(message);
                        }
                    }
                });
    }

    /**
     * 获取首页商品列表
     */
    @Override
    public void getMainData(int start, String groupType, String groupCode, String entityType, String fieldName, String id) {
        Api.getDefault().getMainProductList(start, groupType, groupCode, entityType, fieldName, id)
                .compose(RxHandleResult.<ProductListBean<GoodsBean>>handleResult())
                .subscribe(new RxSubscriber<ProductListBean<GoodsBean>>(mContext) {
                    @Override
                    public void _onNext(ProductListBean<GoodsBean> goodsBeanPageInfo) {
                        if (mView != null) {
                            if (goodsBeanPageInfo.page.totalRows > 0) {
                                mView.showContent();
                                mView.stopLoadingMore();
                                mView.stopRefresh();
                                mView.bindData(goodsBeanPageInfo.page);
                            } else {
                                mView.showEmpty();
                            }
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        if (mView != null) {
                            mView.stopRefresh();
                            mView.showNetworkFail(message);
                        }
                    }
                });
    }

    /**
     * 获取专题商品列表
     */
    @Override
    public void getTopicData(int start, String type, String mongoId, String imageId, String hotspotId) {
        Api.getDefault().getTopicProductList(start, type, mongoId, imageId, hotspotId)
                .compose(RxHandleResult.<ProductListBean<GoodsBean>>handleResult())
                .subscribe(new RxSubscriber<ProductListBean<GoodsBean>>(mContext) {
                    @Override
                    public void _onNext(ProductListBean<GoodsBean> goodsBeanPageInfo) {
                        if (mView != null) {
                            if (goodsBeanPageInfo.page.totalRows > 0) {
                                mView.showContent();
                                mView.stopLoadingMore();
                                mView.stopRefresh();
                                mView.bindData(goodsBeanPageInfo.page);
                            } else {
                                mView.showEmpty();
                            }
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        if (mView != null) {
                            mView.stopRefresh();
                            mView.showNetworkFail(message);
                        }
                    }
                });
    }


}
