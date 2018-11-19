package com.zhaotai.uzao.ui.person.collection.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;

/**
 * Time: 2017/5/22
 * Created by LiYou
 * Description :
 */

public interface CollectionMaterialSearchContract {

    interface View extends BaseSwipeView {
        void showCollectionMaterialList(PageInfo<MaterialListBean> list);
    }

    abstract class Presenter extends BasePresenter {

        /**
         * 获取收藏的商品
         */
        public abstract void getCollectMaterialList(int start, String name);

    }
}
