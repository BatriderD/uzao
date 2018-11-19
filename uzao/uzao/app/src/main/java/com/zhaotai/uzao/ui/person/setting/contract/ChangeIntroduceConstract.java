package com.zhaotai.uzao.ui.person.setting.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;

/**
 * description: 修改个人信息
 * author : zp
 * date: 2017/7/20
 */

public interface ChangeIntroduceConstract {
    interface View extends BaseView {

        void showChangeIntroduceSuccess(String aboutMe);
    }

    abstract class Presenter extends BasePresenter {

        public abstract void setIntroduce(String introduce);
    }
}
