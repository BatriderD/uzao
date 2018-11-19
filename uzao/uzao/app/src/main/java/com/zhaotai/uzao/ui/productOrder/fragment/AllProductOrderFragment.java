package com.zhaotai.uzao.ui.productOrder.fragment;

import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.bean.EventBean.produceOrder.AllProduceOrderReceiveRemitEvent;
import com.zhaotai.uzao.bean.EventBean.produceOrder.AllSampleProduceReceiveEvent;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ProductOrderBean;
import com.zhaotai.uzao.constants.OrderStatus;
import com.zhaotai.uzao.ui.order.activity.LogisticsActivity;
import com.zhaotai.uzao.ui.productOrder.activity.ProduceAddressSelectActivity;
import com.zhaotai.uzao.ui.productOrder.activity.ProduceOrderClosedActivity;
import com.zhaotai.uzao.ui.productOrder.activity.ProduceSampleDeliverActivity;
import com.zhaotai.uzao.ui.productOrder.activity.ProduceSampleReceiveActivity;
import com.zhaotai.uzao.ui.productOrder.activity.ProduceWaitDeliverActivity;
import com.zhaotai.uzao.ui.productOrder.activity.ProduceWaitLastPayActivity;
import com.zhaotai.uzao.ui.productOrder.activity.ProduceWaitReceiveActivity;
import com.zhaotai.uzao.ui.productOrder.activity.ProduceWaitReceiveLastPayActivity;
import com.zhaotai.uzao.ui.productOrder.activity.ProductOrderSelectPayWayActivity;
import com.zhaotai.uzao.ui.productOrder.activity.SampleFinishedActivity;
import com.zhaotai.uzao.ui.productOrder.activity.SampleWaitPayConfirmAndDeliverActivity;
import com.zhaotai.uzao.ui.productOrder.activity.SampleWaitReceiveActivity;
import com.zhaotai.uzao.ui.productOrder.activity.SamplingProduceFinishedActivity;
import com.zhaotai.uzao.ui.productOrder.activity.SamplingProduceProduceSamplingDeliverActivity;
import com.zhaotai.uzao.ui.productOrder.activity.SamplingProduceProduceSamplingReceiveActivity;
import com.zhaotai.uzao.ui.productOrder.activity.SamplingProduceSampleDeliverActivity;
import com.zhaotai.uzao.ui.productOrder.activity.SamplingProduceSampleReceiveActivity;
import com.zhaotai.uzao.ui.productOrder.activity.SamplingProduceWaitDeliverActivity;
import com.zhaotai.uzao.ui.productOrder.activity.SamplingProduceWaitLastPayActivity;
import com.zhaotai.uzao.ui.productOrder.activity.SamplingProduceWaitReceiveActivity;
import com.zhaotai.uzao.ui.productOrder.activity.SamplingProduceWaitReceiveLastPayActivity;
import com.zhaotai.uzao.ui.productOrder.activity.WaitFirstPayConfirmActivity;
import com.zhaotai.uzao.ui.productOrder.activity.WaitFirstPayProductOrderActivity;
import com.zhaotai.uzao.ui.productOrder.adapter.ProductOrderAdapter;
import com.zhaotai.uzao.ui.productOrder.contract.ProductOrderContract;
import com.zhaotai.uzao.ui.productOrder.presenter.AllProductOrderPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Time: 2017/6/6
 * Created by LiYou
 * Description : 全部订单
 */

