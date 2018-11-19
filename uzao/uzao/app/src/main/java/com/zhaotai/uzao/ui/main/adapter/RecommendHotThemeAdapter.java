package com.zhaotai.uzao.ui.main.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.RecommendBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.GsonUtil;
import com.zhaotai.uzao.utils.ImageSizeUtil;
import com.zhaotai.uzao.utils.ScreenUtils;
import com.zhaotai.uzao.utils.StringUtil;

/**
 * description: 推荐热门主题
 * author : ZP
 * date: 2018/3/22 0022.
 */

public class RecommendHotThemeAdapter extends BaseQuickAdapter<RecommendBean.ValueBean, BaseViewHolder> {
    public RecommendHotThemeAdapter() {
        super(R.layout.item_fragment_recommend_hot_theme_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, RecommendBean.ValueBean item) {
        ImageView imageView = helper.getView(R.id.iv_theme_pic);
        LinearLayout llroot = helper.getView(R.id.ll_theme_root);
        ViewGroup.LayoutParams layoutParams = llroot.getLayoutParams();
        layoutParams.width = (int) (ScreenUtils.getScreenWidth(mContext) / 2.5);
        llroot.setLayoutParams(layoutParams);
        String strContentBody = item.getContentBody();
        if (strContentBody != null) {
            RecommendBean.ThemeContentBody contentBody = GsonUtil.getGson().fromJson(strContentBody, RecommendBean.ThemeContentBody.class);
            String cover = contentBody.getCover();
            if (cover != null) {
                GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(cover), imageView);
            }
            String name = contentBody.getName();
            if (!StringUtil.isEmpty(item.alias)) {
                helper.setText(R.id.tv_theme_name, item.alias);
            } else if (!StringUtil.isEmpty(name)) {
                helper.setText(R.id.tv_theme_name, name);
            }

        }


    }
}
