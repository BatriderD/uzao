package com.zhaotai.uzao.ui.category.goods.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.GoodsDetailMallBean;
import com.zhaotai.uzao.bean.MaterialDetailBean;
import com.zhaotai.uzao.bean.ProductListBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.category.goods.contract.DesignProductListContract;
import com.zhaotai.uzao.ui.design.activity.EditorActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Time: 2018/1/11
 * Created by LiYou
 * Description : 可定制商品列表的presenter
 */

public class DesignProductListPresenter extends DesignProductListContract.Presenter {

    private DesignProductListContract.View view;
    private Map<String, String> params = new HashMap<>();

    public DesignProductListPresenter(DesignProductListContract.View view, Context context) {
        this.view = view;
        mContext = context;
    }

    /**
     * 获取sample可设计商品列表
     *
     * @param start     开始位置
     * @param isLoading 是否loading
     */
    @Override
    public void getDesignProductList(final int start, final boolean isLoading) {
        params.clear();
        params.put("start", String.valueOf(start));
        params.put("designType", "2d");
        params.put("spuType", "sample");
        params.put("length", "10");
        params.put("sort", "default_");
        Api.getDefault().getProductList(params)
                .compose(RxHandleResult.<ProductListBean<GoodsBean>>handleResult())
                .subscribe(new RxSubscriber<ProductListBean<GoodsBean>>(mContext) {
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
                        view.showDesignProductList(goodsBeanPageInfo.page);
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
     * 获取全部可设计商品列表
     *
     * @param start     开始位置
     * @param isLoading 是否loading
     */
    @Override
    public void getAllDesignProductList(final int start, final boolean isLoading) {
        params.clear();
        params.put("start", String.valueOf(start));
        params.put("types", "sample,design");
        params.put("length", "10");
        params.put("sort", "default_");
        Api.getDefault().getProductList(params)
                .compose(RxHandleResult.<ProductListBean<GoodsBean>>handleResult())
                .subscribe(new RxSubscriber<ProductListBean<GoodsBean>>(mContext) {
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
                        view.showDesignProductList(goodsBeanPageInfo.page);
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
     * 判断有几个sku
     *
     * @param spuId              商品id
     * @param materialDetailBean 素材详情
     */
    public void checkIsNeedSku(final String spuId, final MaterialDetailBean materialDetailBean) {
        //判断 sku是否大于1
        Api.getDefault().checkMkuCountIsSingle(spuId)
                .compose(RxHandleResult.<Boolean>handleResult())
                .subscribe(new RxSubscriber<Boolean>(mContext, false) {
                    @Override
                    public void _onNext(final Boolean s) {
                        Api.getDefault().getGoodsDetail(spuId, "all")
                                .compose(RxHandleResult.<GoodsDetailMallBean>handleResult())
                                .subscribe(new RxSubscriber<GoodsDetailMallBean>(mContext) {
                                    @Override
                                    public void _onNext(GoodsDetailMallBean goodsDetail) {
                                        if (s && goodsDetail.skus.size() > 0) {
                                            //因为可定制商品列表都是sample类型的 所以mkuId 商品详情里面没有
                                            getMkuId(goodsDetail.skus.get(0).sequenceNBR, spuId, materialDetailBean, goodsDetail);
                                        } else {
                                            EditorActivity.launch2DWhitMaterial(mContext, spuId, "N", goodsDetail, materialDetailBean);
                                        }
                                    }

                                    @Override
                                    public void _onError(String message) {
                                        view.stopProgress();
                                    }
                                });

                    }

                    @Override
                    public void _onError(String message) {
                        view.stopProgress();
                    }
                });
    }

    /**
     * 获取mkuId
     *
     * @param skuId skuId
     */
    public void getMkuId(String skuId, final String spuId, final MaterialDetailBean materialDetailBean, final GoodsDetailMallBean goodsDetail) {
        Api.getDefault().getMkuId(skuId)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, false) {
                    @Override
                    public void _onNext(String mkuId) {
                        view.stopProgress();
                        EditorActivity.launch2DWhitMaterial(mContext, mkuId, spuId, "N", materialDetailBean);
                    }

                    @Override
                    public void _onError(String message) {
                        view.stopProgress();
                    }
                });
    }
}
