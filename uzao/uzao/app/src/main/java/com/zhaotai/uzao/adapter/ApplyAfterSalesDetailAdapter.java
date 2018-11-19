package com.zhaotai.uzao.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;

/**
 * Time: 2017/6/15
 * Created by LiYou
 * Description : 申请售后详情
 */

public class ApplyAfterSalesDetailAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

    public ApplyAfterSalesDetailAdapter() {
        super(R.layout.item_apply_after_sales_detail_image);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item, (ImageView) helper.getView(R.id.iv_grid_image));
    }
}
