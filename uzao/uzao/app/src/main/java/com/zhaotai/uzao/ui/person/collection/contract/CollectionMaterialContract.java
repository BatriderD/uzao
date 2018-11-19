package com.zhaotai.uzao.ui.person.collection.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.ui.person.collection.model.CategoryCodeBean;

import java.util.List;
import java.util.Map;

/**
 * Time: 2017/5/22
 * Created by LiYou
 * Description :
 */

public interface CollectionMaterialContract {

    interface View extends BaseSwipeView {


        void showCollectionMaterialList(PageInfo<MaterialListBean> list);

        void cancelCollection();

        void showCollectionMaterialCategoryCode(List<CategoryCodeBean> categoryCodeList);

        void showProductNum(String num);

    }

    abstract class Presenter extends BasePresenter {

        /**
         * 获取收藏的商品
         */
        public abstract void getCollectMaterialList(int start, Map<String, String> params);

        /**
         * 取消收藏
         */
        public abstract void cancelCollection(List<MaterialListBean> goods);

        public abstract void getCollectMaterialCategoryCode();

    }
}
