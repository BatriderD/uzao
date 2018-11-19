package com.zhaotai.uzao.ui.theme.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeListBean;
import com.zhaotai.uzao.ui.theme.adapter.NewThemeListAdapter;
import com.zhaotai.uzao.ui.theme.contract.NewThemeListContract;
import com.zhaotai.uzao.ui.theme.presenter.ThemeListPresenter;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.widget.SimpleDividerItemDecoration;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * description:新增的主题列表
 * author : ZP
 * date: 2018/3/16 0016.
 */

public class ThemeListActivity extends BaseActivity implements NewThemeListContract.View, BaseQuickAdapter.RequestLoadMoreListener, OnRefreshListener, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.swipe)
    RefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;


    @BindView(R.id.tv_theme_sort_synthesize)
    TextView tv_Synthesize;

    @BindView(R.id.tv_theme_sort_time)
    TextView tv_time;

    @BindView(R.id.v_sort_synthesize_line)
    View v_synthesize_line;
    @BindView(R.id.v_sort_time_line)
    View v_time_line;

    private NewThemeListAdapter mAdapter;
    private ThemeListPresenter mPresenter;
    private PageInfo data;
    // true 综合  false time
    private boolean sortRule = true;


    public static void launch(Context context) {
        context.startActivity(new Intent(context, ThemeListActivity.class));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_theme_list);
        mTitle.setText("场景列表");

        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        LinearLayoutManager layoutManager = new GridLayoutManager(mContext, 2, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new NewThemeListAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mPresenter = new ThemeListPresenter(this, this);

        mSwipe.setOnRefreshListener(this);
        //设置空adapter点击事件
        mAdapter.setEmptyStateView(this, R.mipmap.ic_status_empty_design, "没有任何设计师，快去关注吧");
        SimpleDividerItemDecoration simpleDividerItemDecoration = new SimpleDividerItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(3));
        // 设置增加间隔
        mRecyclerView.addItemDecoration(simpleDividerItemDecoration);
    }

    @OnClick({R.id.rl_sort_synthesize, R.id.rl_sort_time})
    public void onSortClick(View view) {
        int id = view.getId();
        if (R.id.rl_sort_synthesize == id && !sortRule) {
            //点击的是综合排序  并且当前状态是时间排序
            sortRule = true;
            changeStatusAndGetData();
        } else if (R.id.rl_sort_time == id && sortRule) {
            //点击的是时间排序  并且当前状态是综合排序
            sortRule = false;
            changeStatusAndGetData();
        }
    }

    private void changeStatusAndGetData() {
        changeSortButton(sortRule);
        mPresenter.getThemeList(0, false, sortRule);
    }

    //    改变按钮ui
    public void changeSortButton(boolean sortStatus) {
        if (sortStatus) {
            tv_Synthesize.setSelected(true);
            v_synthesize_line.setVisibility(View.VISIBLE);
            tv_time.setSelected(false);
            v_time_line.setVisibility(View.INVISIBLE);
        } else {
            tv_time.setSelected(true);
            v_time_line.setVisibility(View.VISIBLE);
            tv_Synthesize.setSelected(false);
            v_synthesize_line.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void initData() {
        showLoading();
        changeSortButton(sortRule);
        mPresenter.getThemeList(0, true, true);
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
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
            //加载列表数据
            mPresenter.getThemeList(start, false, sortRule);
        } else {
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPresenter.getThemeList(0, false, sortRule);
    }


    @OnClick(R.id.iv_new_theme_search)
    public void onSearch() {
        ThemeListSearchActivity.launch(this);
    }

    @Override
    public void showThemeList(PageInfo<ThemeListBean> data) {
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
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ThemeListBean item = mAdapter.getItem(position);
        ThemeDetailActivity.launch(this, item.getId());
    }
}
