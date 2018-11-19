package com.zhaotai.uzao.widget;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
    private int mEdgeSpace = 10;
    private int mSpace = 10;

    /**
     * 设置距离  单位是px
     *
     * @param edgeSpace 分割线宽度
     * @param space     边距宽度宽度
     */
    public SimpleDividerItemDecoration(int edgeSpace, int space) {
        this.mEdgeSpace = edgeSpace;
        this.mSpace = space;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        Log.i(TAG, "getItemOffsets");
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter instanceof BaseQuickAdapter) {
            if (((BaseQuickAdapter) adapter).getData().size() == 0 ) {
                return;
            }
        }
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        int childPosition = parent.getChildAdapterPosition(view);
        int itemCount = parent.getAdapter().getItemCount();
        if (manager != null) {
            if (manager instanceof GridLayoutManager) { // 待会再处理
                setGridOffset(((GridLayoutManager) manager).getOrientation(), ((GridLayoutManager) manager).getSpanCount(), outRect, childPosition, itemCount);

            } else if (manager instanceof LinearLayoutManager) {
                setLinearOffset(((LinearLayoutManager) manager).getOrientation(), outRect, childPosition, itemCount);
            }
        }
    }

    private void setGridOffset(int orientation, int spanCount, Rect outRect, int childPosition, int itemCount) {
        float totalSpace = mSpace * (spanCount - 1) + mEdgeSpace * 2;
        float eachSpace = totalSpace / spanCount;
        int column = childPosition % spanCount;
        int row = childPosition / spanCount;

        float left;
        float right;
        float top;
        float bottom;
        if (orientation == GridLayoutManager.VERTICAL) {
            top = 0;
            bottom = mSpace;

            if (childPosition < spanCount) {
                top = 0;
            }
            if (itemCount % spanCount != 0 && itemCount / spanCount == row ||
                    itemCount % spanCount == 0 && itemCount / spanCount == row + 1) {
                bottom = mEdgeSpace;
            }

            if (spanCount == 1) {
                left = mEdgeSpace;
                right = left;
            } else {
                left = column * (eachSpace - mEdgeSpace - mEdgeSpace) / (spanCount - 1) + mEdgeSpace;
                right = eachSpace - left;
            }
        } else {
            left = 0;
            right = mSpace;

            if (childPosition < spanCount) {
                left = mEdgeSpace;
            }
            if (itemCount % spanCount != 0 && itemCount / spanCount == row ||
                    itemCount % spanCount == 0 && itemCount / spanCount == row + 1) {
                right = mEdgeSpace;
            }

            if (spanCount == 1) {
                top = mEdgeSpace;
                bottom = top;
            } else {
                top = column * (eachSpace - mEdgeSpace - mEdgeSpace) / (spanCount - 1) + mEdgeSpace;
                bottom = eachSpace - top;
            }
        }
        outRect.set((int) left, (int) top, (int) right, (int) bottom);
    }


    private void setLinearOffset(int orientation, Rect outRect, int childPosition, int itemCount) {
        if (orientation == LinearLayoutManager.HORIZONTAL) {
            if (childPosition == 0) { // 第一个要设置PaddingLeft
                outRect.set(mEdgeSpace, 0, mSpace, 0);
            } else if (childPosition == itemCount - 1) { // 最后一个设置PaddingRight
                outRect.set(0, 0, mEdgeSpace, 0);
            } else {
                outRect.set(0, 0, mSpace, 0);
            }
        } else {
            if (childPosition == 0) { // 第一个要设置PaddingTop
                outRect.set(0, mEdgeSpace, 0, mSpace);
            } else if (childPosition == itemCount - 1) { // 最后一个要设置PaddingBottom
                outRect.set(0, 0, 0, mEdgeSpace);
            } else {
                outRect.set(0, 0, 0, mSpace);
            }
        }
    }
}