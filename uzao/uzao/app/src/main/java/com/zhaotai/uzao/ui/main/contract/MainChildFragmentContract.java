package com.zhaotai.uzao.ui.main.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BasePullDownView;
import com.zhaotai.uzao.bean.DynamicFormBean;
import com.zhaotai.uzao.bean.MultiMainBean;

import java.util.List;

/**
 * Time: 2018/3/22
 * Created by LiYou
 * Description : 首页
 */

public interface MainChildFragmentContract {

    interface View extends BasePullDownView {
        void bindData(List<MultiMainBean> multiList);

        void showDLoading();

        void dismissDLoading();
    }

    abstract class Presenter extends BasePresenter {
        public abstract void getData();

        public abstract void getMultiTypeData(DynamicFormBean data);
    }
}
