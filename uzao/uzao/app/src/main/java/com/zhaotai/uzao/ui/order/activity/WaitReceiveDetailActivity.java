package com.zhaotai.uzao.ui.order.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.EventBean.RefreshOrderEvent;
import com.zhaotai.uzao.bean.OrderDetailBean;
import com.zhaotai.uzao.bean.OrderDetailMultiBean;
import com.zhaotai.uzao.constants.OrderStatus;
import com.zhaotai.uzao.ui.order.contract.OrderDetailContract;
import com.zhaotai.uzao.ui.order.presenter.OrderDetailPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Time: 2017/6/9
 * Created by LiYou
 * Description : 待收货详情页面
 */

public class WaitReceiveDetailActivity extends BaseOrderDetailActivity implements OrderDetailContract.View
        , BaseQuickAdapter.OnItemChildClickListener {

    private TextView mCountDown;

    public static void launch(Context context, String orderId, int position) {
        Intent intent = new Intent(context, WaitReceiveDetailActivity.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("orderReceivePosition", position);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();
//        headView.findViewById(R.id.ll_order_detail_state).setVisibility(View.VISIBLE);
//        //倒计时
//        mCountDown = (TextView) headView.findViewById(R.id.tv_order_detail_count_down);
//        TextView mState = (TextView) headView.findViewById(R.id.order_detail_state);
//        mState.setText("待收货");
        mPresenter = new OrderDetailPresenter(this, this);
        EventBus.getDefault().register(this);
        mAdapter.setOnItemChildClickListener(this);
    }

    @Override
    protected void initData() {
        String orderId = getIntent().getStringExtra("orderId");
        if (mPresenter != null)
            mPresenter.getOrderDetail(orderId);
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    /**
     * 显示详情
     */
    @Override
    public void showDetail(OrderDetailBean orderDetailBean) {
        initDetail(orderDetailBean);
        orderDetailBean.orderModel.orderStatus = OrderStatus.WAIT_RECEIVE;
        mAdapter.setNewData(mPresenter.constructMultiData(orderDetailBean.orderModel));
        //显示倒计时
//        ifConfirmGoodsTime(orderDetailBean);
    }

    /**
     * 判断是否显示自动收货倒计时
     */
//    private void ifConfirmGoodsTime (OrderDetailBean orderDetailBean){
//        if(!StringUtil.isEmpty(orderDetailBean.orderModel.remainingTime)){
//            mCountDown.setVisibility(View.VISIBLE);
//            String remainingTime = orderDetailBean.orderModel.remainingTime;
//            int milliSeconds = Integer.parseInt(remainingTime);
//            String time =  TimeUtils.millis2FitTimeSpan(milliSeconds,4);
//            mCountDown.setText(getString(R.string.count_time_confirm_goods,time));
//            orderPresenter.countMinDown(Integer.parseInt(remainingTime));
//        }
//    }

    /**
     * 更新确认收货时间
     */
//    @Override
//    public void updateTime(String time) {
//        //mCountDown.setText(getString(R.string.count_time_confirm_goods,time));
//    }
    @Override
    public void finishView() {
        finish();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        final OrderDetailMultiBean data = (OrderDetailMultiBean) adapter.getItem(position);
        if (data == null) return;
        switch (view.getId()) {
            case R.id.tv_order_bottom_left:
                //申请售后
                ApplyAfterSalesActivity.launch(mContext, data.packageOrderNo, data.packageNum);
                break;
            case R.id.tv_order_bottom_middle:
                //查看物流
                LogisticsActivity.launch(mContext, LogisticsActivity.TYPE_ORDER_ID, orderNo);
                break;
            case R.id.tv_order_bottom_right:
                switch (data.state) {
                    case OrderStatus.WAIT_RECEIVE://待收货
                        AlertDialog alert = new AlertDialog.Builder(mContext)
                                .setMessage("您确定已收到商品?")
                                .setNegativeButton("取消", null)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mPresenter.confirmReceiveGoods(data.packageOrderNo);
                                    }
                                }).create();
                        alert.show();
                        break;
                    case OrderStatus.FINISHED://待评价
                        if ("N".equals(data.isCommend)) {
                            //去评价
                            CommentOrderActivity.launch(this, orderNo, data.packageOrderNo);
                        } else {
                            CommentDetailActivity.launch(this, data.packageOrderNo);
                        }
                        break;
                }
                break;
        }

//        if ("N".equals(data.status)) {
//            //申请售后
//            ApplyAfterSalesActivity.launch(WaitReceiveDetailActivity.this, data, position);
//        } else {
//            //售后处理中 跳售后详情
//            ApplyAfterSalesDetailActivity.launch(mContext, data.sequenceNBR);
//        }
    }

    /**
     * 刷新订单
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshOrderEvent refreshOrderEvent) {
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
