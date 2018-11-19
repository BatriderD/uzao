package com.zhaotai.uzao.ui.category.material.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ProductOptionBean;

import java.util.Map;

/**
 * Time: 2018/1/8
 * Created by LiYou
 * Description : 素材分类列表
 */

public interface MaterialCategoryListContract {
    interface View extends BaseSwipeView {
        //显示商品分类列表
        void showMaterialCategoryList(PageInfo<MaterialListBean> list);

        //显示筛选
        void showFilter(ProductOptionBean opt);
    }

    abstract class Presenter extends BasePresenter {

        public abstract void getMaterialCategoryList(int start, boolean isLoading, Map<String, String> params);

    }
}
