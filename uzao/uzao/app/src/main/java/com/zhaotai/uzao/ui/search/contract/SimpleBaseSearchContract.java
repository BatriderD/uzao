package com.zhaotai.uzao.ui.search.contract;

import com.zhaotai.uzao.base.BaseNoSwipeView;
import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.bean.PageInfo;

import java.util.HashMap;

/**
 * Time: 2017/5/15
 * Created by LiYou
 * Description :
 */

public interface SimpleBaseSearchContract<T> {

    interface View<T> extends BaseNoSwipeView {
        //显示商品分类列表
        void showCommodityList(PageInfo<T> list);
    }

    abstract class Presenter extends BasePresenter {

        public abstract void getCommodityList(int start,HashMap<String, String> map);
    }

}
