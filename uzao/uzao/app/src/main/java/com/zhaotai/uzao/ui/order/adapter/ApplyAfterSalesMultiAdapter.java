package com.zhaotai.uzao.ui.order.adapter;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.ApplyAfterSalesMultiBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;

import java.util.List;

/**
 * Time: 2018/7/31 0031
 * Created by LiYou
 * Description :
 */
public class ApplyAfterSalesMultiAdapter extends BaseMultiItemQuickAdapter<ApplyAfterSalesMultiBean, BaseViewHolder> {

    public ApplyAfterSalesMultiAdapter(List<ApplyAfterSalesMultiBean> data) {
        super(data);
        addItemType(ApplyAfterSalesMultiBean.TYPE_SECTION_ORDER_NUM, R.layout.item_after_sales_multi_section_order_num);
        addItemType(ApplyAfterSalesMultiBean.TYPE_ITEM_GOODS_SELECT, R.layout.item_after_sales_multi_goods_select);
        addItemType(ApplyAfterSalesMultiBean.TYPE_ITEM_APPLY, R.layout.item_after_sales_multi_apply_content);
        addItemType(ApplyAfterSalesMultiBean.TYPE_ITEM_IMAGE, R.layout.item_upload_image);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ApplyAfterSalesMultiBean item) {
        switch (item.getItemType()) {
            case ApplyAfterSalesMultiBean.TYPE_SECTION_ORDER_NUM://订单编号
                helper.setText(R.id.tv_order_multi_section_order_num, mContext.getString(R.string.order_num, item.orderId))
                        .setText(R.id.tv_order_multi_section_package_num, item.packageNum);
                break;
            case ApplyAfterSalesMultiBean.TYPE_ITEM_GOODS_SELECT://选择商品
                convertGoods(helper, item);
                break;
            case ApplyAfterSalesMultiBean.TYPE_ITEM_APPLY://申请内容
                helper.addOnClickListener(R.id.tv_apply_after_sales_replace_goods)
                        .addOnClickListener(R.id.tv_apply_after_sales_return_money);
                if (item.applyType != null)
                    switch (item.applyType) {
                        case ApplyAfterSalesMultiBean.APPLY_TYPE_REPLACEMENT:
                            //换货
                            helper.setTextColor(R.id.tv_apply_after_sales_replace_goods, Color.WHITE)
                                    .setBackgroundRes(R.id.tv_apply_after_sales_replace_goods, R.drawable.shape_order_btn_yellow);
                            helper.setTextColor(R.id.tv_apply_after_sales_return_money, Color.parseColor("#262626"))
                                    .setBackgroundRes(R.id.tv_apply_after_sales_return_money, R.drawable.shape_order_btn);
                            break;
                        case ApplyAfterSalesMultiBean.APPLY_TYPE_RETURNED:
                            //退款
                            helper.setTextColor(R.id.tv_apply_after_sales_replace_goods, Color.parseColor("#262626"))
                                    .setBackgroundRes(R.id.tv_apply_after_sales_replace_goods, R.drawable.shape_order_btn);
                            helper.setTextColor(R.id.tv_apply_after_sales_return_money, Color.WHITE)
                                    .setBackgroundRes(R.id.tv_apply_after_sales_return_money, R.drawable.shape_order_btn_yellow);
                            break;
                    }
                ((EditText) helper.getView(R.id.et_apply_after_sales_apply_reason)).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        getItem(0).reason = s.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                break;
            case ApplyAfterSalesMultiBean.TYPE_ITEM_IMAGE://图片
                if (item.isAddImage) {
                    helper.setVisible(R.id.close, false)
                            .setImageResource(R.id.image, R.drawable.add_image)
                            .addOnClickListener(R.id.image);
                } else {
                    helper.setVisible(R.id.close, true)
                            .addOnClickListener(R.id.close)
                            .addOnClickListener(R.id.image);
                    Glide.with(mContext).load(item.imageUri).fitCenter().into((ImageView) helper.getView(R.id.image));
                }
                break;
        }
    }

    /**
     * 商品
     */
    private void convertGoods(BaseViewHolder helper, ApplyAfterSalesMultiBean item) {
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.pic,
                (ImageView) helper.getView(R.id.iv_order_goods_image));
        helper.setText(R.id.tv_order_goods_title, item.name)
                .setText(R.id.tv_order_goods_properties, item.category)
                .setText(R.id.tv_order_goods_price, mContext.getString(R.string.account, item.price))
                .setText(R.id.tv_order_goods_num, String.valueOf(item.availableSkuCount))
                .addOnClickListener(R.id.iv_check_goods)
                .addOnClickListener(R.id.iv_goods_spu_sub)
                .addOnClickListener(R.id.iv_goods_spu_add);

        if (item.isSelected) {
            helper.setImageResource(R.id.iv_check_goods, R.drawable.icon_circle_selected);
        } else {
            helper.setImageResource(R.id.iv_check_goods, R.drawable.icon_circle_unselected);
        }
    }
}
