package com.zhaotai.uzao.ui.design.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.ArtFontTopicBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;

/**
 * description: 艺术字adapter
 * author : ZP
 * date: 2017/12/12 0012.
 */

public class TextArtFontAdapter extends BaseQuickAdapter<ArtFontTopicBean, BaseViewHolder> {


    public TextArtFontAdapter() {
        super(R.layout.item_art_font);
    }

    @Override
    protected void convert(BaseViewHolder helper, ArtFontTopicBean item) {
        ImageView ivFont = helper.getView(R.id.iv_art_font);
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.getImage(), ivFont);
    }

}
