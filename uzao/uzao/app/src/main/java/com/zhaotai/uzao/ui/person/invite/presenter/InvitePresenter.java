package com.zhaotai.uzao.ui.person.invite.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.person.invite.contract.InviteContract;
import com.zhaotai.uzao.ui.person.invite.model.RebateBean;

/**
 * Time: 2017/12/2
 * Created by LiYou
 * Description :
 */

public class InvitePresenter extends InviteContract.Presenter {

    private InviteContract.View view;
    private InviteContract.DetailView mDetailView;//邀请明细

    public InvitePresenter(InviteContract.View view, Context context) {
        this.view = view;
        mContext = context;
    }

    public InvitePresenter(InviteContract.DetailView view, Context context) {
        this.mDetailView = view;
        mContext = context;
    }


    /**
     * 获取分享返利数据
     */
    @Override
    public void getRebate() {
        Api.getDefault().getRebate()
                .compose(RxHandleResult.<RebateBean>handleResult())
                .subscribe(new RxSubscriber<RebateBean>(mContext) {
                    @Override
                    public void _onNext(RebateBean s) {
                        if (mDetailView != null) {
                            mDetailView.showRebate(s);
                        }
                        if (view != null) {
                            view.showRebate(s);
                        }
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 获取邀请明细
     */
    @Override
    public void getInviteDetail(int start) {
        Api.getDefault().getInviteDetail(0)
                .compose(RxHandleResult.<PageInfo<RebateBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<RebateBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<RebateBean> rebateBeanPageInfo) {
                        mDetailView.showDetail(rebateBeanPageInfo);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }
}
