package com.zhaotai.uzao.adapter;


import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.OrderBean;
import com.zhaotai.uzao.bean.OrderGoodsDetailBean;
import com.zhaotai.uzao.bean.OrderMaterialBean;
import com.zhaotai.uzao.constants.OrderStatus;
import com.zhaotai.uzao.ui.order.activity.CloseDetailActivity;
import com.zhaotai.uzao.ui.order.activity.CompleteDetailActivity;
import com.zhaotai.uzao.ui.order.activity.WaitApproveDetailActivity;
import com.zhaotai.uzao.ui.order.activity.WaitDeliveryDetailActivity;
import com.zhaotai.uzao.ui.order.activity.WaitPayDetailActivity;
import com.zhaotai.uzao.ui.order.activity.WaitReceiveDetailActivity;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.ImageSizeUtil;
import com.zhaotai.uzao.utils.StringUtil;

/**
 * Time: 2017/6/6
 * Created by LiYou
 * Description : 订单列表
 */

public class OrderAdapter extends BaseRecyclerAdapter<OrderBean, BaseViewHolder> {

    private Gson gson;

    public OrderAdapter() {
        super(R.layout.item_order_section);
        this.gson = new Gson();
    }

    @Override
    protected void convert(final BaseViewHolder helper, final OrderBean item) {
        String payPrice;
        if (StringUtil.isEmpty(item.payAmountY)) {
            payPrice = item.totalAmountY;
        } else {
            payPrice = item.payAmountY;
        }
        helper.setText(R.id.tv_order_id, mContext.getString(R.string.order_num, item.orderNo))
                .setText(R.id.tv_order_status, orderStatus(item.orderStatus, item.isComment, item.orderType))
                .setText(R.id.tv_order_real_pay, mContext.getString(R.string.account, payPrice));
        bottomButton(helper, item.orderStatus, item.isComment, item.orderType);

        LinearLayout view = helper.getView(R.id.ll_pay_confirm_item);

        //动态添加商品列表
        view.removeAllViews();

        if (item.orderDetailModels != null && item.orderDetailModels.size() > 0) {
            for (int i = 0; i < item.orderDetailModels.size(); i++) {
                View itemView = View.inflate(mContext, R.layout.item_order_goods, null);
                View divider = itemView.findViewById(R.id.tv_order_bottom_divider);
                ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_order_goods_image);
                TextView title = (TextView) itemView.findViewById(R.id.tv_order_goods_title);
                TextView type = (TextView) itemView.findViewById(R.id.tv_order_goods_properties);
                TextView num = (TextView) itemView.findViewById(R.id.tv_order_goods_num);
                TextView price = (TextView) itemView.findViewById(R.id.tv_order_goods_price);
                ImageView activity = (ImageView) itemView.findViewById(R.id.tv_order_activity);

                OrderGoodsDetailBean goods = gson.fromJson(item.orderDetailModels.get(i).orderDetail, OrderGoodsDetailBean.class);
                GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(goods.pic), imageView);
                title.setText(goods.name);
                type.setText(goods.category);
                num.setText(mContext.getString(R.string.goods_number, item.orderDetailModels.get(i).count));
                price.setText(mContext.getString(R.string.account, item.orderDetailModels.get(i).unitPriceY));
                //活动图片
                if (!StringUtil.isEmpty(item.orderDetailModels.get(i).activityIcon)) {
                    activity.setVisibility(View.VISIBLE);
                    GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(item.orderDetailModels.get(i).activityIcon), activity);
                } else {
                    activity.setVisibility(View.GONE);
                }

//                if (i == item.orderDetailModels.size() - 1) {
//                    divider.setVisibility(View.GONE);
//                } else {
//                    divider.setVisibility(View.VISIBLE);
//                }
                view.addView(itemView);
            }
        }

        if (item.materialOrderDetailModels != null && item.materialOrderDetailModels.size() > 0) {
            for (int i = 0; i < item.materialOrderDetailModels.size(); i++) {
                OrderMaterialBean material = item.materialOrderDetailModels.get(i);
                View itemView = View.inflate(mContext, R.layout.item_order_goods, null);
                ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_order_goods_image);
                TextView title = (TextView) itemView.findViewById(R.id.tv_order_goods_title);
                TextView type = (TextView) itemView.findViewById(R.id.tv_order_goods_properties);
                TextView price = (TextView) itemView.findViewById(R.id.tv_order_goods_price);
                if (material.materialInfo != null) {
                    GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + material.materialInfo.thumbnail, imageView);
                    title.setText(material.materialInfo.sourceMaterialName);
                    price.setText(mContext.getString(R.string.account, material.materialInfo.priceY));
                }
                if (material.designerInfo != null) {
                    type.setText(material.designerInfo.nickName);
                }
                view.addView(itemView);
            }
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!item.orderType.equals(OrderStatus.ORDER_TYPE_MATERIAL)) {
                    itemClick(item.orderStatus, item.orderNo, helper.getLayoutPosition());
                }
            }
        });

    }

    /**
     * 订单状态
     */
    private String orderStatus(String orderStatus, String isCommend, String orderType) {
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
                //素材的完成状态只有一种
                if (OrderStatus.ORDER_TYPE_MATERIAL.equals(orderType)) {
                    return "已完成";
                }
                //商品的完成状态 还包括待评价商品
                if (isCommend.equals("N")) {
                    return "待评价";
                }
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
    private void bottomButton(BaseViewHolder helper, String orderStatus, String isComment, String orderType) {
        switch (orderStatus) {
            case OrderStatus.WAIT_PAY://待付款
                helper.setText(R.id.tv_order_bottom_left_button, "取消订单")
                        .setText(R.id.tv_order_bottom_right_button, "去付款")
                        .setVisible(R.id.tv_order_bottom_left_button, true)
                        .setVisible(R.id.tv_order_bottom_right_button, true)
                        .setBackgroundRes(R.id.tv_order_bottom_left_button, R.drawable.shape_order_btn)
                        .setBackgroundRes(R.id.tv_order_bottom_right_button, R.drawable.shape_order_btn_yellow)
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
                        .setBackgroundRes(R.id.tv_order_bottom_right_button, R.drawable.shape_order_btn_yellow)
                        .addOnClickListener(R.id.tv_order_bottom_left_button)
                        .addOnClickListener(R.id.tv_order_bottom_right_button);
                break;
            case OrderStatus.FINISHED: //已完成
                //素材订单
                if (OrderStatus.ORDER_TYPE_MATERIAL.equals(orderType)) {
                    helper.setVisible(R.id.tv_order_bottom_left_button, false)
                            .setVisible(R.id.tv_order_bottom_right_button, false);
                } else {
                    //商品订单
                    helper.setText(R.id.tv_order_bottom_left_button, "查看物流")
                            .addOnClickListener(R.id.tv_order_bottom_right_button)
                            .addOnClickListener(R.id.tv_order_bottom_left_button)
                            .setVisible(R.id.tv_order_bottom_right_button, true)
                            .setVisible(R.id.tv_order_bottom_left_button, true)
                            .setBackgroundRes(R.id.tv_order_bottom_left_button, R.drawable.shape_order_btn);
                    if ("N".equals(isComment)) {
                        //没评价
                        helper.setText(R.id.tv_order_bottom_right_button, "待评价")
                                .setBackgroundRes(R.id.tv_order_bottom_right_button, R.drawable.shape_order_btn_yellow);
                    } else {
                        //已评价
                        helper.setText(R.id.tv_order_bottom_right_button, "查看评价")
                                .setBackgroundRes(R.id.tv_order_bottom_right_button, R.drawable.shape_order_btn_yellow);
                    }
                }
                break;
            case OrderStatus.CANCELED: //已关闭
            case OrderStatus.UN_APPROVE:
            case OrderStatus.CLOSED:
            case OrderStatus.WAIT_REFUND:
                helper.setVisible(R.id.tv_order_bottom_right_button, true)
                        .setText(R.id.tv_order_bottom_right_button, "删除订单")
                        .setBackgroundRes(R.id.tv_order_bottom_right_button, R.drawable.shape_order_btn_yellow)
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
                WaitPayDetailActivity.launch(mContext, orderNo, position, true);
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
