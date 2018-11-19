package com.zhaotai.uzao.ui.shopping.adapter;


import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;

/**
 * Time: 2017/11/22
 * Created by LiYou
 * Description :
 */

public class SimilarProductAdapter extends BaseQuickAdapter<GoodsBean,BaseViewHolder> {

    public SimilarProductAdapter() {
        super(R.layout.item_similar_product);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsBean item) {
        //商品图片
        GlideLoadImageUtil.load(mContext,ApiConstants.UZAOCHINA_IMAGE_HOST + item.thumbnail,(ImageView) helper.getView(R.id.iv_similar_spu_image));
        //商品名字
        helper.setText(R.id.tv_similar_spu_name,item.spuName)
                .setText(R.id.tv_similar_product_price,item.price);
    }
}
