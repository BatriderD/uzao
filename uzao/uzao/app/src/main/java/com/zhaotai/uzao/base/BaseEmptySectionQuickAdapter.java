package com.zhaotai.uzao.base;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.SectionEntity;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.listener.BtnOnClickListener;

import java.util.List;

/**
 * description: 带头的adapter 的空白页面点击
 * author : ZP
 * date: 2018/3/15 0015.
 */

public abstract  class BaseEmptySectionQuickAdapter<T extends SectionEntity, K extends BaseViewHolder> extends BaseSectionQuickAdapter<T, K> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId      The layout resource id of each item.
     * @param sectionHeadResId The section head layout id for each item
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public BaseEmptySectionQuickAdapter(int layoutResId, int sectionHeadResId, List<T> data) {
        super(layoutResId, sectionHeadResId, data);
    }


    public void setEmptyStateView(Context context) {
        View mEmptyStateView = View.inflate(context, R.layout.vw_empty, null);
        super.setEmptyView(mEmptyStateView);
    }

    /**
     * 空白页面
     * @param context context
     * @param resImageId 空白页面图片
     * @param emptyString 空白图片的文字
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
     * 空白页面view的文字和图片点击
     * @param context context
     * @param resImageId 空白页面的图片
     * @param emptyString 空白文字
     * @param emptyBtnString 空白按钮点击的事件
     * @param listener 点击回调
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
