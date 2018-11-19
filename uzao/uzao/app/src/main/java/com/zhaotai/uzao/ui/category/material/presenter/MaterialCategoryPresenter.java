package com.zhaotai.uzao.ui.category.material.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.BaseResult;
import com.zhaotai.uzao.bean.CategoryBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.MultiMaterialCategoryBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.category.material.contract.MaterialCategoryContract;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Time: 2017/5/8
 * Created by LiYou
 * Description : 素材分类
 */

public class MaterialCategoryPresenter extends MaterialCategoryContract.Presenter {

    private MaterialCategoryContract.View mView;
    private List<MultiMaterialCategoryBean> multiList = new ArrayList<>();

    public MaterialCategoryPresenter(Context context, MaterialCategoryContract.View view) {
        mContext = context;
        mView = view;
    }

    /**
     * 获取素材分类
     */
    @Override
    public void getData() {
        //#1 先请求素材分类
        Api.getDefault().getMaterialCategory()
                .compose(RxHandleResult.<CategoryBean>handleResultMap())
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<CategoryBean, ObservableSource<BaseResult<List<MaterialListBean>>>>() {

                    @Override
                    public ObservableSource<BaseResult<List<MaterialListBean>>> apply(@NonNull CategoryBean categoryBean) throws Exception {
                        //#2 构造多布局数据
                        getMultiTypeCategoryData(categoryBean.children);
                        //#3 请求推荐素材
                        return Api.getDefault().getRecommendSearchMaterial("");
                    }
                }).compose(RxHandleResult.<List<MaterialListBean>>handleResult())
                .subscribe(new RxSubscriber<List<MaterialListBean>>(mContext) {
                    @Override
                    public void _onNext(List<MaterialListBean> goodsBeen) {
                        getMultiTypeMaterialData(goodsBeen);
                        if (mView != null) {
                            mView.showContent();
                            mView.stopRefresh();
                            mView.bindData(multiList);
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        if (mView != null) {
                            mView.stopRefresh();
                            mView.showNetworkFail(message);
                        }
                    }
                });
    }

    /**
     * 构造多布局数据
     *
     * @param data 素材分类数据
     */
    @Override
    public void getMultiTypeCategoryData(List<CategoryBean> data) {
        multiList.clear();
        for (int i = 0; i < data.size(); i++) {
            MultiMaterialCategoryBean item = new MultiMaterialCategoryBean(MultiMaterialCategoryBean.TYPE_CATEGORY);
            CategoryBean category = data.get(i);
            item.categoryCode = category.categoryCode;
            item.categoryName = category.categoryName;
            item.children = category.children;
            multiList.add(item);
        }
    }

    /**
     * 构造多布局数据
     *
     * @param data 推荐素材
     */
    @Override
    public void getMultiTypeMaterialData(List<MaterialListBean> data) {
        List<MaterialListBean> materialList;
        if (data.size() > 6) {
            materialList = data.subList(0, 6);
        } else {
            materialList = data;
        }
        multiList.add(new MultiMaterialCategoryBean(MultiMaterialCategoryBean.TYPE_LINE));
        multiList.add(new MultiMaterialCategoryBean(MultiMaterialCategoryBean.TYPE_RECOMMEND_MATERIAL_TITLE));
        for (MaterialListBean materialItem : materialList) {
            MultiMaterialCategoryBean item = new MultiMaterialCategoryBean(MultiMaterialCategoryBean.TYPE_RECOMMEND_MATERIAL);
            item.sequenceNBR = materialItem.sequenceNBR;
            item.thumbnail = materialItem.thumbnail;
            item.sourceMaterialName = materialItem.sourceMaterialName;
            multiList.add(item);
        }
    }

}
