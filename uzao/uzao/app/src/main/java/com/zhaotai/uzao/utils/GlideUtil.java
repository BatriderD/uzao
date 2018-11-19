package com.zhaotai.uzao.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bsaerx.RxSubscriber;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Time: 2017/5/22
 * Created by LiYou
 * Description : Glide加载图片的封装，圆形、圆角，模糊等处理操作用到了jp.wasabeef:glide-transformations:2.0.1
 * Glide默认使用httpurlconnection协议，可以配置为OkHttp
 */

public class GlideUtil {


    private static GlideUtil mInstance;

    private GlideUtil() {
    }

    public static GlideUtil getInstance() {
        if (mInstance == null) {
            synchronized (GlideUtil.class) {
                if (mInstance == null) {
                    mInstance = new GlideUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * 常量
     */
    private static class Contants {
        private static final int BLUR_VALUE = 20; //模糊
        private static final int CORNER_RADIUS = 20; //圆角
        private static final float THUMB_SIZE = 0.5f; //0-1之间  10%原图的大小
    }

    /**
     * 常规加载图片
     *
     * @param context   d
     * @param imageView 图片容器
     * @param imgUrl    图片地址
     * @param isFade    是否需要动画
     */
    public void loadImage(Context context, ImageView imageView,
                          String imgUrl, boolean isFade) {
        if (isFade) {
            Glide.with(context)
                    .load(imgUrl)
                    .error(R.mipmap.ic_error)
                    .crossFade()
                    .priority(Priority.NORMAL) //下载的优先级
                    //all:缓存源资源和转换后的资源 none:不作任何磁盘缓存
                    //source:缓存源资源   result：缓存转换后的资源
                    .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(imgUrl)
                    .error(R.mipmap.ic_error)
                    .into(imageView);
        }
    }

    public void loadImage(Context context, ImageView imageView,
                          String imgUrl) {
        Glide.with(context)
                .load(imgUrl)
                .placeholder(R.mipmap.ic_place_holder)
                .dontAnimate()
                .error(R.mipmap.ic_error)
                .into(imageView);
    }

    public void loadImage(Activity context, ImageView imageView,
                          String imgUrl, int resError, int resHolder, int animate) {
        if (animate == 0) {
            Glide.with(context)
                    .load(imgUrl)
                    .placeholder(resHolder)
                    .dontAnimate()
                    .error(resError)
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(imgUrl)
                    .placeholder(resHolder)
                    .animate(animate)
                    .error(resError)
                    .into(imageView);
        }
    }

    public void loadImage(Fragment context, ImageView imageView,
                          String imgUrl, int resError, int resHolder, int animate) {
        if (animate == 0) {
            Glide.with(context)
                    .load(imgUrl)
                    .placeholder(resHolder)
                    .dontAnimate()
                    .error(resError)
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(imgUrl)
                    .placeholder(resHolder)
                    .animate(animate)
                    .error(resError)
                    .into(imageView);
        }
    }

    public void loadImage(Context context, ImageView imageView,
                          String imgUrl, int resError, int resHolder, int animate) {
        if (animate == 0) {
            Glide.with(context)
                    .load(imgUrl)
                    .placeholder(resHolder)
                    .dontAnimate()
                    .error(resError)
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(imgUrl)
                    .placeholder(resHolder)
                    .animate(animate)
                    .error(resError)
                    .into(imageView);
        }

    }




    /**
     * 加载缩略图
     *
     * @param context   d
     * @param imageView 图片容器
     * @param imgUrl    图片地址
     */
    public void loadThumbnailImage(Context context, ImageView imageView, String imgUrl) {
        Glide.with(context)
                .load(imgUrl)
                .error(R.mipmap.ic_error)
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .thumbnail(Contants.THUMB_SIZE)
                .into(imageView);
    }

    /**
     * 加载图片并设置为指定大小
     *
     * @param context    d
     * @param imageView  d
     * @param imgUrl     d
     * @param withSize   d
     * @param heightSize d
     */
    public void loadOverrideImage(Context context, ImageView imageView,
                                  String imgUrl, int withSize, int heightSize) {
        Glide.with(context)
                .load(imgUrl)
                .error(R.mipmap.ic_error)
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy( DiskCacheStrategy.ALL) //缓存策略
                .override(withSize, heightSize)
                .into(imageView);
    }

    /**
     * 加载图片并对其进行模糊处理
     *
     * @param context   d
     * @param imageView d
     * @param imgUrl    d
     */
    public void loadBlurImage(Context context, ImageView imageView, String imgUrl) {
        Glide.with(context)
                .load(imgUrl)
                .error(R.mipmap.ic_error)
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .bitmapTransform(new BlurTransformation(context, Contants.BLUR_VALUE))
                .into(imageView);
    }

    /**
     * 加载圆图 d
     *
     * @param context   d
     * @param imageView d
     * @param imgUrl    d
     */
    public void loadCircleImage(Context context, ImageView imageView, String imgUrl) {
        Glide.with(context)
                .load(imgUrl)
                .error(R.mipmap.ic_error)
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .bitmapTransform(new CropCircleTransformation(context))
                .into(imageView);
    }

    /**
     * 加载模糊的圆角的图片
     *
     * @param context   d
     * @param imageView d
     * @param imgUrl    d
     */
    public void loadBlurCircleImage(Context context, ImageView imageView, String imgUrl) {
        Glide.with(context)
                .load(imgUrl)
                .error(R.mipmap.ic_error)
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .bitmapTransform(
                        new BlurTransformation(context, Contants.BLUR_VALUE),
                        new CropCircleTransformation(context))
                .into(imageView);
    }

    /**
     * 加载圆角图片
     *
     * @param context   d
     * @param imageView d
     * @param imgUrl    d
     */
    public void loadCornerImage(Context context, ImageView imageView, String imgUrl) {
        Glide.with(context)
                .load(imgUrl)
                .error(R.mipmap.ic_error)
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .bitmapTransform(
                        new RoundedCornersTransformation(
                                context, Contants.CORNER_RADIUS, Contants.CORNER_RADIUS))
                .into(imageView);
    }

    /**
     * 加载模糊的圆角图片
     *
     * @param context   d
     * @param imageView d
     * @param imgUrl    d
     */
    public void loadBlurCornerImage(Context context, ImageView imageView, String imgUrl) {
        Glide.with(context)
                .load(imgUrl)
                .error(R.mipmap.ic_error)
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .bitmapTransform(
                        new BlurTransformation(context, Contants.BLUR_VALUE),
                        new RoundedCornersTransformation(
                                context, Contants.CORNER_RADIUS, Contants.CORNER_RADIUS))
                .into(imageView);
    }

    /**
     * 同步加载图片
     *
     * @param context d
     * @param imgUrl  d
     * @param target  d
     */
    public void loadBitmapSync(Context context, String imgUrl, SimpleTarget<Bitmap> target) {
        Glide.with(context)
                .load(imgUrl)
                .asBitmap()
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .into(target);
    }

    /**
     * 加载gif
     *
     * @param context   d
     * @param imageView d
     * @param imgUrl    d
     */
    public void loadGifImage(Context context, ImageView imageView, String imgUrl) {
        Glide.with(context)
                .load(imgUrl)
                .asGif()
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .error(R.mipmap.ic_error)
                .into(imageView);
    }

    /**
     * 加载gif的缩略图
     *
     * @param context   d
     * @param imageView d
     * @param imgUrl    d
     */
    public void loadGifThumbnailImage(Context context, ImageView imageView, String imgUrl) {
        Glide.with(context)
                .load(imgUrl)
                .asGif()
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .error(R.mipmap.ic_error)
                .thumbnail(Contants.THUMB_SIZE)
                .into(imageView);
    }

    /**
     * 加在制定圆角的圆角图片
     *
     * @param context   上下文
     * @param imageView 目标控件
     * @param res       资源文件
     * @param round     圆角角度
     */
    public void loadImageViewRound(Context context, ImageView imageView, Integer res, int round) {
        Glide.with(context).load(res)
                .placeholder(R.mipmap.ic_place_holder)
                .dontAnimate()
                .error(R.mipmap.ic_error)
                .transform(new GlideRoundTransform(context, round))
                .into(imageView);
    }

    /**
     * 加在制定圆角的圆角图片
     *
     * @param context   上下文
     * @param imageView 目标控件
     * @param imgUrl    图片url
     * @param round     圆角角度
     */
    public void loadImageViewRound(Context context, ImageView imageView, String imgUrl, int round) {
        Glide.with(context).load(imgUrl)
                .placeholder(R.mipmap.ic_place_holder)
                .dontAnimate()
                .error(R.mipmap.ic_error)
                .transform(new GlideRoundTransform(context, round))
                .into(imageView);
    }

     /**
     * 恢复请求，一般在停止滚动的时候
     */
    public void resumeRequests(Context context) {
        Glide.with(context).resumeRequests();
    }

    /**
     * 暂停请求 正在滚动的时候
     */
    public void pauseRequests(Context context) {
        Glide.with(context).pauseRequests();
    }

    /**
     * 清除磁盘缓存
     */
    public void clearDiskCache(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(context).clearDiskCache();//清理磁盘缓存 需要在子线程中执行
            }
        }).start();
    }

    /**
     * 清除内存缓存
     */
    public void clearMemory(Context context) {
        Glide.get(context).clearMemory();//清理内存缓存  可以在UI主线程中进行
    }


    /**
     * 下载图片不保存图片到内存缓存 RxSubscriber<Bitmap>接收
     * @param mContext context
     * @param url 图片地址
     * @param subscriber 接受
     */
    public static void downLoadBitmap(final Context mContext, String url,RxSubscriber<Bitmap> subscriber) {
        Observable.just(url)
                .map(new Function<String, Bitmap>() {
                    @Override
                    public Bitmap apply(@io.reactivex.annotations.NonNull String url) throws Exception {
                        Bitmap myBitmap = Glide.with(mContext)
                                .load(url)
                                .asBitmap()
                                .skipMemoryCache(true)
                                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                        return myBitmap;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
