package com.zhaotai.uzao.ui.theme.adapter;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;

import java.util.List;

/**
 * description:
 * author : ZP
 * date: 2018/2/1 0001.
 */

public class CollectionMaterialToThemeAdapter extends BaseQuickAdapter<MaterialListBean, BaseViewHolder> {
    public CollectionMaterialToThemeAdapter() {
        super(R.layout.item_add_theme_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, MaterialListBean item) {
        ImageView ivPic = helper.getView(R.id.iv_pic);
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.thumbnail, ivPic);
        helper.setText(R.id.tv_name, item.sourceMaterialName);
        LinearLayout lltag = helper.getView(R.id.ll_tag);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 14, 0);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        List<MaterialListBean.TagBean> tags = item.tags;
        if (tags != null) {
            for (int i = 0; i < Math.min(tags.size(), 3); i++) {
                TextView tag = (TextView) View.inflate(mContext, R.layout.item_add_theme_list_tag, null);
                String name = tags.get(i).name;
                tag.setText(name);
                tag.setSelected("促销".equals(name));
                lltag.addView(tag, layoutParams);
            }
        }
        helper.setText(R.id.tv_price, "Y" + item.priceY);
    }
}
