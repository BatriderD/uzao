package com.zhaotai.uzao.ui.order.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.ApplyAfterSalesDetailMultiBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;

import java.util.List;

/**
 * Time: 2018/7/31 0031
 * Created by LiYou
 * Description :售后详情
 */
public class ApplyAfterSalesDetailMultiAdapter extends BaseMultiItemQuickAdapter<ApplyAfterSalesDetailMultiBean, BaseViewHolder> {

    public ApplyAfterSalesDetailMultiAdapter(List<ApplyAfterSalesDetailMultiBean> data) {
        super(data);
        addItemType(ApplyAfterSalesDetailMultiBean.TYPE_SECTION_ORDER_NUM, R.layout.item_after_sales_multi_section_order_num);
        addItemType(ApplyAfterSalesDetailMultiBean.TYPE_ITEM_GOODS, R.layout.item_order_multi_goods_vertical);
        addItemType(ApplyAfterSalesDetailMultiBean.TYPE_ITEM_APPLY, R.layout.item_after_sales_detail_multi_apply_content);
        addItemType(ApplyAfterSalesDetailMultiBean.TYPE_ITEM_SECTION_IMAGE, R.layout.item_section_upload_image);
        addItemType(ApplyAfterSalesDetailMultiBean.TYPE_ITEM_IMAGE, R.layout.item_upload_image);
        addItemType(ApplyAfterSalesDetailMultiBean.TYPE_ITEM_SECTION_TRANSPORT, R.layout.item_after_sales_detail_section_transport);
        addItemType(ApplyAfterSalesDetailMultiBean.TYPE_ITEM_TRANSPORT, R.layout.item_after_sales_detail_transport);
        addItemType(ApplyAfterSalesDetailMultiBean.TYPE_ITEM_REJECT_REASON, R.layout.item_after_sales_detail_reject_reason);
    }

    @Override
    protected void convert(BaseViewHolder helper, ApplyAfterSalesDetailMultiBean item) {
        switch (item.getItemType()) {
            case ApplyAfterSalesDetailMultiBean.TYPE_SECTION_ORDER_NUM://订单编号
                helper.setText(R.id.tv_order_multi_section_order_num, mContext.getString(R.string.after_sales_order_number1, item.orderId))
                        .setText(R.id.tv_order_multi_section_package_num, item.packageNum);
                break;
            case ApplyAfterSalesDetailMultiBean.TYPE_ITEM_GOODS://选择商品
                convertGoods(helper, item);
                break;
            case ApplyAfterSalesDetailMultiBean.TYPE_ITEM_APPLY://申请内容
                helper.setText(R.id.tv_apply_after_sales_apply_reason, item.reason)
                        .setText(R.id.tv_after_sales_detail_type, getType(item.applyType));
                break;
            case ApplyAfterSalesDetailMultiBean.TYPE_ITEM_IMAGE://图片
                helper.setGone(R.id.close, false);
                GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.pic, (ImageView) helper.getView(R.id.image));
                break;
            case ApplyAfterSalesDetailMultiBean.TYPE_ITEM_SECTION_TRANSPORT:
                if (item.platformTransport) {
                    //平台物流
                    helper.setGone(R.id.view_section_transport_line_1, false)
                            .setGone(R.id.view_section_transport_line_2, true);
                } else {
                    helper.setGone(R.id.view_section_transport_line_1, true)
                            .setGone(R.id.view_section_transport_line_2, false);
                }
                helper.setText(R.id.view_section_transport_name, item.sectionTransportName);
                break;
            case ApplyAfterSalesDetailMultiBean.TYPE_ITEM_TRANSPORT://用户退货物流
                helper.setText(R.id.tv_transport_company_name, item.transportName)
                        .setText(R.id.tv_transport_company_num, item.transportNo)
                        .addOnClickListener(R.id.tv_transport_check);
                helper.setGone(R.id.tv_transport_check, !item.platformTransport);
                break;
            case ApplyAfterSalesDetailMultiBean.TYPE_ITEM_REJECT_REASON:
                //售后失败原因
                helper.setText(R.id.tv_apply_after_sales_detail_reject_reason,item.rejectReason);
                break;
        }
    }

    /**
     * 商品
     */
    private void convertGoods(BaseViewHolder helper, ApplyAfterSalesDetailMultiBean item) {
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.pic,
                (ImageView) helper.getView(R.id.iv_order_goods_image));
        helper.setText(R.id.tv_order_goods_title, item.name)
                .setText(R.id.tv_order_goods_properties, item.category)
                .setText(R.id.tv_order_goods_price, mContext.getString(R.string.account, item.price))
                .setText(R.id.tv_order_goods_num, mContext.getString(R.string.goods_number, item.count))
                .addOnClickListener(R.id.iv_check_goods)
                .addOnClickListener(R.id.iv_goods_spu_sub)
                .addOnClickListener(R.id.iv_goods_spu_add);
    }

    private String getType(String type) {
        if ("Returned".equals(type)) {
            return "退款";
        }
        return "换货";
    }
}
