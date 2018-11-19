package com.zhaotai.uzao.ui.brand.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.BrandBean;

/**
 * Time: 2018/1/29
 * Created by LiYou
 * Description : 品牌契约类
 */

public interface BrandContract {

    interface View extends BaseView {

        /**
         * 显示品牌信息
         */
        void showBrandInfo(BrandBean brandBean);

        /**
         * 关注
         */
        void attention();

        /**
         * 未关注
         */
        void cancelAttention();
    }

    abstract class Presenter extends BasePresenter {

        /**
         * 获取品牌信息
         *
         * @param brandId 品牌id
         */
        public abstract void getBrandInfo(String brandId);
    }
}
