package com.zhaotai.uzao.ui.design.material.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.MyMaterialBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;

/**
 * description: 我的素材显示adapter
 * author : ZP
 * date: 2018/1/13 0013.
 */

public class MaterialSearchBoughtShowListAdapter extends BaseQuickAdapter<MyMaterialBean, BaseViewHolder> {


    public MaterialSearchBoughtShowListAdapter() {
        super(R.layout.item_designe_all_material);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyMaterialBean item) {
        ImageView ivPic = helper.getView(R.id.iv_item_all_material_pic);
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.getThumbnail(), ivPic);
        TextView tvPrice = helper.getView(R.id.tv_item_all_material_price);
        tvPrice.setVisibility(View.GONE);
        helper.setText(R.id.tv_material_name, item.getSourceMaterialName());
    }
}
