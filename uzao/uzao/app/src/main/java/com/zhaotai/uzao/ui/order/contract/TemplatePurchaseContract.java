package com.zhaotai.uzao.ui.order.contract;

import com.zhaotai.uzao.adapter.PropertyAdapter;
import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.GoodsDetailMallBean;
import com.zhaotai.uzao.bean.TemplateBean;
import com.zhaotai.uzao.bean.TemplateInfoBean;

/**
 * Time: 2017/9/8
 * Created by LiYou
 * Description :
 */

public interface TemplatePurchaseContract {

    interface View extends BaseView {
        void templateDetail(TemplateInfoBean data);

        void template3DDetail(TemplateInfoBean data);

        void templateDesignProductDetail(GoodsDetailMallBean goodsDetailMallBean);
    }

    abstract class Presenter extends BasePresenter {
        public abstract void getTemplatePurchaseDetail(String mkuId);

        public abstract void getTemplatePurchase3DDetail(String templateId);

    }
}
