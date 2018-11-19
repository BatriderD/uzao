package com.zhaotai.uzao.ui.design.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.TechnologyBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.design.contract.TechnologyContract;

/**
 * description:工艺presenter
 * author : ZP
 * date: 2017/9/28 0028.
 */

public class TechnologyPresenter extends TechnologyContract.Presenter {
    private static final String TAG = TechnologyPresenter.class.toString();
    private TechnologyContract.View mView;

    public TechnologyPresenter(Context context, TechnologyContract.View view) {
        mContext = context;
        mView = view;
    }


    /**
     * 获得工艺信息
     * @param start 分页下表
     */
    @Override
    public void getTechnology(int start) {
        Api.getDefault()
                .getTechnologyInfo(start)
                .compose(RxHandleResult.<PageInfo<TechnologyBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<TechnologyBean>>(mContext, true) {
                    @Override
                    public void _onNext(PageInfo<TechnologyBean> technologyBeanPageInfos) {
                        mView.showTechnologyList(technologyBeanPageInfos);
                    }

                    @Override
                    public void _onError(String message) {
                        mView.showTechnologyListFailed();
                    }
                });
    }
}
