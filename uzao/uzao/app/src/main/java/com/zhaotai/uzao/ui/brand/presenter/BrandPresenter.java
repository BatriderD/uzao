package com.zhaotai.uzao.ui.brand.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.BrandBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.brand.contract.BrandContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Time: 2018/1/29
 * Created by LiYou
 * Description : 品牌
 */

public class BrandPresenter extends BrandContract.Presenter {

    private BrandContract.View view;
    private List<String> ids;

    public BrandPresenter(BrandContract.View view, Context context) {
        this.view = view;
        mContext = context;
        ids = new ArrayList<>();
    }

    /**
     * 获取品牌信息
     *
     * @param brandId 品牌id
     */
    @Override
    public void getBrandInfo(String brandId) {
        Api.getDefault().getBrandIntro(brandId)
                .compose(RxHandleResult.<BrandBean>handleResult())
                .subscribe(new RxSubscriber<BrandBean>(mContext, false) {
                    @Override
                    public void _onNext(BrandBean info) {
                        view.showBrandInfo(info);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 关注品牌
     *
     * @param brandId 品牌id
     */
    public void attentionBrand(String brandId) {
        ids.clear();
        ids.add(brandId);
        Api.getDefault().attentionBrand(ids)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String s) {
                        view.attention();
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 取消关注品牌
     *
     * @param brandId 品牌id
     */
    public void cancelAttentionBrand(String brandId) {
        ids.clear();
        ids.add(brandId);
        Api.getDefault().cancelAttentionBrand(ids)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String s) {
                        view.cancelAttention();
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }
}
