package com.zhaotai.uzao.view;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.zhaotai.uzao.R;

/**
 * description:
 * author : ZP
 * date: 2018/3/19 0019.
 */

public class MyBaseLoadMoreView extends LoadMoreView {
    @Override
    public int getLayoutId() {
        return R.layout.load_more_view_base;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
