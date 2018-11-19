package com.zhaotai.uzao.ui.design.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.xiaopo.flying.sticker.BitmapUtils;
import com.xiaopo.flying.sticker.EmptySticker;
import com.xiaopo.flying.sticker.FontCache;
import com.xiaopo.flying.sticker.MyDrawableSticker;
import com.xiaopo.flying.sticker.MyDrawableStickerDataBean;
import com.xiaopo.flying.sticker.MyTextSticker;
import com.xiaopo.flying.sticker.SourceRectBean;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.StickerDataBean;
import com.xiaopo.flying.sticker.StickerDataInfo;
import com.xiaopo.flying.sticker.TextStickerDataBean;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.EventBean.EventFontToDownLoadBean;
import com.zhaotai.uzao.bean.LayerMetaJsonBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.design.contract.EditorFragmentContract;
import com.zhaotai.uzao.utils.FileUtil;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * description: 编辑器Fragment的 Presenter
 * author : ZP
 * date: 2018/4/9 0009.
 */

public class EditorFragmentPresenter extends EditorFragmentContract.Presenter {
    private EditorFragmentContract.View mView;
    private String TAG = "编辑器presenter:";

    public EditorFragmentPresenter(Context context, EditorFragmentContract.View view) {
        mContext = context;
        this.mView = view;
    }

    /**
     * 将sticker数据化保存
     *
     * @param stickers sticker列表
     * @return 元素的数据列表
     */
    @Override
    public List<StickerDataBean> saveStep(List<Sticker> stickers) {

        ArrayList<StickerDataBean> stickerDataStickerDataBeans = new ArrayList<>();
        for (int i = 0; i < stickers.size(); i++) {
            Sticker sticker = stickers.get(i);
            StickerDataBean stickerData = sticker.getStickerData();
            stickerDataStickerDataBeans.add(stickerData);
        }
        return stickerDataStickerDataBeans;
    }

