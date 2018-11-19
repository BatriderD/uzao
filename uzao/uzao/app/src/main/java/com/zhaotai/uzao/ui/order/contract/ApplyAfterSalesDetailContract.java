package com.zhaotai.uzao.ui.order.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.ApplyAfterSalesDetailMultiBean;

import java.util.List;

/**
 * Time: 2018/8/2
 * Created by LiYou
 * Description :
 */

public interface ApplyAfterSalesDetailContract {

    interface View extends BaseView {
        void showData(List<ApplyAfterSalesDetailMultiBean> data);

        void finishView();
    }

    abstract class Presenter extends BasePresenter {

        //提交申请
        public abstract void getData(String packageOrderId);

    }

}
