package com.zhaotai.uzao.ui.person.collection.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.ui.person.collection.model.CategoryCodeBean;

import java.util.List;
import java.util.Map;

/**
 * Time: 2017/5/22
 * Created by LiYou
 * Description :
 */

public interface CollectionProductContract {

    interface View extends BaseSwipeView {


        void showCollectionProductList(PageInfo<GoodsBean> list);

        void cancelCollection();

        void showCollectionProductCategoryCode(List<CategoryCodeBean> categoryCodeList);

        void showProductNum(String num);

    }

    abstract class Presenter extends BasePresenter {

        /**
         * 获取收藏的商品
         */
        public abstract void getCollectProductList(int start, Map<String, String> params);

        /**
         * 取消收藏
         */
        public abstract void cancelCollection(List<GoodsBean> goods);

        public abstract void getCollectProductCategoryCode();

    }
}
