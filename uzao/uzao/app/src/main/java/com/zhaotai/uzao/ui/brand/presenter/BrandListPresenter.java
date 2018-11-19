package com.zhaotai.uzao.ui.brand.presenter;

import android.content.Context;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.brand.contract.BrandListContract;

/**
 * Time: 2018/1/29
 * Created by LiYou
 * Description : 品牌
 */

public class BrandListPresenter extends BrandListContract.Presenter {

    private BrandListContract.View view;

    public BrandListPresenter(BrandListContract.View view, Context context) {
        this.view = view;
        mContext = context;
    }

    /**
     * 品牌商品
     *
     * @param start   开始位置
     * @param brandId 品牌Id
     */
    public void getBrandProductList(final int start, String brandId) {
        Api.getDefault().getBrandProduct(brandId, start, "")
                .compose(RxHandleResult.<PageInfo<GoodsBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<GoodsBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<GoodsBean> goodsBeanPageInfo) {
                        view.showContent();
                        view.stopLoadingMore();
                        view.showProduct(goodsBeanPageInfo);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            view.showNetworkFail(message);
                        } else {
                            view.loadingFail();
                        }
                    }
                });
    }

    /**
     * 品牌商品 搜索
     *
     * @param start      开始位置
     * @param brandId    品牌Id
     * @param searchWord 搜索内容
     */
    public void getBrandProductList(final int start, String brandId, String searchWord) {
        Api.getDefault().getBrandProduct(brandId, start, searchWord)
                .compose(RxHandleResult.<PageInfo<GoodsBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<GoodsBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<GoodsBean> goodsBeanPageInfo) {
                        if (goodsBeanPageInfo.totalRows > 0) {
                            view.showContent();
                            view.stopLoadingMore();
                            view.showProduct(goodsBeanPageInfo);
                        } else {
                            view.showEmpty(mContext.getString(R.string.empty_search));
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            view.showNetworkFail(message);
                        } else {
                            view.loadingFail();
                        }
                    }
                });
    }

    /**
     * 品牌素材
     *
     * @param start   开始位置
     * @param brandId 品牌Id
     */
    public void getBrandMaterialList(final int start, String brandId) {
        Api.getDefault().getBrandMaterial(brandId, start, "")
                .compose(RxHandleResult.<PageInfo<MaterialListBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<MaterialListBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<MaterialListBean> materialListBeanPageInfo) {
                        view.showContent();
                        view.stopLoadingMore();
                        view.showMaterial(materialListBeanPageInfo);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            view.showNetworkFail(message);
                        } else {
                            view.loadingFail();
                        }
                    }
                });
    }

    /**
     * 品牌素材 -- 搜索
     *
     * @param start      开始位置
     * @param brandId    品牌Id
     * @param searchWord 搜索内容
     */
    public void getBrandMaterialList(final int start, String brandId, String searchWord) {
        Api.getDefault().getBrandMaterial(brandId, start, searchWord)
                .compose(RxHandleResult.<PageInfo<MaterialListBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<MaterialListBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<MaterialListBean> materialListBeanPageInfo) {
                        if (materialListBeanPageInfo.totalRows > 0) {
                            view.showContent();
                            view.stopLoadingMore();
                            view.showMaterial(materialListBeanPageInfo);
                        } else {
                            view.showEmpty(mContext.getString(R.string.empty_search));
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            view.showNetworkFail(message);
                        } else {
                            view.loadingFail();
                        }
                    }
                });
    }

    /**
     * 品牌主题
     *
     * @param start   开始位置
     * @param brandId 品牌Id
     */
    public void getBrandThemeList(final int start, String brandId) {
        Api.getDefault().getBrandTheme(brandId, start, "")
                .compose(RxHandleResult.<PageInfo<ThemeBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<ThemeBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<ThemeBean> materialListBeanPageInfo) {
                        view.showContent();
                        view.stopLoadingMore();
                        view.showTheme(materialListBeanPageInfo);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            view.showNetworkFail(message);
                        } else {
                            view.loadingFail();
                        }
                    }
                });
    }

    /**
     * 品牌主题 -- 搜索
     *
     * @param start      开始位置
     * @param brandId    品牌id
     * @param searchWord 搜索内容
     */
    public void getBrandThemeList(final int start, String brandId, String searchWord) {
        Api.getDefault().getBrandTheme(brandId, start, searchWord)
                .compose(RxHandleResult.<PageInfo<ThemeBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<ThemeBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<ThemeBean> materialListBeanPageInfo) {
                        if (materialListBeanPageInfo.totalRows > 0) {
                            view.showContent();
                            view.stopLoadingMore();
                            view.showTheme(materialListBeanPageInfo);
                        } else {
                            view.showEmpty(mContext.getString(R.string.empty_search));
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            view.showNetworkFail(message);
                        } else {
                            view.loadingFail();
                        }
                    }
                });
    }

}
