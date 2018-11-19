package com.zhaotai.uzao.ui.order.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.adapter.AfterSalesAdapter;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.AfterSalesBean;
import com.zhaotai.uzao.bean.EventBean.ChangeAfterSalesStatusEvent;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.ui.order.contract.MyAfterSalesContract;
import com.zhaotai.uzao.ui.order.presenter.MyAfterSalesPresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/**
 * Time: 2017/6/2
 * Created by LiYou
 * Description : 售后列表
 */

public class MyAfterSalesActivity extends BaseActivity implements MyAfterSalesContract.View, BaseQuickAdapter.RequestLoadMoreListener, OnRefreshListener {

    @BindView(R.id.swipe)
    RefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private AfterSalesAdapter mAdapter;
    private MyAfterSalesPresenter mPresenter;
    private PageInfo<AfterSalesBean> data = new PageInfo<>();

    public static void launch(Context context) {
        context.startActivity(new Intent(context, MyAfterSalesActivity.class));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_sold_order);
        mTitle.setText(R.string.my_customer_service);
        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new AfterSalesAdapter();
        mRecycler.setAdapter(mAdapter);
        mSwipe.setOnRefreshListener(this);
        mAdapter.setOnLoadMoreListener(this, mRecycler);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AfterSalesBean item = (AfterSalesBean) adapter.getItem(position);
                if (item != null) {
                    //等待顾客发货
                    if ("waitCustDeliver".equals(item.status)) {
                        ApplyAfterSalesDetailAddTransportActivity.launch((Activity) mContext, item.applyNo);
                    } else {
                        ApplyAfterSalesDetailActivity.launch(mContext, item.applyNo);
                    }
                }
            }
        });

        mAdapter.setEmptyStateView(mContext, R.mipmap.ic_state_empty_order, "没有售后订单哦");

        mPresenter = new MyAfterSalesPresenter(this, this);
    }

    @Override
    protected void initData() {
        showLoading();
        mPresenter.getAfterSalesList(0, true);
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @Override
    public void showAfterSalesList(PageInfo<AfterSalesBean> pageInfo) {
        data = pageInfo;
        if (pageInfo.currentPage == Constant.CURRENTPAGE_HOME) {
//            如果是首页 就设置新数据
            mAdapter.setNewData(pageInfo.list);
        } else {
//            不是第一页 就刷新
            mAdapter.addData(pageInfo.list);
        }
    }


    /**
     * 添加物流成功改变售后订单状态
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeAfterSalesStatusEvent changeAfterSalesStatusEvent) {
        List<AfterSalesBean> data = mAdapter.getData();
        for (AfterSalesBean afterSalesBean : data) {
            if (afterSalesBean.applyNo.equals(changeAfterSalesStatusEvent.applyNo)) {
                afterSalesBean.status = "confirming";
            }
        }
    }

    /**
     * 上拉加载
     */
    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getAfterSalesList(start, false);
        } else {
            mAdapter.loadMoreEnd();
        }
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//        刷新页面
        mPresenter.getAfterSalesList(Constant.PAGING_HOME, false);
    }

    /**
     * 停止加载更多状态
     */
    @Override
    public void stopLoadingMore() {
        mAdapter.loadMoreComplete();
    }

    /**
     * 停止刷新状态
     */
    @Override
    public void stopRefresh() {
        mSwipe.finishRefresh();
    }

    @Override
    public void loadingFail() {
        mAdapter.loadMoreFail();
    }
}
