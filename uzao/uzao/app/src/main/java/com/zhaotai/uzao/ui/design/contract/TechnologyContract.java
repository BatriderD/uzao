package com.zhaotai.uzao.ui.design.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.TechnologyBean;

/**
 * 工艺控制类
 */

public interface TechnologyContract {

    interface View extends BaseView {


        void showTechnologyList(PageInfo<TechnologyBean> technologyBeanPageInfos);

        void showTechnologyListFailed();
    }

    abstract class Presenter extends BasePresenter {


        public abstract void getTechnology(int start);
    }

}
