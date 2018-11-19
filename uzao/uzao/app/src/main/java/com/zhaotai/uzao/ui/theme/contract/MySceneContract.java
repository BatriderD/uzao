package com.zhaotai.uzao.ui.theme.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeBean;

import java.util.ArrayList;

/**
 * Time: 2018/1/19
 * Created by LiYou
 * Description :我的主题页面
 */

public interface MySceneContract {

    interface View extends BaseSwipeView {


        void showMyThemeList(PageInfo<ThemeBean> data);

        void showDelSuccess();
    }

    abstract class Presenter extends BasePresenter {
        public abstract void getMyManagerSceneList(int i, boolean b);

        public abstract void delTheme(ArrayList<String> themeIds);
    }
}
