package com.zhaotai.uzao.base;

/**
 * time:2017/5/4
 * description: 只实现了下拉刷新，没有上啦加载功能多的BaseView
 * author: LiYou
 */

public interface BasePullDownView extends BaseView {
    /**
     * 自动下拉刷新
     */
    void autoRefresh();

    /**
     * 停止下拉刷新状态
     */
    void stopRefresh();

}
