package com.zhaotai.uzao.widget.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhaotai.uzao.bean.MyTrackBean;
import com.zhaotai.uzao.ui.person.track.adapter.MyTrackAdapter;

import java.util.List;

/**
 * time:2016/6/27
 * description: 我的足迹分割线
 *
 * @author sunjianfei
 */
public class MyTrackItemDecoration extends RecyclerView.ItemDecoration {

    private int spacing;
    private MyTrackAdapter mAdapter;
    private int margin;

    public MyTrackItemDecoration(int margin, int spacing, MyTrackAdapter adapter) {
        this.margin = margin;
        this.spacing = spacing / 2;
        this.mAdapter = adapter;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        List<MyTrackBean> mData = mAdapter.getData();
        if (mData.size() == 0) {
            return;
        }
        int position = parent.getChildAdapterPosition(view); // item position
        if (position >= mData.size()) {
            return;
        }
        MyTrackBean myTrackBean = mData.get(position);
        int inSidePos = myTrackBean.inSidePos;
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
