package com.zhaotai.uzao.ui.design.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.ArtFontTabBean;
import com.zhaotai.uzao.bean.ArtFontTopicBean;
import com.zhaotai.uzao.bean.PageInfo;

/**
 * 流行词控制类
 */

public interface ArtFontMainContract {

    interface View extends BaseSwipeView {

        void showTabList(PageInfo<ArtFontTabBean> info);

        void showTopicList(PageInfo<ArtFontTopicBean> info);

        void showTopicListError(String message);
    }

    abstract class Presenter extends BasePresenter {
        public abstract void getArtTabList(int Start);

        public abstract void getTopicFontList(int Start,String topicWord);
    }

}
