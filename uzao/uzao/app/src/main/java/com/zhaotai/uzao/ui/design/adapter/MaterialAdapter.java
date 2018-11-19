package com.zhaotai.uzao.ui.design.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.ui.design.bean.MaterialBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.transform.CornerTransform;

import java.util.List;

/**
 * 材质选择器adapter
 */

public class MaterialAdapter extends BaseQuickAdapter<MaterialBean, BaseViewHolder> {
    public MaterialAdapter(List<MaterialBean> data) {
        super(R.layout.item_mode_material, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, MaterialBean item) {
        helper.setText(R.id.tv_mode_material_name, item.textureName);

        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.textureImage, (ImageView) helper.getView(R.id.iv_mode_material_image), R.mipmap.ic_place_holder, new CornerTransform(mContext, 6));
        if (item.isSelected) {
            helper.setVisible(R.id.iv_mode_material_image_mask, true);
        } else {
            helper.setVisible(R.id.iv_mode_material_image_mask, false);
        }
    }
}
