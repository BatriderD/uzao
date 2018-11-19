package com.zhaotai.uzao.ui.order.activity;

import android.content.Context;
import android.content.Intent;

import com.zhaotai.uzao.bean.OrderDetailBean;
import com.zhaotai.uzao.ui.order.contract.OrderDetailContract;
import com.zhaotai.uzao.ui.order.presenter.OrderDetailPresenter;

/**
 * Time: 2017/6/9
 * Created by LiYou
 * Description : 待发货订单详情页面
 */

public class WaitDeliveryDetailActivity extends BaseOrderDetailActivity implements OrderDetailContract.View {


    public static void launch(Context context, String orderId) {
        Intent intent = new Intent(context, WaitDeliveryDetailActivity.class);
        intent.putExtra("orderId", orderId);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();
        mPresenter = new OrderDetailPresenter(this, this);
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
        mAdapter.addData(mPresenter.constructMultiData(orderDetailBean.orderModel));
    }

    @Override
    public void finishView() {

    }

}
