package com.zhaotai.uzao.ui.discuss.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.DiscussCenterBean;

/**
 * description: 讨论中心控制类
 * author : zp
 * date: 2017/7/14
 */

public interface DiscussCenterDialogContract {
    interface View extends BaseSwipeView {
        void showCenterList(DiscussCenterBean centerBean);

        void showLikeSuccess(int pos);

        void showDisLikeSuccess(int pos);

        void showCommitDiscussSuccess();

        void showDelCommentSuccess(int pos);


    }

    abstract class Presenter extends BasePresenter {

        public abstract void like(String materialId, int pos);

        public abstract void disLike(String sequenceNBR, int position);

        public abstract void commitDiscussDiscuss(String type,String materialId, String parentId, String firstCommentId, String word);

        public abstract void getDiscussList(String firstCommentId, int i, boolean b);

        public abstract void del(String dicussType, String sequenceNBR,int pos);
    }
}
