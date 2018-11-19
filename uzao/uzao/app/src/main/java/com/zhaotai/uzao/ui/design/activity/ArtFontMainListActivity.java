package com.zhaotai.uzao.ui.design.activity;

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
import com.zhaotai.uzao.bean.ArtFontTabBean;
import com.zhaotai.uzao.bean.ArtFontTopicBean;
import com.zhaotai.uzao.bean.EventBean.EventBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.design.adapter.ArtFontContentAdapter;
import com.zhaotai.uzao.ui.design.adapter.ArtFontTabAdapter;
import com.zhaotai.uzao.ui.design.contract.ArtFontMainContract;
import com.zhaotai.uzao.ui.design.presenter.ArtFontMainPresenter;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.utils.StatusBarUtil;
import com.zhaotai.uzao.widget.MultipleStatusView;
import com.zhaotai.uzao.widget.SimpleDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

/**
 * description:艺术字列表页
 * author : ZP
 * date: 2018/1/8 0008.
 */

public class ArtFontMainListActivity extends BaseActivity implements ArtFontMainContract.View, OnRefreshListener {
    @BindView(R.id.swipe)
    RefreshLayout mSwipe;

    @BindView(R.id.rc_catch_word_tab)
    public RecyclerView rc_tab;

    @BindView(R.id.rc_catch_word)
    public RecyclerView rc_content;

    @BindView(R.id.multiple_status_view1)
    public MultipleStatusView multipleStatusView1;

    private ArtFontMainPresenter mPresenter;
    private ArtFontTabAdapter tabAdapter;
    private PageInfo<ArtFontTabBean> data;
    private ArtFontContentAdapter contentAdapter;
    private PageInfo<ArtFontTopicBean> topicData;
    private String topicWord;


    public static void launch(Activity context) {
        Intent intent = new Intent(context, ArtFontMainListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_catch_word);
        mTitle.setText("艺术字");
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rc_tab.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        rc_content.setLayoutManager(new GridLayoutManager(mContext, 2));
        tabAdapter = new ArtFontTabAdapter();
        tabAdapter.bindToRecyclerView(rc_tab);
        tabAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                tabAdapter.setselected(position);
                topicWord = tabAdapter.getData().get(position).getTopicCode();
                autoRefresh();
            }
        });
        rc_tab.setAdapter(tabAdapter);
        tabAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (data.hasNextPage) {
                    int start = data.pageStartRow + data.pageRecorders;
                    //加载列表数据
                    mPresenter.getArtTabList(start);
                } else {
                    tabAdapter.loadMoreEnd(true);
                }
            }
        }, rc_tab);


        contentAdapter = new ArtFontContentAdapter();
        rc_content.setAdapter(contentAdapter);
        rc_content.addItemDecoration(new SimpleDividerItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(3)));

        contentAdapter.bindToRecyclerView(rc_content);
        contentAdapter.setEmptyStateView(this, R.mipmap.ic_state_empty_1, "抱歉没有内容，去别处逛逛吧");
        contentAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (topicData.hasNextPage) {
                    int start = topicData.pageStartRow + topicData.pageRecorders;
                    //加载列表数据
                    getTopicArtFontList(start, topicWord);
                } else {
                    contentAdapter.loadMoreEnd();
                }
            }
        }, rc_content);
        contentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //下载此字体
                EventBus.getDefault().post(new EventBean<>(contentAdapter.getData().get(position), EventBusEvent.CHANGE_ART_FONT));
                finish();
            }
        });
        mSwipe.setOnRefreshListener(this);
        multipleStatusView1.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topicWord != null) {
                    getTopicArtFontList(0, topicWord);
                }
            }
        });
        mPresenter = new ArtFontMainPresenter(this, this);
    }


    @Override
    public void handleStatusBar() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.color_black));
    }

    private void autoRefresh() {
        mSwipe.autoRefresh(GlobalVariable.SWIPE_DELAYED, GlobalVariable.SWIPE_DURATION, GlobalVariable.SWIPE_DRAG_RATE);
    }

    @Override
    protected void initData() {
        showLoading();
        mPresenter.getArtTabList(0);
    }


    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @Override
    public void showTabList(PageInfo<ArtFontTabBean> data) {
        this.data = data;
        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
//            如果是首页 就设置新数据
            showContent();
            tabAdapter.setNewData(data.list);
            tabAdapter.setselected(0);
            List<ArtFontTabBean> list = data.list;
            if (list != null && list.size() > 0) {
                topicWord = list.get(0).getTopicCode();
                autoRefresh();
            }
        } else {
//            不是第一页 就刷新
            tabAdapter.addData(data.list);
        }
    }

    @Override
    public void showTopicList(PageInfo<ArtFontTopicBean> info) {
        this.topicData = info;
        if (topicData.currentPage == Constant.CURRENTPAGE_HOME) {
//            如果是首页 就设置新数据
            multipleStatusView1.showContent();
            contentAdapter.setNewData(topicData.list);
        } else {
//            不是第一页 就刷新
            contentAdapter.addData(info.list);
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

    public void getTopicArtFontList(int start, String topicCode) {
        mPresenter.getTopicFontList(start, topicCode);
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
        mPresenter.getTopicFontList(0, topicWord);
    }
}
