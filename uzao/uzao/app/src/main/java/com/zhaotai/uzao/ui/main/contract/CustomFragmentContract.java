package com.zhaotai.uzao.ui.main.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BasePullDownView;
import com.zhaotai.uzao.bean.DynamicFormBean;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.MainTabBean;
import com.zhaotai.uzao.bean.MultiCustomBean;
import com.zhaotai.uzao.bean.MultiMainBean;

import java.util.List;

/**
 * Time: 2018/3/22
 * Created by LiYou
 * Description : 首页
 */

public interface CustomFragmentContract {

    interface View extends BasePullDownView {
        void bindData(List<MultiCustomBean> data);

        void notifyItemChange(int startPosition, int count);
    }

    abstract class Presenter extends BasePresenter {

        public abstract void getData(String groupCode);

        public abstract void getMultiTypeCategoryData(List<MainTabBean> data);

        public abstract void getMultiTypeSpuData(List<GoodsBean> data);

        public abstract void changeCategoryTextColor(int pressPosition);

        public abstract void categoryRoute(MultiCustomBean item);
    }
}
