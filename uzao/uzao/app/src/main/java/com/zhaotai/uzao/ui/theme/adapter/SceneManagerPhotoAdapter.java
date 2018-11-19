package com.zhaotai.uzao.ui.theme.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.SceneManagerAlbumBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.utils.ScreenUtils;
import com.zhaotai.uzao.utils.StringUtil;

public class SceneManagerPhotoAdapter extends BaseRecyclerAdapter<SceneManagerAlbumBean, BaseViewHolder> {
    private boolean able = false;

    public SceneManagerPhotoAdapter(boolean ableManage) {
        super(R.layout.item_scene_manager_photo);
        this.able = ableManage;
    }

    @Override
    protected void convert(BaseViewHolder helper, SceneManagerAlbumBean item) {
        ImageView ivPhoto = helper.getView(R.id.iv_photo_head);
        ViewGroup.LayoutParams layoutParams = ivPhoto.getLayoutParams();
        layoutParams.width = (int) (ScreenUtils.getScreenWidth(mContext) / 2 - PixelUtil.dp2px(25));
        layoutParams.height = (int) (ScreenUtils.getScreenWidth(mContext) / 2 - PixelUtil.dp2px(25));
        ivPhoto.setLayoutParams(layoutParams);

        if (item.isAdd) {
            ivPhoto.setImageResource(R.drawable.ic_add_photo);
            helper.setText(R.id.tv_photo_name, " ");
        } else if (StringUtil.isEmpty(item.getAlbumCover())) {
            ivPhoto.setImageResource(R.drawable.ic_empty_photo);
        } else {
            GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.getAlbumCover(), ivPhoto);
        }

        String photoCount = item.getPhotoCount();
        helper.setText(R.id.tv_photo_name, item.getAlbumName())
                .setText(R.id.tv_photo_count, StringUtil.isEmpty(photoCount) || photoCount.equals("0") ? "" : "(" + photoCount + ")")
                .setGone(R.id.iv_photo_del, !item.isAdd && able)
                .addOnClickListener(R.id.iv_photo_del);

    }
}
