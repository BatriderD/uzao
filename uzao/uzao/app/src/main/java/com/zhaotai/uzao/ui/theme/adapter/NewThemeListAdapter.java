package com.zhaotai.uzao.ui.theme.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.ThemeListBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.ImageSizeUtil;
import com.zhaotai.uzao.utils.StringUtil;

/**
 * description:
 * author : ZP
 * date: 2018/3/17 0017.
 */

public class NewThemeListAdapter extends BaseRecyclerAdapter<ThemeListBean, BaseViewHolder> {
    public NewThemeListAdapter() {
        super(R.layout.item_theme_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, ThemeListBean item) {
        helper.setText(R.id.tv_theme_name, item.getThemeName())
                .setText(R.id.tv_theme_comment, StringUtil.formatCount(item.getCommentCount()))
                .setText(R.id.tv_theme_visited, StringUtil.formatCount(item.getViewCount()))
                .setText(R.id.tv_theme_collected, StringUtil.formatCount(item.getFavoriteCount()));

        ImageView ivPic = helper.getView(R.id.iv_theme_pic);
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(item.getPic(), ImageSizeUtil.ImageSize.X200), ivPic, R.mipmap.ic_place_holder, R.mipmap.ic_error);
    }
}
