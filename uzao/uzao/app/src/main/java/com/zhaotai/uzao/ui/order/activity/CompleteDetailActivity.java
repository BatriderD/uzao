package com.zhaotai.uzao.ui.order.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.EventBean.RefreshOrderEvent;
import com.zhaotai.uzao.bean.OrderDetailBean;
import com.zhaotai.uzao.bean.OrderDetailMultiBean;
import com.zhaotai.uzao.ui.order.contract.OrderDetailContract;
import com.zhaotai.uzao.ui.order.presenter.OrderDetailPresenter;
import com.zhaotai.uzao.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Time: 2017/6/9
 * Created by LiYou
 * Description : 完成详情页面
 */

public class CompleteDetailActivity extends BaseOrderDetailActivity implements OrderDetailContract.View, BaseQuickAdapter.OnItemChildClickListener {

    private static final String EXTRA_KEY_ORDER_ID = "extra_key_order_id";
    private static final String EXTRA_KEY_ADAPTER_POSITION = "extra_key_adapter_position";

    /**
     * @param orderId  orderId
     * @param position adapter 位置
     */
    public static void launch(Context context, String orderId, int position) {
        Intent intent = new Intent(context, CompleteDetailActivity.class);
        intent.putExtra(EXTRA_KEY_ORDER_ID, orderId);
        intent.putExtra(EXTRA_KEY_ADAPTER_POSITION, position);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();
        //注册EventBus
        EventBus.getDefault().register(this);
        mPresenter = new OrderDetailPresenter(this, this);
        mAdapter.setOnItemChildClickListener(this);
    }

    @Override
    protected void initData() {
        String orderId = getIntent().getStringExtra(EXTRA_KEY_ORDER_ID);
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
        mAdapter.setNewData(mPresenter.constructMultiData(orderDetailBean.orderModel));
    }

    @Override
    public void finishView() {
        finish();
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (StringUtil.isEmpty(orderNo)) {
            return;
        }
        OrderDetailMultiBean multiBean = (OrderDetailMultiBean) adapter.getItem(position);
        if (multiBean == null) return;
        switch (view.getId()) {
            case R.id.tv_order_bottom_right:
                if ("Y".equals(multiBean.isCommend)) {
                    CommentDetailActivity.launch(mContext, multiBean.packageOrderNo);
                } else {
                    CommentOrderActivity.launch(mContext, orderNo, multiBean.packageOrderNo);
                }
                break;
            case R.id.tv_order_bottom_middle:
                //查看物流
                LogisticsActivity.launch(mContext, LogisticsActivity.TYPE_ORDER_ID, orderNo);
                break;
        }
    }

    /**
     * 评论成功 改变评论按钮状态
     *
     * @param event 评论成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(RefreshOrderEvent event) {
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
