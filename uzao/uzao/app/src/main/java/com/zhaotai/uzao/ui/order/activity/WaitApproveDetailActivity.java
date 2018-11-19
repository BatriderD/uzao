package com.zhaotai.uzao.ui.order.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.OrderDetailBean;
import com.zhaotai.uzao.ui.order.contract.OrderDetailContract;
import com.zhaotai.uzao.ui.order.presenter.OrderDetailPresenter;

import butterknife.OnClick;

/**
 * Time: 2017/6/9
 * Created by LiYou
 * Description : 待审核订单详情页面
 */

public class WaitApproveDetailActivity extends BaseOrderDetailActivity implements OrderDetailContract.View {

    public static void launch(Context context, String orderId) {
        Intent intent = new Intent(context, WaitApproveDetailActivity.class);
        intent.putExtra("orderId", orderId);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();
        LinearLayout headerLayout = mAdapter.getHeaderLayout();
        headerLayout.findViewById(R.id.ll_order_detail_state).setVisibility(View.VISIBLE);
        TextView mState = (TextView) headerLayout.findViewById(R.id.order_detail_state);
        mState.setText("待审核");
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
        finish();
    }

    @OnClick(R.id.tv_order_detail_right_btn)
    public void onClickCancleOrder() {
        AlertDialog alert = new AlertDialog.Builder(this)
                .setMessage("您确定要取消订单吗?")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        alert.show();
    }
}
