package com.zhaotai.uzao.ui.person.collection.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.bean.EventBean.PersonInfo;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.person.collection.contract.CollectionProductContract;
import com.zhaotai.uzao.ui.person.collection.fragment.CollectionProductFragment;
import com.zhaotai.uzao.ui.person.collection.model.CategoryCodeBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Time: 2017/5/22
 * Created by LiYou
 * Description : 收藏商品
 */

public class CollectionProductPresenter extends CollectionProductContract.Presenter {

    private CollectionProductContract.View view;
    private Context context;

    public CollectionProductPresenter(CollectionProductContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    /**
     * 获取收藏商品列表
     *
     * @param start 开始位置
     */
    @Override
    public void getCollectProductList(final int start, final Map<String, String> params) {
        params.put("start", start + "");
        Api.getDefault().getCollectionProductList(params)
                .compose(RxHandleResult.<PageInfo<GoodsBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<GoodsBean>>(context, true) {
                    @Override
                    public void _onNext(PageInfo<GoodsBean> goodsBean) {
                        view.showContent();
                        view.stopLoadingMore();
                        view.showCollectionProductList(goodsBean);
                        if (params.get("start").equals("0") && params.get("isMarkDown").isEmpty()
                                && params.get("status").isEmpty()) {
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
    public void cancelCollection(final List<GoodsBean> goods) {
        final List<String> list = new ArrayList<>();
        for (int i = 0; i < goods.size(); i++) {
            if (goods.get(i).isSelected) {
                list.add(goods.get(i).spuId);
            }
        }
        if (list.size() > 0) {
            Api.getDefault().deleteCollectProduct(list)
                    .compose(RxHandleResult.<String>handleResult())
                    .subscribe(new RxSubscriber<String>(context, true) {
                        @Override
                        public void _onNext(String s) {
                            PersonInfo info = new PersonInfo();
                            info.code = EventBusEvent.REFRESH_COLLECTION;
                            info.interestDesignSpuCount = String.valueOf(list.size());
                            EventBus.getDefault().post(info);
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
    public void getCollectProductCategoryCode() {
        Api.getDefault().getCollectionCategoryCode("designSpu")
                .compose(RxHandleResult.<List<CategoryCodeBean>>handleResult())
                .subscribe(new RxSubscriber<List<CategoryCodeBean>>(context) {
                    @Override
                    public void _onNext(List<CategoryCodeBean> s) {
                        s.add(0, new CategoryCodeBean("", "全部商品", true));
                        view.showCollectionProductCategoryCode(s);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 全选 和取消全选
     */
    public void changeSelectState(List<GoodsBean> goods, boolean isSelect) {
        for (int i = 0; i < goods.size(); i++) {
            goods.get(i).isSelected = isSelect;
        }
    }

    /**
     * 遍历
     */
    public boolean checkSelectState(List<GoodsBean> goods) {
        for (int i = 0; i < goods.size(); i++) {
            if (!goods.get(i).isSelected) return false;
        }
        return true;
    }

    public Map<String, String> createParams(Map<String, String> params, int status, String categoryCode) {
        params.clear();
        switch (status) {
            case CollectionProductFragment.STATUS_NORMAL://全部商品
                params.put("categoryCode", categoryCode);
                params.put("isMarkDown", "");
                params.put("status", "");
                break;
            case CollectionProductFragment.STATUS_CUT://降价商品
                params.put("isMarkDown", "Y");
                params.put("categoryCode", "");
                params.put("status", "");
                break;
            case CollectionProductFragment.STATUS_FAILURE://失效商品
                params.put("status", "unPublished");
                params.put("categoryCode", "");
                params.put("isMarkDown", "");
                break;
        }
        return params;
    }
}
