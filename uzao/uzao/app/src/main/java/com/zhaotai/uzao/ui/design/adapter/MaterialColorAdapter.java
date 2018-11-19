package com.zhaotai.uzao.ui.design.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.ui.design.bean.MaterialColorBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.transform.CornerTransform;

import java.util.List;

/**
 * 颜色选择器adapter
 */

public class MaterialColorAdapter extends BaseQuickAdapter<MaterialColorBean, BaseViewHolder> {
    public MaterialColorAdapter(List<MaterialColorBean> data) {
        super(R.layout.item_mode_material, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, MaterialColorBean item) {
        helper.setText(R.id.tv_mode_material_name, item.colourName)
                .setVisible(R.id.tv_mode_material_name,false);

        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.colourImage, (ImageView) helper.getView(R.id.iv_mode_material_image), R.mipmap.ic_place_holder, new CornerTransform(mContext, 6));
        if (item.isSelected) {
            helper.setVisible(R.id.iv_mode_material_image_mask, true);
        } else {
            helper.setVisible(R.id.iv_mode_material_image_mask, false);
        }
    }
}
