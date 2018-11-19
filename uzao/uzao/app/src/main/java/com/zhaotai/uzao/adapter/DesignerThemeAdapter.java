package com.zhaotai.uzao.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.ThemeBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.ImageSizeUtil;

/**
 * Time: 2018/1/29
 * Created by LiYou
 * Description :设计师页面-主题列表
 */

public class DesignerThemeAdapter extends BaseRecyclerAdapter<ThemeBean, BaseViewHolder> {

    public DesignerThemeAdapter() {
        super(R.layout.item_designer_material);
    }

    @Override
    protected void convert(BaseViewHolder helper, ThemeBean item) {
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(item.pic), (ImageView) helper.getView(R.id.iv_item_designer_list_material_img));
        helper.setText(R.id.tv_item_designer_list_material_name, item.themeName)
                .setText(R.id.tv_comment_count, Integer.valueOf(item.commentCount) > 999 ? "999+" : item.commentCount)
                .setText(R.id.tv_view_count, Integer.valueOf(item.viewCount) > 999 ? "999+" : item.commentCount)
                .setText(R.id.tv_like_count, Integer.valueOf(item.favoriteCount) > 999 ? "999+" : item.commentCount);
    }

}
