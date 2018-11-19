package com.zhaotai.uzao.ui.design.material.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;

import java.text.DecimalFormat;

/**
 * description: 所有素材 的Adapter
 * author : ZP
 * date: 2018/1/8 0008.
 */

public class MaterialAllListAdapter extends BaseRecyclerAdapter<MaterialListBean, BaseViewHolder> {
    private DecimalFormat df = new DecimalFormat("######0.00");

    public MaterialAllListAdapter() {
        super(R.layout.item_designe_all_material);
    }


    @Override
    protected void convert(BaseViewHolder helper, MaterialListBean item) {
        ImageView iv = helper.getView(R.id.iv_item_all_material_pic);
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.pic, iv);

        TextView tvPrice = helper.getView(R.id.tv_item_all_material_price);
        Double price = Double.valueOf(item.priceY);
        if (price == 0) {
            //免费
            helper.setText(R.id.tv_item_all_material_price, "免费");
            tvPrice.setSelected(false);
        } else {
            //正确使用价格
            String priceY = df.format(price);
            helper.setText(R.id.tv_item_all_material_price, "¥" + priceY);
            tvPrice.setSelected(false);

        }
        helper.setText(R.id.tv_material_name, item.materialName);

    }
}
