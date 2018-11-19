package com.zhaotai.uzao.base;

/**
 * time:2017/5/4
 * description:
 * author: LiYou
 */

public interface BaseView {

    void showNetworkFail(String msg);

    void showEmpty();

    void showEmpty(String emptyText);

    void showLoading();

    void showContent();
}
