package com.zhaotai.uzao.ui.theme.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.ThemeBean;

import java.util.List;

/**
 * Time: 2018/1/19
 * Created by LiYou
 * Description :
 */

public interface EditThemeContract {

    interface View extends BaseView {


        void showThemeCover(String s);

        void showSaveSuccess(ThemeBean bean, String themeId);

        void showTheme(ThemeBean themeBean);

    }

    abstract class Presenter extends BasePresenter {
        public abstract void getThemeData(String themeId);

        public abstract void upThemePic(String url);

        public abstract void checkData(String themeId, String themeName, String url, List<ThemeBean.TagsBean> tags, String description, String isPublic);

        public abstract void saveTheme(ThemeBean themeBean);

        public abstract void changeTheme(String themeid, ThemeBean themeBean);
    }
}
