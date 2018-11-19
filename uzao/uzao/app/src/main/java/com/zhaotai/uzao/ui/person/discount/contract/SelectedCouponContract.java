package com.zhaotai.uzao.ui.person.discount.contract;


import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeNoPushView;
import com.zhaotai.uzao.bean.DiscountCouponBean;

import java.util.List;

/**
 * Time: 2017/7/18
 * Created by LiYou
 * Description : 选择优惠券页面控制类
 */

public interface SelectedCouponContract {

    interface View extends BaseSwipeNoPushView {

        void showDiscountList(List<DiscountCouponBean> list);

    }

    abstract class Presenter extends BasePresenter {

        //        获取优惠券列表
        public abstract void getCouponList(boolean isLoading, String money);
    }

}
