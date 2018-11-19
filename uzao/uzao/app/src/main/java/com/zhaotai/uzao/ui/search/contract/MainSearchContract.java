package com.zhaotai.uzao.ui.search.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.AssociateBean;
import com.zhaotai.uzao.bean.DesignerBean;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.MainSearchBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ProductOptionBean;
import com.zhaotai.uzao.bean.ThemeListBean;

import java.util.List;
import java.util.Map;

/**
 * Time: 2017/5/15
 * Created by LiYou
 * Description :
 */

public interface MainSearchContract {

    interface View extends BaseSwipeView {

        //显示全部搜索结果
        void showAllList(MainSearchBean data);


        //显示商品分类列表
        void showCommodityList(PageInfo<GoodsBean> list);

        void showMaterialList(PageInfo<MaterialListBean> materialList);

        void showThemeList(PageInfo<ThemeListBean> themeList);

        void showDesignerList(PageInfo<DesignerBean> designerList);

        //显示筛选
        void showFilter(ProductOptionBean opt);


        void showAssociateList(List<AssociateBean> associateList);

        void selectCategoryTab(int position);

        void changeDesigner(int pos, boolean status);
    }

    abstract class Presenter extends BasePresenter {

        public abstract void getCommodityList(int start, Map<String, String> map);
    }

}
