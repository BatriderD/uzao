package com.zhaotai.uzao.ui.designer.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.DesignerBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.transform.CircleTransform;

/**
 * description: 设计师列表
 * author : ZP
 * date: 2018/3/16 0016.
 */

public class DesignerListAdapter extends BaseRecyclerAdapter<DesignerBean, BaseViewHolder> {

    public DesignerListAdapter() {
        super(R.layout.item_designer_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, DesignerBean item) {
        ImageView ivAvatar = helper.getView(R.id.iv_designer_avatar);
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.avatar, ivAvatar, R.drawable.ic_default_head, R.drawable.ic_default_head, new CircleTransform(mContext));

        //设计师名字
        helper.setText(R.id.tv_nick_name, item.nickName);
        //作品数

        //粉丝数
        helper.setText(R.id.tv_designer_info, "粉丝:" +
                item.followCount + "\n作品数:" + String.valueOf(item.spuCount + item.materialCount));

        TextView attention = helper.getView(R.id.tv_designer_attention);
        if (item.data == null || !"Y".equals(item.data.isFavorited)) {
            attention.setText("关注");
            attention.setSelected(true);
        } else {
            attention.setText("已关注");
            attention.setSelected(false);
        }

        helper.addOnClickListener(R.id.tv_designer_attention);
    }
}
