package com.zhaotai.uzao.ui.theme.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeModuleBean;
import com.zhaotai.uzao.bean.ThemeTemplateBean;

import java.util.ArrayList;

/**
 * Time: 2018/1/19
 * Created by LiYou
 * Description :主题模板页面
 */

public interface ThemeTemplateContract {

    interface View extends BaseSwipeView {

        void showThemeTemplateList(PageInfo<ThemeTemplateBean> data);

        void showSaveSuccess();

        void getPreviewUrlSuccess( int position, String url);
    }

    abstract class Presenter extends BasePresenter {
        public abstract void getThemeTemplateList(int i, boolean b);

        public abstract void getPreviewTheme(String themeId, int position, String templateId, String json);

        public abstract void saveThemeAll(String themeId, String templateId, String json, ArrayList<ThemeModuleBean> themeModuleBeanArrayList);
    }
}
