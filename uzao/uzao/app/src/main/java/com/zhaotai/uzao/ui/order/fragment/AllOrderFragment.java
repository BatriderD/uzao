package com.zhaotai.uzao.ui.order.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.HomeActivity;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.bean.DictionaryBean;
import com.zhaotai.uzao.bean.EventBean.CancelOrderAllEvent;
import com.zhaotai.uzao.bean.EventBean.RefreshAllOrderEvent;
import com.zhaotai.uzao.bean.EventBean.RefreshOrderEvent;
import com.zhaotai.uzao.bean.EventBean.RemoveOrderEvent;
import com.zhaotai.uzao.bean.OrderBean;
import com.zhaotai.uzao.bean.OrderMultiBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.constants.OrderStatus;
import com.zhaotai.uzao.listener.BtnOnClickListener;
import com.zhaotai.uzao.ui.order.activity.ApplyAfterSalesActivity;
import com.zhaotai.uzao.ui.order.activity.CloseDetailActivity;
import com.zhaotai.uzao.ui.order.activity.CommentDetailActivity;
import com.zhaotai.uzao.ui.order.activity.CommentOrderActivity;
import com.zhaotai.uzao.ui.order.activity.CompleteDetailActivity;
import com.zhaotai.uzao.ui.order.activity.LogisticsActivity;
import com.zhaotai.uzao.ui.order.activity.PayConfirmOrderFromListActivity;
import com.zhaotai.uzao.ui.order.activity.WaitApproveDetailActivity;
import com.zhaotai.uzao.ui.order.activity.WaitDeliveryDetailActivity;
import com.zhaotai.uzao.ui.order.activity.WaitPayDetailActivity;
import com.zhaotai.uzao.ui.order.activity.WaitReceiveDetailActivity;
import com.zhaotai.uzao.ui.order.adapter.CancelOrderAdapter;
import com.zhaotai.uzao.ui.order.adapter.OrderMultiAdapter;
import com.zhaotai.uzao.ui.order.contract.OrderContract;
import com.zhaotai.uzao.ui.order.presenter.OrderPresenter;
import com.zhaotai.uzao.widget.dialog.UIBottomSheet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

/**
 * Time: 2017/6/6
 * Created by LiYou
 * Description : 全部订单
 */

