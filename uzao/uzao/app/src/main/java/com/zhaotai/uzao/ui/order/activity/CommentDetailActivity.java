package com.zhaotai.uzao.ui.order.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.adapter.CommentDetailAdapter;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.CommentBean;
import com.zhaotai.uzao.ui.order.contract.CommentOrderContract;
import com.zhaotai.uzao.ui.order.presenter.CommentPresenter;

import java.util.List;

import butterknife.BindView;


/**
 * Time: 2017/6/12
 * Created by LiYou
 * Description : 评价详情页面
 */

public class CommentDetailActivity extends BaseActivity implements CommentOrderContract.View,
        BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.recycler_comment_order)
    RecyclerView mRecycler;

    private CommentPresenter mPresenter;
    private CommentDetailAdapter mAdapter;

    private static final String EXTRA_KEY_COMMENT_ORDER_ID = "EXTRA_KEY_COMMENT_ORDER_ID";

    /**
     * 打开评价详情页面
     *
     * @param context 上下文
     * @param orderId 商品id
     */
    public static void launch(Context context, String orderId) {
        Intent intent = new Intent(context, CommentDetailActivity.class);
        intent.putExtra(EXTRA_KEY_COMMENT_ORDER_ID, orderId);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_comment_order);
        mTitle.setText("我的评价");
        mPresenter = new CommentPresenter(this, this);
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new CommentDetailAdapter();
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        String orderId = getIntent().getStringExtra(EXTRA_KEY_COMMENT_ORDER_ID);
        mPresenter.getCommentList(orderId);
    }


    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @Override
    public void showCommentDetailList(List<CommentBean> data) {
        mAdapter.addData(data);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void showProgress(String message) {
    }

    @Override
    public void stopProgress() {

    }

    @Override
    public void finishView() {

    }
}
