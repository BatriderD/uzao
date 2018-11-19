package com.zhaotai.uzao.ui.design.catchWord.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.CatchWordBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.utils.ScreenUtils;

/**
 * description:  流行词内容列表bean
 * author : ZP
 * date: 2018/1/8 0008.
 */

public class CatchContentAdapter extends BaseRecyclerAdapter<CatchWordBean, BaseViewHolder> {
    public CatchContentAdapter() {
        super(R.layout.item_catch_content);
    }

    @Override
    protected void convert(BaseViewHolder helper, CatchWordBean item) {
        CardView cd_content = helper.getView(R.id.cd_home_list);
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) cd_content.getLayoutParams();
//        layoutParams.width = ScreenUtils.getScreenWidth(mContext) / 2 - (int) PixelUtil.dp2px(9);
        layoutParams.height = ScreenUtils.getScreenWidth(mContext) / 2 - (int) PixelUtil.dp2px(9);
        cd_content.setLayoutParams(layoutParams);

        ImageView ivContent = helper.getView(R.id.iv_catch_word);
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.getImage(), ivContent);
    }
}
