package com.zhaotai.uzao.ui.person.address.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.RegionBean;

import java.util.List;

/**
 * description:地区选择管理
 * author : zp
 * date: 2017/7/14
 */

public interface ReginSelectContract {
    interface View extends BaseView {

        /**
         * 显示数据
         *
         * @param regionBeen 数据列表
         */
         void showRegin(List<RegionBean> regionBeen);

    }

    abstract class Presenter extends BasePresenter {
        /**
         * 获得省信息列表
         */
        public abstract void getProvinces();

        /**
         * 获得地区信息
         */
        public abstract void getRegion(String code);
    }
}
