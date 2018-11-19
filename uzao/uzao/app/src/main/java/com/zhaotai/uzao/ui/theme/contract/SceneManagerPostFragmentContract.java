package com.zhaotai.uzao.ui.theme.contract;

import com.zhaotai.uzao.base.BaseNoSwipeView;
import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.SceneManagerPostBean;

public interface SceneManagerPostFragmentContract {
    interface View extends BaseNoSwipeView {

        void showPostData(PageInfo<SceneManagerPostBean> beans);

        void showDelPostSuccess(int mSelectedPos);

        void showDelPostFailed();

        void showTopPostSuccess();

        void showTopPostFailed();

        void showEssencePostSuccess(int mSelectedPos);

        void showEssencePostFailed();
    }

    abstract class Presenter extends BasePresenter {

        public abstract void getPostList(int start, boolean loadingStatus, String themeId);

        public abstract void delPost(String id, int mSelectedPos);

        public abstract void topPost(String id);


        public abstract void essencePost(String id, int mSelectedPos);
    }
}
