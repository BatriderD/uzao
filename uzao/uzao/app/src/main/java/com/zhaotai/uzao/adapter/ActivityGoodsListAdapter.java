package com.zhaotai.uzao.adapter;


import android.content.Context;
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
import com.zhaotai.uzao.bean.FilterBean;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.utils.StringUtil;


/**
 * Time: 2017/5/12
 * Created by LiYou
 * Description : 活动 列表
 */

public class ActivityGoodsListAdapter extends BaseQuickAdapter<GoodsBean, BaseViewHolder> {
    private Context mContext;

    public ActivityGoodsListAdapter(Context context) {
        super(R.layout.item_category_goods_list);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsBean item) {
        helper.setText(R.id.iv_activity_spu_name, item.spuName)
                .setText(R.id.tv_activity_price, "¥" + item.priceY);

        if (StringUtil.isEmpty(item.designerId)) {
            helper.setText(R.id.tv_activity_designer, "设计师:  " + GlobalVariable.UZAO_MATERIAL_NAME);
        } else {
            if (item.data != null)
                helper.setText(R.id.tv_activity_designer, "设计师:  " + item.data.nickName);
        }
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.pic, (ImageView) helper.getView(R.id.iv_activity_spu_image));
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.activityIcon, (ImageView) helper.getView(R.id.iv_activity_icon));
        LinearLayout mLlTag = helper.getView(R.id.ll_activity_tag);
        mLlTag.removeAllViews();
        //标签
        for (FilterBean tag1 : item.tags) {
            TextView tagView = new TextView(mContext);
            tagView.setPadding((int) PixelUtil.dp2px(3), 0, (int) PixelUtil.dp2px(3), 0);
            tagView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_bg_tag));
            tagView.setTextColor(Color.parseColor("#fc5900"));
            tagView.setTextSize(9);
            tagView.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMarginEnd(8);
            tagView.setLayoutParams(params);
            tagView.setText(tag1.name);
            mLlTag.addView(tagView);
        }
    }
}
