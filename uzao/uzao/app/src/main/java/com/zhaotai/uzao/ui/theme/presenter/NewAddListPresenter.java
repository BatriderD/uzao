package com.zhaotai.uzao.ui.theme.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.bean.NewAddListBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.theme.contract.NewAddListContract;

/**
 * Time: 2018/1/19
 * Created by LiYou
 * Description :  新增列表presenter
 */

public class NewAddListPresenter extends NewAddListContract.Presenter {
    private NewAddListContract.View mView;

    public NewAddListPresenter(Context context, NewAddListContract.View mView) {
        this.mView = mView;
        mContext = context;
    }

    @Override
    public void getNewAddList(final int start, final boolean isLoading, String themeId, String type) {
        Api.getDefault().getNewAddThemeContentList(themeId, type, start)
                .compose(RxHandleResult.<PageInfo<NewAddListBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<NewAddListBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<NewAddListBean> pageInfo) {
                        if (isLoading && start == Constant.PAGING_HOME) {
                            mView.showContent();
                        } else if (start == Constant.PAGING_HOME) {
                            mView.showContent();
                            mView.stopRefresh();
                        } else {
                            mView.stopLoadingMore();
                        }
                        mView.showNewAddList(pageInfo);
                    }

                    @Override
                    public void _onError(String message) {
                        mView.showNetworkFail(message);
                    }
                });
    }
}
