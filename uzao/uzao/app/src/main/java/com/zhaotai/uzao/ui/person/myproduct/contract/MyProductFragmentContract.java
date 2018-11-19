package com.zhaotai.uzao.ui.person.myproduct.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.PageInfo;

/**
 * Time: 2017/5/22
 * Created by LiYou
 * Description :
 */

public interface MyProductFragmentContract {

    interface View extends BaseSwipeView {
        //显示商品列表
        void showMyProductList(PageInfo<GoodsBean> list);

        /**
         * 停止加载更多
         */
        @Override
        void stopLoadingMore();

        /**
         * 停止刷新
         */
        @Override
        void stopRefresh();

        /**
         * 改变订单状态
         */
        void changeStatus(String status, int position);

        /**
         * 显示下架信息
         */
        void showTip(String tip);

        /**
         * 显示过期信息
         */
        void showExpireTip(String spuId, String tip, int position);

        /**
         * 素材续费成功
         */
        void renewSuccess();

        /**
         * 显示加载框
         */
        void showProgressDialog();

        /**
         * 隐藏加载框
         */
        void dismissProgressDialog();
    }

    abstract class Presenter extends BasePresenter {

        /**
         * 获取我的商品
         */
        public abstract void getMyProductList(int start, boolean inLoginStatus, String status);


        /**
         * 提交审核
         */
        public abstract void submitMyProductToCheck(String spuId, String isTemplate, int position);
    }
}
