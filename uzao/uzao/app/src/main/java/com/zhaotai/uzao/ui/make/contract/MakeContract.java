package com.zhaotai.uzao.ui.make.contract;

import android.content.Context;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.MakeBean;
import com.zhaotai.uzao.bean.PageInfo;

import java.util.Map;

/**
 * Time: 2017/8/26
 * Created by LiYou
 * Description :
 */

public interface MakeContract {
    interface View extends BaseSwipeView {
        void showMakeList(PageInfo<MakeBean> pageInfo);

        void showProgress();

        void stopProgress();
    }

    abstract class MakePresenter extends BasePresenter {
        public abstract void getMakeList(int start, boolean isLoading);

        public abstract void loginIm();

        public abstract void createImUser(Context context, Map<String, String> map);

    }
}
