package com.zhaotai.uzao.ui.productOrder.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;

/**
 * Time: 2017/8/23
 * Created by LiYou
 * Description :
 */

public interface ProducePayContract {

    interface View extends BaseView {
        void showProgress();
        void dismissProgress();
        void pay(String payInfo);
        void finishView();
    }

    abstract class Presenter extends BasePresenter {
        public abstract void getProducePayInfo(String payWay, String orderNo, String source);

        public abstract void callBack(boolean type);
    }
}
