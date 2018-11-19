package com.zhaotai.uzao.ui.theme.contract;

import com.zhaotai.uzao.base.BaseNoSwipeView;
import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;

import java.util.HashMap;

/**
 * Time: 2018/1/19
 * Created by LiYou
 * Description : 收藏商品素材加入主题列表
 */

public interface CollectionToThemeContract {

    interface ProductView extends BaseNoSwipeView {

        void showProductList(PageInfo<GoodsBean> goodsBean);
    }

    interface MaterialView extends BaseNoSwipeView {

        void showMaterialList(PageInfo<MaterialListBean> goodsBean);
    }

    abstract class Presenter extends BasePresenter {

        public abstract void getCollectMaterialList(int start, boolean loginStatus, HashMap<String, String> params);

        public abstract void getCollectProductList(int start, boolean loginStatus, HashMap<String, String> params);
    }
}
