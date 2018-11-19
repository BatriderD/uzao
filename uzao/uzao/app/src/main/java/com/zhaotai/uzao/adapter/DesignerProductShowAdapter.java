package com.zhaotai.uzao.adapter;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.FilterBean;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.GlideUtil;
import com.zhaotai.uzao.utils.ImageSizeUtil;
import com.zhaotai.uzao.utils.PixelUtil;

/**
 * description:  设计师主页列表adapter
 * author : zp
 * date: 2017/8/3
 */

public class DesignerProductShowAdapter extends BaseRecyclerAdapter<GoodsBean, BaseViewHolder> {

    public DesignerProductShowAdapter() {
        super(R.layout.item_designer_product_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsBean item) {
        ImageView iv_product = helper.getView(R.id.iv_item_designer_list_product_img);
        //图片
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(item.pic), iv_product);
        //名称
        helper.setText(R.id.tv_item_designer_list_product_name, item.spuName);
        //商品标签
        LinearLayout mLlTag = helper.getView(R.id.ll_tag);
        mLlTag.removeAllViews();
        for (FilterBean tag1 : item.tags) {
            TextView tagView = new TextView(mContext);
            tagView.setPadding((int) PixelUtil.dp2px(6), 0, (int) PixelUtil.dp2px(6), 0);
            tagView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_bg_tag));
            tagView.setTextColor(Color.parseColor("#fc5900"));
            tagView.setTextSize(9);
            tagView.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMarginEnd(8);
            tagView.setLayoutParams(params);
            tagView.setText(tag1.name);
            mLlTag.addView(tagView);
        }
    }
}
