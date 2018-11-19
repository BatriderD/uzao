package com.zhaotai.uzao.ui.person.collection.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.person.collection.contract.CollectionThemeContract;
import com.zhaotai.uzao.ui.person.collection.model.CategoryCodeBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Time: 2017/5/22
 * Created by LiYou
 * Description : 收藏的主题
 */

public class CollectionThemePresenter extends CollectionThemeContract.Presenter {

    private CollectionThemeContract.View view;
    private Context context;

    public CollectionThemePresenter(CollectionThemeContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    /**
     * 获取收藏主题列表
     *
     * @param start 开始位置
     */
    @Override
    public void getCollectThemeList(final int start, final Map<String, String> params, String status) {
        params.clear();
        params.put("start", String.valueOf(start));
        params.put("status", status);
        Api.getDefault().getCollectionThemeList(params)
                .compose(RxHandleResult.<PageInfo<ThemeBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<ThemeBean>>(context, true) {
                    @Override
                    public void _onNext(PageInfo<ThemeBean> goodsBean) {
                        view.showContent();
                        view.stopLoadingMore();
                        view.showCollectionThemeList(goodsBean);
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
    public void cancelCollection(final List<ThemeBean> goods) {
        final List<String> list = new ArrayList<>();
        for (int i = 0; i < goods.size(); i++) {
            if (goods.get(i).isSelected()) {
                list.add(goods.get(i).themeId);
            }
        }
        if (list.size() > 0) {
            Api.getDefault().deleteCollectTheme(list)
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
    public void getCollectThemeCategoryCode() {
        Api.getDefault().getCollectionCategoryCode("theme")
                .compose(RxHandleResult.<List<CategoryCodeBean>>handleResult())
                .subscribe(new RxSubscriber<List<CategoryCodeBean>>(context) {
                    @Override
                    public void _onNext(List<CategoryCodeBean> s) {
                        s.add(0, new CategoryCodeBean("", "全部"));
                        view.showCollectionThemeCategoryCode(s);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 全选 和取消全选
     */
    public void changeSelectState(List<ThemeBean> goods, boolean isSelect) {
        for (int i = 0; i < goods.size(); i++) {
            goods.get(i).selected = isSelect;
        }
    }

    /**
     * 遍历
     */
    public boolean checkSelectState(List<ThemeBean> goods) {
        for (int i = 0; i < goods.size(); i++) {
            if (!goods.get(i).selected) return false;
        }
        return true;
    }

}
