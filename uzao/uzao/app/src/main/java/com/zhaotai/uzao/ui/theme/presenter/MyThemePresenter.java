package com.zhaotai.uzao.ui.theme.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.theme.contract.MyThemeContract;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Time: 2018/1/19
 * Created by LiYou
 * Description :我的主题presenter
 */

public class MyThemePresenter extends MyThemeContract.Presenter {
    private MyThemeContract.View mView;
    private HashMap<String, String> params = new HashMap<>();

    public MyThemePresenter(Context context, MyThemeContract.View mView) {
        this.mView = mView;
        mContext = context;
    }

    /**
     * 获得我的主题列表数据
     * @param start 起始位置
     * @param isLoading 是否是下拉刷新
     */
    @Override
    public void getMyThemeList(final int start, final boolean isLoading) {
        params.put("start", String.valueOf(start));
        params.put("length", "10");
        Api.getDefault().getMyThemeList(params)
                .compose(RxHandleResult.<PageInfo<ThemeBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<ThemeBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<ThemeBean> data) {
                        if (isLoading && start == Constant.PAGING_HOME) {
                            mView.showContent();
                            mView.stopLoadingMore();
                        } else if (start == 0) {
                            mView.stopRefresh();
                        } else {
                            mView.stopLoadingMore();
                        }
                        mView.showMyThemeList(data);
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

    /**
     * 删除主题
     * @param themeIds 主题id
     */
    @Override
    public void delTheme(ArrayList<String> themeIds) {
        Api.getDefault().delMyThemeList(themeIds)
                .compose(RxHandleResult.<List<ThemeBean>>handleResult())
                .subscribe(new RxSubscriber<List<ThemeBean>>(mContext) {
                    @Override
                    public void _onNext(List<ThemeBean> s) {
                        mView.showDelSuccess();
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("删除失败");
                    }
                });
    }
}
