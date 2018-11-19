package com.zhaotai.uzao.ui.search.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.MyMaterialBean;
import com.zhaotai.uzao.bean.PageInfo;

/**
 * description: 搜索页面 已购素材控制类
 * author : zp
 * date: 2017/7/14
 */

public interface MyMaterialBoughtFragmentContract {
    interface View extends BaseSwipeView {

        void showMaterialList(PageInfo<MyMaterialBean> data);

    }

    abstract class Presenter extends BasePresenter {
        public abstract void getMyBoughtMaterial(int start, String keyWord);
    }
}
