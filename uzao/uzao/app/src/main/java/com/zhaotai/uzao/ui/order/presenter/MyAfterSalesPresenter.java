package com.zhaotai.uzao.ui.order.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.bean.AfterSalesBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.order.contract.MyAfterSalesContract;

/**
 * Time: 2017/6/2
 * Created by LiYou
 * Description :
 */

public class MyAfterSalesPresenter extends MyAfterSalesContract.Presenter {

    private MyAfterSalesContract.View view;

    public MyAfterSalesPresenter(Context context, MyAfterSalesContract.View view) {
        mContext = context;
        this.view = view;
    }

    /**
     * 获取售后列表
     *
     * @param start    开始位置
     * @param loadding 加载
     */
    @Override
    public void getAfterSalesList(final int start, final boolean loadding) {
        Api.getDefault().getAfterSalesList(start)
                .compose(RxHandleResult.<PageInfo<AfterSalesBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<AfterSalesBean>>(mContext, false) {
                    @Override
                    public void _onNext(PageInfo<AfterSalesBean> orderGoodsBeanPageInfo) {
                        if (loadding && start == Constant.PAGING_HOME) {
                            view.showContent();
                            view.stopLoadingMore();
                        } else if (start == 0) {
                            view.stopRefresh();
                        } else {
                            view.stopLoadingMore();
                        }
                        view.showAfterSalesList(orderGoodsBeanPageInfo);
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
