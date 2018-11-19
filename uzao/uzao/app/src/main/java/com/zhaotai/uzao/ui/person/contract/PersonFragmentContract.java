package com.zhaotai.uzao.ui.person.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.PersonBean;

import java.util.List;

/**
 * description: 个人fragment管理
 * author : zp
 * date: 2017/7/14
 */

public interface PersonFragmentContract {
    interface View extends BaseView {
        /**
         * 设置用户信息
         *
         * @param personInfo 用户信息类
         */
        void showPersonInfo(PersonBean personInfo);

        /**
         * 设置用户未登录
         */
        void showUnLogin();

        /**
         * 展示未读消息数
         *
         * @param msg 未读消息
         */
        void showUnHandleMessage(int msg);

        /**
         * 展示可用优惠券数目
         * @param size
         */
        void showDiscountSize(int size);

        /**
         * 显示猜你喜欢
         */
        void showRecommend(List<GoodsBean> goodsBeenList);
    }

    abstract class Presenter extends BasePresenter {
        /**
         * 获取登录状态
         */
        public abstract void getLoginStage();

        /**
         * 获取个人信息
         */
        public abstract void getPersonInfo();



        public abstract void getUnHandleMessage();

        /**
         * 获取未使用优惠券
         */
        public abstract void getUnusedDiscountList();
    }
}
