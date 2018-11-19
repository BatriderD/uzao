package com.zhaotai.uzao.ui.person.feedback;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;

/**
 * Time: 2017/7/19
 * Created by LiYou
 * Description :
 */

public interface FeedBackContract {

    interface View extends BaseView {

        void showSuccess();
    }

    abstract class Presenter extends BasePresenter {

    }

}
