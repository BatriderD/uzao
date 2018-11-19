package com.zhaotai.uzao.ui.productOrder.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ProductOrderBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.productOrder.contract.ProductOrderContract;

/**
 * Time: 2017/8/22
 * Created by LiYou
 * Description :
 */

public class CompleteAndCloseOrderPresenter extends ProductOrderContract.CompleteAndClosePresenter {

    private ProductOrderContract.CompleteAndCloseView view;

    public CompleteAndCloseOrderPresenter(Context context, ProductOrderContract.CompleteAndCloseView view) {
        mContext = context;
        this.view = view;
    }

    /**
     * 获取完成生产订单
     */
    @Override
    public void getCompleteProductOrderList(final int start, final boolean isLoading) {
        Api.getDefault().getProductOrderList(start,"finished")
                .compose(RxHandleResult.<PageInfo<ProductOrderBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<ProductOrderBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<ProductOrderBean> orderBeanPageInfo) {

                        if (isLoading && start == 0) {
                            view.showContent();
                            view.stopLoadingMore();
                        } else if (start == 0) {
                            view.stopRefresh();
                        } else {
                            view.stopLoadingMore();
                        }
                        view.showProductOrderList(orderBeanPageInfo);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            view.stopRefresh();
                            view.showNetworkFail(message);
                        } else {
                            view.loadingFail();
                        }
                    }
                });
    }

    /**
     * 获取关闭生产订单
     */
    @Override
    public void getCloseProductOrderList(final int start, final boolean isLoading) {
        Api.getDefault().getProductOrderList(start,"closed")
                .compose(RxHandleResult.<PageInfo<ProductOrderBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<ProductOrderBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<ProductOrderBean> orderBeanPageInfo) {

                        if (isLoading && start == 0) {
                            view.showContent();
                            view.stopLoadingMore();
                        } else if (start == 0) {
                            view.stopRefresh();
                        } else {
                            view.stopLoadingMore();
                        }
                        view.showProductOrderList(orderBeanPageInfo);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            view.stopRefresh();
                            view.showNetworkFail(message);
                        } else {
                            view.loadingFail();
                        }
                    }
                });
    }
}
