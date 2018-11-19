package com.zhaotai.uzao.ui.main.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.DynamicBodyBean;
import com.zhaotai.uzao.bean.DynamicValuesBean;
import com.zhaotai.uzao.utils.ImageSizeUtil;

import java.util.List;

/**
 * Time: 2018/3/23
 * Created by LiYou
 * Description : 品牌
 */

public class MainChildBrandAdapter extends BaseQuickAdapter<DynamicValuesBean, BaseViewHolder> {
    private Gson gson;

    public MainChildBrandAdapter(@Nullable List<DynamicValuesBean> data) {
        super(R.layout.item_main_child_brand_image, data);
        gson = new Gson();
    }

    @Override
    protected void convert(BaseViewHolder helper, DynamicValuesBean item) {
        DynamicBodyBean body = gson.fromJson(item.contentBody, DynamicBodyBean.class);
        Glide.with(mContext).load(ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(body.brandLogo))
                .into((ImageView) helper.getView(R.id.iv_main_child_brand_pic));
    }
}
