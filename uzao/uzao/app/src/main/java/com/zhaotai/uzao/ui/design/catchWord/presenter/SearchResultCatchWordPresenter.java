package com.zhaotai.uzao.ui.design.catchWord.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.CatchWordBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.design.catchWord.contract.SearchResultCatchWordContract;

import java.util.HashMap;
import java.util.Map;

/**
 * description: 流行词presenter
 * author : ZP
 * date:2018/1/8
 */

public class SearchResultCatchWordPresenter extends SearchResultCatchWordContract.Presenter {
    private SearchResultCatchWordContract.View mView;

    public SearchResultCatchWordPresenter(Context context, SearchResultCatchWordContract.View view) {
        this.mContext = context;
        mView = view;
    }

    @Override
    public void getSearchList(String keyWord, int start) {
        Map<String, String> params = new HashMap<>();
        params.put("text", keyWord);
        params.put("start", String.valueOf(start));
        Api.getDefault().getSearchCatchWordPageList(params)
                .compose(RxHandleResult.<PageInfo<CatchWordBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<CatchWordBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<CatchWordBean> catchWordBeanPageInfo) {
                        mView.showSearchList(catchWordBeanPageInfo);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }
}
