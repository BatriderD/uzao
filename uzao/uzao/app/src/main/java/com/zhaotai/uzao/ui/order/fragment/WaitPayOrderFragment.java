package com.zhaotai.uzao.ui.order.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.bean.DictionaryBean;
import com.zhaotai.uzao.bean.EventBean.CancelOrderAllEvent;
import com.zhaotai.uzao.bean.EventBean.CancelOrderEvent;
import com.zhaotai.uzao.bean.EventBean.RefreshOrderEvent;
import com.zhaotai.uzao.bean.EventBean.RefreshWaitPayOrderEvent;
import com.zhaotai.uzao.bean.OrderBean;
import com.zhaotai.uzao.bean.OrderMultiBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.constants.OrderStatus;
import com.zhaotai.uzao.ui.order.activity.PayConfirmOrderActivity;
import com.zhaotai.uzao.ui.order.activity.PayConfirmOrderFromListActivity;
import com.zhaotai.uzao.ui.order.activity.WaitPayDetailActivity;
import com.zhaotai.uzao.ui.order.adapter.CancelOrderAdapter;
import com.zhaotai.uzao.ui.order.adapter.OrderMultiAdapter;
import com.zhaotai.uzao.ui.order.contract.OrderContract;
import com.zhaotai.uzao.ui.order.presenter.OrderPresenter;
import com.zhaotai.uzao.widget.dialog.UIBottomSheet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/6/6
 * Created by LiYou
 * Description : 待付款订单
 */

