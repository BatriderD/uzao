package com.zhaotai.uzao.ui.person.message.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.MessageCommentBean;

/**
 * Time: 2017/5/24
 * Created by LiYou
 * Description : 消息中心
 */

public interface MessageCommentContract {

    interface View extends BaseView {
        void showData(MessageCommentBean s);

        void showDisLikeSuccess();

        void showLikeSuccess();

        void showCommitDiscussDiscussSuccess();
    }

    abstract class Presenter extends BasePresenter {
        public abstract void like(String materialId);

        public abstract void disLike(String sequenceNBR);

        public abstract void commitDiscussDiscuss(String type,String materialId, String parentId, String firstCommentId, String word) ;
        public abstract void getCommentData(String id);
    }
}
