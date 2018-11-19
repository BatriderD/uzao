package com.zhaotai.uzao.ui.design.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.ArtWorkBean;
import com.zhaotai.uzao.utils.GlideUtil;
import com.zhaotai.uzao.utils.StringUtil;

/**
 * description:  图片滤镜adapter
 * author : ZP
 * date: 2017/12/28 0028.
 */

public class MyFilterAdapter extends BaseQuickAdapter<ArtWorkBean, BaseViewHolder> {
    public MyFilterAdapter() {
        super(R.layout.item_filter_content);
    }

    @Override
    protected void convert(BaseViewHolder helper, ArtWorkBean item) {
        helper.setText(R.id.tv_filter_name, item.getDescription());
        if (StringUtil.isEmpty(item.getEntryKey())) {
            GlideUtil.getInstance().loadImageViewRound(mContext, (ImageView) helper.getView(R.id.iv_filter_content), R.drawable.icon_filter_original, 5);
            helper.setImageResource(R.id.iv_filter_content, R.drawable.icon_filter_original);
        } else {
            GlideUtil.getInstance().loadImageViewRound(mContext, (ImageView) helper.getView(R.id.iv_filter_content), ApiConstants.UZAOCHINA_IMAGE_HOST + item.getEntryValue(), 5);
//            GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.getEntryValue(), (ImageView) helper.getView(R.id.iv_filter_content), new CornerTransform(mContext, 5));
        }
    }
}
