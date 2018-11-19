package com.zhaotai.uzao.ui.theme.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeListBean;

/**
 * Time: 2018/1/19
 * Created by LiYou
 * Description :我的主题页面
 */

public interface NewThemeListContract {

    interface View extends BaseSwipeView {

        void showThemeList(PageInfo<ThemeListBean> data);
    }

    abstract class Presenter extends BasePresenter {

        public abstract void getThemeList(int start, boolean loadingStatus, boolean sort);
    }
}
