package com.zhaotai.uzao.ui.search.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.AssociateBean;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.ProductListBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.search.contract.CommodityAndSearchContract;

import java.util.List;
import java.util.Map;

/**
 * Time: 2017/11/30
 * Created by LiYou
 * Description : 商品搜索
 */

public class CommodityAndSearchPresenter extends CommodityAndSearchContract.Presenter {

    private CommodityAndSearchContract.View mView;

    public CommodityAndSearchPresenter(CommodityAndSearchContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    /**
     * 获取商品
     *
     * @param start  开始位置
     * @param params 参数
     */
    @Override
    public void getCommodityList(final int start, final Map<String, String> params) {
        Api.getDefault().getProductList(params)
                .compose(RxHandleResult.<ProductListBean<GoodsBean>>handleResult())
                .subscribe(new RxSubscriber<ProductListBean<GoodsBean>>(mContext, true) {
                    @Override
                    public void _onNext(ProductListBean<GoodsBean> goodsBeanPageInfo) {
                        mView.stopLoadingMore();
                        mView.showCommodityList(goodsBeanPageInfo.page);
                        if (params.get("needOption").equals("Y")) {
                            mView.showFilter(goodsBeanPageInfo.opt);
                        }
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
     * 关键字推荐
     */
    public void getAssociate(String queryWord) {
        Api.getDefault().getAssociate(queryWord, "spu")
                .compose(RxHandleResult.<List<AssociateBean>>handleResult())
                .subscribe(new RxSubscriber<List<AssociateBean>>(mContext) {
                    @Override
                    public void _onNext(List<AssociateBean> s) {
                        mView.showAssociateList(s);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }
}
