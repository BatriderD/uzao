package com.zhaotai.uzao.ui.category.goods.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.CategoryBean;

import java.util.List;

/**
 * Time: 2017/5/15
 * Created by LiYou
 * Description : 列别控制类
 */

public interface CateGoryContract {

    interface View extends BaseView {
        //显示商品详情
        void showLeftList(List<CategoryBean> data);

        //展示右侧list数目
        void showRightList(List<CategoryBean> data,int position);

    }

    abstract class Presenter extends BasePresenter {
        public abstract void getLeftList(boolean needReflash);

        public abstract void getLeftListFromNet();

        public abstract void getRightList(String categoryCode,  int position);
    }

}
