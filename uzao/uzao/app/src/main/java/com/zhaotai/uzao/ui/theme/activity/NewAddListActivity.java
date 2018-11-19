package com.zhaotai.uzao.ui.theme.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.EventBean.EventBean;
import com.zhaotai.uzao.bean.NewAddListBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeModuleBean;
import com.zhaotai.uzao.ui.theme.adapter.NewAddListAdapter;
import com.zhaotai.uzao.ui.theme.contract.NewAddListContract;
import com.zhaotai.uzao.ui.theme.presenter.NewAddListPresenter;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.widget.SimpleDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * description: 新增列表页
 * author : ZP
 * date: 2018/1/24 0024.
 */

public class NewAddListActivity extends BaseActivity implements NewAddListContract.View, BaseQuickAdapter.RequestLoadMoreListener
        , BaseQuickAdapter.OnItemClickListener, OnRefreshListener {


    @BindView(R.id.swipe)
    RefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private NewAddListAdapter mAdapter;
    private NewAddListPresenter mPresenter;

    //存放订单数据
    private PageInfo<NewAddListBean> data = new PageInfo<>();
    private String themeId;
    private String type;

    public static void launch(Context context, String themeId, String type) {
        Intent intent = new Intent(context, NewAddListActivity.class);
        intent.putExtra("themeId", themeId);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_new_add_list);
        mSwipe.setOnRefreshListener(this);
        mRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter = new NewAddListAdapter();
        mAdapter.setOnLoadMoreListener(this, mRecycler);
        //条目点击
        mAdapter.setOnItemClickListener(this);
        mAdapter.setEmptyStateView(this, R.mipmap.ic_state_empty_1, "抱歉没有内容，去别处逛逛吧");
        mRecycler.setAdapter(mAdapter);
        mRecycler.addItemDecoration(new SimpleDividerItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(3)));

        mPresenter = new NewAddListPresenter(this, this);
    }


    @Override
    protected void initData() {
        //请求新增主题列表
        Intent intent = getIntent();
        themeId = intent.getStringExtra("themeId");
        type = intent.getStringExtra("type");
        if ("addSpu".equals(type)) {
            mTitle.setText("最近新增商品");
        } else {
            mTitle.setText("最近新增素材");
        }
        showLoading();
        mPresenter.getNewAddList(0, true, themeId, type);
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @Override
    public boolean hasTitle() {
        return true;
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


    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getNewAddList(start, false, themeId, type);
        } else {
            mAdapter.loadMoreEnd();
        }
    }

    /**
     * 显示新增列表数据
     *
     * @param pageInfo 新增列表数据
     */
    @Override
    public void showNewAddList(PageInfo<NewAddListBean> pageInfo) {
        this.data = pageInfo;
        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
//            如果是首页 就设置新数据
            mAdapter.setNewData(this.data.list);
        } else {
//            不是第一页 就刷新
            mAdapter.addData(this.data.list);
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        NewAddListBean item = mAdapter.getItem(position);
        ThemeModuleBean.ThemeContentModel contentModel = new ThemeModuleBean.ThemeContentModel();
        //增加进入新增的进入主题
        contentModel.setEntityType(item.entityType);
        contentModel.setEntityId(item.entityId);
        contentModel.setEntityName(item.entityName);
        contentModel.setEntityPic(item.entityPic);
        contentModel.setEntityPriceY(item.entityPriceY);
        contentModel.setBuyCounts("0");
        contentModel.setBuyCounts("0");
        EventBus.getDefault().post(new EventBean<>(contentModel, EventBusEvent.ADD_MODULE_TO_THEME));
        finish();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPresenter.getNewAddList(0, false, themeId, type);
    }
}
