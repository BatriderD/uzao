package com.zhaotai.uzao.ui.search.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.AssociateBean;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.GoodsDetailMallBean;
import com.zhaotai.uzao.bean.MaterialDetailBean;
import com.zhaotai.uzao.bean.ProductListBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.design.activity.EditorActivity;
import com.zhaotai.uzao.ui.search.contract.DesignProductAndSearchContract;

import java.util.List;
import java.util.Map;

/**
 * Time: 2017/11/30
 * Created by LiYou
 * Description :
 */

public class DesignProductAndSearchPresenter extends DesignProductAndSearchContract.Presenter {

    private DesignProductAndSearchContract.View mView;

    public DesignProductAndSearchPresenter(DesignProductAndSearchContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    public void getAssociate(String queryWord) {
        Api.getDefault().getAssociate(queryWord, "spu")
                .compose(RxHandleResult.<List<AssociateBean>>handleResult())
                .subscribe(new RxSubscriber<List<AssociateBean>>(mContext) {
                    @Override
                    public void _onNext(List<AssociateBean> s) {
                        mView.showAssociateList(s);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    @Override
    public void getDesignProductList(int start, Map<String, String> map) {
        map.put("designType", "2d");
        Api.getDefault().getProductList(map)
                .compose(RxHandleResult.<ProductListBean<GoodsBean>>handleResult())
                .subscribe(new RxSubscriber<ProductListBean<GoodsBean>>(mContext, true) {
                    @Override
                    public void _onNext(ProductListBean<GoodsBean> goodsBeanPageInfo) {
                        mView.stopLoadingMore();
                        mView.showDesignProductList(goodsBeanPageInfo.page);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * @param spuId              商品id
     * @param materialDetailBean 素材详情
     * @param type               类型
     */
    public void checkIsNeedSku(final String spuId, final MaterialDetailBean materialDetailBean, final String type) {
        mView.showProgress();
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
                                    public void _onNext(GoodsDetailMallBean data) {
                                        if (s && data.skus.size() > 0) {
                                            getMkuId(data.skus.get(0).sequenceNBR, spuId, materialDetailBean, type);
                                        } else {
                                            mView.stopProgress();
                                            //进编辑器,选择 sku
                                            if (type.equals("design")) {
                                                EditorActivity.launch2DWhitMaterial(mContext, spuId, "N", data, materialDetailBean);
                                            } else {
                                                EditorActivity.launch2DWhitMaterial(mContext, spuId, "Y", data, materialDetailBean);
                                            }
                                        }
                                    }

                                    @Override
                                    public void _onError(String message) {
                                        mView.stopProgress();
                                    }
                                });

                    }

                    @Override
                    public void _onError(String message) {
                        mView.stopProgress();
                    }
                });
    }

    /**
     * 获取mkuid
     *
     * @param skuId skuId
     */
    public void getMkuId(String skuId, final String spuId, final MaterialDetailBean materialDetailBean, final String type) {
        Api.getDefault().getMkuId(skuId)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, false) {
                    @Override
                    public void _onNext(String mkuId) {
                        mView.stopProgress();
                        if (type.equals("design")) {
                            EditorActivity.launch2DWhitMaterial(mContext, mkuId, spuId, "N", materialDetailBean);
                        } else {
                            EditorActivity.launch2DWhitMaterial(mContext, mkuId, spuId, "Y", materialDetailBean);
                        }

                    }

                    @Override
                    public void _onError(String message) {
                        mView.stopProgress();
                    }
                });
    }
}
