package com.zhaotai.uzao.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.listener.BtnOnClickListener;
import com.zhaotai.uzao.view.MyBaseLoadMoreView;

import java.util.List;

/**
 * Time: 2018/3/15
 * Created by LiYou
 * Description : 空白页面的帮助类
 */

public abstract class BaseRecyclerAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> {


    public BaseRecyclerAdapter(@LayoutRes int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
        setLoadMoreView(new MyBaseLoadMoreView());
    }

    public BaseRecyclerAdapter(@Nullable List<T> data) {
        this(0, data);
    }

    public BaseRecyclerAdapter(@LayoutRes int layoutResId) {
        this(layoutResId, null);
    }

    public void setEmptyStateView(Context context) {
        View mEmptyStateView = View.inflate(context, R.layout.vw_empty, null);
        super.setEmptyView(mEmptyStateView);
    }

    /**
     * 空白的页面的view
     * @param context context
     * @param resImageId 空白图片
     * @param emptyString 空白页面文字
     */
    public void setEmptyStateView(Context context, int resImageId, String emptyString) {
        View mEmptyStateView = View.inflate(context, R.layout.vw_empty, null);
        ImageView emptyImage = (ImageView) mEmptyStateView.findViewById(R.id.empty_retry_view);
        TextView emptyText = (TextView) mEmptyStateView.findViewById(R.id.empty_view_tv);

        emptyImage.setImageResource(resImageId);
        emptyText.setText(emptyString);
        super.setEmptyView(mEmptyStateView);
    }

    /**
     * 空白页面view
     * @param context context
     * @param resImageId 空白页面图片
     * @param emptyString 空白文字
     * @param emptyBtnString 空白文字点击事件
     * @param listener 空白页面重试点击监听
     */
    public void setEmptyStateView(Context context, int resImageId, String emptyString, String emptyBtnString, final BtnOnClickListener listener) {
        View mEmptyStateView = View.inflate(context, R.layout.vw_empty, null);
        ImageView emptyImage = (ImageView) mEmptyStateView.findViewById(R.id.empty_retry_view);
        TextView emptyText = (TextView) mEmptyStateView.findViewById(R.id.empty_view_tv);
        TextView emptyBtn = (TextView) mEmptyStateView.findViewById(R.id.empty_view_btn);

        emptyImage.setImageResource(resImageId);
        emptyText.setText(emptyString);
        emptyBtn.setVisibility(View.VISIBLE);
        emptyBtn.setText(emptyBtnString);
        emptyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.btnOnClickListener();
            }
        });

        super.setEmptyView(mEmptyStateView);
    }
    
}
