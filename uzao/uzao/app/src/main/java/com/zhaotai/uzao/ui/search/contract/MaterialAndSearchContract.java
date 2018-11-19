package com.zhaotai.uzao.ui.search.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.AssociateBean;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ProductListBean;
import com.zhaotai.uzao.bean.ProductOptionBean;

import java.util.List;
import java.util.Map;

/**
 * Time: 2017/5/15
 * Created by LiYou
 * Description :
 */

public interface MaterialAndSearchContract {

    interface View extends BaseSwipeView {
        void showMaterialList(PageInfo<MaterialListBean> list);

        void showAssociateList(List<AssociateBean> associateBeen);

        //显示筛选
        void showFilter(ProductOptionBean opt);
    }

    abstract class Presenter extends BasePresenter {

        public abstract void getMaterialList(int start, Map<String, String> map);
    }

}
