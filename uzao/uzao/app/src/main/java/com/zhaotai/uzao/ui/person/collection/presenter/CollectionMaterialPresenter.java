package com.zhaotai.uzao.ui.person.collection.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.person.collection.contract.CollectionMaterialContract;
import com.zhaotai.uzao.ui.person.collection.fragment.CollectionMaterialFragment;
import com.zhaotai.uzao.ui.person.collection.model.CategoryCodeBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Time: 2017/5/22
 * Created by LiYou
 * Description : 收藏素材
 */

public class CollectionMaterialPresenter extends CollectionMaterialContract.Presenter {

    private CollectionMaterialContract.View view;
    private Context context;

    public CollectionMaterialPresenter(CollectionMaterialContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    /**
     * 获取收藏素材列表
     *
     * @param start 开始位置
     */
    @Override
    public void getCollectMaterialList(final int start, final Map<String, String> params) {
        params.put("start", String.valueOf(start));
        Api.getDefault().getCollectionMaterialList(params)
                .compose(RxHandleResult.<PageInfo<MaterialListBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<MaterialListBean>>(context, true) {
                    @Override
                    public void _onNext(PageInfo<MaterialListBean> goodsBean) {
                        view.showContent();
                        view.stopLoadingMore();
                        view.showCollectionMaterialList(goodsBean);
                        if (params.get("start").equals("0") && params.get("status").isEmpty()) {
                            view.showProductNum(String.valueOf(goodsBean.totalRows));
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
     * 取消收藏
     */
    @Override
    public void cancelCollection(final List<MaterialListBean> goods) {
        final List<String> list = new ArrayList<>();
        for (int i = 0; i < goods.size(); i++) {
            if (goods.get(i).isSelected) {
                list.add(goods.get(i).materialId);
            }
        }
        if (list.size() > 0) {
            Api.getDefault().cancelCollectMaterial(list)
                    .compose(RxHandleResult.<String>handleResult())
                    .subscribe(new RxSubscriber<String>(context, true) {
                        @Override
                        public void _onNext(String s) {
                            view.cancelCollection();
                        }

                        @Override
                        public void _onError(String message) {

                        }
                    });
        }
    }

    /**
     * 获取一级分类列表
     */
    @Override
    public void getCollectMaterialCategoryCode() {
        Api.getDefault().getCollectionCategoryCode("material")
                .compose(RxHandleResult.<List<CategoryCodeBean>>handleResult())
                .subscribe(new RxSubscriber<List<CategoryCodeBean>>(context) {
                    @Override
                    public void _onNext(List<CategoryCodeBean> s) {
                        s.add(0, new CategoryCodeBean("", "全部素材",true));
                        view.showCollectionMaterialCategoryCode(s);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 全选 和取消全选
     */
    public void changeSelectState(List<MaterialListBean> goods, boolean isSelect) {
        for (int i = 0; i < goods.size(); i++) {
            goods.get(i).isSelected = isSelect;
        }
    }

    /**
     * 遍历
     */
    public boolean checkSelectState(List<MaterialListBean> goods) {
        for (int i = 0; i < goods.size(); i++) {
            if (!goods.get(i).isSelected) return false;
        }
        return true;
    }

    public Map<String, String> createParams(Map<String, String> params, int status, String categoryCode) {
        params.clear();
        switch (status) {
            case CollectionMaterialFragment.STATUS_NORMAL://全部素材
                params.put("categoryCode", categoryCode);
                params.put("status", "");
                break;
            case CollectionMaterialFragment.STATUS_SOLD_OUT://下架素材
                params.put("categoryCode", "");
                params.put("status", "unPublished");
                break;
        }
        return params;
    }
}
