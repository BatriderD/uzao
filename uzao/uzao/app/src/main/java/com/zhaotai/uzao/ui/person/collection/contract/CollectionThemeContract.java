package com.zhaotai.uzao.ui.person.collection.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeBean;
import com.zhaotai.uzao.ui.person.collection.model.CategoryCodeBean;

import java.util.List;
import java.util.Map;

/**
 * Time: 2017/5/22
 * Created by LiYou
 * Description :
 */

public interface CollectionThemeContract {

    interface View extends BaseSwipeView {


        void showCollectionThemeList(PageInfo<ThemeBean> list);

        void cancelCollection();

        void showCollectionThemeCategoryCode(List<CategoryCodeBean> categoryCodeList);

        void showProductNum(String num);

    }

    abstract class Presenter extends BasePresenter {

        /**
         * 获取收藏的商品
         */
        public abstract void getCollectThemeList(int start, Map<String, String> params, String status);

        /**
         * 取消收藏
         */
        public abstract void cancelCollection(List<ThemeBean> goods);

        public abstract void getCollectThemeCategoryCode();

    }
}
