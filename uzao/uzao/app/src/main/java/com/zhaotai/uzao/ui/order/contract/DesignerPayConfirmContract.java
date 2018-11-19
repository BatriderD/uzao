package com.zhaotai.uzao.ui.order.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;

/**
 * Time: 2018/1/9
 * Created by LiYou
 * Description :
 */

public interface DesignerPayConfirmContract {

    interface View extends BaseView {
        void finishView();

        void showProgress();

        void stopProgress();
    }

    abstract class Presenter extends BasePresenter {

    }
}
