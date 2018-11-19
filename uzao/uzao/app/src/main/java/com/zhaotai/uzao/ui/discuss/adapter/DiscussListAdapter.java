package com.zhaotai.uzao.ui.discuss.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.DiscussBean;
import com.zhaotai.uzao.bean.MaterialDiscussBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.GsonUtil;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.TimeUtils;
import com.zhaotai.uzao.utils.transform.CircleTransform;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * description: 素材讨论
 * author : ZP
 * date: 2018/1/9 0009.
 */

public class DiscussListAdapter extends BaseRecyclerAdapter<DiscussBean, BaseViewHolder> {
    // type =0   隐藏；   type=1 查看全部 ； type = 2 查看对话 3 隐藏点赞
    private int type;
    private boolean likeDelVisable = true;

    public DiscussListAdapter(int type) {
        super(R.layout.item_material_discuss);
        this.type = type;
    }

    public void setLikeDelVisable(boolean likeDelVisable) {
        this.likeDelVisable = likeDelVisable;
    }


    @Override
    protected void convert(BaseViewHolder helper, DiscussBean item) {
        ImageView headPhoto = helper.getView(R.id.iv_discuss_head_photo);
        String userInfo = item.getUserInfo();
        MaterialDiscussBean.DesignInfoBean designInfoBean = GsonUtil.getGson().fromJson(userInfo, MaterialDiscussBean.DesignInfoBean.class);

        if (designInfoBean != null) {
            if (type == 2 && !item.isFirst) {
                String toNickName = designInfoBean.getToNickName();
                if (StringUtil.isEmpty(toNickName)) {
                    helper.setVisible(R.id.tv_discuss_to_nick_name, false);
                    helper.setVisible(R.id.tv_discuss_to, false);
                } else {
                    helper.setText(R.id.tv_discuss_to_nick_name, toNickName);
                    helper.setVisible(R.id.tv_discuss_to_nick_name, true);
                    helper.setVisible(R.id.tv_discuss_to, true);
                }

            } else {
                helper.setVisible(R.id.tv_discuss_to_nick_name, false);
                helper.setVisible(R.id.tv_discuss_to, false);
            }
            String nickName = designInfoBean.getNickName();
            if (100 == designInfoBean.userType && nickName != null) {
                helper.setText(R.id.tv_discuss_nick_name, nickName);
                GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + designInfoBean.getAvatar(), headPhoto, R.drawable.ic_default_head, new CircleTransform(mContext));
            } else if (300 == designInfoBean.userType) {
                helper.setText(R.id.tv_discuss_nick_name, "优造中国");
                GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + designInfoBean.getAvatar(), headPhoto, R.drawable.ic_uzao_default_head, new CircleTransform(mContext));
            }
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        helper.setText(R.id.tv_discuss_time, TimeUtils.dateFormat(Long.valueOf(item.getRecDate()), simpleDateFormat))
                .setText(R.id.tv_discuss_content, item.getCommentBody())
                .setText(R.id.tv_discuss_like, String.valueOf(item.getUpvoteCount()))
                .setGone(R.id.tv_discuss_like, likeDelVisable)
                .setGone(R.id.iv_discuss_del, likeDelVisable);

        helper.getView(R.id.tv_discuss_like)
                .setSelected(item.isUpvote());
        switch (type) {
            case 0:
                helper.setVisible(R.id.iv_discuss_all, false);
                break;
            case 1:
                helper.setText(R.id.iv_discuss_all, "查看全部");
                helper.setVisible(R.id.iv_discuss_all, true);
                break;
            case 2:
                helper.setText(R.id.iv_discuss_all, "查看对话");
                helper.setVisible(R.id.iv_discuss_all, "Y".equals(item.getIsConversation()));
                break;
        }
        String userId = LoginHelper.getUserId();
        helper.setVisible(R.id.iv_discuss_del, userId != null && userId.equals(item.getUserId()));
        helper.addOnClickListener(R.id.tv_discuss_like)
                .addOnClickListener(R.id.iv_discuss_all)
                .addOnClickListener(R.id.iv_discuss_to_talk)
                .addOnClickListener(R.id.iv_discuss_del);

    }
}
