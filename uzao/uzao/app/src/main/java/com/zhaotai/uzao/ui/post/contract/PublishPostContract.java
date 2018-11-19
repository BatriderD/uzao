package com.zhaotai.uzao.ui.post.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.SceneManagerPostBean;

/**
 * Time: 2018/7/27 0027
 * Created by LiYou
 * Description :
 */
public interface PublishPostContract {
    interface View extends BaseView {

        void insertImage(String imagePath);

        void showProgress();

        void stopProgress();

        void finishView(SceneManagerPostBean postBean);
    }

    abstract class Presenter extends BasePresenter {

    }
}
