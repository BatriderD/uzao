package com.zhaotai.uzao.ui.shopping.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.utils.DecimalUtil;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.ImageSizeUtil;
import com.zhaotai.uzao.utils.StringUtil;

import java.util.List;

/**
 * Time: 2018/3/20
 * Created by LiYou
 * Description :
 */

public class ShoppingCartAdapter extends BaseMultiItemQuickAdapter<MultiCartItem, BaseViewHolder> {


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ShoppingCartAdapter(List<MultiCartItem> data) {
        super(data);
        addItemType(MultiCartItem.TYPE_NORMAL, R.layout.item_shopping_cart_normal);
        addItemType(MultiCartItem.TYPE_ACTIVITY, R.layout.item_shopping_cart_activity);
        addItemType(MultiCartItem.TYPE_INVALID, R.layout.item_shopping_cart_invalid);
        addItemType(MultiCartItem.TYPE_GUESS_TITLE, R.layout.include_guess_you_like_title);
        addItemType(MultiCartItem.TYPE_RECOMMEND, R.layout.item_recommend_spu_divider);
        addItemType(MultiCartItem.TYPE_EMPTY_GOODS, R.layout.vw_empty_shopping_cart);
        addItemType(MultiCartItem.TYPE_EMPTY_INVALID_GOODS, R.layout.vw_empty_shopping_cart_invalid);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiCartItem item) {
        switch (helper.getItemViewType()) {
            case MultiCartItem.TYPE_NORMAL://正常商品
                if (!StringUtil.isEmpty(item.spuName) && !StringUtil.isEmpty(item.unitPrice) && !StringUtil.isEmpty(item.count)
                        && !StringUtil.isEmpty(item.storeCount) && !StringUtil.isEmpty(item.properties)) {
                    helper.setText(R.id.shopping_cart_title, item.spuName)
                            .setText(R.id.shopping_cart_spu_price, String.format(mContext.getResources().getString(R.string.account), DecimalUtil.getPrice(item.unitPrice)))
                            .setText(R.id.shopping_cart_spu_num, Integer.valueOf(item.count) < Integer.valueOf(item.storeCount) ?
                                    item.count : item.storeCount)
                            .setText(R.id.shopping_cart_spu_properties, item.properties.replace(";", "  "));
                }
                //商品图片
                GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(item.spuPic), (ImageView) helper.getView(R.id.shopping_cart_spu_image));
                if (item.isGroupHeader) {
                    helper.setGone(R.id.ll_shopping_cart_section_normal, true);
                } else {
                    helper.setGone(R.id.ll_shopping_cart_section_normal, false);
                }
                helper.addOnClickListener(R.id.iv_shopping_cart_section_check)
                        .addOnClickListener(R.id.iv_check_goods)
                        .addOnClickListener(R.id.shopping_cart_spu_image)
                        .addOnClickListener(R.id.shopping_cart_spu_add)
                        .addOnClickListener(R.id.shopping_cart_spu_sub)
                        .addOnClickListener(R.id.shopping_cart_spu_num);

                //checkbox
                if (item.isSelect) {
                    helper.setImageResource(R.id.iv_check_goods, R.drawable.icon_circle_selected);
                } else {
                    helper.setImageResource(R.id.iv_check_goods, R.drawable.icon_circle_unselected);
                }
                if (item.isGroupHeader) {
                    if (item.isGroupSelect) {
                        helper.setImageResource(R.id.iv_shopping_cart_section_check, R.drawable.icon_circle_selected);
                    } else {
                        helper.setImageResource(R.id.iv_shopping_cart_section_check, R.drawable.icon_circle_unselected);
                    }
                }
                break;
            case MultiCartItem.TYPE_ACTIVITY://活动商品
                helper.setText(R.id.shopping_cart_title, item.spuName)
                        .setText(R.id.shopping_cart_spu_price, String.format(mContext.getResources().getString(R.string.account), DecimalUtil.getPrice(item.unitPrice)))
                        .setText(R.id.shopping_cart_spu_num, item.count)
                        .setText(R.id.shopping_cart_spu_properties, item.properties.replace(";", "  "));
                //商品图片
                GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(item.spuPic), (ImageView) helper.getView(R.id.shopping_cart_spu_image));
                if (item.isGroupHeader) {
                    helper.setGone(R.id.ll_shopping_cart_section_activity, true)
                            .addOnClickListener(R.id.tv_shopping_cart_section_right_text);
                    GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(item.activityIcon), (ImageView) helper.getView(R.id.iv_shopping_cart_section_icon));
                    if (item.cut == null) {
                        helper.setText(R.id.tv_shopping_cart_section_right_text, "去凑单");
                    } else {
                        helper.setText(R.id.tv_shopping_cart_section_right_text, "再逛逛");
                    }
                } else {
                    helper.setGone(R.id.ll_shopping_cart_section_activity, false);
                }
                helper.addOnClickListener(R.id.iv_shopping_cart_section_check)
                        .addOnClickListener(R.id.iv_check_goods)
                        .addOnClickListener(R.id.shopping_cart_spu_image)
                        .addOnClickListener(R.id.shopping_cart_spu_add)
                        .addOnClickListener(R.id.shopping_cart_spu_sub)
                        .addOnClickListener(R.id.shopping_cart_spu_num);

                //checkbox
                if (item.isSelect) {
                    helper.setImageResource(R.id.iv_check_goods, R.drawable.icon_circle_selected);
                } else {
                    helper.setImageResource(R.id.iv_check_goods, R.drawable.icon_circle_unselected);
                }
                if (item.isGroupHeader) {
                    if (item.isGroupSelect) {
                        helper.setImageResource(R.id.iv_shopping_cart_section_check, R.drawable.icon_circle_selected);
                    } else {
                        helper.setImageResource(R.id.iv_shopping_cart_section_check, R.drawable.icon_circle_unselected);
                    }
                }
                break;
            case MultiCartItem.TYPE_INVALID://失效商品
                helper.setText(R.id.shopping_cart_title, item.spuName)
                        .setText(R.id.shopping_cart_spu_price, String.format(mContext.getResources().getString(R.string.account), DecimalUtil.getPrice(item.unitPrice)))
                        .setText(R.id.shopping_cart_spu_properties, item.properties.replace(";", "  "));
                //商品图片
                GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(item.spuPic), (ImageView) helper.getView(R.id.shopping_cart_spu_image));
                if (item.isGroupHeader) {
                    helper.setGone(R.id.ll_shopping_cart_section_invalid, true)
                            .setText(R.id.tv_shop_cart_failure_goods_count, String.valueOf(item.invalidCount))
                            .addOnClickListener(R.id.tv_shopping_cart_section_right_clear_failure);
                } else {
                    helper.setGone(R.id.ll_shopping_cart_section_invalid, false);
                }
                helper.addOnClickListener(R.id.tv_shop_cart_similar_goods);
                break;
            case MultiCartItem.TYPE_GUESS_TITLE://猜你喜欢title
                break;
            case MultiCartItem.TYPE_RECOMMEND://推荐商品
                GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(item.spuPic)
                        , (ImageView) helper.getView(R.id.iv_product_spu_image));

                helper.setText(R.id.iv_product_spu_name, item.spuName)
                        .setText(R.id.tv_product_introduce, item.description)
                        .setText(R.id.tv_product_price, item.displayPriceY)
                        .addOnClickListener(R.id.iv_product_spu_image);
                break;
            case MultiCartItem.TYPE_EMPTY_GOODS:
                helper.addOnClickListener(R.id.empty_view_btn);
                break;
            case MultiCartItem.TYPE_EMPTY_INVALID_GOODS:
                helper.addOnClickListener(R.id.empty_view_btn);
                break;
        }

    }
}
