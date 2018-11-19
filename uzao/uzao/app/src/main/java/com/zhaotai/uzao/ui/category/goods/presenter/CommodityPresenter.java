package com.zhaotai.uzao.ui.category.goods.presenter;


import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.ProductListBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.category.goods.contract.CommodityListContract;

import java.util.Map;

/**
 * Time: 2017/5/15
 * Created by LiYou
 * Description : 商品
 */

public class CommodityPresenter extends CommodityListContract.Presenter {

    private CommodityListContract.View view;
    private String categoryCode1;
    private String categoryCode2;
    private String categoryCode3;
    private String ppValue = "";

    public CommodityPresenter(CommodityListContract.View view, Context context) {
        this.view = view;
        mContext = context;
    }

    public void getNavigateList(final int start, String categoryCode, final boolean isLoading) {
        Api.getDefault().getNavigateProductList(start, categoryCode)
                .compose(RxHandleResult.<ProductListBean<GoodsBean>>handleResult())
                .subscribe(new RxSubscriber<ProductListBean<GoodsBean>>(mContext) {
                    @Override
                    public void _onNext(ProductListBean<GoodsBean> product) {
                        if (product.category != null) {
                            categoryCode1 = product.category.cat1;
                            categoryCode2 = product.category.cat2;
                            categoryCode3 = product.category.cat3;
                        }
                        if (product.checkedOpt != null && product.checkedOpt.size() > 0) {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < product.checkedOpt.size(); i++) {
                                sb.append(product.checkedOpt.get(i).tc)
                                        .append(":");
                                for (int j = 0; j < product.checkedOpt.get(i).pps.size(); j++) {
                                    sb.append(product.checkedOpt.get(i).pps.get(j).pc)
                                            .append(",");
                                }
                                sb.append(";");
                            }
                            ppValue = sb.toString();
                        }
                        if (isLoading && start == Constant.PAGING_HOME) {
                            view.showContent();
                            view.stopLoadingMore();
                        } else if (start == 0) {
                            view.stopRefresh();
                        } else {
                            view.stopLoadingMore();
                        }
                        view.showNavigateList(product.page);
                        if (start == 0) {
                            view.showFilter(product.opt);
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        if (view != null)
                            view.showNetworkFail(message);
                    }
                });
    }


    /**
     * 商品列表
     *
     * @param start     开始位置
     * @param isLoading 是否加载
     * @param params    请求参数
     */
    @Override
    public void getCategoryList(final int start, final boolean isLoading, final Map<String, String> params) {
        params.put("start", String.valueOf(start));
        params.put("categoryCode1", categoryCode1);
        params.put("categoryCode2", categoryCode2);
        params.put("categoryCode3", categoryCode3);
        params.put("ppValues", ppValue);
        Api.getDefault().getProductList(params)
                .compose(RxHandleResult.<ProductListBean<GoodsBean>>handleResult())
                .subscribe(new RxSubscriber<ProductListBean<GoodsBean>>(mContext, !isLoading) {
                    @Override
                    public void _onNext(ProductListBean<GoodsBean> goodsBeanPageInfo) {
                        if (isLoading && start == Constant.PAGING_HOME) {
                            view.showContent();
                            view.stopLoadingMore();
                        } else if (start == 0) {
                            view.stopRefresh();
                        } else {
                            view.stopLoadingMore();
                        }
                        view.showCategoryList(goodsBeanPageInfo.page);
                        //判断是否需要显示筛选
                        if (params.get("needOption").equals("Y")) {
                            view.showFilter(goodsBeanPageInfo.opt);
                        }
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
     * 获取活动商品列表
     *
     * @param start     开始位置
     * @param isLoading 是否loading
     */
    @Override
    public void getActivityList(final int start, final boolean isLoading, final Map<String, String> params) {
        Api.getDefault().getActivityGoodsList(params)
                .compose(RxHandleResult.<ProductListBean<GoodsBean>>handleResult())
                .subscribe(new RxSubscriber<ProductListBean<GoodsBean>>(mContext, false) {
                    @Override
                    public void _onNext(ProductListBean<GoodsBean> goodsBeanPageInfo) {
                        if (isLoading && start == Constant.PAGING_HOME) {
                            view.showContent();
                            view.stopLoadingMore();
                        } else if (start == 0) {
                            view.stopRefresh();
                        } else {
                            view.stopLoadingMore();
                        }
                        view.showCategoryList(goodsBeanPageInfo.page);
                        if (params.get("needOption").equals("Y")) {
                            view.showFilter(goodsBeanPageInfo.opt);
                        }
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
