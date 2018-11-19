package com.zhaotai.uzao.ui.search.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.DesignerBean;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.MainSearchBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ProductListBean;
import com.zhaotai.uzao.bean.ThemeListBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.search.contract.CollectionSearchContract;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Time: 2017/11/30
 * Created by LiYou
 * Description :
 */

public class CollectionSearchPresenter extends CollectionSearchContract.Presenter {

    private CollectionSearchContract.View mView;

    public CollectionSearchPresenter(CollectionSearchContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    /**
     * 搜索全部
     *
     * @param collectionId 集合id
     */
    public void searchCollection(String collectionId) {
        Api.getDefault().searchCollection(collectionId)
                .compose(RxHandleResult.<MainSearchBean>handleResult())
                .subscribe(new RxSubscriber<MainSearchBean>(mContext) {
                    @Override
                    public void _onNext(MainSearchBean searchBean) {
                        if (mView != null) {
                            mView.showAllList(searchBean);
                        }
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }


    @Override
    public void getCommodityList(final int start, final Map<String, String> params) {
        Api.getDefault().getProductList(params)
                .compose(RxHandleResult.<ProductListBean<GoodsBean>>handleResult())
                .subscribe(new RxSubscriber<ProductListBean<GoodsBean>>(mContext, true) {
                    @Override
                    public void _onNext(ProductListBean<GoodsBean> goodsBeanPageInfo) {
                        mView.stopLoadingMore();
                        mView.showCommodityList(goodsBeanPageInfo.page);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            mView.showNetworkFail(message);
                        } else {
                            mView.loadingFail();
                        }
                    }
                });
    }

    /**
     * 素材列表
     *
     * @param start  开始位置
     * @param params 参数
     */
    public void getMaterialList(final int start, final Map<String, String> params) {
        Api.getDefault().getMaterialList(params)
                .compose(RxHandleResult.<ProductListBean<MaterialListBean>>handleResult())
                .subscribe(new RxSubscriber<ProductListBean<MaterialListBean>>(mContext, true) {
                    @Override
                    public void _onNext(ProductListBean<MaterialListBean> materialList) {
                        mView.stopLoadingMore();
                        mView.showMaterialList(materialList.page);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            mView.showNetworkFail(message);
                        } else {
                            mView.loadingFail();
                        }
                    }
                });
    }

    /**
     * 主题列表
     */
    public void getThemeList(final int start, Map<String, String> params) {
        Api.getDefault().getThemeList(params)
                .compose(RxHandleResult.<PageInfo<ThemeListBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<ThemeListBean>>(mContext, true) {
                    @Override
                    public void _onNext(PageInfo<ThemeListBean> themeBeanPageInfo) {
                        mView.stopLoadingMore();
                        mView.showThemeList(themeBeanPageInfo);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            mView.showNetworkFail(message);
                        } else {
                            mView.loadingFail();
                        }
                    }
                });
    }

    /**
     * 设计师列表
     */
    public void getDesignerList(final int start, Map<String, String> params) {
        Api.getDefault().getDesignerList(params)
                .compose(RxHandleResult.<PageInfo<DesignerBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<DesignerBean>>(mContext, true) {
                    @Override
                    public void _onNext(PageInfo<DesignerBean> designerBeanPageInfo) {
                        mView.stopLoadingMore();
                        mView.showDesignerList(designerBeanPageInfo);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            mView.showNetworkFail(message);
                        } else {
                            mView.loadingFail();
                        }
                    }
                });
    }


    /**
     * 关注设计师
     *
     * @param id 设计师id
     */
    public void attentionDesigner(final int pos, String id) {
        Api.getDefault().attentionDesigner(id)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String s) {
                        mView.changeDesigner(pos, true);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort(message);
                    }
                });
    }

    /**
     * 取消关注设计师
     *
     * @param id 设计师id
     */
    public void cancelDesigner(final int pos, String id) {
        List<String> idList = new ArrayList<>();
        idList.add(id);
        Api.getDefault().cancelAttentionDesigner(idList)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String s) {
                        mView.changeDesigner(pos, false);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("数据请求失败");
                    }
                });
    }
}
