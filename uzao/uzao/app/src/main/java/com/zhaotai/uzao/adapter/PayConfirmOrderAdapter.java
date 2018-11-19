package com.zhaotai.uzao.adapter;


import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.OrderGoodsDetailBean;
import com.zhaotai.uzao.bean.OrderMaterialBean;
import com.zhaotai.uzao.bean.PayOrderDetailBean;
import com.zhaotai.uzao.constants.OrderStatus;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.ImageSizeUtil;
import com.zhaotai.uzao.utils.StringUtil;


/**
 * Time: 2017/7/26
 * Created by LiYou
 * Description : 支付订单
 */

public class PayConfirmOrderAdapter extends BaseQuickAdapter<PayOrderDetailBean, BaseViewHolder> {

    private Gson gson;

    public PayConfirmOrderAdapter() {
        super(R.layout.item_pay_confirm_order);
        gson = new Gson();
    }

    @Override
    protected void convert(BaseViewHolder helper, PayOrderDetailBean item) {
        helper.setText(R.id.tv_pay_confirm_id, mContext.getString(R.string.order_num, item.sequenceNBR))
                .setText(R.id.tv_pay_confirm_order_price, mContext.getString(R.string.order_money, item.totalAmountY));
        LinearLayout view = helper.getView(R.id.ll_pay_confirm_item);

        //动态添加商品列表
        view.removeAllViews();
        if (item.materialOrderDetailModels != null && item.materialOrderDetailModels.size() > 0) {
            for (int i = 0; i < item.materialOrderDetailModels.size(); i++) {
                OrderMaterialBean material = item.materialOrderDetailModels.get(i);
                View itemView = View.inflate(mContext, R.layout.item_pay_order_goods, null);
                ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_pay_confirm_spu_image);
                TextView title = (TextView) itemView.findViewById(R.id.tv_pay_confirm_title);
                TextView type = (TextView) itemView.findViewById(R.id.tv_pay_confirm_properties);
                TextView price = (TextView) itemView.findViewById(R.id.tv_pay_confirm_spu_price);
                if (material.materialInfo != null) {
                    GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(material.materialInfo.thumbnail), imageView);
                    title.setText(material.materialInfo.sourceMaterialName);
                    price.setText(mContext.getString(R.string.account, material.materialInfo.priceY));
                }
                if (material.designerInfo != null) {
                    type.setText(material.designerInfo.nickName);
                }

                view.addView(itemView);
            }
        }

        if (item.orderDetailModels != null && item.orderDetailModels.size() > 0) {
            for (int i = 0; i < item.orderDetailModels.size(); i++) {
                View itemView = View.inflate(mContext, R.layout.item_pay_order_goods, null);
                View divider = itemView.findViewById(R.id.tv_pay_confirm_bottom_divider);
                ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_pay_confirm_spu_image);
                TextView title = (TextView) itemView.findViewById(R.id.tv_pay_confirm_title);
                TextView type = (TextView) itemView.findViewById(R.id.tv_pay_confirm_properties);
                TextView num = (TextView) itemView.findViewById(R.id.tv_confirm_order_child_num);
                TextView price = (TextView) itemView.findViewById(R.id.tv_pay_confirm_spu_price);
                ImageView activity = (ImageView) itemView.findViewById(R.id.tv_pay_confirm_activity);

                OrderGoodsDetailBean goods = gson.fromJson(item.orderDetailModels.get(i).orderDetail, OrderGoodsDetailBean.class);
                GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + goods.pic, imageView);
                title.setText(goods.name);
                type.setText(goods.category);
                num.setText(mContext.getString(R.string.goods_number, item.orderDetailModels.get(i).count));
                price.setText(mContext.getString(R.string.account, item.orderDetailModels.get(i).payAmountY));
                //活动图片
                if (!StringUtil.isEmpty(item.orderDetailModels.get(i).avtivityName)) {
                    activity.setVisibility(View.VISIBLE);
                    GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(item.orderDetailModels.get(i).activityIcon, ImageSizeUtil.ImageSize.X50), activity);
                } else {
                    activity.setVisibility(View.GONE);
                }

                if (i == item.orderDetailModels.size() - 1) {
                    divider.setVisibility(View.GONE);
                } else {
                    divider.setVisibility(View.VISIBLE);
                }
                view.addView(itemView);
            }
        }


        switch (item.orderType) {
            case OrderStatus.ORDER_TYPE_MATERIAL:
                helper.setGone(R.id.ll_pay_confirm_address, false)
                        .setGone(R.id.iv_address_bottom_line, false);
                break;
            case OrderStatus.ORDER_TYPE_PRODUCT:
                //地址
                helper.setVisible(R.id.ll_pay_confirm_address, true)
                        .setVisible(R.id.iv_address_bottom_line, true)
                        .setText(R.id.tv_pay_confirm_receive_people, item.receiverName)
                        .setText(R.id.tv_pay_confirm_receive_phone, item.receiverMobile)
                        .setText(R.id.tv_pay_confirm_receive_address, item.receiverAddress)
                        .addOnClickListener(R.id.ll_pay_confirm_address);
                break;
            default:
                //地址
                helper.setVisible(R.id.ll_pay_confirm_address, true)
                        .setVisible(R.id.iv_address_bottom_line, true)
                        .setText(R.id.tv_pay_confirm_receive_people, item.receiverName)
                        .setText(R.id.tv_pay_confirm_receive_phone, item.receiverMobile)
                        .setText(R.id.tv_pay_confirm_receive_address, item.receiverAddress)
                        .addOnClickListener(R.id.ll_pay_confirm_address);
                break;
        }


    }
}
