package com.zhaotai.uzao.ui.theme.adapter;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.ThemeTemplateBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.ScreenUtils;

/**
 * description:
 * author : ZP
 * date: 2018/1/23 0023.
 */

public class ThemeTemplateAdapter extends BaseQuickAdapter<ThemeTemplateBean, BaseViewHolder> {
    private int selected = -1;

    public ThemeTemplateAdapter() {
        super(R.layout.item_theme_template);
    }

    @Override
    protected void convert(BaseViewHolder helper, ThemeTemplateBean item) {
        ImageView ivPic = helper.getView(R.id.iv_theme_template_pic);
        GlideLoadImageUtil.load(mContext, ApiConstants.THEME_TEMPLATE + item.getWebPicUrl(), ivPic);
        System.out.println("xxx" + ApiConstants.THEME_TEMPLATE + item.getWebPicUrl());
        RelativeLayout rl_pic = helper.getView(R.id.rl_pic);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rl_pic.getLayoutParams();
        layoutParams.width = (ScreenUtils.getScreenWidth(mContext) - 24) / 2;
        layoutParams.height = (ScreenUtils.getScreenWidth(mContext) - 24) / 2;
        helper.setVisible(R.id.iv_theme_template_selected_sign, item.selected);
        rl_pic.setLayoutParams(layoutParams);
        helper.setText(R.id.tv_theme_template_name, item.getName());
        helper.addOnClickListener(R.id.tv_theme_template_preview);

    }

    public void setselected(int position) {
        if (selected >= 0 && getData().size() > selected) {
            getItem(selected).selected = false;
        }
        getItem(position).selected = true;
        notifyItemChanged(selected);
        notifyItemChanged(position);
        selected = position;
    }

    public int getSelected() {
        return selected;
    }

}
