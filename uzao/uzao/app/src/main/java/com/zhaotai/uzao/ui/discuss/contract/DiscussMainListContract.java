package com.zhaotai.uzao.ui.discuss.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.DiscussBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeBean;

/**
 * description: 讨论祝列表控制类
 * author : zp
 * date: 2017/7/14
 */

public interface DiscussMainListContract {
    interface View extends BaseSwipeView {

        void showMaterialDiscussList(PageInfo<DiscussBean> discussBeanPageBean);

        void showLikeSuccess(int pos);

        void showDisLikeSuccess(int pos);

        void showCommitDiscussSuccess();

        void showCommitDiscussDicussSuccess();

        void showDelCommentSuccess(int pos);

        void showThemeDetail(ThemeBean themeBean);
    }

    abstract class Presenter extends BasePresenter {

        public abstract void like(String materialId, int pos);

        public abstract void disLike(String sequenceNBR, int position);

        public abstract void commitDiscuss(String type, String materialId, String word);


        public abstract void commitDiscussDiscuss(String type,String materialId, String parentId, String firstCommentId, String word);


        public abstract void getDiscussList(String type, String discussObjId, int start, boolean inLoginstatus);

        public abstract void del(String discussType, String sequenceNBR, int position);

        public abstract void getThemeData(String discussObjId);
    }
}
