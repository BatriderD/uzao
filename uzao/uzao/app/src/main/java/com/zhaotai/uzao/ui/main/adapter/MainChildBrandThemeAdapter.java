package com.zhaotai.uzao.ui.main.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.DynamicValuesBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;

import java.util.List;

/**
 * Time: 2018/3/23
 * Created by LiYou
 * Description : 品牌
 */

public class MainChildBrandThemeAdapter extends BaseQuickAdapter<DynamicValuesBean, BaseViewHolder> {

    public MainChildBrandThemeAdapter(@Nullable List<DynamicValuesBean> data) {
        super(R.layout.item_main_child_brand_theme_image, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DynamicValuesBean item) {
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.image, (ImageView) helper.getView(R.id.iv_main_child_brand_pic));
    }
}
