package com.zhaotai.uzao.ui.person.attention.presenter;

import android.content.Context;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.BrandBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.person.attention.contract.AttentionBrandContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Time: 2017/5/22
 * Created by LiYou
 * Description :关注的品牌管理
 */

public class AttentionBrandPresenter extends AttentionBrandContract.Presenter {

    private AttentionBrandContract.View view;

    public AttentionBrandPresenter(AttentionBrandContract.View view, Context context) {
        this.view = view;
        this.mContext = context;
    }

    /**
     * 获取品牌列表
     *
     * @param start 开始位置
     */
    @Override
    public void getBrandList(final int start) {
        Api.getDefault().getAttentionBrandList(start)
                .compose(RxHandleResult.<PageInfo<BrandBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<BrandBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<BrandBean> brandBeanPageInfo) {
                        view.stopLoadingMore();
                        view.showContent();
                        view.showBrandList(brandBeanPageInfo);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            view.showNetworkFail(message);
                        }
                    }
                });
    }

    /**
     * 搜索品牌
     *
     * @param start     开始位置
     * @param brandName 品牌名字关键字
     */
    public void getBrandList(final int start, String brandName) {
        Api.getDefault().getAttentionBrandList(start, brandName)
                .compose(RxHandleResult.<PageInfo<BrandBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<BrandBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<BrandBean> brandBeanPageInfo) {
                        if (brandBeanPageInfo.totalRows > 0) {
                            view.showContent();
                            view.stopLoadingMore();
                            view.showBrandList(brandBeanPageInfo);
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
     * 取消关注
     *
     * @param id       品牌Id
     * @param position 位置
     */
    @Override
    public void cancelAttentionBrand(String id, final int position) {
        List<String> ids = new ArrayList<>();
        ids.add(id);
        Api.getDefault().cancelAttentionBrand(ids)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext) {
                    @Override
                    public void _onNext(String s) {
                        view.cancelAttention(position);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }
}
