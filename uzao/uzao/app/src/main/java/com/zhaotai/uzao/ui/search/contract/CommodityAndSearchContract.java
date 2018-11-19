package com.zhaotai.uzao.ui.search.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.AssociateBean;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ProductOptionBean;

import java.util.List;
import java.util.Map;

/**
 * Time: 2017/5/15
 * Created by LiYou
 * Description :
 */

public interface CommodityAndSearchContract {

    interface View extends BaseSwipeView {
        //显示商品分类列表
        void showCommodityList(PageInfo<GoodsBean> list);

        //显示筛选
        void showFilter(ProductOptionBean opt);

        void showAssociateList(List<AssociateBean> associateList);
    }

    abstract class Presenter extends BasePresenter {

        public abstract void getCommodityList(int start, Map<String, String> map);
    }

}
