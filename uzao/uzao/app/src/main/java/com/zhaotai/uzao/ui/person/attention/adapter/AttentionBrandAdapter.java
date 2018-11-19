package com.zhaotai.uzao.ui.person.attention.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.BrandBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;

/**
 * Time: 2018/1/29
 * Created by LiYou
 * Description :
 */

public class AttentionBrandAdapter extends BaseRecyclerAdapter<BrandBean, BaseViewHolder> {

    public AttentionBrandAdapter() {
        super(R.layout.item_attention_brand);
    }

    @Override
    protected void convert(BaseViewHolder helper, BrandBean item) {
        helper.setText(R.id.tv_brand_name, item.brandName)
                .setText(R.id.tv_brand_fans, "粉丝  " + item.followCount);

        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.brandLogo, (ImageView) helper.getView(R.id.iv_brand_pic));

        helper.addOnClickListener(R.id.rl_attention)
                .addOnClickListener(R.id.tv_brand_attention)
                .addOnClickListener(R.id.tv_brand_discuss);
    }
}
