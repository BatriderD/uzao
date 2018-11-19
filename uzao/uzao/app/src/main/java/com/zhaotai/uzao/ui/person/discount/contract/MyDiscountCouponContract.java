package com.zhaotai.uzao.ui.person.discount.contract;


import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.DiscountCouponBean;
import com.zhaotai.uzao.bean.PageInfo;

/**
 * Time: 2017/7/18
 * Created by LiYou
 * Description :
 */

public interface MyDiscountCouponContract {

    interface View extends BaseSwipeView {

        void showDiscountList(PageInfo<DiscountCouponBean> list);

    }

    abstract class Presenter extends BasePresenter {
        /**
         * 未使用优惠券
         */
        public abstract void getUnusedDiscountList(int start, boolean isLoading);

        /**
         * 已使用优惠券
         */
        public abstract void getUsedDiscountList(int start, boolean isLoading);

        /**
         * 已过期
         */
        public abstract void getOverdueDiscountList(int start, boolean isLoading);
    }

}
