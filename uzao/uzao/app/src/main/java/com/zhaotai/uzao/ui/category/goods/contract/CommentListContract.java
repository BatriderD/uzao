package com.zhaotai.uzao.ui.category.goods.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.CommentBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.StartBean;
import com.zhaotai.uzao.bean.StartCountBean;

import java.util.List;

/**
 * Time: 2017/8/18
 * Created by LiYou
 * Description : 评论列表
 */

public interface CommentListContract {
    interface View extends BaseSwipeView {
        //显示评论列表
        void showCommentList(PageInfo<CommentBean> data);

        //显示评分
        void showCommentStart(List<StartCountBean> start);
    }

    abstract class Presenter extends BasePresenter {
        public abstract void getCommentList(int start, boolean isLoading, String entityId, boolean haveImage, String startScore);
    }
}
