package com.zhaotai.uzao.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;
import com.zhaotai.uzao.R;

/**
 * Time: 2017/5/18
 * Created by LiYou
 * Description : 轮播器的 图片加载起
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Glide.with(context).load((String)path).placeholder(R.mipmap.ic_place_holder).into(imageView);
    }
}
