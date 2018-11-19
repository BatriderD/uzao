package com.zhaotai.uzao.adapter;


import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.OrderGoodsBean;
import com.zhaotai.uzao.bean.OrderGoodsDetailBean;
import com.zhaotai.uzao.constants.OrderStatus;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.ImageSizeUtil;
import com.zhaotai.uzao.utils.StringUtil;

/**
 * Time: 2017/6/6
 * Created by LiYou
 * Description : 订单详情 --待收货
 */

public class OrderReceiveDetailAdapter extends BaseQuickAdapter<OrderGoodsBean, BaseViewHolder> {


    private Gson gson;

    public OrderReceiveDetailAdapter() {
        super(R.layout.item_order_goods);
        gson = new Gson();
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderGoodsBean item) {
        OrderGoodsDetailBean orderGoodsDetailBean = gson.fromJson(item.orderDetail, OrderGoodsDetailBean.class);
        helper.setText(R.id.tv_order_goods_title, orderGoodsDetailBean.name)
                .setText(R.id.tv_order_goods_properties, orderGoodsDetailBean.category)
                .setText(R.id.tv_order_goods_num, mContext.getString(R.string.goods_number, item.count))
                .setText(R.id.tv_order_goods_price, mContext.getString(R.string.account, item.unitPriceY))
                .setVisible(R.id.tv_order_detail_after_sales, true)
                .addOnClickListener(R.id.tv_order_detail_after_sales);

        //售后状态
        checkAfterSalesState(helper, item);
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + orderGoodsDetailBean.pic, (ImageView) helper.getView(R.id.iv_order_goods_image));
        //分割线
        if (helper.getLayoutPosition() == dataSize()) {
            helper.setVisible(R.id.tv_order_bottom_divider, false);
        } else {
            helper.setVisible(R.id.tv_order_bottom_divider, true);
        }

        if(!StringUtil.isEmpty(item.activityIcon)){
            //活动图片
            GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(item.activityIcon, ImageSizeUtil.ImageSize.X50), (ImageView) helper.getView(R.id.tv_order_activity), R.drawable.full_cut, R.drawable.full_cut);
        }

    }

    private int dataSize() {
        return getData().size();
    }


    /**
     * 售后状态
     */
    private void checkAfterSalesState(BaseViewHolder helper, OrderGoodsBean item) {
        switch (item.status) {
            case OrderStatus.WAIT_CONFIRM://待处理
            case OrderStatus.CONFIRMING://处理中
                helper.setText(R.id.tv_order_detail_after_sales, "售后处理中")
                        .addOnClickListener(R.id.tv_order_detail_after_sales)
                        .setBackgroundRes(R.id.tv_order_detail_after_sales, R.drawable.shape_order_btn);
                break;
            case OrderStatus.REJECT:
                helper.getView(R.id.tv_order_detail_after_sales).setBackground(null);
                helper.setText(R.id.tv_order_detail_after_sales, "售后失败");
                break;
            case OrderStatus.REPLACE:
                helper.setText(R.id.tv_order_detail_after_sales, "已换货");
                helper.getView(R.id.tv_order_detail_after_sales).setBackground(null);
                break;
            case OrderStatus.RETURNED:
                helper.setText(R.id.tv_order_detail_after_sales, "已退货");
                helper.getView(R.id.tv_order_detail_after_sales).setBackground(null);
                break;
            case "N": // 待收货状态 完成状态  关闭状态
                helper.setText(R.id.tv_order_detail_after_sales, "申请售后")
                        .setVisible(R.id.tv_order_detail_after_sales,true)
                        .addOnClickListener(R.id.tv_order_detail_after_sales)
                        .setBackgroundRes(R.id.tv_order_detail_after_sales, R.drawable.shape_order_btn);
                break;
        }
    }

}
