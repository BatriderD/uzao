package com.zhaotai.uzao.ui.search.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;

/**
 * Time: 2017/11/30
 * Created by LiYou
 * Description : 商品列表
 */

public class ProductListAdapter extends BaseQuickAdapter<GoodsBean, BaseViewHolder> {

    public ProductListAdapter() {
        super(R.layout.item_product_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsBean item) {
        //商品图片
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.pic, (ImageView) helper.getView(R.id.iv_product_spu_image));
        //商品名字
        helper.setText(R.id.iv_product_spu_name, item.spuName)
                .setText(R.id.tv_product_introduce, item.describe)
                .setText(R.id.tv_product_price, item.priceY);
        //判断是否有进行的活动
        if (item.data != null && item.data.hasGoingActivity.equals("Y")) {
            helper.setGone(R.id.tv_spu_tag_activity, true)
                    .setText(R.id.tv_spu_tag_activity, item.data.activityName);
        } else {
            helper.setGone(R.id.tv_spu_tag_activity, false);
        }
        //判断商品类型 定制商品 显示 可定制
        if (item.spuType.equals("mall")) {
            helper.setGone(R.id.tv_spu_tag_design, false);
        } else {
            helper.setGone(R.id.tv_spu_tag_design, true)
                    .setText(R.id.tv_spu_tag_design, "可定制");
        }
    }
}
