package com.zhaotai.uzao.ui.theme.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.NewAddListBean;
import com.zhaotai.uzao.bean.PageInfo;

/**
 * Time: 2018/1/19
 * Created by LiYou
 * Description : 新增列表
 */

public interface NewAddListContract {

    interface View extends BaseSwipeView {
        void showNewAddList(PageInfo<NewAddListBean> pageInfo);

    }

    abstract class Presenter extends BasePresenter {

        public abstract void getNewAddList(int i, boolean isLoading, String themeId,String type);
    }
}
