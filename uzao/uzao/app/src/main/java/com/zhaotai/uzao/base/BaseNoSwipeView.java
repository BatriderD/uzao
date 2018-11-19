package com.zhaotai.uzao.base;

/**
 * time:2017/5/4
 * description:
 * author: LiYou
 */

public interface BaseNoSwipeView extends BaseView {


    /**
     * 停止加载更多状态
     */
    void stopLoadingMore();

    /**
     * 加载更多失败
     */
    void loadingMoreFail();


}
