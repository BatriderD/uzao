package com.zhaotai.uzao.ui.theme.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeListBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.theme.contract.NewThemeListContract;

import java.util.HashMap;

/**
 * description: 新增的主题列表presenter
 * author : ZP
 * date: 2018/3/17 0017.
 */

public class ThemeListPresenter extends NewThemeListContract.Presenter {

    private NewThemeListContract.View mView;

    public ThemeListPresenter(Context context, NewThemeListContract.View mView) {
        this.mView = mView;
        mContext = context;
    }

    /**
     * 请求主题列表
     * @param start 开始位置
     * @param isLoadingFinish 是否是loading状态页
     * @param sort 排序方式  true 综合  false time
     */
    @Override
    public void getThemeList(final int start, final boolean isLoadingFinish, boolean sort) {
        HashMap<String, String> params = new HashMap<>();
        params.put("start", String.valueOf(start));
        params.put("length", "10");
        params.put("sort", sort ? "default_" : "sort");
        Api.getDefault().getThemeList(params)
                .compose(RxHandleResult.<PageInfo<ThemeListBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<ThemeListBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<ThemeListBean> data) {
                        mView.showThemeList(data);
                        //更新我的足迹数量
                        if (isLoadingFinish && start == 0) {
                            mView.showContent();
                            mView.stopLoadingMore();
                        } else if (start == 0) {
                            mView.stopRefresh();
                        } else {
                            mView.stopLoadingMore();
                        }
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
