package com.zhaotai.uzao.widget.decoration;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhaotai.uzao.bean.MultiMainBean;

import java.util.List;

/**
 * time:2018/3/29
 * description:
 *
 * @author ly
 */
public class MainChildItemDecoration extends RecyclerView.ItemDecoration {

    private int spacing;
    private List<MultiMainBean> mData;
    private int margin;

    public MainChildItemDecoration(int margin, int spacing, @NonNull List<MultiMainBean> data) {
        this.margin = margin;
        this.spacing = spacing / 2;
        this.mData = data;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        MultiMainBean item = mData.get(position);
        int inSidePos = item.inSidePos;
        if (inSidePos == -1) {
//            不需要轮播的
            return;
        }

        int i = inSidePos % 2;
        if (i == 0) {
            outRect.left = margin;
            outRect.right = spacing;
        } else {
            outRect.left = spacing;
            outRect.right = margin;
        }
    }
}
