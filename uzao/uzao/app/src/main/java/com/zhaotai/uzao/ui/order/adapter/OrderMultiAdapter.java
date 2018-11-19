package com.zhaotai.uzao.ui.order.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.OrderMultiBean;
import com.zhaotai.uzao.constants.OrderStatus;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.StringUtil;

import java.util.List;

/**
 * Time: 2018/7/18 0018
 * Created by LiYou
 * Description :我的订单列表
 */
public class OrderMultiAdapter extends BaseMultiItemQuickAdapter<OrderMultiBean, BaseViewHolder> {

    public OrderMultiAdapter(List<OrderMultiBean> data) {
        super(data);
        addItemType(OrderMultiBean.TYPE_SECTION_ORDER_NUM, R.layout.item_order_multi_section_order_num);
        addItemType(OrderMultiBean.TYPE_SECTION_WAIT_DELIVERY, R.layout.item_order_multi_section_wait_deliver);
        addItemType(OrderMultiBean.TYPE_SECTION_WAIT_PAY, R.layout.item_order_multi_section_wait_pay);
        addItemType(OrderMultiBean.TYPE_SECTION_PACKAGE, R.layout.item_order_multi_section_package);
        addItemType(OrderMultiBean.TYPE_ITEM_GOODS_VERTICAL, R.layout.item_order_multi_goods_vertical);
        addItemType(OrderMultiBean.TYPE_ITEM_GOODS_HORIZONTAL, R.layout.item_order_multi_goods_horizontal);
        addItemType(OrderMultiBean.TYPE_ITEM_GOODS_SINGLE_WITH_BOTTOM, R.layout.item_order_multi_goods_single_with_bottom);
        addItemType(OrderMultiBean.TYPE_ITEM_GOODS_HORIZONTAL_WITH_BOTTOM, R.layout.item_order_multi_goods_horizontal_with_bottom);
        addItemType(OrderMultiBean.TYPE_ITEM_GOODS_SECTION_MATERIAL, R.layout.item_order_multi_section_material);
        addItemType(OrderMultiBean.TYPE_ITEM_MATERIAL, R.layout.item_order_detail_item);
        addItemType(OrderMultiBean.TYPE_ITEM_BOTTOM, R.layout.item_order_multi_goods_bottom);
        addItemType(OrderMultiBean.TYPE_ITEM_BOTTOM_PRICE, R.layout.item_order_multi_goods_bottom_price);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderMultiBean item) {
        switch (helper.getItemViewType()) {
            case OrderMultiBean.TYPE_SECTION_ORDER_NUM:
                helper.setText(R.id.tv_order_multi_section_order_num, mContext.getString(R.string.order_num, item.orderNo));
                break;
            case OrderMultiBean.TYPE_SECTION_WAIT_DELIVERY://待发货表头
                break;
            case OrderMultiBean.TYPE_SECTION_WAIT_PAY://代付款表头
                helper.setText(R.id.tv_order_multi_section_order_num, mContext.getString(R.string.order_num, item.orderNo))
                        .addOnClickListener(R.id.tv_pay_order_choose);
                if (item.isSelected) {
                    helper.setImageResource(R.id.tv_pay_order_choose, R.drawable.icon_circle_selected);
                } else {
                    helper.setImageResource(R.id.tv_pay_order_choose, R.drawable.icon_circle_unselected);
                }
                break;
            case OrderMultiBean.TYPE_SECTION_PACKAGE:
                helper.setText(R.id.tv_order_multi_section_package_num, item.packageNum)
                        .setText(R.id.tv_order_multi_section_package_status, getStatus(item));
                break;
            case OrderMultiBean.TYPE_ITEM_GOODS_VERTICAL:
                GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.pic,
                        (ImageView) helper.getView(R.id.iv_order_goods_image));
                helper.setText(R.id.tv_order_goods_title, item.name)
                        .setText(R.id.tv_order_goods_properties, item.category)
                        .setText(R.id.tv_order_goods_price, mContext.getString(R.string.account, item.price))
                        .setText(R.id.tv_order_goods_num, mContext.getString(R.string.goods_number, item.count));
                helper.setVisible(R.id.view_cut_line, !item.isLast);
                break;
            case OrderMultiBean.TYPE_ITEM_GOODS_HORIZONTAL:
                if (!StringUtil.isEmpty(item.pic1)) {
                    GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.pic1,
                            (ImageView) helper.getView(R.id.iv_order_multi_goods_horizontal_pic1));
                }
                if (!StringUtil.isEmpty(item.pic2)) {
                    GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.pic2,
                            (ImageView) helper.getView(R.id.iv_order_multi_goods_horizontal_pic2));
                }
                if (!StringUtil.isEmpty(item.pic3)) {
                    GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.pic3,
                            (ImageView) helper.getView(R.id.iv_order_multi_goods_horizontal_pic3));
                }
                helper.setText(R.id.iv_order_multi_goods_horizontal_num, mContext.getString(R.string.many_account, item.horizontalSize));
                helper.setVisible(R.id.view_cut_line, !item.isLast);
                break;
            case OrderMultiBean.TYPE_ITEM_GOODS_SINGLE_WITH_BOTTOM:
                initSingleWithBottom(helper, item);
                break;
            case OrderMultiBean.TYPE_ITEM_GOODS_HORIZONTAL_WITH_BOTTOM:
                initHorizontalWithBottom(helper, item);
                break;
            case OrderMultiBean.TYPE_ITEM_GOODS_SECTION_MATERIAL:
                break;
            case OrderMultiBean.TYPE_ITEM_MATERIAL:
                initItemMaterial(helper, item);
                break;
            case OrderMultiBean.TYPE_ITEM_BOTTOM:
                initBottom(helper, item);
                break;
            case OrderMultiBean.TYPE_ITEM_BOTTOM_PRICE:
                initBottom(helper, item);
                initBottomPrice(helper, item);
                break;
        }
    }

    /**
     * 素材
     *
     * @param helper 帮助类
     * @param item   数据
     */
    private void initItemMaterial(BaseViewHolder helper, OrderMultiBean item) {
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.pic,
                (ImageView) helper.getView(R.id.iv_order_goods_image));
        helper.setText(R.id.tv_order_goods_title, item.name)
                .setText(R.id.tv_order_goods_properties, item.category)
                .setText(R.id.tv_order_goods_price, mContext.getString(R.string.account, item.price))
                .setVisible(R.id.tv_material_time, true)
                .setText(R.id.tv_material_time, item.time);
    }

    /**
     * 待收货以后的状态 带bottom
     *
     * @param helper 帮助类
     * @param item   数据
     */
    private void initHorizontalWithBottom(BaseViewHolder helper, OrderMultiBean item) {
        if (!StringUtil.isEmpty(item.pic1)) {
            GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.pic1,
                    (ImageView) helper.getView(R.id.iv_order_multi_goods_horizontal_pic1));
        }
        if (!StringUtil.isEmpty(item.pic2)) {
            GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.pic2,
                    (ImageView) helper.getView(R.id.iv_order_multi_goods_horizontal_pic2));
        }
        if (!StringUtil.isEmpty(item.pic3)) {
            GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.pic3,
                    (ImageView) helper.getView(R.id.iv_order_multi_goods_horizontal_pic3));
        }
        helper.setText(R.id.tv_order_package, item.packageNum)
                .setText(R.id.tv_order_state, getStatus(item));
        initBottom(helper, item);
    }

    /**
     * 待收货 以后的状态 带bottom
     *
     * @param helper 帮助类
     * @param item   数据
     */
    private void initSingleWithBottom(BaseViewHolder helper, OrderMultiBean item) {
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.pic,
                (ImageView) helper.getView(R.id.iv_order_goods_image));
        helper.setText(R.id.tv_order_goods_title, item.name)
                .setText(R.id.tv_order_goods_properties, item.category)
                .setText(R.id.tv_order_goods_price, mContext.getString(R.string.account, item.price))
                .setText(R.id.tv_order_package, item.packageNum)
                .setText(R.id.tv_order_state, getStatus(item));
        initBottom(helper, item);
    }

    private void initBottom(BaseViewHolder helper, OrderMultiBean item) {
        helper.setVisible(R.id.view_cut_line, !item.isLast);
        switch (StringUtil.isEmpty(item.packageStatus) ? item.orderStatus : item.packageStatus) {
            case OrderStatus.WAIT_PAY://待付款
                helper.setVisible(R.id.tv_order_bottom_left, false)
                        .setVisible(R.id.tv_order_bottom_middle, true)
                        .setVisible(R.id.tv_order_bottom_right, true)
                        .setText(R.id.tv_order_bottom_middle, "取消订单")
                        .setText(R.id.tv_order_bottom_right, "付款")
                        .addOnClickListener(R.id.tv_order_bottom_middle)
                        .addOnClickListener(R.id.tv_order_bottom_right);
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
                if (!StringUtil.isEmpty(item.isCommend) && item.isCommend.equals("N")) {
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
                        .setText(R.id.tv_order_bottom_right, "删除订单")
                        .addOnClickListener(R.id.tv_order_bottom_right);
                break;
            default:
                break;
        }
    }

    private void initBottomPrice(BaseViewHolder helper, OrderMultiBean item) {
        helper.setText(R.id.tv_order_bottom_price, mContext.getString(R.string.money_price, item.orderPrice));
    }

    /**
     * 获取订单状态
     *
     * @param item 数据
     * @return 订单String状态
     */
    private String getStatus(OrderMultiBean item) {

        String state;
        switch (StringUtil.isEmpty(item.packageStatus) ? item.orderStatus : item.packageStatus) {
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
                if (!StringUtil.isEmpty(item.isCommend) && item.isCommend.equals("N")) {
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
        return state;
    }
}
