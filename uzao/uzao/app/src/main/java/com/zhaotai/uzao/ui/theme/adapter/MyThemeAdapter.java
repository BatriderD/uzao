package com.zhaotai.uzao.ui.theme.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.ThemeBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.ScreenUtils;
import com.zhaotai.uzao.utils.TimeUtils;

/**
 * description: 我的主题Adapter
 * author : ZP
 * date: 2018/1/23 0023.
 */

public class MyThemeAdapter extends BaseRecyclerAdapter<ThemeBean, BaseViewHolder> {

    private boolean showManager = false;

    public MyThemeAdapter() {
        super(R.layout.item_my_theme);
    }

    @Override
    protected void convert(BaseViewHolder helper, ThemeBean item) {

        helper.setText(R.id.tv_my_theme_time, "创建时间：" + TimeUtils.dateFormatToYear_Month_Day(item.recDate))
                //设置隐私还是公开的状态
                .setText(R.id.tv_my_theme_status, "Y".equals(item.isPublic) ? "公开" : "私密")
                //设置评论数
                .setText(R.id.tv_my_theme_comment, item.commentCount)
                //设置浏览数
                .setText(R.id.tv_my_theme_visited, item.viewCount)
                //设置昵称
                .setText(R.id.tv_my_theme_nickname, item.name)
                //设置收藏数
                .setText(R.id.tv_my_theme_collected, item.favoriteCount);

        //设置图片
        ImageView ivPic = helper.getView(R.id.iv_my_theme_pic);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivPic.getLayoutParams();
        layoutParams.width = ScreenUtils.getScreenWidth(mContext);
        //这个比例是计算来的 表明了大致宽度
        layoutParams.height = (int) (ScreenUtils.getScreenWidth(mContext) * 0.672);
        ivPic.setLayoutParams(layoutParams);
        //设置图片
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.cover, ivPic);
        //根据状态显示与否和是否是编辑状态
        helper.getView(R.id.iv_my_theme_selected_sign).setVisibility(showManager ? View.VISIBLE : View.GONE);
        helper.getView(R.id.iv_my_theme_selected_sign).setSelected(item.isSelected());
        helper.addOnClickListener(R.id.tv_my_theme_editor);
    }


    /**
     * 设置管理状态
     *
     * @param showManager 管理状态  true 管理  false 普通
     */
    public void setShowManager(boolean showManager) {
        this.showManager = showManager;
    }

}
