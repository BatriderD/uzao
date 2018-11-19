package com.zhaotai.uzao.ui.design.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.DesignBean;
import com.zhaotai.uzao.bean.PageInfo;

/**
 * Time: 2017/9/5
 * Created by LiYou
 * Description :
 */

public interface MyDesignContract {
    interface View extends BaseSwipeView {
        void showDesignList(PageInfo<DesignBean> data);

    }

    abstract class Presenter extends BasePresenter {
        public abstract void getMyDesign(int start,boolean isLoading);
    }
}
