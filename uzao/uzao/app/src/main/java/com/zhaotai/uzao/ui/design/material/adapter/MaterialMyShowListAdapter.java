package com.zhaotai.uzao.ui.design.material.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.MaterialDetailBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;

import java.text.DecimalFormat;

/**
 * description: 我的素材显示adapter
 * author : ZP
 * date: 2018/1/13 0013.
 */

public class MaterialMyShowListAdapter extends BaseRecyclerAdapter<MaterialDetailBean, BaseViewHolder> {
    // 0收藏 1 购买 2 上传
    private int type;
    private DecimalFormat df = new DecimalFormat("######0.00");
    public MaterialMyShowListAdapter() {
        super(R.layout.item_designe_all_material);
    }

    @Override
    protected void convert(BaseViewHolder helper, MaterialDetailBean item) {
        ImageView ivPic = helper.getView(R.id.iv_item_all_material_pic);
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.thumbnail, ivPic);
        TextView tvPrice = helper.getView(R.id.tv_item_all_material_price);
        Double price;
        switch (type) {
            case 0:
                price = Double.valueOf(item.priceY);
                tvPrice.setSelected(false);
                if (price == 0) {
                    helper.setText(R.id.tv_item_all_material_price, "免费");
                } else {
                    String priceY = df.format(price);
                    helper.setText(R.id.tv_item_all_material_price, "¥" + priceY);
                }
                break;

            case 1:
                helper.setText(R.id.tv_item_all_material_price, "已购");
                tvPrice.setSelected(true);
                break;
            case 2:
                price = Double.valueOf(item.priceY);
                tvPrice.setSelected(false);
                if (price == 0) {
                    helper.setText(R.id.tv_item_all_material_price, "免费");
                } else {
                    String priceY = df.format(price);
                    helper.setText(R.id.tv_item_all_material_price, "¥" + priceY);

                }
                break;
        }
        helper.setText(R.id.tv_material_name, item.sourceMaterialName);
    }

    public void setType(int type) {
        this.type = type;
    }
}
