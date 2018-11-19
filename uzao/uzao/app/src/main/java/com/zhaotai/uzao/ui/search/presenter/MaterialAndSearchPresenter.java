package com.zhaotai.uzao.ui.search.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.AssociateBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.ProductListBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.search.contract.MaterialAndSearchContract;

import java.util.List;
import java.util.Map;

/**
 * Time: 2017/11/30
 * Created by LiYou
 * Description :素材搜索
 */

public class MaterialAndSearchPresenter extends MaterialAndSearchContract.Presenter {

    private MaterialAndSearchContract.View mView;

    public MaterialAndSearchPresenter(MaterialAndSearchContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    /**
     * 获取素材列表
     *
     * @param start  开始位置
     * @param params 参数
     */
    @Override
    public void getMaterialList(final int start, final Map<String, String> params) {
        Api.getDefault().getMaterialList(params)
                .compose(RxHandleResult.<ProductListBean<MaterialListBean>>handleResult())
                .subscribe(new RxSubscriber<ProductListBean<MaterialListBean>>(mContext, true) {
                    @Override
                    public void _onNext(ProductListBean<MaterialListBean> goodsBeanPageInfo) {
                        mView.stopLoadingMore();
                        mView.showMaterialList(goodsBeanPageInfo.page);
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

    public void getAssociate(String queryWord) {
        Api.getDefault().getAssociate(queryWord, "material")
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
