package com.zhaotai.uzao.ui.person.collection.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeBean;

/**
 * Time: 2017/5/22
 * Created by LiYou
 * Description :
 */

public interface CollectionThemeSearchContract {

    interface View extends BaseSwipeView {
        void showCollectionThemeList(PageInfo<ThemeBean> list);
    }

    abstract class Presenter extends BasePresenter {

        /**
         * 获取收藏的主题
         */
        public abstract void getCollectThemeList(int start, String name);

    }
}
