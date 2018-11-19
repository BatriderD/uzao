package com.zhaotai.uzao.ui.brand.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeBean;

/**
 * Time: 2018/1/29
 * Created by LiYou
 * Description : 品牌列表
 */

public interface BrandListContract {

    interface View extends BaseSwipeView {

        void showProduct(PageInfo<GoodsBean> goodsBean);

        void showMaterial(PageInfo<MaterialListBean> materialList);

        void showTheme(PageInfo<ThemeBean> themeBean);
    }

    abstract class Presenter extends BasePresenter {

    }
}
