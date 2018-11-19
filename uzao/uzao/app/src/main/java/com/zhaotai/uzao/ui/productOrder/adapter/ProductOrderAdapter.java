package com.zhaotai.uzao.ui.productOrder.adapter;


import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.OrderGoodsDetailBean;
import com.zhaotai.uzao.bean.ProductOrderBean;
import com.zhaotai.uzao.constants.OrderStatus;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;

/**
 * Time: 2017/6/6
 * Created by LiYou
 * Description : 订单列表
 */

public class ProductOrderAdapter extends BaseQuickAdapter<ProductOrderBean, BaseViewHolder> {

    private Gson gson;
    //打样
    private static final String SAMPLING = "Sampling";
    //大货
    private static final String PRODUCE = "Produce";
    //打样加大货
    private static final String SAMPLING_PRODUCE = "SamplingProduce";

    public ProductOrderAdapter() {
        super(R.layout.item_product_order);
        this.gson = new Gson();
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ProductOrderBean item) {
        helper.setText(R.id.tv_order_id, orderNo(item.orderType,item.orderNo))
                .setText(R.id.tv_order_status, orderStatus(item.orderStatus));
        bottomButton(helper, item.orderStatus,item.orderType,item.freightType);

        //商品信息
        OrderGoodsDetailBean spuInfo = gson.fromJson(item.orderProfile, OrderGoodsDetailBean.class);
        //价格
        String price ;
        if("collectFreight".equals(item.freightType)){
            price = mContext.getString(R.string.account,item.totalAmountY);
        }else  {
            price = mContext.getString(R.string.account,item.totalAmountY)
                    +mContext.getString(R.string.include_freight, item.freightAmountY);
        }

        //商品照片
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + spuInfo.thumbnail, (ImageView) helper.getView(R.id.iv_order_goods_image));
        helper.setText(R.id.tv_order_goods_title, spuInfo.designName)//商品名
                .setText(R.id.tv_order_goods_num, mContext.getString(R.string.goods_number,item.count))//商品数量
                .setText(R.id.tv_order_goods_price,price)//价格
                .setVisible(R.id.tv_order_bottom_divider,false);

    }

    /**
     * 订单编号
     */
    private String orderNo(String orderType,String orderId){
        switch (orderType){
            case SAMPLING:
                return mContext.getString(R.string.order_num_sample, orderId);
            case PRODUCE:
            case SAMPLING_PRODUCE:
                return mContext.getString(R.string.order_num_product, orderId);
        }
        return "";
    }

    /**
     * 订单状态-大货
     */
    private String orderStatus(String orderStatus) {
        switch (orderStatus) {
            case OrderStatus.WAIT_PAY:
                return "待付款";
            case OrderStatus.WAIT_PAY_CONFIRM:
                return "待收款";
            case OrderStatus.WAIT_FIRST_PAY:
                return "待付首款";
            case OrderStatus.WAIT_FIRST_PAY_CONFIRM:
                return "待收首款";
            case OrderStatus.WAIT_SAMPLING_DELIVER:
                return "样品生产中";
            case OrderStatus.WAIT_SAMPLING_RECEIVE:
                return "样品待确认";
            case OrderStatus.WAIT_PRODUCE_DELIVERY:
                return "大货样生产中";
            case OrderStatus.WAIT_PRODUCE_RECEIVE:
                return "大货样确认";
            case OrderStatus.WAIT_LAST_PAY:
                return "待付尾款";
            case OrderStatus.WAIT_LAST_PAY_CONFIRM:
                return "待收尾款";
            case OrderStatus.WAIT_DELIVERY:
                return "待发货";
            case OrderStatus.WAIT_RECEIVE:
                return "待收货";
            case OrderStatus.FINISHED:
                return "已完成";
            case OrderStatus.CANCELED:
            case OrderStatus.CLOSED:
                return "已关闭";
            default:
                return "";
        }
    }

    /**
     * 根据订单状态 显示 底部button
     */
    private void bottomButton(BaseViewHolder helper, String orderStatus,String orderType ,String freightType) {
        switch (orderStatus) {
            case OrderStatus.WAIT_PAY:
                helper.setText(R.id.tv_product_order_bottom_first_button, "查看详情")
                        .setText(R.id.tv_product_order_bottom_third_button, "付款")
                        .setVisible(R.id.tv_product_order_bottom_first_button, true)
                        .setVisible(R.id.tv_product_order_bottom_second_button, false)
                        .setVisible(R.id.tv_product_order_bottom_third_button, true)
                        .addOnClickListener(R.id.tv_product_order_bottom_third_button)
                        .addOnClickListener(R.id.tv_product_order_bottom_first_button);
                break;
            case OrderStatus.WAIT_FIRST_PAY:
                helper.setText(R.id.tv_product_order_bottom_first_button, "查看详情")
                        .setText(R.id.tv_product_order_bottom_third_button, "支付首款")
                        .setVisible(R.id.tv_product_order_bottom_first_button, true)
                        .setVisible(R.id.tv_product_order_bottom_second_button, false)
                        .setVisible(R.id.tv_product_order_bottom_third_button, true)
                        .addOnClickListener(R.id.tv_product_order_bottom_third_button)
                        .addOnClickListener(R.id.tv_product_order_bottom_first_button);
                break;
            case OrderStatus.WAIT_PAY_CONFIRM://待收款
            case OrderStatus.WAIT_FIRST_PAY_CONFIRM://待收首款
            case OrderStatus.WAIT_SAMPLING_DELIVER://打样生产中
                helper.setText(R.id.tv_product_order_bottom_first_button, "查看详情")
                        .setVisible(R.id.tv_product_order_bottom_first_button, true)
                        .setVisible(R.id.tv_product_order_bottom_second_button, false)
                        .setVisible(R.id.tv_product_order_bottom_third_button, false)
                        .addOnClickListener(R.id.tv_product_order_bottom_first_button);
                break;
            case OrderStatus.WAIT_PRODUCE_DELIVERY://大货样生产中
                helper.setText(R.id.tv_product_order_bottom_first_button, "查看详情")
                        .setVisible(R.id.tv_product_order_bottom_first_button, true)
                        .setVisible(R.id.tv_product_order_bottom_second_button, false)
                        .setVisible(R.id.tv_product_order_bottom_third_button, false)
                        .addOnClickListener(R.id.tv_product_order_bottom_first_button);
                break;
            case OrderStatus.WAIT_SAMPLING_RECEIVE://样品待确认
                helper.setText(R.id.tv_product_order_bottom_first_button, "物流信息")
                        .setText(R.id.tv_product_order_bottom_second_button, "查看详情")
                        .setText(R.id.tv_product_order_bottom_third_button, "去确认")
                        .setVisible(R.id.tv_product_order_bottom_first_button, true)
                        .setVisible(R.id.tv_product_order_bottom_second_button, true)
                        .setVisible(R.id.tv_product_order_bottom_third_button, true)
                        .addOnClickListener(R.id.tv_product_order_bottom_first_button)
                        .addOnClickListener(R.id.tv_product_order_bottom_second_button)
                        .addOnClickListener(R.id.tv_product_order_bottom_third_button);
                break;
            case OrderStatus.WAIT_PRODUCE_RECEIVE://大货样确认
                helper.setText(R.id.tv_product_order_bottom_first_button, "物流信息")
                        .setText(R.id.tv_product_order_bottom_second_button, "查看详情")
                        .setText(R.id.tv_product_order_bottom_third_button, "确认样品")
                        .setVisible(R.id.tv_product_order_bottom_first_button, true)
                        .setVisible(R.id.tv_product_order_bottom_second_button, true)
                        .setVisible(R.id.tv_product_order_bottom_third_button, true)
                        .addOnClickListener(R.id.tv_product_order_bottom_first_button)
                        .addOnClickListener(R.id.tv_product_order_bottom_second_button)
                        .addOnClickListener(R.id.tv_product_order_bottom_third_button);
                break;
            case OrderStatus.WAIT_LAST_PAY://待付尾款
                helper.setText(R.id.tv_product_order_bottom_first_button, "查看详情")
                        .setText(R.id.tv_product_order_bottom_third_button, "支付尾款")
                        .setVisible(R.id.tv_product_order_bottom_first_button, true)
                        .setVisible(R.id.tv_product_order_bottom_second_button, false)
                        .setVisible(R.id.tv_product_order_bottom_third_button, true)
                        .addOnClickListener(R.id.tv_product_order_bottom_first_button)
                        .addOnClickListener(R.id.tv_product_order_bottom_third_button);
                break;
            case OrderStatus.WAIT_LAST_PAY_CONFIRM://待收尾款
                helper.setText(R.id.tv_product_order_bottom_first_button, "查看详情")
                        .setVisible(R.id.tv_product_order_bottom_first_button, true)
                        .setVisible(R.id.tv_product_order_bottom_second_button, false)
                        .setVisible(R.id.tv_product_order_bottom_third_button, false)
                        .addOnClickListener(R.id.tv_product_order_bottom_first_button);
                break;
            case OrderStatus.WAIT_DELIVERY://待发货
                helper.setText(R.id.tv_product_order_bottom_first_button, "查看详情")
                        .setVisible(R.id.tv_product_order_bottom_first_button, true)
                        .setVisible(R.id.tv_product_order_bottom_second_button, false)
                        .setVisible(R.id.tv_product_order_bottom_third_button, false)
                        .addOnClickListener(R.id.tv_product_order_bottom_first_button);
                break;
            case OrderStatus.WAIT_RECEIVE: //待收货
                if(OrderStatus.SAMPLING_PRODUCE.equals(orderType) ||
                        OrderStatus.PRODUCE.equals(orderType) && "collectFreight".equals(freightType)){
                    helper.setVisible(R.id.tv_product_order_bottom_first_button,false);
                }else {
                    helper.setText(R.id.tv_product_order_bottom_first_button, "物流信息")
                            .setVisible(R.id.tv_product_order_bottom_first_button,true)
                            .addOnClickListener(R.id.tv_product_order_bottom_first_button);
                }
                helper.setText(R.id.tv_product_order_bottom_second_button, "查看详情")
                        .setText(R.id.tv_product_order_bottom_third_button, "确认收货")
                        .setVisible(R.id.tv_product_order_bottom_second_button, true)
                        .setVisible(R.id.tv_product_order_bottom_third_button, true)
                        .addOnClickListener(R.id.tv_product_order_bottom_second_button)
                        .addOnClickListener(R.id.tv_product_order_bottom_third_button);
                break;
            case OrderStatus.FINISHED: //已完成
                helper.setText(R.id.tv_product_order_bottom_second_button, "查看详情")
                        .setVisible(R.id.tv_product_order_bottom_first_button,false)
                        .setVisible(R.id.tv_product_order_bottom_second_button, true)
                        .setVisible(R.id.tv_product_order_bottom_third_button, false)
                        .addOnClickListener(R.id.tv_product_order_bottom_second_button);
                break;
            case OrderStatus.CANCELED: //已关闭
            case OrderStatus.CLOSED:
                helper.setText(R.id.tv_product_order_bottom_first_button, "查看详情")
                        .setVisible(R.id.tv_product_order_bottom_first_button, true)
                        .setVisible(R.id.tv_product_order_bottom_second_button, false)
                        .setVisible(R.id.tv_product_order_bottom_third_button, false)
                        .addOnClickListener(R.id.tv_product_order_bottom_first_button);
                break;
            default:
                break;
        }
    }
}
