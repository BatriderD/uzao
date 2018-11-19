package com.zhaotai.uzao.ui.design.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.ArtFontTopicBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.ScreenUtils;

/**
 * description:  艺术字内容item
 * author : ZP
 * date: 2018/1/8 0008.
 */

public class ArtFontContentAdapter extends BaseRecyclerAdapter<ArtFontTopicBean, BaseViewHolder> {
    public ArtFontContentAdapter() {
        super(R.layout.item_catch_content);
    }

    @Override
    protected void convert(BaseViewHolder helper, ArtFontTopicBean item) {
        CardView cardView = helper.getView(R.id.cd_home_list);
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) cardView.getLayoutParams();
        layoutParams.height = (ScreenUtils.getScreenWidth(mContext) - 12) / 2;
        cardView.setLayoutParams(layoutParams);

        ImageView ivContent = helper.getView(R.id.iv_catch_word);

        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.getImage(), ivContent, R.mipmap.ic_place_holder, R.mipmap.ic_error);
    }
}
