package com.zhaotai.uzao.ui.design.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.xiaopo.flying.sticker.BitmapUtils;
import com.xiaopo.flying.sticker.StickerDataInfo;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.ArtWorkBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.design.contract.MyFilterContract;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * description:  滤镜presenter
 * author : ZP
 * date: 2017/9/28 0028.
 */

public class MyFilterPresenter extends MyFilterContract.Presenter {
    private MyFilterContract.View mView;

    public MyFilterPresenter(Context context, MyFilterContract.View view) {
        mContext = context;
        mView = view;
    }

    //    invert 反色  grayScale 灰白 GrayScale 浮雕 oil 油画
    @Override
    public void getFilterList(String url) {

        Api.getDefault().getFilterListPic()
                .compose(RxHandleResult.<List<ArtWorkBean>>handleResult())
                .subscribe(new RxSubscriber<List<ArtWorkBean>>(mContext, true) {
                    @Override
                    public void _onNext(List<ArtWorkBean> artWorkBean) {
                        mView.showFilterList(artWorkBean);
                    }

                    @Override
                    public void _onError(String message) {
                        System.out.println();
                    }
                });

    }

    @Override
    public void getFilterPic(final String type, String url) {
        if (StringUtil.isEmpty(type)) {
            mView.showPic("", "");
            return;
        }
        Api.getDefault().getFilterPic(type, url)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext) {
                    @Override
                    public void _onNext(String s) {
                        mView.showPic(s, type);
                        System.out.println("滤镜获取成功" + s);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("滤镜增加失败");
                        mView.showPic("", "");
                    }
                });
    }

    /**
     * 获得正确的图片并返回
     *
     * @param info
     */
    @Override
    public void getCurrentBitmap(final StickerDataInfo info) {
//        标准恢复图片
        Observable.just(info)
                .map(new Function<StickerDataInfo, Bitmap>() {
                    @Override
                    public Bitmap apply(@NonNull StickerDataInfo info) throws Exception {
                        //1.获取图片：滤镜后的图片或者原图
                        Bitmap resultBitmap;
                        Bitmap bitmap;
                        if (StringUtil.isEmpty(info.filterType)) {
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
                if (info.isAlph) {
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
                        mView.showCurrentBitmap(bitmap);
                        Log.d("MyFilterPresenter", "获取图片成功_onNext:" + bitmap);

                    }

                    @Override
                    public void _onError(String message) {
                        mView.showCurrentBitmap(null);
                        Log.d("MyFilterPresenter", "获取图片失败_onError: " + message);
                    }
                });
    }
}
