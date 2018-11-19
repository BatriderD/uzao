package com.zhaotai.uzao.ui.person.myproduct.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.TemplateBean;

import java.util.List;

/**
 * Time: 2017/9/11
 * Created by LiYou
 * Description :
 */

public interface ModifyTemplateDetailContract {

    interface View extends BaseView {

        void bindData(TemplateBean data);

        void showExpireTip(String spuId, String tip);

        void showTip(String tip);

        void finishView();

        void dismissTipDialog();

        void renewSuccess();
    }

    abstract class Presenter extends BasePresenter {
        /**
         * 获取商品详情
         */
        public abstract void getData(String spuId);

        /**
         * 申请上架
         */
        public abstract void putawayToStore(String spuId, TemplateBean data, String name, String des, String idea,
                                            List<TemplateBean.TagsBean> tags, final int position);

        /**
         * 保存作品
         */
        public abstract void saveTemplate(String spuId, TemplateBean data, int position);

        /**
         * 检查数据
         */
        public abstract boolean verifyData(TemplateBean data);
    }
}
