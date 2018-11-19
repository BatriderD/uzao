package com.zhaotai.uzao.ui.person.discount.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.bean.DiscountCouponBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.person.discount.contract.MyDiscountCouponContract;

/**
 * Time: 2017/7/19
 * Created by LiYou
 * Description : 我的优惠券页面presenter
 */

public class MyDiscountPresenter extends MyDiscountCouponContract.Presenter {

    private MyDiscountCouponContract.View view;

    public MyDiscountPresenter(MyDiscountCouponContract.View view, Context context) {
        this.view = view;
        mContext = context;
    }

    /**
     * 获取未使用的优惠券列表
     * @param start 开始
     * @param isLoading 是否为loading状态页
     */
    @Override
    public void getUnusedDiscountList(final int start, final boolean isLoading) {
        Api.getDefault().getDiscountList("N",start)
                .compose(RxHandleResult.<PageInfo<DiscountCouponBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<DiscountCouponBean>>(mContext, false) {
                    @Override
                    public void _onNext(PageInfo<DiscountCouponBean> discountCouponBean) {
                        if (isLoading && start == Constant.PAGING_HOME) {
                            view.showContent();
                            view.stopLoadingMore();
                        } else if (start == 0) {
                            view.stopRefresh();
                        } else {
                            view.stopLoadingMore();
                        }
                        view.showDiscountList(discountCouponBean);
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
     * 获取已使用的优惠券列表
     * @param start 开始
     * @param isLoading 是否为loading状态页
     */
    @Override
    public void getUsedDiscountList(final int start, final boolean isLoading) {
        Api.getDefault().getDiscountList("Y",start)
                .compose(RxHandleResult.<PageInfo<DiscountCouponBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<DiscountCouponBean>>(mContext, false) {
                    @Override
                    public void _onNext(PageInfo<DiscountCouponBean> discountCouponBean) {
                        if (isLoading && start == Constant.PAGING_HOME) {
                            view.showContent();
                            view.stopLoadingMore();
                        } else if (start == 0) {
                            view.stopRefresh();
                        } else {
                            view.stopLoadingMore();
                        }
                        view.showDiscountList(discountCouponBean);
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
     * 获取过期的优惠券列表
     * @param start 开始
     * @param isLoading 是否为loading状态页
     */
    @Override
    public void getOverdueDiscountList(final int start, final boolean isLoading) {
        Api.getDefault().getDiscountList("D",start)
                .compose(RxHandleResult.<PageInfo<DiscountCouponBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<DiscountCouponBean>>(mContext, false) {
                    @Override
                    public void _onNext(PageInfo<DiscountCouponBean> discountCouponBean) {
                        if (isLoading && start == Constant.PAGING_HOME) {
                            view.showContent();
                            view.stopLoadingMore();
                        } else if (start == 0) {
                            view.stopRefresh();
                        } else {
                            view.stopLoadingMore();
                        }
                        view.showDiscountList(discountCouponBean);
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
