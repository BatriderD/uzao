package com.zhaotai.uzao.ui.shopping.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.CommentBean;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.PageInfo;

import java.util.List;

/**
 * Time: 2017/8/18
 * Created by LiYou
 * Description :
 */

public interface SimilarProductContract {
    interface View extends BaseView {
        //显示相似商品列表
        void showSimilarProductList(List<GoodsBean> data);
    }

    abstract class Presenter extends BasePresenter {
        public abstract void getSimilarProduct(boolean isLoading, String spuId);
    }
}
