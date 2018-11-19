package com.zhaotai.uzao.ui.design.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.MyPicBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.design.contract.MyPictureContract;

/**
 * description: 我的照片presenter
 * author : ZP
 * date: 2017/11/23 0023.
 */

public class MyPicturePresenter extends MyPictureContract.Presenter {
    private MyPictureContract.View mView;

    public MyPicturePresenter(Context context, MyPictureContract.View view) {
        this.mContext = context;
        mView = view;
    }

    @Override
    public void getMyPictureList(final int start, final boolean isLoading) {
        Api.getDefault().getMyPictureList(start)
                .compose(RxHandleResult.<PageInfo<MyPicBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<MyPicBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<MyPicBean> myPicBeanPageInfo) {
                        if (isLoading && start == 0) {
                            mView.showContent();
                            mView.stopLoadingMore();
                        } else if (start == 0) {
                            mView.stopRefresh();
                        } else {
                            mView.stopLoadingMore();
                        }
                        mView.showDesignList(myPicBeanPageInfo);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            mView.stopRefresh();
                            mView.showNetworkFail(message);
                        } else {
                            mView.loadingFail();
                        }
                    }
                });
    }
}
