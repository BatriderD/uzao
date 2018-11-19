package com.zhaotai.uzao.ui.design.material.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.PageInfo;

/**
 * 素材列表
 * author : ZP
 * date: 2018/3/9 0009.
 */

public interface MaterialListMyContract {

    interface View extends BaseSwipeView {

        void showContentList(PageInfo page);
    }

    abstract class Presenter extends BasePresenter {


        public abstract void getMyBoughtMaterial(int start, boolean loadingStatus);

        public abstract void getUpLoadMaterialList(final int start, final boolean loadingStatus);

        public abstract void getMyCollectMaterial(int start, boolean b);
    }

}
