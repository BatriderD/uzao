package com.zhaotai.uzao.ui.theme.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.bean.CategoryBean;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.theme.contract.AddProductToThemeContract;

import java.util.HashMap;

/**
 * Time: 2018/1/19
 * Created by LiYou
 * Description :  新增商品到主题的素材列表presenter
 */

public class AddProductToThemePresenter extends AddProductToThemeContract.Presenter {
    private AddProductToThemeContract.View mView;

    public AddProductToThemePresenter(Context context, AddProductToThemeContract.View mView) {
        this.mView = mView;
        mContext = context;
    }

    @Override
    public void getTabList() {
        Api.getDefault().getCategory()
                .compose(RxHandleResult.<CategoryBean>handleResult())
                .subscribe(new RxSubscriber<CategoryBean>(mContext, false) {

                    @Override
                    public void _onNext(CategoryBean categoryBeen) {
                        mView.showTabList(categoryBeen.children);
//
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    @Override
    public void getProductCategoryList(final int start, final boolean isLoading, String categoryCode1) {
        final HashMap<String, String> params = new HashMap<>();
        params.put("categoryCode1", categoryCode1);
        params.put("start", String.valueOf(start));
        params.put("length", "10");
//        params.put("queryWord", "");
        Api.getDefault().getThemeProductList(params)
                .compose(RxHandleResult.<PageInfo<GoodsBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<GoodsBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<GoodsBean> data) {
                        if (isLoading && start == Constant.PAGING_HOME) {
                            mView.showContent();
                            mView.stopLoadingMore();
                        } else if (start == 0) {
                            mView.stopRefresh();
                        } else {
                            mView.stopLoadingMore();
                        }
                        mView.showProductCategoryList(data);
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
