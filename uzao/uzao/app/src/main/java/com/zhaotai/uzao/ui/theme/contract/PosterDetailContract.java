package com.zhaotai.uzao.ui.theme.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.DiscussBean;
import com.zhaotai.uzao.bean.PageInfo;

/**
 * Time: 2018/7/27
 * Created by zp
 * Description :帖子详情管理
 */

public interface PosterDetailContract {

    interface View extends BaseSwipeView {

        void showMaterialDiscussList(PageInfo<DiscussBean> pageInfo);

        void showCommitDiscussSuccess();

        void showCommitDiscussDicussSuccess();
    }

    abstract class Presenter extends BasePresenter {

        public abstract void getPostDetail(String postId);

        public abstract void getDiscussList(String posts, String postId, int start, boolean loadingStatus);

        public abstract void commitDiscussDiscuss(String type, String materialId, String parentId, String firstCommentId, String word, String themeId);

        public abstract void commitDiscuss(String discussType, String postId, String word,String themeId);
    }
}
