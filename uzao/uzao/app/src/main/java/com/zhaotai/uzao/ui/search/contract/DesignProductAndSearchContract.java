package com.zhaotai.uzao.ui.search.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.AssociateBean;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * Time: 2017/5/15
 * Created by LiYou
 * Description :
 */

public interface DesignProductAndSearchContract {

    interface View extends BaseSwipeView {
        void showDesignProductList(PageInfo<GoodsBean> list);

        void showAssociateList(List<AssociateBean> associateBeen);

        void showProgress();

        void stopProgress();
    }

    abstract class Presenter extends BasePresenter {

        public abstract void getDesignProductList(int start, Map<String, String> map);
    }

}
