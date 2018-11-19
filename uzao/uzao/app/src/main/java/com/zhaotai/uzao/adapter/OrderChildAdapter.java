package com.zhaotai.uzao.adapter;


import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.OrderGoodsDetailBean;
import com.zhaotai.uzao.bean.OrderItemBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;

import java.util.List;

/**
 * Time: 2017/6/6
 * Created by LiYou
 * Description : 订单列表
 */

public class OrderChildAdapter extends BaseQuickAdapter<OrderItemBean, BaseViewHolder> {

    private Gson gson;
    private List<OrderItemBean> data;

    public OrderChildAdapter(List<OrderItemBean> data, Gson gson) {
        super(R.layout.item_order_goods, data);
        this.gson = gson;
        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderItemBean item) {
        OrderGoodsDetailBean orderGoodsDetail = gson.fromJson(item.detail, OrderGoodsDetailBean.class);
        helper.setText(R.id.tv_order_goods_title, orderGoodsDetail.name)
                .setText(R.id.tv_order_goods_properties, orderGoodsDetail.category)
                .setText(R.id.tv_order_goods_num, mContext.getString(R.string.goods_number, item.count))
                .setText(R.id.tv_order_goods_price, mContext.getString(R.string.account, item.unitPriceY));
        //商品图片
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + orderGoodsDetail.pic, (ImageView) helper.getView(R.id.iv_order_goods_image));

        if (helper.getLayoutPosition() == data.size() - 1) {
            helper.setVisible(R.id.tv_order_bottom_divider, false);

        } else {
            helper.setVisible(R.id.tv_order_bottom_divider, true);
        }

    }

}