public class AllProductOrderFragment extends BaseFragment implements ProductOrderContract.AllView, BaseQuickAdapter.RequestLoadMoreListener
        , BaseQuickAdapter.OnItemChildClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipe)
    SwipeRefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private ProductOrderAdapter mAdapter;
    private AllProductOrderPresenter mPresenter;
    //存放订单数据
    private PageInfo<ProductOrderBean> data = new PageInfo<>();


    public static AllProductOrderFragment newInstance() {
        return new AllProductOrderFragment();
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
        mAdapter = new ProductOrderAdapter();
        mAdapter.setOnLoadMoreListener(this, mRecycler);
        //条目点击
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setEmptyView(R.layout.vw_order_empty);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void initPresenter() {
        mPresenter = new AllProductOrderPresenter(_mActivity, this);
    }

    @Override
    public void initData() {
        showLoading();
        mPresenter.getAllProductOrderList(0, true);
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
                    case OrderStatus.WAIT_PRODUCE_RECEIVE:
                    case OrderStatus.WAIT_SAMPLING_RECEIVE:
                    case OrderStatus.WAIT_RECEIVE:
                    case OrderStatus.FINISHED:
                        //物流信息
                        LogisticsActivity.launch(_mActivity, LogisticsActivity.TYPE_PRODUCE_ID, info.orderNo);
                        break;
                    case OrderStatus.WAIT_PAY://待付款
                    case OrderStatus.WAIT_FIRST_PAY://待付首款
                        WaitFirstPayProductOrderActivity.launch(_mActivity, info.orderNo, info.orderType, position, true);
                        break;
                    case OrderStatus.WAIT_PAY_CONFIRM:
                        SampleWaitPayConfirmAndDeliverActivity.launch(_mActivity, info.orderNo, "待收款");
                        break;
                    case OrderStatus.WAIT_FIRST_PAY_CONFIRM://待收首款
                        WaitFirstPayConfirmActivity.launch(_mActivity, info.orderNo, info.orderType);
                        break;
                    case OrderStatus.WAIT_SAMPLING_DELIVER://样品生产中
                        SamplingProduceSampleDeliverActivity.launch(_mActivity, info.orderNo);
                        break;
                    case OrderStatus.WAIT_PRODUCE_DELIVERY://大货样生产中
                        switch (info.orderType) {
                            case OrderStatus.PRODUCE:
                                ProduceSampleDeliverActivity.launch(_mActivity, info.orderNo);
                                break;
                            case OrderStatus.SAMPLING_PRODUCE:
                                SamplingProduceProduceSamplingDeliverActivity.launch(_mActivity, info.orderNo);
                                break;
                        }
                        break;
                    case OrderStatus.WAIT_LAST_PAY://待付尾款
                        switch (info.orderType) {
                            case OrderStatus.SAMPLING_PRODUCE:
                                SamplingProduceWaitLastPayActivity.launch(_mActivity, info.orderNo, position, true);
                                break;
                            case OrderStatus.PRODUCE:
                                ProduceWaitLastPayActivity.launch(_mActivity, info.orderNo, position, true);
                                break;
                        }
                        break;
                    case OrderStatus.WAIT_LAST_PAY_CONFIRM://待收尾款
                        switch (info.orderType) {
                            case OrderStatus.SAMPLING_PRODUCE:
                                SamplingProduceWaitReceiveLastPayActivity.launch(_mActivity, info.orderNo);
                                break;
                            case OrderStatus.PRODUCE:
                                ProduceWaitReceiveLastPayActivity.launch(_mActivity, info.orderNo);
                                break;
                        }
                        break;
                    case OrderStatus.WAIT_DELIVERY://待发货
                        switch (info.orderType) {
                            case OrderStatus.SAMPLING_PRODUCE:
                                SamplingProduceWaitDeliverActivity.launch(_mActivity, info.orderNo);
                                break;
                            case OrderStatus.PRODUCE:
                                ProduceWaitDeliverActivity.launch(_mActivity, info.orderNo);
                                break;
                            case OrderStatus.SAMPLING:
                                SampleWaitPayConfirmAndDeliverActivity.launch(_mActivity, info.orderNo, "待发货");
                                break;
                        }
                        break;
                    case OrderStatus.CLOSED://关闭
                        ProduceOrderClosedActivity.launch(_mActivity, info.orderNo, info.orderType);
                        break;
                }
                break;
            case R.id.tv_product_order_bottom_second_button:
                switch (info.orderType) {
                    case OrderStatus.SAMPLING_PRODUCE://打样加大货
                        switch (info.orderStatus) {
                            case OrderStatus.WAIT_SAMPLING_RECEIVE://--打样确认
                                SamplingProduceSampleReceiveActivity.launch(_mActivity, info.orderNo, position, true);
                                break;
                            case OrderStatus.WAIT_PRODUCE_RECEIVE://大货样确认
                                SamplingProduceProduceSamplingReceiveActivity.launch(_mActivity, info.orderNo, position, true);
                                break;
                            case OrderStatus.WAIT_RECEIVE://待收货
                                SamplingProduceWaitReceiveActivity.launch(_mActivity, info.orderNo, position, true);
                                break;
                            case OrderStatus.FINISHED://已完成
                                SamplingProduceFinishedActivity.launch(_mActivity, info.orderNo, info.orderType);
                                break;
                        }
                        break;
                    case OrderStatus.PRODUCE://大货
                        switch (info.orderStatus) {
                            case OrderStatus.WAIT_PRODUCE_RECEIVE://大货样确认
                                ProduceSampleReceiveActivity.launch(_mActivity, info.orderNo, position, true);
                                break;
                            case OrderStatus.WAIT_RECEIVE://待收货
                                ProduceWaitReceiveActivity.launch(_mActivity, info.orderNo, position, true);
                                break;
                            case OrderStatus.FINISHED://已完成
                                SamplingProduceFinishedActivity.launch(_mActivity, info.orderNo, info.orderType);
                                break;
                        }
                        break;
                    case OrderStatus.SAMPLING://打样
                        switch (info.orderStatus) {
                            case OrderStatus.WAIT_RECEIVE:
                                SampleWaitReceiveActivity.launch(_mActivity, info.orderNo, position, true);
                                break;
                            case OrderStatus.FINISHED:
                                SampleFinishedActivity.launch(_mActivity, info.orderNo);
                                break;
                        }
                        break;
                }
                break;
            case R.id.tv_product_order_bottom_third_button:
                switch (info.orderStatus) {
                    case OrderStatus.WAIT_PAY:
                        ProductOrderSelectPayWayActivity.launch(_mActivity, info.orderNo, "pay");
                        break;
                    case OrderStatus.WAIT_FIRST_PAY:
                        ProductOrderSelectPayWayActivity.launch(_mActivity, info.orderNo, "firstPay");
                        break;
                    case OrderStatus.WAIT_SAMPLING_RECEIVE: //样品待确认
                        confirmSampleGoods(info.orderNo, position, info.orderStatus, "sampling");
                        break;
                    case OrderStatus.WAIT_PRODUCE_RECEIVE://大货样品待确认
                        confirmSampleGoods(info.orderNo, position, info.orderStatus, "produce");
                        break;
                    case OrderStatus.WAIT_LAST_PAY://待付尾款
                        if ("collectFreight".equals(info.freightType)) {
                            ProduceAddressSelectActivity.launch(_mActivity, info.orderNo, info.count);
                        } else {
                            ProductOrderSelectPayWayActivity.launch(_mActivity, info.orderNo, "lastPay");
                        }
                        break;
                    case OrderStatus.WAIT_RECEIVE:
                        switch (info.orderType) {
                            case OrderStatus.SAMPLING:
                                confirmGoods(info.orderNo, position, info.orderStatus);
                                break;
                            case OrderStatus.SAMPLING_PRODUCE:
                                confirmGoods(info.orderNo, position, info.orderStatus);
                                break;
                            case OrderStatus.PRODUCE:
                                confirmGoods(info.orderNo, position, info.orderStatus);
                                break;
                        }
                        break;
                }
                break;
        }
    }


    /**
     * 确认收货 -- 样品的确认收货
     *
     * @param type "produce"大货样 "sample" 打样
     */
    private void confirmSampleGoods(final String orderNo, final int position, final String orderStatus, final String type) {
        AlertDialog alert = new AlertDialog.Builder(_mActivity)
                .setMessage("您确定已收到商品?")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.receiveSampleProduct(orderNo, type, position, true, orderStatus);
                    }
                }).create();
        alert.show();
    }

    /**
     * 确认收货 -- 打样订单收货、大货和大货打样批量生产收货
     */
    private void confirmGoods(final String orderNo, final int position, final String orderStatus) {
        AlertDialog alert = new AlertDialog.Builder(_mActivity)
                .setMessage("您确定已收到商品?")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.receiveProduct(orderNo, position, true, orderStatus);
                    }
                }).create();
        alert.show();
    }

    /**
     * 确认样品
     *
     * @param info 确认样品
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AllSampleProduceReceiveEvent info) {
        switch (info.orderStatus) {
            case OrderStatus.WAIT_PRODUCE_RECEIVE://大货样确认
                this.data.list.get(info.position).orderStatus = OrderStatus.WAIT_LAST_PAY;
                break;
            case OrderStatus.WAIT_SAMPLING_RECEIVE://打样确认
                this.data.list.get(info.position).orderStatus = OrderStatus.WAIT_PRODUCE_DELIVERY;
                break;
            case OrderStatus.WAIT_RECEIVE:
                this.data.list.get(info.position).orderStatus = OrderStatus.FINISHED;
                break;
        }
        mAdapter.notifyItemChanged(info.position);
    }

    /**
     * 确认汇款
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AllProduceOrderReceiveRemitEvent info) {
        switch (info.orderStatus) {
            case OrderStatus.WAIT_PAY://待付款
                this.data.list.get(info.position).orderStatus = OrderStatus.WAIT_PAY_CONFIRM;
                break;
            case OrderStatus.WAIT_FIRST_PAY://待付首款
                this.data.list.get(info.position).orderStatus = OrderStatus.WAIT_FIRST_PAY_CONFIRM;
                break;
            case OrderStatus.WAIT_LAST_PAY:
                this.data.list.get(info.position).orderStatus = OrderStatus.WAIT_LAST_PAY_CONFIRM;
                break;
        }
        mAdapter.notifyItemChanged(info.position);
    }


    /**
     * 显示订单列表
     */
    @Override
    public void showAllProductOrderList(PageInfo<ProductOrderBean> data) {
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
        mPresenter.getAllProductOrderList(0, false);
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getAllProductOrderList(start, false);
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
