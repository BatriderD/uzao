package com.zhaotai.uzao.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.OrderBean;
import com.zhaotai.uzao.constants.OrderStatus;
import com.zhaotai.uzao.ui.order.activity.CloseDetailActivity;
import com.zhaotai.uzao.ui.order.activity.CompleteDetailActivity;
import com.zhaotai.uzao.ui.order.activity.WaitApproveDetailActivity;
import com.zhaotai.uzao.ui.order.activity.WaitDeliveryDetailActivity;
import com.zhaotai.uzao.ui.order.activity.WaitPayDetailActivity;
import com.zhaotai.uzao.ui.order.activity.WaitReceiveDetailActivity;
import com.zhaotai.uzao.widget.CustomLinearLayoutManager;

/**
 * Time: 2017/6/6
 * Created by LiYou
 * Description : 订单列表
 */

public class OrderPayAdapter extends BaseRecyclerAdapter<OrderBean, BaseViewHolder> {

    private Gson gson;

    public OrderPayAdapter() {
        super(R.layout.item_order_pay_wwtt);
        this.gson = new Gson();
    }

    @Override
    protected void convert(final BaseViewHolder helper, final OrderBean item) {
        helper.setText(R.id.tv_order_id, mContext.getString(R.string.order_num, item.orderNo))
                .setText(R.id.tv_order_status, orderStatus(item.orderStatus))
                .setText(R.id.tv_order_real_pay, mContext.getString(R.string.account, item.totalAmountY));
        bottomButton(helper, item.orderStatus);


        RecyclerView recyclerView = helper.getView(R.id.recycler_order_goods);
        CustomLinearLayoutManager linearLayoutManager = new CustomLinearLayoutManager(mContext);
        linearLayoutManager.setScrollEnabled(false);
        recyclerView.setLayoutManager(linearLayoutManager);

        if (item.orderType.equals(OrderStatus.ORDER_TYPE_MATERIAL)) {
            //素材
            OrderChildMaterialAdapter orderChildAdapter = new OrderChildMaterialAdapter(item.materialOrderDetailModels);
            recyclerView.setAdapter(orderChildAdapter);
        } else {
            //商品
            OrderChildAdapter orderChildAdapter = new OrderChildAdapter(item.orderPackage.unDelivery, gson);
            recyclerView.setAdapter(orderChildAdapter);
            orderChildAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    itemClick(item.orderStatus, item.orderNo, helper.getLayoutPosition());
                }
            });
        }

        //判断是否选中
        helper.addOnClickListener(R.id.tv_pay_order_choose);
        if (item.isSelectd) {
            helper.setImageResource(R.id.tv_pay_order_choose, R.drawable.icon_circle_selected);
        } else {
            helper.setImageResource(R.id.tv_pay_order_choose, R.drawable.icon_circle_unselected);
        }
    }

    /**
     * 订单状态
     */
    private String orderStatus(String orderStatus) {
        switch (orderStatus) {
            case OrderStatus.WAIT_PAY://待付款
                return "待付款";
            case OrderStatus.WAIT_APPROVE://待审核
                return "待审核";
            case OrderStatus.WAIT_HANDLE: //待发货
            case OrderStatus.WAIT_DELIVERY:
                return "待发货";
            case OrderStatus.WAIT_RECEIVE: //待收货
                return "待收货";
            case OrderStatus.FINISHED: //已完成
                return "已完成";
            case OrderStatus.CANCELED: //已关闭
            case OrderStatus.UN_APPROVE:
            case OrderStatus.CLOSED:
            case OrderStatus.WAIT_REFUND:
                return "已关闭";
            default:
                return "";
        }
    }

    /**
     * 根据订单状态 显示 底部button
     */
    private void bottomButton(BaseViewHolder helper, String orderStatus) {
        switch (orderStatus) {
            case OrderStatus.WAIT_PAY://待付款
                helper.setText(R.id.tv_order_bottom_left_button, "取消订单")
                        .setText(R.id.tv_order_bottom_right_button, "去付款")
                        .setVisible(R.id.tv_order_bottom_left_button, true)
                        .setVisible(R.id.tv_order_bottom_right_button, true)
                        .setBackgroundRes(R.id.tv_order_bottom_left_button, R.drawable.shape_order_btn)
                        .setBackgroundRes(R.id.tv_order_bottom_right_button, R.drawable.shape_order_btn)
                        .addOnClickListener(R.id.tv_order_bottom_left_button)
                        .addOnClickListener(R.id.tv_order_bottom_right_button);
                break;
            case OrderStatus.WAIT_APPROVE://待审核
                helper.setVisible(R.id.tv_order_bottom_left_button, false)
                        .setVisible(R.id.tv_order_bottom_right_button, false);
                break;
            case OrderStatus.WAIT_HANDLE: //待发货
            case OrderStatus.WAIT_DELIVERY:
                helper.setVisible(R.id.tv_order_bottom_left_button, false)
                        .setVisible(R.id.tv_order_bottom_right_button, false);
                break;
            case OrderStatus.WAIT_RECEIVE: //待收货
                helper.setText(R.id.tv_order_bottom_left_button, "查看物流")
                        .setVisible(R.id.tv_order_bottom_left_button, true)
                        .setText(R.id.tv_order_bottom_right_button, "确认收货")
                        .setVisible(R.id.tv_order_bottom_right_button, true)
                        .setBackgroundRes(R.id.tv_order_bottom_left_button, R.drawable.shape_order_btn)
                        .setBackgroundRes(R.id.tv_order_bottom_right_button, R.drawable.shape_order_btn)
                        .addOnClickListener(R.id.tv_order_bottom_left_button)
                        .addOnClickListener(R.id.tv_order_bottom_right_button);
                break;
            case OrderStatus.FINISHED: //已完成
                helper.setText(R.id.tv_order_bottom_right_button, "查看物流")
                        .addOnClickListener(R.id.tv_order_bottom_right_button)
                        .setVisible(R.id.tv_order_bottom_right_button, true)
                        .setBackgroundRes(R.id.tv_order_bottom_right_button, R.drawable.shape_order_btn);
                helper.getView(R.id.tv_order_bottom_left_button).setVisibility(View.INVISIBLE);
                break;
            case OrderStatus.CANCELED: //已关闭
            case OrderStatus.UN_APPROVE:
            case OrderStatus.CLOSED:
            case OrderStatus.WAIT_REFUND:
                helper.setVisible(R.id.tv_order_bottom_right_button, true)
                        .setText(R.id.tv_order_bottom_right_button, "删除订单")
                        .setBackgroundRes(R.id.tv_order_bottom_right_button, R.drawable.shape_order_btn)
                        .addOnClickListener(R.id.tv_order_bottom_right_button);
                helper.getView(R.id.tv_order_bottom_left_button).setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    public void itemClick(String orderStatus, String orderNo, int position) {
        //跳转详情
        switch (orderStatus) {
            case OrderStatus.WAIT_PAY://待付款
                WaitPayDetailActivity.launch(mContext, orderNo, position, false);
                break;
            case OrderStatus.WAIT_APPROVE://待审核
                WaitApproveDetailActivity.launch(mContext, orderNo);
                break;
            case OrderStatus.WAIT_HANDLE://待发货
            case OrderStatus.WAIT_DELIVERY://待发货
                WaitDeliveryDetailActivity.launch(mContext, orderNo);
                break;
            case OrderStatus.WAIT_RECEIVE://待收货
                WaitReceiveDetailActivity.launch(mContext, orderNo, position);
                break;
            case OrderStatus.FINISHED://已完成
                CompleteDetailActivity.launch(mContext, orderNo, position);
                break;
            case OrderStatus.CANCELED: //已关闭
            case OrderStatus.UN_APPROVE:
            case OrderStatus.CLOSED:
                CloseDetailActivity.launch(mContext, orderNo);
                break;
        }
    }


}