public class WaitPayOrderFragment extends BaseFragment implements OrderContract.View, BaseQuickAdapter.RequestLoadMoreListener,
        BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener, OnRefreshListener {

    @BindView(R.id.swipe)
    RefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.rl_order_pay)
    RelativeLayout mRlOrder;
    @BindView(R.id.tv_pay_them)
    TextView mPayThem;

    private OrderMultiAdapter mAdapter;
    private OrderPresenter mPresenter;

    private UIBottomSheet uiBottomSheet;
    //存放订单数据
    private PageInfo<OrderBean> data = new PageInfo<>();
    private String orderNo = "";
    private int itemPosition = 0;

    public static WaitPayOrderFragment newInstance() {
        return new WaitPayOrderFragment();
    }

    @Override
    protected int layoutId() {
        return R.layout.frag_pay_order_list;
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
        EventBus.getDefault().register(this);
        mSwipe.setOnRefreshListener(this);
        mRecycler.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mAdapter = new OrderMultiAdapter(new ArrayList<OrderMultiBean>());
        mAdapter.setOnLoadMoreListener(this, mRecycler);
        mAdapter.setOnItemChildClickListener(this);
        mRecycler.setAdapter(mAdapter);
        setEmptyStateView(_mActivity, R.mipmap.ic_state_empty_order, "没有相关状态的订单哦");
        mAdapter.disableLoadMoreIfNotFullPage(mRecycler);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemClickListener(this);
        mRlOrder.setVisibility(View.VISIBLE);
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
        mPresenter.getWaitPayOrderList(0, true);
    }

    /**
     * 显示订单列表
     */
    @Override
    public void showOrderList(PageInfo<OrderBean> data) {
        if (data.list.size() > 0) {
            mRlOrder.setVisibility(View.VISIBLE);
        } else {
            mRlOrder.setVisibility(View.GONE);
        }
        this.data = data;
        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
            //如果是首页 就设置新数据
            mAdapter.setNewData(mPresenter.constructMultiDataWaitPay(data.list));
        } else {
            //不是第一页 就刷新
            mAdapter.addData(mPresenter.constructMultiDataWaitPay(data.list));
        }
    }

    /**
     * 取消原因
     */
    @Override
    public void showCancelReason(List<DictionaryBean> dictionaryBeanList) {
        if (uiBottomSheet == null) {
            uiBottomSheet = new UIBottomSheet(_mActivity);
            View bottomSheetView = LayoutInflater.from(_mActivity).inflate(R.layout.view_recycle, null);
            bottomSheetView.setBackgroundColor(ContextCompat.getColor(_mActivity, R.color.white));
            RecyclerView mRecycler = (RecyclerView) bottomSheetView.findViewById(R.id.recycler);
            mRecycler.setLayoutManager(new LinearLayoutManager(_mActivity));
            CancelOrderAdapter cancelOrderAdapter = new CancelOrderAdapter();
            cancelOrderAdapter.setNewData(dictionaryBeanList);
            mRecycler.setAdapter(cancelOrderAdapter);
            uiBottomSheet.setContentView(bottomSheetView);
            uiBottomSheet.show();
            cancelOrderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    DictionaryBean cancelReason = (DictionaryBean) adapter.getItem(position);
                    if (cancelReason != null)
                        mPresenter.waitPayDetailCancelOrder(orderNo, cancelReason.entryValue, itemPosition);
                }
            });
        } else {
            uiBottomSheet.show();
        }
    }

    /**
     * 条目点击
     */
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        OrderMultiBean info = (OrderMultiBean) adapter.getItem(position);
        if (info == null) return;
        switch (view.getId()) {
            case R.id.tv_order_bottom_middle:
                orderNo = info.orderNo;
                itemPosition = position;
                //取消订单
                mPresenter.getCancelReason();
                break;
            case R.id.tv_order_bottom_right:
                ArrayList<String> orderIds = new ArrayList<>();
                orderIds.add(info.orderNo);
                switch (info.orderType) {
                    case OrderStatus.ORDER_TYPE_MATERIAL://素材
                        mPresenter.gotoPayMaterial(mAdapter, orderIds);
                        break;
                    case OrderStatus.ORDER_TYPE_PRODUCT://商品
                    case OrderStatus.ORDER_TYPE_SAMPLE_DESIGN://模板商品
                        PayConfirmOrderFromListActivity.launch(_mActivity, orderIds, false);
                        break;
                }
                break;
            //选择
            case R.id.tv_pay_order_choose:
                info.isSelected = !info.isSelected;
                mAdapter.notifyItemChanged(position);
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        OrderMultiBean info = (OrderMultiBean) adapter.getItem(position);
        if (info == null) return;
        orderNo = info.orderNo;
        if (!info.isMaterial)
            WaitPayDetailActivity.launch(_mActivity, orderNo, position, false);
    }


    /**
     * 合并支付
     */
    @OnClick(R.id.tv_pay_them)
    public void payThem() {
        boolean hasMaterial = false;
        boolean hasProduct = false;
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            if (mAdapter.getData().get(i).isSelected) {
                if (mAdapter.getData().get(i).isMaterial) {
                    hasMaterial = true;
                } else {
                    hasProduct = true;
                }
            }
        }

        if (hasProduct) {
            ArrayList<String> orderIds = new ArrayList<>();
            for (int i = 0; i < mAdapter.getData().size(); i++) {
                if (mAdapter.getData().get(i).isSelected) {
                    orderIds.add(mAdapter.getData().get(i).orderNo);
                }
            }
            if (orderIds.size() > 0) {
                PayConfirmOrderActivity.launch(_mActivity, orderIds);
            }
        } else if (hasMaterial) {
            ArrayList<String> orderIds = new ArrayList<>();
            for (int i = 0; i < mAdapter.getData().size(); i++) {
                if (mAdapter.getData().get(i).isSelected) {
                    orderIds.add(mAdapter.getData().get(i).orderNo);
                }
            }

            if (orderIds.size() > 0) {
                mPresenter.gotoPayMaterial(mAdapter, orderIds);
            }
        }
    }

    /**
     * 取消订单
     *
     * @param cancelOrderEvent 取消事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CancelOrderEvent cancelOrderEvent) {
        mAdapter.remove(cancelOrderEvent.position);
        if (uiBottomSheet != null)
            uiBottomSheet.dismiss();
    }

    /**
     * 取消订单-在全部列表取消订单
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CancelOrderAllEvent cancelOrderEvent) {
        mSwipe.autoRefresh();
    }

    /**
     * @param refreshWaitPayOrderEvent 刷新事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshWaitPayOrderEvent refreshWaitPayOrderEvent) {
        if (mPresenter != null) {
            mRecycler.scrollToPosition(0);
            mPresenter.getWaitPayOrderList(0, false);
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
     * 下拉刷新
     */
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (mPresenter != null)
            mPresenter.getWaitPayOrderList(0, false);
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getWaitPayOrderList(start, false);
        } else {
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
