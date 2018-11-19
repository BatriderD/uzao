package com.zhaotai.uzao.ui.theme.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.SceneManagerPostBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.TimeUtils;
import com.zhaotai.uzao.utils.transform.BaseTransform;
import com.zhaotai.uzao.utils.transform.CircleTransform;

/**
 * 帖子管理页面的管理功能
 */
public class SceneManagerPostAdapter extends BaseRecyclerAdapter<SceneManagerPostBean, BaseViewHolder> {
    private boolean able;

    public SceneManagerPostAdapter(boolean able) {
        super(R.layout.item_scene_manager_post);
        this.able = able;
    }

    @Override
    protected void convert(BaseViewHolder helper, SceneManagerPostBean item) {
//        int position = helper.getPosition();

//        View image = helper.getView(R.id.iv_post_image);
//        if (position % 3 == 0) {
//            video.setVisibility(View.GONE);
//            image.setVisibility(View.GONE);
//        } else if (position % 3 == 1) {
//            video.setVisibility(View.VISIBLE);
//            image.setVisibility(View.GONE);
//        } else if (position % 3 == 2) {
//            video.setVisibility(View.GONE);
//            image.setVisibility(View.VISIBLE);
//        }
        View video = helper.getView(R.id.iv_post_video);
        video.setVisibility(View.GONE);
        //头像
        ImageView ivHead = helper.getView(R.id.iv_post_head);
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.getAvatar(), ivHead, R.drawable.ic_default_head, R.drawable.ic_default_head, new CircleTransform(mContext));
        //内容
        String images = item.getImages();
        String[] imgs = images.split(",");
        ImageView ivImage = helper.getView(R.id.iv_post_image);
        if (imgs.length != 0 && !StringUtil.isEmpty(imgs[0])) {
            ivImage.setVisibility(View.VISIBLE);
            GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + imgs[0], ivImage, R.mipmap.ic_place_holder,R.mipmap.ic_error);
        } else {
            ivImage.setVisibility(View.GONE);
        }


        helper.setText(R.id.tv_post_name, item.getNickName() == null ? "未知用户" : item.getNickName())
                .setText(R.id.tv_post_time, TimeUtils.dateFormatToYear_Month_Day(Long.valueOf(item.getCreateDate())))
                .setText(R.id.tv_post_title, item.getTitle())
                .setText(R.id.tv_post_content, item.getAbstractContent())
                .setText(R.id.iv_post_comment, item.getCommentCount() + "条评论")
                .addOnClickListener(R.id.iv_post_more)
                .setGone(R.id.iv_post_more, able)
                .setGone(R.id.iv_post_essence, item.getIsEssence().equals("Y"));
    }
}
