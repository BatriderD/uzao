package com.zhaotai.uzao.ui.category.goods.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ProductOptionBean;

import java.util.Map;

/**
 * Time: 2017/5/15
 * Created by LiYou
 * Description : 商品列表
 */

public interface CommodityListContract {

    interface View extends BaseSwipeView {
        //显示商品分类列表
        void showCategoryList(PageInfo<GoodsBean> list);

        //显示导航商品列表
        void showNavigateList(PageInfo<GoodsBean> list);

        //显示筛选
        void showFilter(ProductOptionBean opt);
    }

    abstract class Presenter extends BasePresenter {

        public abstract void getCategoryList(int start, boolean isLoading, Map<String, String> params);

        public abstract void getActivityList(int start, boolean isLoading, Map<String, String> params);
    }

}
