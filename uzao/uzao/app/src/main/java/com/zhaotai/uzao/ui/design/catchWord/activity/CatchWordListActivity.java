package com.zhaotai.uzao.ui.design.catchWord.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.ArtFontTopicBean;
import com.zhaotai.uzao.bean.CatchWordBean;
import com.zhaotai.uzao.bean.CatchWordTabBean;
import com.zhaotai.uzao.bean.EventBean.EventBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.ui.design.catchWord.adapter.CatchContentAdapter;
import com.zhaotai.uzao.ui.design.catchWord.adapter.CatchTabAdapter;
import com.zhaotai.uzao.ui.design.catchWord.contract.CatchWordContract;
import com.zhaotai.uzao.ui.design.catchWord.presenter.CatchWordPresenter;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.utils.StatusBarUtil;
import com.zhaotai.uzao.widget.MultipleStatusView;
import com.zhaotai.uzao.widget.SimpleDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

/**
 * description:流行词列表页
 * author : ZP
 * date: 2018/1/8 0008.
 */

public class CatchWordListActivity extends BaseActivity implements CatchWordContract.View, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener, OnRefreshListener {


    @BindView(R.id.swipe)
    RefreshLayout mSwipe;

    @BindView(R.id.rc_catch_word_tab)
    public RecyclerView rc_tab;

    @BindView(R.id.rc_catch_word)
    public RecyclerView rc_content;


    @BindView(R.id.multiple_status_view1)
    public MultipleStatusView multipleStatusView1;


    private CatchWordPresenter mPresenter;
    private CatchTabAdapter catchTabAdapter;
    private CatchContentAdapter contentAdapter;
    private PageInfo<CatchWordBean> data;
    private String categoryCode;

    public static void launch(Activity context) {
        Intent intent = new Intent(context, CatchWordListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_catch_word);
        mTitle.setText(R.string.catch_word);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        rc_tab.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        catchTabAdapter = new CatchTabAdapter();
        rc_tab.setAdapter(catchTabAdapter);
        catchTabAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                catchTabAdapter.setselected(position);
                categoryCode = catchTabAdapter.getData().get(position).getCategoryCode();
                autoRefresh();
            }
        });

        mSwipe.setOnRefreshListener(this);
        rc_content.setLayoutManager(new GridLayoutManager(mContext, 2));
        contentAdapter = new CatchContentAdapter();
        contentAdapter.bindToRecyclerView(rc_content);
        contentAdapter.setEmptyStateView(this, R.mipmap.ic_state_empty_1, "抱歉没有内容，去别处逛逛吧");
        rc_content.setAdapter(contentAdapter);
        contentAdapter.setOnLoadMoreListener(this, rc_content);
        contentAdapter.setOnItemClickListener(this);
        rc_content.addItemDecoration(new SimpleDividerItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(3)));

        multipleStatusView1.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryCode != null) {
                    autoRefresh();
                }
            }
        });

        mPresenter = new CatchWordPresenter(this, this);
    }

    @Override
    public void handleStatusBar() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.color_black));
    }


    private void autoRefresh() {
        mSwipe.autoRefresh(0, 3, 2);
    }

    @Override
    protected void initData() {
        showLoading();
        mPresenter.getCatchWordTabList();
    }


    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @Override
    public void showTabList(List<CatchWordTabBean> tabList) {
        catchTabAdapter.setNewData(tabList);
        if (tabList.size() > 0) {
            catchTabAdapter.setselected(0);
            categoryCode = tabList.get(0).getCategoryCode();
            autoRefresh();
        }

    }

    @Override
    public void showCatchWordContentList(PageInfo<CatchWordBean> info) {
        this.data = info;
        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
//            如果是首页 就设置新数据
            contentAdapter.setNewData(data.list);
        } else {
//            不是第一页 就刷新
            contentAdapter.addData(info.list);
        }
        if (data.currentPage == data.totalPages) {
            contentAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showTopicListError(String msg) {
        if (multipleStatusView1 != null) {
            if (this.getString(R.string.no_net).equals(msg)) {
                multipleStatusView1.showError(getString(R.string.no_net));
            } else {
                multipleStatusView1.showError("数据错误");
            }
        }
    }

    @Override
    public void showTopicListLoading() {
        multipleStatusView1.showLoading();
    }

    @Override
    public void showTopicListContent() {
        multipleStatusView1.showContent();
    }

    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getCatchWordContentList(start, categoryCode);
        } else {
            contentAdapter.loadMoreEnd();
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ArtFontTopicBean artFontTopicBean = new ArtFontTopicBean();
        artFontTopicBean.setWordartFileName(contentAdapter.getData().get(position).getExtend1());
        artFontTopicBean.setWordartName(contentAdapter.getData().get(position).getWordartName());
        artFontTopicBean.text = contentAdapter.getData().get(position).getText();
        EventBus.getDefault().post(new EventBean<>(artFontTopicBean, EventBusEvent.ADD_EDITOR_WORD));
        finish();
    }


    @Override
    public void stopRefresh() {
        mSwipe.finishRefresh();
    }

    @Override
    public void loadingFail() {
        contentAdapter.loadMoreFail();
    }

    @Override
    public void stopLoadingMore() {
        contentAdapter.loadMoreComplete();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPresenter.getCatchWordContentList(0, categoryCode);
    }
}
