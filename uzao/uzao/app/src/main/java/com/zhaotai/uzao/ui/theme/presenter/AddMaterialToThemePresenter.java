package com.zhaotai.uzao.ui.theme.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.bean.CategoryBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.theme.contract.AddMaterialToThemeContract;

import java.util.HashMap;

/**
 * Time: 2018/1/19
 * Created by LiYou
 * Description :  新增素材列表presenter
 */

public class AddMaterialToThemePresenter extends AddMaterialToThemeContract.Presenter {
    private AddMaterialToThemeContract.View mView;

    public AddMaterialToThemePresenter(Context context, AddMaterialToThemeContract.View mView) {
        this.mView = mView;
        mContext = context;
    }

    @Override
    public void getTabList() {
        Api.getDefault().getMaterialCategory()
                .compose(RxHandleResult.<CategoryBean>handleResult())
                .subscribe(new RxSubscriber<CategoryBean>(mContext) {

                    @Override
                    public void _onNext(CategoryBean categoryBean) {
                        mView.showTabList(categoryBean);
                    }

                    @Override
                    public void _onError(String message) {
                        mView.showNetworkFail("网络请求失败");
                    }
                });
    }

    @Override
    public void getMaterialCategoryList(final int start, final boolean isLoading, String categoryCode1) {
        final HashMap<String, String> params = new HashMap<>();
        params.put("categoryCode1", categoryCode1);
        params.put("start", String.valueOf(start));
        params.put("length", "10");
        Api.getDefault().getThemeMaterialList(params)
                .compose(RxHandleResult.<PageInfo<MaterialListBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<MaterialListBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<MaterialListBean> data) {
                        if (isLoading && start == Constant.PAGING_HOME) {
                            mView.showContent();
                            mView.stopLoadingMore();
                        } else if (start == 0) {
                            mView.stopRefresh();
                        } else {
                            mView.stopLoadingMore();
                        }
                        mView.showMaterialCategoryList(data);
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
