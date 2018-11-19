package com.zhaotai.uzao.ui.design.material.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.CategoryBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.ProductListBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.design.material.contract.MaterialListAllContract;

import java.util.HashMap;


/**
 * 全部素材列表presenter
 * Created by zp
 */

public class MaterialAllListPresenter extends MaterialListAllContract.Presenter {
    private MaterialListAllContract.View mView;

    public MaterialAllListPresenter(Context context, MaterialListAllContract.View view) {
        mContext = context;
        mView = view;
    }

    /**
     * 获得标签列表
     */
    @Override
    public void getTabList() {
        Api.getDefault().getMaterialCategory()
                .compose(RxHandleResult.<CategoryBean>handleResult())
                .subscribe(new RxSubscriber<CategoryBean>(mContext) {
                    @Override
                    public void _onNext(CategoryBean bean) {
                        mView.showTabList(bean.children);
                    }

                    @Override
                    public void _onError(String message) {
                        mView.showNetworkFail(message);
                    }
                });
    }


    /**
     * 获得标签获得具体内容列表
     * @param start 起始位置
     * @param tabCode 起始标签的tabCode
     * @param loadingStatus 档期页面的状态 是状态页面的loading 还是下拉刷新的loading
     */
    @Override
    public void getContentList(final int start, String tabCode, final boolean loadingStatus) {
        HashMap<String, String> params = new HashMap<>();
        params.put("start", String.valueOf(start));
        params.put("categoryCode1", tabCode);
        Api.getDefault()
                .getMaterialList(params)
                .compose(RxHandleResult.<ProductListBean<MaterialListBean>>handleResult())
                .subscribe(new RxSubscriber<ProductListBean<MaterialListBean>>(mContext) {
                    @Override
                    public void _onNext(ProductListBean<MaterialListBean> materialListBeanProductListBean) {
                        mView.showContentList(materialListBeanProductListBean.page);
                        if (start == 0) {
                            mView.showContent();
                            mView.stopRefresh();
                        } else {
                            mView.stopLoadingMore();
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            mView.stopRefresh();
                            mView.showNetworkFail(message);
                        } else {
                            mView.loadingFail();
                        }
                    }
                });
    }
}
