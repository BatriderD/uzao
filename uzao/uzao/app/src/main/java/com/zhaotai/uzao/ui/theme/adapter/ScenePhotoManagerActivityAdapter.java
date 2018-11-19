package com.zhaotai.uzao.ui.theme.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.ScenePhotoManagerBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;

public class ScenePhotoManagerActivityAdapter extends BaseRecyclerAdapter<ScenePhotoManagerBean, BaseViewHolder> {


    private boolean mAble;

    public ScenePhotoManagerActivityAdapter() {
        super(R.layout.item_scene_photo_manager);
    }

    @Override
    protected void convert(BaseViewHolder helper, ScenePhotoManagerBean item) {
        ImageView ivContent = helper.getView(R.id.iv_content);
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.getFileId(), ivContent, R.mipmap.ic_place_holder, R.mipmap.ic_error);
        helper.getView(R.id.iv_selector).setVisibility(mAble ? View.VISIBLE : View.INVISIBLE);
        helper.getView(R.id.iv_selector).setSelected(item.selected);
        helper.addOnClickListener(R.id.iv_selector);

    }

    public void ableSelected(boolean able) {
        mAble = able;
    }

    public boolean isAbleSelected() {
        return mAble;
    }
}