public class AllOrderFragment extends BaseFragment implements OrderContract.View, BaseQuickAdapter.RequestLoadMoreListener
        , BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener, OnRefreshListener {

    @BindView(R.id.swipe)
    SmartRefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private OrderMultiAdapter mAdapter;
    private OrderPresenter mPresenter;
    //存放订单数据
    private PageInfo<OrderBean> data = new PageInfo<>();
    private UIBottomSheet uiBottomSheet;
    private String orderNo = "";

    public static AllOrderFragment newInstance() {
        return new AllOrderFragment();
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
        EventBus.getDefault().register(this);
        mSwipe.setOnRefreshListener(this);
        mRecycler.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        mAdapter = new OrderMultiAdapter(new ArrayList<OrderMultiBean>());
        mAdapter.setOnLoadMoreListener(this, mRecycler);
        //条目点击
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemClickListener(this);
        mRecycler.setAdapter(mAdapter);
        //设置空页面
        setEmptyStateView(_mActivity, R.mipmap.ic_state_empty_order, "居然还没有订单,好东西,手慢无", "去逛逛", new BtnOnClickListener() {
            @Override
            public void btnOnClickListener() {
                HomeActivity.launch(_mActivity, 1);
            }
        });
    }

    public void setEmptyStateView(Context context, int resImageId, String emptyString, String emptyBtnString, final BtnOnClickListener listener) {
        View mEmptyStateView = View.inflate(context, R.layout.vw_empty, null);
        ImageView emptyImage = (ImageView) mEmptyStateView.findViewById(R.id.empty_retry_view);
        TextView emptyText = (TextView) mEmptyStateView.findViewById(R.id.empty_view_tv);
        TextView emptyBtn = (TextView) mEmptyStateView.findViewById(R.id.empty_view_btn);

        emptyImage.setImageResource(resImageId);
        emptyText.setText(emptyString);
        emptyBtn.setVisibility(View.VISIBLE);
        emptyBtn.setText(emptyBtnString);
        emptyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.btnOnClickListener();
            }
        });

        mAdapter.setEmptyView(mEmptyStateView);
    }

    @Override
    public void initPresenter() {
        mPresenter = new OrderPresenter(_mActivity, this);
    }

    @Override
    public void initData() {
        showLoading();
        mPresenter.getAllOrderList(0, true);
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        OrderMultiBean info = (OrderMultiBean) adapter.getItem(position);
        if (info == null) return;
        orderNo = info.orderNo;
        if (!info.isMaterial)
            switch (info.orderStatus) {
                case OrderStatus.WAIT_PAY://待付款
                    WaitPayDetailActivity.launch(_mActivity, orderNo, position, true);
                    break;
                case OrderStatus.WAIT_APPROVE://待审核
                    WaitApproveDetailActivity.launch(_mActivity, orderNo);
                    break;
                case OrderStatus.WAIT_HANDLE://待发货
                case OrderStatus.WAIT_DELIVERY://待发货
                    WaitDeliveryDetailActivity.launch(_mActivity, orderNo);
                    break;
                case OrderStatus.WAIT_RECEIVE://待收货
                    WaitReceiveDetailActivity.launch(_mActivity, orderNo, position);
                    break;
                case OrderStatus.FINISHED://已完成
                    CompleteDetailActivity.launch(_mActivity, orderNo, position);
                    break;
                case OrderStatus.CANCELED: //已关闭
                case OrderStatus.UN_APPROVE:
                case OrderStatus.CLOSED:
                    CloseDetailActivity.launch(_mActivity, orderNo);
                    break;
            }
    }

    /**
     * 条目点击
     */
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        OrderMultiBean info = (OrderMultiBean) adapter.getItem(position);
        if (info == null) return;
        orderNo = info.orderNo;
        switch (view.getId()) {
            case R.id.tv_order_bottom_left:
                if (OrderStatus.WAIT_RECEIVE.equals(info.packageStatus)) {
                    //申请售后
                    ApplyAfterSalesActivity.launch(_mActivity, info.packageOrderNo, info.packageNum);
                }
                break;
            case R.id.tv_order_bottom_middle:
                switch (info.orderStatus) {
                    case OrderStatus.WAIT_PAY://待付款
                        mPresenter.getCancelReason();
                        break;
                    case OrderStatus.WAIT_APPROVE://待审核
                        break;
                    case OrderStatus.WAIT_RECEIVE://待收货
                        //查看物流
                        LogisticsActivity.launch(_mActivity, LogisticsActivity.TYPE_ORDER_ID, info.packageOrderNo);
                        break;
                    case OrderStatus.FINISHED://已完成
                        //查看物流
                        LogisticsActivity.launch(_mActivity, LogisticsActivity.TYPE_ORDER_ID, info.packageOrderNo);
                        break;
                }
                break;
            case R.id.tv_order_bottom_right:
                if (OrderStatus.WAIT_PAY.equals(info.orderStatus)) {
                    //待付款
                    ArrayList<String> orderIds = new ArrayList<>();
                    orderIds.add(info.orderNo);
                    switch (info.orderType) {
                        case OrderStatus.ORDER_TYPE_MATERIAL://素材
                            mPresenter.gotoPayMaterial(mAdapter, orderIds);
                            break;
                        case OrderStatus.ORDER_TYPE_PRODUCT://商品
                        case OrderStatus.ORDER_TYPE_SAMPLE_DESIGN://模板商品
                            PayConfirmOrderFromListActivity.launch(_mActivity, orderIds, true);
                            break;
                    }
                }

                if (OrderStatus.CANCELED.equals(info.orderStatus) ||
                        OrderStatus.UN_APPROVE.equals(info.orderStatus) ||
                        OrderStatus.CLOSED.equals(info.orderStatus) ||
                        OrderStatus.WAIT_REFUND.equals(info.orderStatus)) {
                    //删除订单
                    deleteOrder(info.orderNo, adapter, position);
                }

                if (OrderStatus.WAIT_RECEIVE.equals(info.packageStatus)) {
                    //确认收货
                    confirmGoods(info.packageOrderNo);
                }

                if (OrderStatus.FINISHED.equals(info.packageStatus)) {
                    if ("N".equals(info.isCommend)) {
                        //去评价
                        CommentOrderActivity.launch(_mActivity, info.orderNo, info.packageOrderNo);
                    } else {
                        CommentDetailActivity.launch(_mActivity, info.packageOrderNo);
                    }
                }
                break;
        }
    }

    /**
     * 删除订单
     *
     * @param orderNo  订单id
     * @param adapter  适配器
     * @param position item位置
     */
    private void deleteOrder(final String orderNo, final BaseQuickAdapter adapter, final int position) {
        AlertDialog alert = new AlertDialog.Builder(_mActivity)
                .setMessage("您确定要删除订单吗?")
                .setNegativeButton("取消", null)
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.deleteOrder(orderNo, adapter, position);
                    }
                }).create();
        alert.show();
    }

    /**
     * 确认收货
     *
     * @param packageOrderNo 包裹订单id
     */
    private void confirmGoods(final String packageOrderNo) {
        AlertDialog alert = new AlertDialog.Builder(_mActivity)
                .setMessage("您确定已收到商品?")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.confirmReceiveGoods(packageOrderNo);
                    }
                }).create();
        alert.show();
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
            mAdapter.addData(mPresenter.constructMultiData(data.list));
        }
    }

    /**
     * 取消原因
     *
     * @param dictionaryBeanList 字典数据
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
                        mPresenter.cancelOrder(orderNo, cancelReason.entryValue);
                }
            });
        } else {
            uiBottomSheet.show();
        }
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
     * 付款成功刷新订单
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshAllOrderEvent refreshAllOrder) {
        if (data != null && mPresenter != null) {
            mRecycler.scrollToPosition(0);
            mPresenter.getAllOrderList(0, false);
        }
    }

    /**
     * 刷新订单
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshOrderEvent refreshOrderEvent) {
        mSwipe.autoRefresh();
    }

    /**
     * 接收关闭详情的 删除订单
     *
     * @param removeOrderEvent 删除订单
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RemoveOrderEvent removeOrderEvent) {
        List<OrderMultiBean> data = mAdapter.getData();
        Iterator<OrderMultiBean> iterator = data.iterator();
        while (iterator.hasNext()) {
            if (removeOrderEvent.orderNo.equals(iterator.next().orderNo)) {
                iterator.remove();
            }
        }
        mAdapter.notifyDataSetChanged();
    }


    /**
     * 取消订单
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CancelOrderAllEvent cancelOrderEvent) {
        List<OrderMultiBean> data = mAdapter.getData();
        for (OrderMultiBean multiBean : data) {
            if (cancelOrderEvent.orderNo.equals(multiBean.orderNo)) {
                multiBean.orderStatus = OrderStatus.CANCELED;
            }
        }
        if (uiBottomSheet != null) {
            uiBottomSheet.dismiss();
        }
        mAdapter.notifyDataSetChanged();
    }


    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (mPresenter != null)
            mPresenter.getAllOrderList(0, false);
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getAllOrderList(start, false);
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
