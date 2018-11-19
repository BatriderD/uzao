package com.zhaotai.uzao.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.ImageSizeUtil;

/**
 * Time: 2018/3/7
 * Created by LiYou
 * Description : 商品推荐
 */

public class RecommendSpuAdapter extends BaseQuickAdapter<GoodsBean, BaseViewHolder> {

    public RecommendSpuAdapter() {
        super(R.layout.item_recommend_spu);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsBean item) {

        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(item.thumbnail)
                , (ImageView) helper.getView(R.id.iv_product_spu_image));

        helper.setText(R.id.iv_product_spu_name, item.spuName)
                .setText(R.id.tv_product_introduce, item.description)
                .setText(R.id.tv_product_price, item.displayPriceY);
    }
}
