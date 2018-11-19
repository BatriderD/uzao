package com.zhaotai.uzao.ui.order.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.EventBean.RemoveOrderEvent;
import com.zhaotai.uzao.bean.OrderDetailBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.order.contract.OrderDetailContract;
import com.zhaotai.uzao.ui.order.presenter.OrderDetailPresenter;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Time: 2017/6/9
 * Created by LiYou
 * Description : 已关闭详情页面
 */

public class CloseDetailActivity extends BaseOrderDetailActivity implements OrderDetailContract.View,
        View.OnClickListener {

    public static void launch(Context context, String orderId) {
        Intent intent = new Intent(context, CloseDetailActivity.class);
        intent.putExtra("orderId", orderId);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();
        mRlBottom.setVisibility(View.VISIBLE);
        findViewById(R.id.tv_order_detail_left_btn).setVisibility(View.GONE);
        TextView mRight = (TextView) findViewById(R.id.tv_order_detail_right_btn);
        mRight.setBackground(mContext.getDrawable(R.drawable.shape_order_btn));
        mRight.setText("删除订单");
        mRight.setOnClickListener(this);
        LinearLayout headerLayout = mAdapter.getHeaderLayout();
        headerLayout.findViewById(R.id.ll_order_detail_state).setVisibility(View.VISIBLE);
        TextView mState = (TextView) headerLayout.findViewById(R.id.order_detail_state);
        mState.setText("已关闭");
        //注册EventBus
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


    @Override
    public void onClick(View v) {
        if (StringUtil.isEmpty(orderNo)) return;
        switch (v.getId()) {
            case R.id.tv_order_detail_right_btn:
                AlertDialog alert = new AlertDialog.Builder(mContext)
                        .setMessage("您确定要删除订单吗?")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteOrder(orderNo);
                            }
                        }).create();
                alert.show();
                break;
        }
    }

    /**
     * 删除订单
     *
     * @param orderId 订单id
     */
    public void deleteOrder(String orderId) {
        Api.getDefault().deleteOrder(orderId)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String s) {
                        EventBus.getDefault().post(new RemoveOrderEvent(orderNo));
                        finish();
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("删除订单失败");
                    }
                });
    }

}
