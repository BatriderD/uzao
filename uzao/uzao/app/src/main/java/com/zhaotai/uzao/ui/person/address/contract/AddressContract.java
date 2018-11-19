package com.zhaotai.uzao.ui.person.address.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.AddressBean;

import java.util.List;

/**
 * time:2017/5/4
 * description:
 * author: LiYou
 */

public interface AddressContract {

    interface View extends BaseView {

        /**
         * 显示地址列表
         *
         * @param list 数据列表
         */
        void showAddressList(List<AddressBean> list);

        /**
         * 删除指定条目
         *
         * @param pos 条目位置
         */
        void delItemSuccess(int pos);

        /**
         * 指定条目显示为默认
         *
         * @param pos 条目位置
         */
        void setItemDefault(int pos);

    }

    abstract class Presenter extends BasePresenter {
        /**
         * 获取地址列表
         */
        public abstract void getAddressList();

        /**
         * 删除指定条目
         *
         */
        public abstract void delAddressItem(String id, int pos);

        /**
         * 设置指定条目为默认地址 逻辑处理
         *
         * @param addressBeen 数据
         * @param pos  条目位置
         */
        public abstract void setItemDefault(AddressBean addressBeen, int pos);
    }
}
