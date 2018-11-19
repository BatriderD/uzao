package com.zhaotai.uzao.adapter;


import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.OrderGoodsBean;
import com.zhaotai.uzao.bean.OrderGoodsDetailBean;
import com.zhaotai.uzao.constants.OrderStatus;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.GlideUtil;
import com.zhaotai.uzao.utils.StringUtil;

/**
 * Time: 2017/6/6
 * Created by LiYou
 * Description : 订单详情列表
 */

public class OrderDetailAdapter extends BaseQuickAdapter<OrderGoodsBean, BaseViewHolder> {


    private Gson gson;

    public OrderDetailAdapter() {
        super(R.layout.item_order_goods);
        gson = new Gson();
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderGoodsBean item) {
        if (OrderStatus.ORDER_TYPE_MATERIAL.equals(item.orderType)) {
            //素材
            helper.setText(R.id.tv_order_goods_title, item.materialName)
                    .setText(R.id.tv_order_goods_properties, item.materialDesigner)
                    .setText(R.id.tv_order_goods_price, mContext.getString(R.string.account, item.materialPriceY));
            GlideLoadImageUtil.load(mContext,ApiConstants.UZAOCHINA_IMAGE_HOST + item.materialThumbnail,(ImageView) helper.getView(R.id.iv_order_goods_image));

        } else {
            OrderGoodsDetailBean orderGoodsDetailBean = gson.fromJson(item.orderDetail, OrderGoodsDetailBean.class);
            helper.setText(R.id.tv_order_goods_title, orderGoodsDetailBean.name)
                    .setText(R.id.tv_order_goods_properties, orderGoodsDetailBean.category)
                    .setText(R.id.tv_order_goods_num, mContext.getString(R.string.goods_number, item.count))
                    .setText(R.id.tv_order_goods_price, mContext.getString(R.string.account, item.unitPriceY));

            GlideLoadImageUtil.load(mContext,ApiConstants.UZAOCHINA_IMAGE_HOST + orderGoodsDetailBean.pic,(ImageView) helper.getView(R.id.iv_order_goods_image));
            //活动图片
            if (!StringUtil.isEmpty(item.activityIcon)) {
                helper.setVisible(R.id.tv_order_activity, true);
                GlideLoadImageUtil.load(mContext,ApiConstants.UZAOCHINA_IMAGE_HOST + item.activityIcon,(ImageView) helper.getView(R.id.tv_order_activity));
            } else {
                helper.setVisible(R.id.tv_order_activity, false);
            }

        }

        if (helper.getLayoutPosition() == dataSize()) {
            helper.setVisible(R.id.tv_order_bottom_divider, false);

        } else {
            helper.setVisible(R.id.tv_order_bottom_divider, true);
        }
    }

    private int dataSize() {
        return getData().size();
    }


}
