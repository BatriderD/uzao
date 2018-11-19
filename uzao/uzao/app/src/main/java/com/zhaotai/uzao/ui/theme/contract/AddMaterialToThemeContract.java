package com.zhaotai.uzao.ui.theme.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.CategoryBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;

/**
 * Time: 2018/1/19
 * Created by LiYou
 * Description : 素材加入主题列表
 */

public interface AddMaterialToThemeContract {

    interface View extends BaseSwipeView {


        void showTabList(CategoryBean categoryBean);


        void showMaterialCategoryList(PageInfo<MaterialListBean> page);
    }

    abstract class Presenter extends BasePresenter {

        public abstract void getTabList();

        public abstract void getMaterialCategoryList(int i, boolean b, String categoryCode1);
    }
}
