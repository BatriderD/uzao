package com.zhaotai.uzao.widget.decoration;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhaotai.uzao.bean.RecommendBean;

import java.util.List;

/**
 * time:2016/6/27
 * description:
 *
 * @author sunjianfei
 */
public class RecommendItemDecoration extends RecyclerView.ItemDecoration {

    private int spacing;
    private List<RecommendBean> mData;
    private int margin;

    public RecommendItemDecoration(int margin, int spacing, @NonNull List<RecommendBean> data) {
        this.margin = margin;
        this.spacing = spacing / 2;
        this.mData = data;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        RecommendBean recommendBean = mData.get(position);
        int inSidePos = recommendBean.inSidePos;
        if (recommendBean.getItemType() == RecommendBean.END || inSidePos == -2) {
//            不需要轮播的
            return;
        }
        if (inSidePos == -1) {
            outRect.left = margin;
            outRect.right = margin;
        } else {
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
}