    /**
     * 恢复数据
     *
     * @param layerMeta 保存的数据
     * @param scale     缩放系数
     */
    public void parseLayerMeta(final List<LayerMetaJsonBean.LayerMetaBean> layerMeta, final float scale) {
        if (layerMeta.size() == 0) {
//            通知所有已经被添加了
            mView.stickerAllInitFinish();
            return;
        }
        Observable.fromIterable(layerMeta)
                .concatMap(new Function<LayerMetaJsonBean.LayerMetaBean, ObservableSource<Sticker>>() {
                    @Override
                    public ObservableSource<Sticker> apply(@NonNull LayerMetaJsonBean.LayerMetaBean layerMetaBean) throws Exception {
                        Matrix matrix = new Matrix();
                        float x = scale * (layerMetaBean.getLocal().getX() - layerMetaBean.getLocal().getWidth() / 2);
                        float y = scale * (layerMetaBean.getLocal().getY() - layerMetaBean.getLocal().getHeight() / 2);
                        matrix.postScale(layerMetaBean.getScale() * scale, layerMetaBean.getScale() * scale);
                        Log.d(TAG, "啦啦啦apply: showWidth" + layerMetaBean.getLocal().getWidth() + " showHeight:" + layerMetaBean.getLocal().getHeight());
                        matrix.postRotate(layerMetaBean.getRotation(), layerMetaBean.getLocal().getWidth() * scale / 2, layerMetaBean.getLocal().getHeight() * scale / 2);
                        matrix.postTranslate(x, y);
                        //缩放系数计算
                        String locked = layerMetaBean.getLocked();
                        String type = layerMetaBean.getType();
                        if ("bitmap".equals(type)) {
//                            图片类型
//                          控件
                            MyDrawableSticker drawableSticker;

                            String url = layerMetaBean.getImg();
//                            获得原始图片
                            Bitmap originalBitmap;
                            try {
                                originalBitmap = Glide.with(mContext)
                                        .load(ApiConstants.UZAOCHINA_IMAGE_HOST + url)
                                        .asBitmap()
                                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                        .get();
                            } catch (Exception e) {
                                return Observable.just((Sticker) new EmptySticker());
                            }


                            Bitmap showBitmap = BitmapUtils.copyBitmap(originalBitmap);

//                            1 买单透明度
                            if (layerMetaBean.isTransparent()) {
                                Bitmap white_bitmap = BitmapUtils.changeBitmapWhite(showBitmap);
//                                回收bitmap
                                BitmapUtils.recycleBitmap(showBitmap);
                                showBitmap = white_bitmap;
                            }
//                            2买单裁剪
                            LayerMetaJsonBean.LayerMetaBean.SourceRectBean sourceRect = layerMetaBean.getSourceRect();
                            SourceRectBean sourceRectBean = null;
                            if (sourceRect != null) {
                                Bitmap clipBitmap = Bitmap.createBitmap(showBitmap, sourceRect.getX(), sourceRect.getY(), sourceRect.getWidth(), sourceRect.getHeight());
                                BitmapUtils.recycleBitmap(showBitmap);
                                showBitmap = clipBitmap;
                                sourceRectBean = new SourceRectBean(sourceRect.getX(), sourceRect.getY(), sourceRect.getWidth(), sourceRect.getHeight());
                            }

//                            生成drawableSticker
                            BitmapDrawable bitmapDrawable = new BitmapDrawable(mContext.getResources(), showBitmap);
                            drawableSticker = new MyDrawableSticker(url, bitmapDrawable);

                            drawableSticker.setMatrix(matrix);
                            //赋值设置信息
                            StickerDataInfo info = drawableSticker.getInfo();
                            info.isAlph = layerMetaBean.isTransparent();
                            info.url = url;
                            info.clipBean = sourceRectBean;
                            info.version = layerMetaBean.getVersion();
                            if (!StringUtil.isEmpty(layerMetaBean.getMaterialId())) {
                                info.materialId = layerMetaBean.getMaterialId();
                                info.isVector = layerMetaBean.isVector();
                                if (layerMetaBean.isVector()) {
                                    info.resizeScale = 1.0f;
                                } else {
                                    info.resizeScale = layerMetaBean.getResizeScale();
                                }
                            } else {
                                info.resizeScale = layerMetaBean.getResizeScale();
                            }
                            LayerMetaJsonBean.LayerMetaBean.Filter filter = layerMetaBean.getFilter();
                            if (filter != null) {
                                info.filterType = filter.getType();
                                info.filterName = filter.getName();
                                info.filterPic = filter.getImage();
                            }
                            info.localStanderScale = layerMetaBean.getScale() / layerMetaBean.getResizeScale() * scale;
                            drawableSticker.setLocked(locked);
                            return Observable.just((Sticker) drawableSticker);

                        } else if ("text".equals(type)) {
//                            文字类型
                            String fontFileName = layerMetaBean.getFontFile();
                            int version = layerMetaBean.getVersion();
                            String wordartId = layerMetaBean.getFontId();
                            String fontFamily = layerMetaBean.getFontFamily();
                            File fontFile = FileUtil.getFontFile(mContext, fontFileName);
                            int color = Color.BLACK;
                            try {
                                color = Color.parseColor(layerMetaBean.getColor());
                            } catch (Exception e) {
                                ToastUtil.showShort("颜色解析错误");
                            }
                            String replaceText = layerMetaBean.getText().replace("<br>", "\n");
                            Typeface fromFile = FontCache.getFromFile(fontFileName, fontFile.getAbsolutePath());
                            replaceText = replaceText.replace("<bt>", " ");
                            MyTextSticker textSticker = new MyTextSticker(mContext)
                                    .setTextColor(color)
                                    .setText(replaceText)
                                    .setLineSpacing(layerMetaBean.getLetterSpacing(), 1.0f)
                                    .setHorizon(!"vertical".equals(layerMetaBean.getAlign()))
                                    .setTypeface(fontFileName, fontFamily, fromFile, version, wordartId)
                                    .setTextSize(layerMetaBean.getFontSize())
                                    .resizeText();
                            textSticker.setMatrix(matrix);
                            textSticker.setLocked(locked);
                            return Observable.just((Sticker) textSticker);
                        }
                        return Observable.just((Sticker) new EmptySticker());

                    }


                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<Sticker>(mContext) {

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void _onNext(Sticker sticker) {
                        if (sticker instanceof EmptySticker) {
                            //已知错误的捕捉
                            mView.addStickerSingleFinish();
                        } else {
                            mView.addSticker(sticker);
                            Log.d(TAG, "_onNext: 我恢复了一个" + sticker);
                            reloadSticker(sticker);
                        }

                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("数据恢复错误");
                        mView.stickerAllInitFinish();
                    }
                });

    }

    /**
     * 撤销功能
     *
     * @param stickerDataBeen 本地保存的数据
     */
    @Override
    public void reShowData(List<StickerDataBean> stickerDataBeen) {
        if (stickerDataBeen == null) {
            return;
        }
        final ArrayList<Sticker> stickers = new ArrayList<>();

        Observable.fromIterable(stickerDataBeen)
                .concatMap(new Function<StickerDataBean, ObservableSource<Sticker>>() {
                    @Override
                    public ObservableSource<Sticker> apply(@NonNull StickerDataBean stickerDataBean) throws Exception {
                        Matrix matrix = new Matrix();
                        //1恢复矩阵位置信息 恢复不需要跨端所以就用矩阵比较方便
                        matrix.setValues(stickerDataBean.matrixData);

                        if (stickerDataBean instanceof TextStickerDataBean) {
                            // 如果是文字信息
                            MyTextSticker myTextSticker = new MyTextSticker(mContext).initMyTextSicker((TextStickerDataBean) stickerDataBean, mContext);
                            // 文字控件恢复自己的文字信息
                            myTextSticker.setMatrix(matrix);
                            return Observable.just((Sticker) myTextSticker);
                        } else if (stickerDataBean instanceof MyDrawableStickerDataBean) {
                            MyDrawableStickerDataBean myDrawableSitckerBean = (MyDrawableStickerDataBean) stickerDataBean;
                            Bitmap bitmap;
                            //有没有滤镜
                            if (StringUtil.isEmpty(myDrawableSitckerBean.filterPic)) {
                                //没有使用滤镜图片
                                bitmap = Glide.with(mContext)
                                        .load(ApiConstants.UZAOCHINA_IMAGE_HOST + myDrawableSitckerBean.originalUrl)
                                        .asBitmap()
                                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                        .get();
                            } else {
                                //使用了滤镜图片
                                bitmap = Glide.with(mContext)
                                        .load(ApiConstants.UZAOCHINA_IMAGE_HOST + myDrawableSitckerBean.filterPic)
                                        .asBitmap()
                                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                        .get();
                            }
                            //这里复制的原因是 sticker回收后glide的缓存会取出来空
                            Bitmap showBitmap = Bitmap.createBitmap(bitmap);
                            // 1 买单透明度
                            if (myDrawableSitckerBean.isAlph) {
                                Bitmap white_bitmap = BitmapUtils.changeBitmapWhite(showBitmap);
                                //回收bitmap
                                BitmapUtils.recycleBitmap(showBitmap);
                                showBitmap = white_bitmap;
                            }
                            //2买单裁剪
                            SourceRectBean clipBean = myDrawableSitckerBean.clipBean;
                            if (clipBean != null) {
                                //裁剪图片
                                Bitmap clipBitmap = Bitmap.createBitmap(showBitmap, clipBean.getX(), clipBean.getY(), clipBean.getWidth(), clipBean.getHeight());
                                BitmapUtils.recycleBitmap(showBitmap);
                                showBitmap = clipBitmap;
                            }
                            //初始化控件
                            BitmapDrawable bitmapDrawable = new BitmapDrawable(mContext.getResources(), showBitmap);
                            MyDrawableSticker myDrawableSticker = new MyDrawableSticker(myDrawableSitckerBean.originalUrl, bitmapDrawable);
                            myDrawableSticker.initDrawableSticker(myDrawableSitckerBean);
                            myDrawableSticker.setMatrix(matrix);
                            return Observable.just((Sticker) myDrawableSticker);
                        }
                        return null;

                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<Sticker>(mContext) {

                    @Override
                    public void onComplete() {
                        System.out.println("添加完成");
                        mView.reShow(stickers);
                    }

                    @Override
                    public void _onNext(Sticker sticker) {
                        System.out.println("回复矩阵" + sticker.getMatrix().toString());
                        stickers.add(sticker);
                    }

                    @Override
                    public void _onError(String message) {
                        Log.d(TAG, "_onError: " + message);
                        System.out.println("恢复上一步数据错误" + message);
                    }
                });
    }


    /**
     * 重新处理需要异步下载处理的sticker  如文字需要下载字体的 和图片需要添加滤镜的
     *
     * @param sticker 需要处理的sticker
     */
    private void reloadSticker(Sticker sticker) {
        if (sticker instanceof MyDrawableSticker) {
            reloadDrawable((MyDrawableSticker) sticker);
        } else if (sticker instanceof MyTextSticker) {
            reLoadText((MyTextSticker) sticker);
        }
    }


    /**
     * 异步处理文字控件
     *
     * @param textSticker 文字控件
     */
    private void reLoadText(final MyTextSticker textSticker) {
        final String fileName = textSticker.getFileName();
        final File fontFile = FileUtil.getFontFile(mContext, fileName);

        Typeface fromFile = FontCache.getFromFile(fileName, fontFile.getAbsolutePath());
        if (fontFile.exists() || fromFile != null) {
            textSticker.setTypeface(textSticker.getFileName(), textSticker.getFontName(), fromFile, textSticker.getVersion(), textSticker.getWordartId());
            mView.addStickerSingleFinish();
            return;
        }
//        通知activity去下载对应字体
        EventFontToDownLoadBean toDownLoadFontBean = new EventFontToDownLoadBean(textSticker.getFileName(), textSticker.getFontName());
        EventBus.getDefault().post(toDownLoadFontBean);
    }

    /**
     * 处理图片控件 的滤镜
     *
     * @param drawableSticker 图片控件
     */
    private void reloadDrawable(final MyDrawableSticker drawableSticker) {
        final StickerDataInfo info = drawableSticker.getInfo();
        if (!StringUtil.isEmpty(info.filterType) && "filter".equals(info.filterType) && !StringUtil.isEmpty(info.filterName)) {
            //是滤镜的图片
            Api.getDefault().getFilterPic(info.filterName, info.url)
                    .compose(RxHandleResult.<String>handleResultMap())
                    .map(new Function<String, Bitmap>() {
                        @Override
                        public Bitmap apply(@NonNull String s) throws Exception {
                            Log.d(TAG, "apply: 滤镜图是" +
                                    "filterType" + info.filterType + ApiConstants.UZAOCHINA_IMAGE_HOST + s);
                            info.filterPic = s;
                            Bitmap bitmap = Glide.with(mContext)
                                    .load(ApiConstants.UZAOCHINA_IMAGE_HOST + info.filterPic)
                                    .asBitmap()
                                    .skipMemoryCache(true) // 不使用内存缓存
                                    .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                            return BitmapUtils.copyBitmap(bitmap);
                        }
                    }).map(new Function<Bitmap, Bitmap>() {
                @Override
                public Bitmap apply(@NonNull Bitmap bitmap) throws Exception {
                    if (info.isAlph) {
                        Bitmap bitmapWhite = BitmapUtils.changeBitmapWhite(bitmap);
                        BitmapUtils.recycleBitmap(bitmap);
                        return bitmapWhite;
                    } else {
                        return bitmap;
                    }

                }
            }).map(new Function<Bitmap, Bitmap>() {
                @Override
                public Bitmap apply(@NonNull Bitmap bitmap) throws Exception {
                    SourceRectBean clipBean = info.clipBean;
                    if (clipBean != null) {
                        Bitmap clipBitmap = Bitmap.createBitmap(bitmap, clipBean.getX(), clipBean.getY(), clipBean.getWidth(), clipBean.getHeight());
                        if (clipBitmap != bitmap) {
                            BitmapUtils.recycleBitmap(bitmap);
                        }
                        return clipBitmap;
                    }
                    return bitmap;
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new RxSubscriber<Bitmap>(mContext) {
                @Override
                public void _onNext(Bitmap bitmap) {
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(mContext.getResources(), bitmap);
                    drawableSticker.setDrawable(bitmapDrawable);
                    mView.addStickerSingleFinish();
                    Log.d(TAG, "_onNext: reloadSuccess:" + drawableSticker);
                }

                @Override
                public void _onError(String message) {
                    mView.addStickerSingleFinish();
                    Log.d(TAG, "_onNext: reloadfail:" + drawableSticker);
                }
            });
        } else {
            mView.addStickerSingleFinish();
            Log.d(TAG, "_onNext: reloadNo:" + drawableSticker);
        }
    }
}
