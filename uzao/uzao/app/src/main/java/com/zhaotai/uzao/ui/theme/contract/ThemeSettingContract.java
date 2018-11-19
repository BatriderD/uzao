package com.zhaotai.uzao.ui.theme.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.TemplateJsonBean;
import com.zhaotai.uzao.bean.ThemeBean;
import com.zhaotai.uzao.bean.ThemeModuleBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Time: 2018/1/19
 * Created by LiYou
 * Description : 主题设置页面控制类
 */

public interface ThemeSettingContract {

    interface View extends BaseView {


        void showThemeContent(List<ThemeModuleBean> themeModuleBean);

        void showSaveModuleSuccess(int position);

        void showDeleteSuccess(int position);

        void collectDataSuccess(TemplateJsonBean templateJsonBean, ArrayList<ThemeModuleBean> upSaveModuleList);

        void showCollectFailed();
    }

    abstract class Presenter extends BasePresenter {

        public abstract void getThemeContent(String themeId);

        public abstract void saveModule(String themeId, int position, ThemeModuleBean themeModuleBean);

        public abstract void delModule(String themeId, int position);

        public abstract void resultData(ThemeBean themeBean, String themeId, List<ThemeModuleBean> data);
    }
}
