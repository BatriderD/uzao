package com.zhaotai.uzao.base;

/**
 * time:2017/5/4
 * description:
 * author: LiYou
 */

public interface BaseSwipeView extends BaseView {
    /**
     * 停止加载更多状态
     */
    void stopLoadingMore();

    /**
     * 停止下拉刷新状态
     */
    void stopRefresh();

    /**
     * 上拉加载失败
     */
    void loadingFail();
}
