package com.zhaotai.uzao.ui.person.material.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.MyUploadMaterialBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.TimeUtils;

import java.text.DecimalFormat;

/**
 * description: 我的素材 上传素材
 * author : ZP
 * date: 2018/1/9 0009.
 */

public class MyUploadMaterialAdapter extends BaseRecyclerAdapter<MyUploadMaterialBean, BaseViewHolder> {
    public MyUploadMaterialAdapter() {
        super(R.layout.item_my_upload_material);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyUploadMaterialBean item) {
        ImageView ivPic = helper.getView(R.id.iv_my_upload_material_pic);
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.getThumbnail(), ivPic);
        DecimalFormat df = new DecimalFormat("######0.00");
        String priceY = String.valueOf(item.getPrice());
        if (item.getPriceY() != null) {
            priceY = df.format(Double.valueOf(item.getPriceY()));
        }
        helper.setText(R.id.tv_my_upload_material_title, item.getSourceMaterialName())
                .setText(R.id.tv_my_material_upload_time, TimeUtils.dateFormatToYear_Month_Day(Long.valueOf(item.getRecDate())))
                .setText(R.id.tv_my_upload_material_price, "free".equals(item.getSaleMode()) ? "免费" : "¥" + priceY);

    }
}
