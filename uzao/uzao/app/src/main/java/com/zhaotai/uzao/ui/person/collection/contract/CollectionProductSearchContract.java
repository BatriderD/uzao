package com.zhaotai.uzao.ui.person.collection.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.PageInfo;

/**
 * Time: 2017/5/22
 * Created by LiYou
 * Description :
 */

public interface CollectionProductSearchContract {

    interface View extends BaseSwipeView {
        void showCollectionProductList(PageInfo<GoodsBean> list);
    }

    abstract class Presenter extends BasePresenter {

        /**
         * 获取收藏的商品
         */
        public abstract void getCollectProductList(int start,String name);

    }
}
