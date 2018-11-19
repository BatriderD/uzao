package com.zhaotai.uzao.ui.shopping.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.SimilarBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.shopping.contract.SimilarProductContract;

/**
 * Time: 2017/11/22
 * Created by LiYou
 * Description :相似商品
 */

public class SimilarProductPresenter extends SimilarProductContract.Presenter {

    SimilarProductContract.View mView;

    public SimilarProductPresenter(SimilarProductContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    /**
     * 获取相似商品
     *
     * @param isLoading isLoading
     * @param spuId     商品Id
     */
    @Override
    public void getSimilarProduct(final boolean isLoading, String spuId) {
        Api.getDefault().getSimilarGoods(spuId)
                .compose(RxHandleResult.<SimilarBean>handleResult())
                .subscribe(new RxSubscriber<SimilarBean>(mContext, !isLoading) {
                    @Override
                    public void _onNext(SimilarBean goodsBeen) {
                        if (isLoading) {
                            mView.showContent();
                        }
                        mView.showSimilarProductList(goodsBeen.recommendSpuList);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }
}
