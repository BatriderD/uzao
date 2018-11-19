package com.zhaotai.uzao.bean;

import android.graphics.Bitmap;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.bsaerx.TokenException;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.utils.bitmap.BitmapLoadUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * description: 上传工具类
 * author : ZP
 * date: 2017/12/14 0014.
 */

public class UpLoadPicUtils {
    /**
     * 上传文件 目前主要是图片 上传文件 上传目录
     *
     * @param file
     * @param rxSubscriber
     */
    public static void upLoadpic(File file, RxSubscriber<UploadFileBean> rxSubscriber) {
        // 创建 RequestBody，用于封装构建RequestBody
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part  和后端约定好Key，这里的partName是用image
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        Api.getDefault().uploadTheFile(body)
                .compose(RxHandleResult.<UploadFileBean>handleResult())
                .subscribe(rxSubscriber);
    }

    /**
     * 上传文件 目前主要是图片 上传文件 上传目录
     *
     * @param path
     * @param rxSubscriber
     */
    public static void upLoadAndZipPic(String path, RxSubscriber<UploadFileBean> rxSubscriber) {

        Bitmap bitmap = BitmapLoadUtils.decodeSampledBitmapFromFile(path, 1024, 1024);
        if (bitmap == null) {
            ToastUtil.showShort("数据上传失败");
            return;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        String timeStamp = simpleDateFormat.format(new Date());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), bos.toByteArray());

        MultipartBody.Part body = MultipartBody.Part.createFormData("file", timeStamp + ".JPEG", requestFile);

