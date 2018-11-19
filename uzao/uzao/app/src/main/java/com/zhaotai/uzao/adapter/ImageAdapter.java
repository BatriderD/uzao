package com.zhaotai.uzao.adapter;

import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;

import java.util.List;

/**
 * Time: 2017/6/13
 * Created by LiYou
 * Description : 上传图片
 */

public class ImageAdapter extends BaseQuickAdapter<Uri, BaseViewHolder> {

    public ImageAdapter(@LayoutRes int layoutResId, @Nullable List<Uri> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Uri item) {

        if (item.toString().equals("add")) {
            helper.setVisible(R.id.close, false)
                    .addOnClickListener(R.id.image);
            ((ImageView) helper.getView(R.id.image)).setScaleType(ImageView.ScaleType.FIT_CENTER);
            ((ImageView) helper.getView(R.id.image)).setImageResource(R.drawable.add_image);
        } else {
            helper.setVisible(R.id.close, true)
                    .addOnClickListener(R.id.image)
                    .addOnClickListener(R.id.close);

            Glide.with(mContext).load(item).centerCrop().into((ImageView) helper.getView(R.id.image));
        }
    }
}
