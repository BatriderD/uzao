package com.zhaotai.uzao.ui.category.material.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BasePullDownView;
import com.zhaotai.uzao.bean.CategoryBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.MultiMaterialCategoryBean;

import java.util.List;

/**
 * Time: 2018/3/22
 * Created by LiYou
 * Description : 首页
 */

public interface MaterialCategoryContract {

    interface View extends BasePullDownView {
        void bindData(List<MultiMaterialCategoryBean> data);
    }

    abstract class Presenter extends BasePresenter {

        public abstract void getData();

        public abstract void getMultiTypeCategoryData(List<CategoryBean> data);

        public abstract void getMultiTypeMaterialData(List<MaterialListBean> data);

    }
}
