package com.zhaotai.uzao.ui.designer.presenter;

import android.content.Context;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.designer.contract.DesignerListContract;

/**
 * Time: 2018/1/29
 * Created by LiYou
 * Description : 设计师列表
 */

public class DesignerListPresenter extends DesignerListContract.Presenter {

    private DesignerListContract.View view;

    public DesignerListPresenter(DesignerListContract.View view, Context context) {
        this.view = view;
        mContext = context;
    }

    /**
     * 设计师商品
     */
    public void getDesignerProductList(final int start, String designerId) {
        Api.getDefault().getDesignerProduct(designerId, start, "")
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
     * 设计师商品 搜索
     */
    public void getDesignerProductList(final int start, String designerId, String searchWord) {
        Api.getDefault().getDesignerProduct(designerId, start, searchWord)
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
     * 设计师素材
     */
    public void getDesignerMaterialList(final int start, String designerId) {
        Api.getDefault().getDesignerMaterial(designerId, start, "")
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
     * 设计师素材 -- 搜索
     */
    public void getDesignerMaterialList(final int start, String designerId, String searchWord) {
        Api.getDefault().getDesignerMaterial(designerId, start, searchWord)
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
     * 设计师主题
     */
    public void getDesignerThemeList(final int start, String designerId) {
        Api.getDefault().getDesignerTheme(designerId, start, "")
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
     * 设计师主题 -- 搜索
     */
    public void getDesignerThemeList(final int start, String designerId, String searchWord) {
        Api.getDefault().getDesignerTheme(designerId, start, searchWord)
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
