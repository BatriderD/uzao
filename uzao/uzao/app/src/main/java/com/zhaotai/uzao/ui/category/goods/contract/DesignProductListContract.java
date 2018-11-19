package com.zhaotai.uzao.ui.category.goods.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.PageInfo;

/**
 * Time: 2018/1/11
 * Created by LiYou
 * Description : 定制商品列表
 */

public interface DesignProductListContract {

    interface View extends BaseSwipeView {
        //显示可定制商品列表
        void showDesignProductList(PageInfo<GoodsBean> list);

        void showProgress();

        void stopProgress();
    }

    abstract class Presenter extends BasePresenter {
        /**
         * 获取sample可定制商品列表
         *
         * @param start     开始位置
         * @param isLoading isLoading
         */
        public abstract void getDesignProductList(int start, boolean isLoading);

        /**
         * 获取All可定制商品列表
         *
         * @param start     开始位置
         * @param isLoading isLoading
         */
        public abstract void getAllDesignProductList(int start, boolean isLoading);

    }

}
