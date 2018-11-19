package com.zhaotai.uzao.ui.order.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.bean.DictionaryBean;
import com.zhaotai.uzao.bean.EventBean.RefreshOrderEvent;
import com.zhaotai.uzao.bean.OrderBean;
import com.zhaotai.uzao.bean.OrderMultiBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.ui.order.activity.CommentDetailActivity;
import com.zhaotai.uzao.ui.order.activity.CommentOrderActivity;
import com.zhaotai.uzao.ui.order.activity.CompleteDetailActivity;
import com.zhaotai.uzao.ui.order.activity.LogisticsActivity;
import com.zhaotai.uzao.ui.order.adapter.OrderMultiAdapter;
import com.zhaotai.uzao.ui.order.contract.OrderContract;
import com.zhaotai.uzao.ui.order.presenter.OrderPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Time: 2017/6/6
 * Created by LiYou
 * Description : 待评价
 */

public class WaitCommendOrderFragment extends BaseFragment implements OrderContract.View, BaseQuickAdapter.RequestLoadMoreListener,
        BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener, OnRefreshListener {

    @BindView(R.id.swipe)
    RefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private OrderMultiAdapter mAdapter;
    private OrderPresenter mPresenter;
    //存放订单数据
    private PageInfo<OrderBean> data = new PageInfo<>();

    public static WaitCommendOrderFragment newInstance() {
        return new WaitCommendOrderFragment();
    }

    @Override
    protected int layoutId() {
        return R.layout.frag_all_order;
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
    public void initView() {
        mSwipe.setOnRefreshListener(this);
        mRecycler.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mAdapter = new OrderMultiAdapter(new ArrayList<OrderMultiBean>());
        mAdapter.setOnLoadMoreListener(this, mRecycler);
        //条目点击
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemClickListener(this);
        mRecycler.setAdapter(mAdapter);
        setEmptyStateView(_mActivity, R.mipmap.ic_state_empty_order, "没有相关状态的订单哦");
        EventBus.getDefault().register(this);
    }

    public void setEmptyStateView(Context context, int resImageId, String emptyString) {
        View mEmptyStateView = View.inflate(context, R.layout.vw_empty, null);
        ImageView emptyImage = (ImageView) mEmptyStateView.findViewById(R.id.empty_retry_view);
        TextView emptyText = (TextView) mEmptyStateView.findViewById(R.id.empty_view_tv);

        emptyImage.setImageResource(resImageId);
        emptyText.setText(emptyString);
        mAdapter.setEmptyView(mEmptyStateView);
    }

    @Override
    public void initPresenter() {
        mPresenter = new OrderPresenter(_mActivity, this);
    }

    @Override
    public void initData() {
        showLoading();
        mPresenter.getWaitCommendOrder(0, true);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

    }

    /**
     * 显示订单列表
     */
    @Override
    public void showOrderList(PageInfo<OrderBean> data) {
        this.data = data;
        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
            //如果是首页 就设置新数据
            mAdapter.setNewData(mPresenter.constructMultiData(data.list));
        } else {
            //不是第一页 就刷新
            mAdapter.addData(mPresenter.constructMultiData(data.list));
        }
    }

    @Override
    public void showCancelReason(List<DictionaryBean> dictionaryBeanList) {

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
     * 条目点击事件
     */
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        OrderMultiBean info = (OrderMultiBean) adapter.getItem(position);
        if (info == null) return;
        switch (view.getId()) {
            case R.id.tv_order_bottom_middle:
                //查看物流
                LogisticsActivity.launch(_mActivity, LogisticsActivity.TYPE_ORDER_ID, info.orderNo);
                break;
            case R.id.tv_order_bottom_right:
                //去评价
                if ("N".equals(info.isCommend)) {
                    //去评价
                    CommentOrderActivity.launch(_mActivity, info.orderNo, info.packageOrderNo);
                } else {
                    CommentDetailActivity.launch(_mActivity, info.packageOrderNo);
                }
                break;
        }
    }

    /**
     * 条目点击
     */

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        OrderMultiBean info = (OrderMultiBean) adapter.getItem(position);
        if (info == null) return;
        CompleteDetailActivity.launch(_mActivity, info.orderNo, position);
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (mPresenter != null)
            mPresenter.getWaitCommendOrder(0, false);
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getWaitCommendOrder(start, false);
        } else {
            mAdapter.loadMoreEnd();
        }
    }

    /**
     * 刷新订单
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshOrderEvent refreshOrderEvent) {
        mSwipe.autoRefresh();
    }


    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
