package com.zhaotai.uzao.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

/**
 * Time: 2017/7/28
 * Created by LiYou
 * Description :
 */

public class CustomGridLayoutManager extends GridLayoutManager {

    private boolean isScrollEnabled = true;

    public CustomGridLayoutManager(Context context,int spanCount) {
        super(context, spanCount);
    }

    public CustomGridLayoutManager(Context context, int spanCount, int orientation,
                                   boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }


    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}
