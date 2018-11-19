package com.zhaotai.uzao.ui.design.catchWord.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.CatchWordBean;
import com.zhaotai.uzao.bean.CatchWordTabBean;
import com.zhaotai.uzao.bean.PageInfo;

import java.util.List;

/**
 * 流行词控制类
 */

public interface CatchWordContract {

    interface View extends BaseSwipeView {

        void showTabList(List<CatchWordTabBean> tabList);

        void showCatchWordContentList(PageInfo<CatchWordBean> catchWordBeanPageInfo);

        void showTopicListError(String message);

        void showTopicListLoading();
        void showTopicListContent();
    }

    abstract class Presenter extends BasePresenter {

        public abstract void getCatchWordTabList();

        public abstract void getCatchWordContentList(int start, String parentCode);
    }

}
