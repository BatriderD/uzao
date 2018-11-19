package com.zhaotai.uzao.ui.order.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.AfterSalesBean;
import com.zhaotai.uzao.bean.PageInfo;

/**
 * Time: 2017/6/2
 * Created by LiYou
 * Description :
 */

public interface MyAfterSalesContract {

    interface View extends BaseSwipeView {

        //显示列表
        void showAfterSalesList(PageInfo<AfterSalesBean> pageInfo);
    }

    abstract class Presenter extends BasePresenter {

        //获取列表
        public abstract void getAfterSalesList(int start, boolean inLoginstatus);
    }
}
