package com.zhaotai.uzao.ui.person.setting.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;

/**
 * description: 设置管理类
 * author : zp
 * date: 2017/7/20
 */

public interface SettingConstract {
    interface View extends BaseView {
        void showCacheSize(String size);

        /**
         * 设置密码区域文字
         */
        void setPasswordText(int passwordStatus);
    }

    abstract class Presenter extends BasePresenter {
        /**
         * 开启puhs
         */
        public abstract void openPush();

        /**
         * 关闭push
         */
        public abstract void closePush();

        /**
         * 获取缓存大小
         */
        public abstract void getCacheSize();
        /**
         * 清除缓存大小
         */
        public abstract void cleanCache();


        /**
         * 根据用户有无密码设置密码文字
         */
        public abstract  void setPasswordItem();
    }
}
