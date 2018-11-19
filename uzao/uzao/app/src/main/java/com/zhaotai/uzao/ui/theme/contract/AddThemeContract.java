package com.zhaotai.uzao.ui.theme.contract;

import com.zhaotai.uzao.base.BaseNoSwipeView;
import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeBean;

/**
 * Time: 2018/1/19
 * Created by LiYou
 * Description :
 */

public interface AddThemeContract {

    interface View extends BaseNoSwipeView {


        void showMyThemeList(PageInfo<ThemeBean> data);

        void addThemeSuccess(String sequenceNBR);
    }

    abstract class Presenter extends BasePresenter {

        public abstract void getThemeList(int start);

        public abstract void addTheme(String themeName, String entityType, String entityId);
    }
}
