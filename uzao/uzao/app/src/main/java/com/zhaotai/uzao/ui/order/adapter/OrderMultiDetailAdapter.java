package com.zhaotai.uzao.ui.order.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.OrderDetailMultiBean;
import com.zhaotai.uzao.constants.OrderStatus;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.TimeUtils;

import java.util.List;

/**
 * Time: 2018/7/17 0017
 * Created by LiYou
 * Description :
 */
public class OrderMultiDetailAdapter extends BaseMultiItemQuickAdapter<OrderDetailMultiBean, BaseViewHolder> {

    public OrderMultiDetailAdapter(List<OrderDetailMultiBean> data) {
        super(data);
        addItemType(OrderDetailMultiBean.TYPE_SECTION, R.layout.item_order_detail_section);
        addItemType(OrderDetailMultiBean.TYPE_SECTION_VIEW, R.layout.item_order_detail_section_view);
        addItemType(OrderDetailMultiBean.TYPE_SECTION_MATERIAL, R.layout.item_order_detail_section_material);
        addItemType(OrderDetailMultiBean.TYPE_SECTION_WAIT_RECEIVE, R.layout.item_order_detail_section_wait_receive);
        addItemType(OrderDetailMultiBean.TYPE_ITEM, R.layout.item_order_detail_item);
        addItemType(OrderDetailMultiBean.TYPE_BOTTOM, R.layout.item_order_detail_bottom);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderDetailMultiBean item) {
        switch (helper.getItemViewType()) {
            case OrderDetailMultiBean.TYPE_SECTION:
                initSection(helper, item);
                break;
            case OrderDetailMultiBean.TYPE_SECTION_MATERIAL:
                break;
            case OrderDetailMultiBean.TYPE_SECTION_WAIT_RECEIVE:
                initSection(helper, item);
                helper.setText(R.id.tv_order_detail_package, item.packageNum);
                if (!StringUtil.isEmpty(item.remainingReceiveTime)) {
                    String time = TimeUtils.millis2FitTimeSpan(Long.valueOf(item.remainingReceiveTime), 1);
                    if (time != null && time.isEmpty()) {
                        time = "1";
                    }
                    helper.setText(R.id.tv_order_detail_package_remaining_receiveTime, time + "天后默认收货");
                }
                break;
            case OrderDetailMultiBean.TYPE_ITEM:
                initItem(helper, item);
                break;
            case OrderDetailMultiBean.TYPE_BOTTOM:
                initBottom(helper, item);
                break;
        }
    }

    private void initSection(BaseViewHolder helper, OrderDetailMultiBean item) {
        helper.setText(R.id.tv_order_detail_package, item.packageNum);
        String state;
        switch (item.state) {
            case OrderStatus.WAIT_PAY://待付款
                state = "待付款";
                break;
            case OrderStatus.WAIT_APPROVE://待审核
                state = "待审核";
                break;
            case OrderStatus.WAIT_HANDLE: //待发货
            case OrderStatus.WAIT_DELIVERY:
                state = "待发货";
                break;
            case OrderStatus.WAIT_RECEIVE: //待收货
                state = "待收货";
                break;
            case OrderStatus.FINISHED: //已完成
                //商品的完成状态 还包括待评价商品
                if (item.isCommend.equals("N")) {
                    state = "待评价";
                    break;
                }
                state = "已完成";
                break;
            case OrderStatus.CANCELED: //已关闭
            case OrderStatus.UN_APPROVE:
            case OrderStatus.CLOSED:
            case OrderStatus.WAIT_REFUND:
                state = "已关闭";
                break;
            default:
                state = "";
        }
        helper.setText(R.id.tv_order_detail_state, state);
    }

    private void initItem(BaseViewHolder helper, OrderDetailMultiBean item) {
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.pic,
                (ImageView) helper.getView(R.id.iv_order_goods_image));

        helper.setText(R.id.tv_order_goods_title, item.name)
                .setText(R.id.tv_order_goods_properties, item.category)
                .setText(R.id.tv_order_goods_price, mContext.getString(R.string.account, item.price));

        if (item.isMaterial) {
            helper.setVisible(R.id.tv_material_time, true)
                    .setText(R.id.tv_material_time, item.time);
        } else {
            helper.setText(R.id.tv_order_goods_num, mContext.getString(R.string.goods_number, item.count));
        }
    }

    private void initBottom(BaseViewHolder helper, OrderDetailMultiBean item) {
        switch (item.state) {
            case OrderStatus.WAIT_PAY://待付款
                break;
            case OrderStatus.WAIT_APPROVE://待审核
                break;
            case OrderStatus.WAIT_HANDLE: //待发货
            case OrderStatus.WAIT_DELIVERY:
                break;
            case OrderStatus.WAIT_RECEIVE: //待收货
                helper.setVisible(R.id.tv_order_bottom_left, true)
                        .setVisible(R.id.tv_order_bottom_middle, true)
                        .setVisible(R.id.tv_order_bottom_right, true)
                        .setText(R.id.tv_order_bottom_left, "申请售后")
                        .setText(R.id.tv_order_bottom_middle, "查看物流")
                        .setText(R.id.tv_order_bottom_right, "确认收货")
                        .addOnClickListener(R.id.tv_order_bottom_left)
                        .addOnClickListener(R.id.tv_order_bottom_middle)
                        .addOnClickListener(R.id.tv_order_bottom_right);
                break;
            case OrderStatus.FINISHED: //已完成
                //商品的完成状态 还包括待评价商品
                if (item.isCommend.equals("N")) {
                    //"待评价";
                    helper.setVisible(R.id.tv_order_bottom_left, false)
                            .setVisible(R.id.tv_order_bottom_middle, true)
                            .setVisible(R.id.tv_order_bottom_right, true)
                            .setText(R.id.tv_order_bottom_middle, "查看物流")
                            .setText(R.id.tv_order_bottom_right, "去评价")
                            .addOnClickListener(R.id.tv_order_bottom_middle)
                            .addOnClickListener(R.id.tv_order_bottom_right);
                    break;
                }
                //"已完成";
                helper.setVisible(R.id.tv_order_bottom_left, false)
                        .setVisible(R.id.tv_order_bottom_middle, true)
                        .setVisible(R.id.tv_order_bottom_right, true)
                        .setText(R.id.tv_order_bottom_middle, "查看物流")
                        .setText(R.id.tv_order_bottom_right, "查看评价")
                        .addOnClickListener(R.id.tv_order_bottom_middle)
                        .addOnClickListener(R.id.tv_order_bottom_right);
                break;
            case OrderStatus.CANCELED: //已关闭
            case OrderStatus.UN_APPROVE:
            case OrderStatus.CLOSED:
            case OrderStatus.WAIT_REFUND:
                helper.setVisible(R.id.tv_order_bottom_left, false)
                        .setVisible(R.id.tv_order_bottom_middle, false)
                        .setVisible(R.id.tv_order_bottom_right, true)
                        .setText(R.id.tv_order_bottom_right, "删除")
                        .addOnClickListener(R.id.tv_order_bottom_right);
                break;
            default:
                break;
        }
    }
}
