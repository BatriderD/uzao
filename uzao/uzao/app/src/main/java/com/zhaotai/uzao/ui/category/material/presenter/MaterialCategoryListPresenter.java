package com.zhaotai.uzao.ui.category.material.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.ProductListBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.category.material.contract.MaterialCategoryListContract;

import java.util.Map;

/**
 * Time: 2018/1/8
 * Created by LiYou
 * Description : 素材分类列表
 */

public class MaterialCategoryListPresenter extends MaterialCategoryListContract.Presenter {

    private MaterialCategoryListContract.View view;

    public MaterialCategoryListPresenter(MaterialCategoryListContract.View view, Context context) {
        this.view = view;
        mContext = context;
    }

    /**
     * 获取分类列表
     */
    @Override
    public void getMaterialCategoryList(final int start, final boolean isLoading, final Map<String, String> params) {
        Api.getDefault().getMaterialList(params)
                .compose(RxHandleResult.<ProductListBean<MaterialListBean>>handleResult())
                .subscribe(new RxSubscriber<ProductListBean<MaterialListBean>>(mContext, false) {
                    @Override
                    public void _onNext(ProductListBean<MaterialListBean> goodsBeanPageInfo) {
                        if (isLoading && start == Constant.PAGING_HOME) {
                            view.showContent();
                            view.stopLoadingMore();
                        } else if (start == 0) {
                            view.stopRefresh();
                        } else {
                            view.stopLoadingMore();
                        }
                        view.showMaterialCategoryList(goodsBeanPageInfo.page);
                        //判断是否需要筛选
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
