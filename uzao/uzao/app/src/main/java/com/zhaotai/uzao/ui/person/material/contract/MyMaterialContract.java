package com.zhaotai.uzao.ui.person.material.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.MyBoughtMaterialCategoryBean;
import com.zhaotai.uzao.bean.MyMaterialBean;
import com.zhaotai.uzao.bean.MyUploadMaterialBean;
import com.zhaotai.uzao.bean.PageInfo;

import java.util.List;

/**
 * description: 个人fragment管理
 * author : zp
 * date: 2017/7/14
 */

public interface MyMaterialContract {
    interface View extends BaseSwipeView {

        void showMaterialList(PageInfo<MyMaterialBean> data);

        void showCategoryList(List<MyBoughtMaterialCategoryBean> beans);

        void showUploadList(PageInfo<MyUploadMaterialBean> info);

        void showDelSuccess(int position);
    }

    abstract class Presenter extends BasePresenter {
        public abstract void getMyBoughtMaterial(int start, String code);

        public abstract void getMyBoughtMaterialCategory();

        public abstract void getUpLoadMaterialList(int i);

        public abstract void delMaterial(String sourceMaterialId, int position);
    }
}
