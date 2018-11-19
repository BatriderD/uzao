package com.zhaotai.uzao.ui.design.material.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.CategoryBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/7/12.
 */

public interface MaterialListAllContract {

    interface View extends BaseSwipeView {

        void showTabList(List<CategoryBean> lists);

        void showContentList(PageInfo<MaterialListBean> page);

        void showContentError(String msg);
    }

    abstract class Presenter extends BasePresenter {

        public abstract void getTabList();

        public abstract void getContentList(int i, String tabCode,boolean loadingStatus);
    }

}
