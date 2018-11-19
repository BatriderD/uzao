package com.zhaotai.uzao.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;

/**
 * Time: 2018/1/26
 * Created by LiYou
 * Description :
 */

public class RecommendDesignProductAdapter extends BaseQuickAdapter<GoodsBean, BaseViewHolder> {


    public RecommendDesignProductAdapter() {
        super(R.layout.item_recommend_design_product);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsBean item) {
        helper.setText(R.id.tv_product_name, item.spuName);
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.thumbnail, (ImageView) helper.getView(R.id.iv_product_pic));
        helper.addOnClickListener(R.id.tv_go_to_design);
    }
}
