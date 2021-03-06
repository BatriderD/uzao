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
import com.zhaotai.uzao.bean.EventBean.CancelWaitApproveOrderEvent;
import com.zhaotai.uzao.bean.OrderBean;
import com.zhaotai.uzao.bean.OrderMultiBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.ui.order.activity.WaitApproveDetailActivity;
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
 * Description : 待审核
 */

public class WaitApproveOrderFragment extends BaseFragment implements OrderContract.View, BaseQuickAdapter.RequestLoadMoreListener,
        BaseQuickAdapter.OnItemClickListener, OnRefreshListener {

    @BindView(R.id.swipe)
    RefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private OrderMultiAdapter mAdapter;
    private OrderPresenter mPresenter;
    //存放订单数据
    private PageInfo<OrderBean> data = new PageInfo<>();


    public static WaitApproveOrderFragment newInstance() {
        return new WaitApproveOrderFragment();
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
        mAdapter.setOnItemClickListener(this);
        mRecycler.setAdapter(mAdapter);

        EventBus.getDefault().register(this);
        setEmptyStateView(_mActivity, R.mipmap.ic_state_empty_order, "没有相关状态的订单哦");
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
        mPresenter.getWaitApproveOrderList(0, true);
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
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        OrderMultiBean info = (OrderMultiBean) adapter.getItem(position);
        if (info == null) return;
        WaitApproveDetailActivity.launch(_mActivity, info.orderNo);
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (mPresenter != null)
            mPresenter.getWaitApproveOrderList(0, false);
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getWaitApproveOrderList(start, false);
        } else {
            mAdapter.loadMoreEnd();
        }
    }

    /**
     * 取消订单
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CancelWaitApproveOrderEvent cancelOrderEvent) {
        mAdapter.remove(cancelOrderEvent.position);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

}
