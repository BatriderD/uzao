package com.zhaotai.uzao.ui.category.goods.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.adapter.CommentListAdapter;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.CommentBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.StartCountBean;
import com.zhaotai.uzao.ui.category.goods.adapter.PopCommentTabAdapter;
import com.zhaotai.uzao.ui.category.goods.contract.CommentListContract;
import com.zhaotai.uzao.ui.category.goods.presenter.CommentListPresent;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/8/18
 * Created by LiYou
 * Description : 评价列表
 */

public class CommentListActivity extends BaseActivity implements CommentListContract.View, BaseQuickAdapter.RequestLoadMoreListener, OnRefreshListener {

    @BindView(R.id.swipe)
    RefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.right_btn)
    Button mRightBtn;

    @BindView(R.id.view_comment_tab_left_line)
    View mTabLeftLine;
    @BindView(R.id.view_comment_tab_right_line)
    View mTabRightLine;

    @BindView(R.id.rl_comment_tab_left)
    RelativeLayout mRlCommentTabLeft;

    //存放评价数据
    private PageInfo<CommentBean> data = new PageInfo<>();
    private String star = "";
    /**
     * 查看全部 还是带照片评论
     */
    private boolean haveImage;
    private PopupWindow mPop;

    private static final String EXTRA_KEY_SPU_ID = "extra_key_spu_id";
    private CommentListPresent mPresenter;
    private PopCommentTabAdapter mTabAdapter;
    private CommentListAdapter mAdapter;
    private String spuId;

    /**
     * @param spuId 商品ID
     */
    public static void launch(Context context, String spuId) {
        Intent intent = new Intent(context, CommentListActivity.class);
        intent.putExtra(EXTRA_KEY_SPU_ID, spuId);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_comment_list);
        mTitle.setText("商品评价");
        mTabAdapter = new PopCommentTabAdapter();
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mSwipe.setOnRefreshListener(this);
        mTabAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                haveImage = false;
                if (position == 0) {
                    star = "";
                    mPresenter.getCommentList(0, false, spuId, haveImage, star);
                } else {
                    StartCountBean item = (StartCountBean) adapter.getItem(position);
                    if (item != null) {
                        star = String.valueOf(item.STAR_SCORE);
                        mPresenter.getCommentList(0, false, spuId, haveImage, star);
                    }
                }
                mPop.dismiss();
                mTabRightLine.setVisibility(View.GONE);
                mTabLeftLine.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void initData() {
        if (mPresenter == null) {
            mPresenter = new CommentListPresent(this, this);
        }
        spuId = getIntent().getStringExtra(EXTRA_KEY_SPU_ID);
        mPresenter.getCommentList(0, false, spuId, haveImage, "");
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    /**
     * 全部
     */
    @OnClick(R.id.rl_comment_tab_left)
    public void onClickTabLeft() {
        showPop();
    }


    /**
     * 有图
     */
    @OnClick(R.id.tv_comment_tab_right_text)
    public void onClickHaveImage() {
        haveImage = true;
        star = "";
        mTabRightLine.setVisibility(View.VISIBLE);
        mTabLeftLine.setVisibility(View.GONE);
        mPresenter.getCommentList(0, false, spuId, haveImage, star);
    }

    @Override
    public void showCommentList(PageInfo<CommentBean> data) {
        if (mAdapter == null) {
            mAdapter = new CommentListAdapter(data.list);
            mAdapter.setOnLoadMoreListener(this, mRecycler);
            mRecycler.setAdapter(mAdapter);
        }
        this.data = data;
        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
            //如果是首页 就设置新数据
            mAdapter.setNewData(data.list);
        } else {
            //不是第一页 就刷新
            mAdapter.addData(data.list);
        }
    }

    @Override
    public void showCommentStart(List<StartCountBean> start) {
        mTabAdapter.setNewData(start);
    }

    private void initPop() {
        mPop = new PopupWindow(this);
        mPop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        RecyclerView popView = (RecyclerView) LayoutInflater.from(this).inflate(R.layout.view_recycle, null);
        popView.setLayoutManager(new LinearLayoutManager(mContext));
        popView.setAdapter(mTabAdapter);
        mPop.setContentView(popView);
        mPop.setBackgroundDrawable(new ColorDrawable(0xffffffff));
        mPop.setOutsideTouchable(false);
        mPop.setFocusable(true);
    }

    public void showPop() {
        if (mPop == null) {
            initPop();
        }
        mPop.showAsDropDown(mRlCommentTabLeft);
    }

    @Override
    public void stopLoadingMore() {
        mAdapter.loadMoreComplete();
    }

    @Override
    public void stopRefresh() {
        mSwipe.finishRefresh();
    }

    @Override
    public void loadingFail() {
        mAdapter.loadMoreFail();
    }

    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            mPresenter.getCommentList(start, false, spuId, haveImage, star);
            //加载列表数据
        } else {
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if(mPresenter != null){
            mPresenter.getCommentList(0, false, spuId, haveImage, star);
        }
    }
}
