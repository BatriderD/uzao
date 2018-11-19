package com.zhaotai.uzao.ui.theme.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeListBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.search.contract.SimpleBaseSearchContract;
import com.zhaotai.uzao.ui.search.presenter.SimpleBaseSearchPresenter;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.HashMap;

/**
 * description:主题列表搜索的presenter
 * author : ZP
 * date: 2018/1/30 0030.
 */

public class ThemeListSearchPresenter extends SimpleBaseSearchPresenter {

    public ThemeListSearchPresenter(SimpleBaseSearchContract.View view, Context context) {
        super(view, context);
    }

    /**
     * 获得主题列表的搜索数据
     * @param start 开始位置
     * @param params 搜索参数
     */
    @Override
    public void getCommodityList(final int start, HashMap<String, String> params) {
        params.put("start", String.valueOf(start));
        params.put("start", String.valueOf(start));
        Api.getDefault().getThemeList(params)
                .compose(RxHandleResult.<PageInfo<ThemeListBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<ThemeListBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<ThemeListBean> data) {
                        mView.showCommodityList(data);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            ToastUtil.showShort("网络请求失败");
                        } else {
                            mView.loadingMoreFail();
                        }
                    }
                });

    }
}