        Api.getDefault().uploadTheFile(body)
                .compose(RxHandleResult.<UploadFileBean>handleResult())
                .subscribe(rxSubscriber);
    }

    public static void upLoadPic(Bitmap bitmap, RxSubscriber<UploadFileBean> rxSubscriber) {
        Observable.just(bitmap)
                .concatMap(new Function<Bitmap, ObservableSource<MultipartBody.Part>>() {
                    @Override
                    public ObservableSource<MultipartBody.Part> apply(@io.reactivex.annotations.NonNull Bitmap bitmap) throws Exception {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
                        String timeStamp = simpleDateFormat.format(new Date());

                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 80, bos);
                        RequestBody requestFile =
                                RequestBody.create(MediaType.parse("image/png"), bos.toByteArray());
                        MultipartBody.Part body = MultipartBody.Part.createFormData("file", timeStamp + ".png", requestFile);

                        return Observable.just(body);
                    }
                }).flatMap(new Function<MultipartBody.Part, Observable<BaseResult<UploadFileBean>>>() {
            @Override
            public Observable<BaseResult<UploadFileBean>> apply(@io.reactivex.annotations.NonNull MultipartBody.Part part) throws Exception {
                return Api.getDefault().uploadTheFile(part);
            }
        }).map(new Function<BaseResult<UploadFileBean>, UploadFileBean>() {
            @Override
            public UploadFileBean apply(@io.reactivex.annotations.NonNull BaseResult<UploadFileBean> tBaseResult) throws Exception {
                if (!tBaseResult.getCode().equals("OK")) {
                    if (RxHandleResult.isTokenException(tBaseResult.getMessage())) {
                        throw new TokenException("无效的Token");
                    }
                    throw new Exception(tBaseResult.getMessage());
                }
                return tBaseResult.getResult();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxSubscriber);
    }


    public static void upLoadMyDesign(Bitmap bitmap, RxSubscriber<UploadFileBean> rxSubscriber) {

        Observable.just(bitmap)
                .concatMap(new Function<Bitmap, ObservableSource<MultipartBody.Part>>() {
                    @Override
                    public ObservableSource<MultipartBody.Part> apply(@io.reactivex.annotations.NonNull Bitmap bitmap) throws Exception {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
                        String timeStamp = simpleDateFormat.format(new Date());


                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 80, bos);
                        RequestBody requestFile =
                                RequestBody.create(MediaType.parse("image/png"), bos.toByteArray());
                        MultipartBody.Part body = MultipartBody.Part.createFormData("file", timeStamp + ".png", requestFile);

                        return Observable.just(body);
                    }
                }).flatMap(new Function<MultipartBody.Part, Observable<BaseResult<UploadFileBean>>>() {
            @Override
            public Observable<BaseResult<UploadFileBean>> apply(@io.reactivex.annotations.NonNull MultipartBody.Part part) throws Exception {

                return Api.getDefault().uploadDesignFile(part);
            }
        }).map(new Function<BaseResult<UploadFileBean>, UploadFileBean>() {
            @Override
            public UploadFileBean apply(@io.reactivex.annotations.NonNull BaseResult<UploadFileBean> tBaseResult) throws Exception {
                if (!tBaseResult.getCode().equals("OK")) {
                    if (RxHandleResult.isTokenException(tBaseResult.getMessage())) {
                        throw new TokenException("无效的Token");
                    }
                    throw new Exception(tBaseResult.getMessage());
                }
                return tBaseResult.getResult();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxSubscriber);


    }

    public static void upLoadMyDesign(File file, RxSubscriber<UploadFileBean> rxSubscriber) {

        Observable.just(file)
                .concatMap(new Function<File, ObservableSource<MultipartBody.Part>>() {
                    @Override
                    public ObservableSource<MultipartBody.Part> apply(@io.reactivex.annotations.NonNull File file) throws Exception {
                        RequestBody requestFile =
                                RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        // MultipartBody.Part  和后端约定好Key，这里的partName是用image
                        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

                        return Observable.just(body);
                    }
                }).flatMap(new Function<MultipartBody.Part, Observable<BaseResult<UploadFileBean>>>() {
            @Override
            public Observable<BaseResult<UploadFileBean>> apply(@io.reactivex.annotations.NonNull MultipartBody.Part part) throws Exception {

                return Api.getDefault().uploadDesignFile(part);
            }
        }).map(new Function<BaseResult<UploadFileBean>, UploadFileBean>() {
            @Override
            public UploadFileBean apply(@io.reactivex.annotations.NonNull BaseResult<UploadFileBean> tBaseResult) throws Exception {
                if (!tBaseResult.getCode().equals("OK")) {
                    if (RxHandleResult.isTokenException(tBaseResult.getMessage())) {
                        throw new TokenException("无效的Token");
                    }
                    throw new Exception(tBaseResult.getMessage());
                }
                return tBaseResult.getResult();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxSubscriber);
    }

    /**
     * 上传多图到指定仓库
     *
     * @param bitmaps      多图图片
     * @param bucket       上传图片目标仓库
     * @param rxSubscriber 上传图片
     */
    public static void upLoadFiles(ArrayList<Bitmap> bitmaps, final String bucket, RxSubscriber<List<String>> rxSubscriber) {
//        ArrayList<MultipartBody.Part> bodyList = getBodyList(bitmaps);
//        Api.getDefault().uploadTheFiles(bucket, bodyList)
//                .compose(RxHandleResult.<List<String>>handleResult())
//                .subscribe(rxSubscriber);
    }


    public static ArrayList<MultipartBody.Part> getBodyList(ArrayList<Bitmap> bitmaps) {
        ArrayList<MultipartBody.Part> parts = new ArrayList<>();
        for (Bitmap bitmap : bitmaps) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
            String timeStamp = simpleDateFormat.format(new Date());

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), bos.toByteArray());
            MultipartBody.Part body = MultipartBody.Part.createFormData("files", timeStamp + ".png", requestFile);
            parts.add(body);
        }
        return parts;
    }

    public static MultipartBody.Part getBody(Bitmap bitmap) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        String timeStamp = simpleDateFormat.format(new Date());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), bos.toByteArray());

        return MultipartBody.Part.createFormData("file", timeStamp + ".png", requestFile);
    }
}
