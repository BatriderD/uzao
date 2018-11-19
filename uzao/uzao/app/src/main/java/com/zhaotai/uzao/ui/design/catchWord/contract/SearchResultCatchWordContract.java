package com.zhaotai.uzao.ui.design.catchWord.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.CatchWordBean;
import com.zhaotai.uzao.bean.PageInfo;

/**
 * 流行词控制类
 */

public interface SearchResultCatchWordContract {

    interface View extends BaseView {

        void showSearchList(PageInfo<CatchWordBean> catchWordBeanPageInfo);
    }

    abstract class Presenter extends BasePresenter {

        public abstract void getSearchList(String keyWord, int start);
    }

}
