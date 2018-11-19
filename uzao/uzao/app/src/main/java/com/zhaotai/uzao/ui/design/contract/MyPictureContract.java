package com.zhaotai.uzao.ui.design.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.MyPicBean;
import com.zhaotai.uzao.bean.PageInfo;

/**
 * description: 我的图片控制类
 * author : ZP
 * date: 2017/11/23 0023.
 */

public interface MyPictureContract {

    interface View extends BaseSwipeView {
        void showDesignList(PageInfo<MyPicBean> data);

    }

    abstract class Presenter extends BasePresenter {
        public abstract void getMyPictureList(final int start, final boolean isLoading);
    }
}
