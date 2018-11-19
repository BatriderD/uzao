package com.zhaotai.uzao.ui.person.message.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.MessageDetailBean;
import com.zhaotai.uzao.bean.PageInfo;

/**
 * Time: 2017/5/24
 * Created by LiYou
 * Description : 消息详情管理类
 */

public interface MessageDetailContract {

    interface View extends BaseSwipeView {
        //显示消息列表
        void showMessageList(PageInfo<MessageDetailBean> data);
    }

    abstract class Presenter extends BasePresenter {
        //获取消息列表
        public abstract void getMessageList(String type, int start, boolean inLoginStatus);

        //获取评论消息列表
        public abstract void getCommentMessageList(String type, int start, boolean inLoginStatus);
    }
}
