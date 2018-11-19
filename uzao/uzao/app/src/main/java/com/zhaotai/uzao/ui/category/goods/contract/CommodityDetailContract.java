package com.zhaotai.uzao.ui.category.goods.contract;

import com.zhaotai.uzao.adapter.PropertyAdapter;
import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.ActivityModelBean;
import com.zhaotai.uzao.bean.CommentBean;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.GoodsDetailMallBean;
import com.zhaotai.uzao.bean.MyTrackRequestBean;
import com.zhaotai.uzao.bean.ShoppingPropertyBean;

import java.util.List;

/**
 * Time: 2017/5/15
 * Created by LiYou
 * Description : 商品详情
 */

public interface CommodityDetailContract {

    interface View extends BaseView {
        /**
         * 显示商品详情
         *
         * @param data 商品详情
         */
        void showDetail(GoodsDetailMallBean data);

        //已收藏
        void hasCollect();

        //未收藏
        void unCollect();

        void showProgressDialog();

        void stopProgressDialog();

        //打开规格属性到定制页面
        void showBottomSheetToCustomDesign();

        void setVisibilityCustomDesign(boolean visibility);

        void showComment(CommentBean commentBean);

        void showRecommendSpu(List<GoodsBean> goodsBean);

        void hasShowEmptyView();

        void openShareBoard(boolean hasPoster);
    }

    abstract class Presenter extends BasePresenter {
        /**
         * 获取商品详情
         *
         * @param id 商品id
         */
        public abstract void getDetail(String id);

        /**
         * 收藏商品
         */
        public abstract void collectProduct(String productId);

        /**
         * 加入购物车
         */
        public abstract void addShoppingCart(ShoppingPropertyBean data);

        /**
         * 判断是否登录
         */
        public abstract boolean isLogin();

        /**
         * 检查商品是否收藏
         */
        public abstract void isCollection(String productId);

        /**
         * 取消收藏
         */
        public abstract void deleteCollect(String productId);

        /**
         * 获取skuId
         */
        public abstract GoodsDetailMallBean.Sku getSkuId(PropertyAdapter propertyAdapter, GoodsDetailMallBean data, String count);

        /**
         * 获取活动优惠价格
         */
        public abstract int getCutPrice(ActivityModelBean activityModelBean, int price);

        /**
         * 足迹
         */
        public abstract void addMyTrack(MyTrackRequestBean bean);

        public abstract void hasPoster();
    }

}
