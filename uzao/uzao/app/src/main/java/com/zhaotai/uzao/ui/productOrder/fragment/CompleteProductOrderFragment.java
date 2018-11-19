package com.zhaotai.uzao.ui.productOrder.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ProductOrderBean;
import com.zhaotai.uzao.constants.OrderStatus;
import com.zhaotai.uzao.ui.order.activity.LogisticsActivity;
import com.zhaotai.uzao.ui.productOrder.activity.SampleFinishedActivity;
import com.zhaotai.uzao.ui.productOrder.activity.SamplingProduceFinishedActivity;
import com.zhaotai.uzao.ui.productOrder.adapter.ProductOrderAdapter;
import com.zhaotai.uzao.ui.productOrder.contract.ProductOrderContract;
import com.zhaotai.uzao.ui.productOrder.presenter.CompleteAndCloseOrderPresenter;

import butterknife.BindView;

/**
 * Time: 2017/6/6
 * Created by LiYou
 * Description : 完成订单
 */

public class CompleteProductOrderFragment extends BaseFragment implements ProductOrderContract.CompleteAndCloseView, BaseQuickAdapter.RequestLoadMoreListener
        , BaseQuickAdapter.OnItemChildClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipe)
    SwipeRefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private ProductOrderAdapter mAdapter;
    private CompleteAndCloseOrderPresenter mPresenter;
    //存放订单数据
    private PageInfo<ProductOrderBean> data = new PageInfo<>();


    public static CompleteProductOrderFragment newInstance() {
        return new CompleteProductOrderFragment();
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @Override
    protected boolean hasLazy() {
        return true;
    }

    @Override
    protected int layoutId() {
        return R.layout.frag_all_order;
    }

    @Override
    public void initView() {
        mSwipe.setOnRefreshListener(this);
        mRecycler.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ProductOrderAdapter();
        mAdapter.setOnLoadMoreListener(this, mRecycler);
        //条目点击
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setEmptyView(R.layout.vw_order_empty);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void initPresenter() {
        mPresenter = new CompleteAndCloseOrderPresenter(_mActivity, this);
    }

    @Override
    public void initData() {
        showLoading();
        mPresenter.getCompleteProductOrderList(0, true);
    }

    /**
     * 条目点击
     */
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        ProductOrderBean info = (ProductOrderBean) adapter.getItem(position);
        if (info == null) return;
        switch (view.getId()) {
            case R.id.tv_product_order_bottom_first_button:
                switch (info.orderStatus) {
                    case OrderStatus.FINISHED:
                        //物流信息
                        LogisticsActivity.launch(_mActivity, LogisticsActivity.TYPE_PRODUCE_ID, info.orderNo);
                        break;
                }
                break;
            case R.id.tv_product_order_bottom_second_button:
                switch (info.orderType) {
                    case OrderStatus.SAMPLING_PRODUCE://打样加大货
                        switch (info.orderStatus) {
                            case OrderStatus.FINISHED://已完成
                                SamplingProduceFinishedActivity.launch(_mActivity, info.orderNo,info.orderType);
                                break;
                        }
                        break;
                    case OrderStatus.PRODUCE://大货
                        switch (info.orderStatus) {
                            case OrderStatus.FINISHED://已完成
                                SamplingProduceFinishedActivity.launch(_mActivity, info.orderNo,info.orderType);
                                break;
                        }
                        break;
                    case OrderStatus.SAMPLING://打样
                        switch (info.orderStatus) {
                            case OrderStatus.FINISHED:
                                SampleFinishedActivity.launch(_mActivity,info.orderNo);
                                break;
                        }
                        break;
                }
                break;
        }
    }

    /**
     * 显示订单列表
     */
    @Override
    public void showProductOrderList(PageInfo<ProductOrderBean> data) {
        this.data = data;
        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
//            如果是首页 就设置新数据
            mAdapter.setNewData(this.data.list);
        } else {
//            不是第一页 就刷新
            mAdapter.addData(this.data.list);
        }
    }

    @Override
    public void stopLoadingMore() {
        mAdapter.loadMoreComplete();
    }

    @Override
    public void stopRefresh() {
        if (mSwipe.isRefreshing()) {
            mSwipe.setRefreshing(false);
        }
    }

    @Override
    public void loadingFail() {
        mAdapter.loadMoreFail();
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        mPresenter.getCompleteProductOrderList(0, false);
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getCompleteProductOrderList(start, false);
        } else {
            mAdapter.loadMoreEnd();
        }
    }
}
