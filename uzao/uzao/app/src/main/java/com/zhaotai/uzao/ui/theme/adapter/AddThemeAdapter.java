package com.zhaotai.uzao.ui.theme.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.ThemeBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;

/**
 * description:
 * author : ZP
 * date: 2018/1/22 0022.
 */

public class AddThemeAdapter extends BaseQuickAdapter<ThemeBean,BaseViewHolder> {
    public AddThemeAdapter() {
        super(R.layout.item_add_theme);
    }

    @Override
    protected void convert(BaseViewHolder helper, ThemeBean item) {
        ImageView ivPic = helper.getView(R.id.iv_theme_pic);
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.cover, ivPic);
        helper.setText(R.id.tv_theme_name, item.name)
                .setText(R.id.tv_material_count, item.materialCount + "件素材")
                .setText(R.id.tv_product_count, item.spuCount + "件商品");
    }
}
