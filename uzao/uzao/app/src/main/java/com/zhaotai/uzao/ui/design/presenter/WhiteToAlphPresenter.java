package com.zhaotai.uzao.ui.design.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.xiaopo.flying.sticker.BitmapUtils;
import com.xiaopo.flying.sticker.StickerDataInfo;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.design.contract.WhiteToAlphlContract;
import com.zhaotai.uzao.utils.StringUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * description:  白色背景透明化的presenter
 * author : ZP
 * date: 2017/9/28 0028.
 */

public class WhiteToAlphPresenter extends WhiteToAlphlContract.Presenter {
    private WhiteToAlphlContract.View mView;

    public WhiteToAlphPresenter(Context context, WhiteToAlphlContract.View view) {
        mContext = context;
        mView = view;
    }

    /**
     * 获得正确的图片并返回
     *
     * @param info
     * @param isChanged
     */
    @Override
    public void getCurrentBitmap(final StickerDataInfo info, final boolean isChanged) {
        //        标准恢复图片
        Observable.just(info)
                .map(new Function<StickerDataInfo, Bitmap>() {
                    @Override
                    public Bitmap apply(@NonNull StickerDataInfo info) throws Exception {
                        //1.获取图片：滤镜后的图片或者原图
                        Bitmap resultBitmap;
                        Bitmap bitmap;
                        if (StringUtil.isEmpty(info.filterName)) {
                            if (StringUtil.isEmpty(info.url)) {
                                Observable.error(new Exception("图片信息错误"));
                            }
                            bitmap = Glide.with(mContext)
                                    .load(ApiConstants.UZAOCHINA_IMAGE_HOST + info.url)
                                    .asBitmap()
                                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();

                        } else {
                            bitmap = Glide.with(mContext)
                                    .load(ApiConstants.UZAOCHINA_IMAGE_HOST + info.filterPic)
                                    .asBitmap()
                                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                        }
                        resultBitmap = BitmapUtils.copyBitmap(bitmap);
                        return resultBitmap;
                    }
                }).map(new Function<Bitmap, Bitmap>() {
            @Override
            public Bitmap apply(@io.reactivex.annotations.NonNull Bitmap srcBitmap) throws Exception {
//                根据需要进行白色背景透明化
                if (isChanged) {
                    Bitmap bitmap = BitmapUtils.changeBitmapWhite(srcBitmap);
                    BitmapUtils.recycleBitmap(srcBitmap);
                    return bitmap;
                } else {
                    return srcBitmap;
                }
            }
        }).map(new Function<Bitmap, Bitmap>() {
            @Override
            public Bitmap apply(@io.reactivex.annotations.NonNull Bitmap srcBitmap) throws Exception {
                if (info.clipBean != null) {
                    Bitmap bitmap = Bitmap.createBitmap(srcBitmap, info.clipBean.getX(), info.clipBean.getY(), info.clipBean.getWidth(), info.clipBean.getHeight());
                    BitmapUtils.recycleBitmap(srcBitmap);
                    return bitmap;
                } else {
                    return srcBitmap;
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<Bitmap>(mContext) {
                    @Override
                    public void _onNext(Bitmap bitmap) {
                        Log.d("WhiteToAlphPresenter", "获取图片成功_onNext: ");
                        mView.showChangedBitmap(bitmap);
                    }

                    @Override
                    public void _onError(String message) {
                        Log.d("WhiteToAlphPresenter", "获取图片失败_onNext: " + message);
                        mView.showChangedBitmap(null);
                    }
                });
    }


}
