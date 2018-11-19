package com.zhaotai.uzao.ui.main.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.MainTabBean;

import java.util.List;

/**
 * Time: 2017/5/8
 * Created by LiYou
 * Description : 新版首页
 */

public interface MainFragmentNewContract {

    interface View extends BaseView {

        void addTabList(List<MainTabBean> children);

        void showUnHandleMessage(Integer integer);
    }

    abstract class Presenter extends BasePresenter {

        public abstract void getTabData();

        public abstract void getUnHandleMessage();
    }
}
