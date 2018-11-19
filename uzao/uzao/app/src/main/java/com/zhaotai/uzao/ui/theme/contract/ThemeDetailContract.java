package com.zhaotai.uzao.ui.theme.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.ThemeBean;

/**
 * Time: 2018/2/6
 * Created by LiYou
 * Description :
 */

public interface ThemeDetailContract {

    interface View extends BaseView {
        void showDetail(ThemeBean themeBean);

        void showCollectTheme(boolean b);

        void changeFavoriteCount(boolean isAdd);

         void openShareBoard(boolean hasPoster);
    }

    abstract class Presenter extends BasePresenter {

        public abstract void getDetail(String themeId);

        public abstract void collectTheme(String themeId);

        public abstract void getCollectStatus(String themeId);

        public abstract void delTheme(String themeId);

        public abstract void hasPoster(String themeId);
    }
}
