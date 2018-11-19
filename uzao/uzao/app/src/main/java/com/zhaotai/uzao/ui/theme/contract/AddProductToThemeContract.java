package com.zhaotai.uzao.ui.theme.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.CategoryBean;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.PageInfo;

import java.util.List;

/**
 * Time: 2018/1/19
 * Created by LiYou
 * Description : 素材加入主题列表
 */

public interface AddProductToThemeContract {

    interface View extends BaseSwipeView {


        void showTabList(List<CategoryBean> categoryBeen);


        void showProductCategoryList(PageInfo<GoodsBean> page);
    }

    abstract class Presenter extends BasePresenter {

        public abstract void getTabList();

        public abstract void getProductCategoryList(int i, boolean b, String categoryCode1);
    }
}
