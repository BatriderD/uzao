package com.zhaotai.uzao.ui.design.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.orhanobut.logger.Logger;
import com.xiaopo.flying.sticker.BitmapStickerIcon;
import com.xiaopo.flying.sticker.BitmapUtils;
import com.xiaopo.flying.sticker.DeleteIconEvent;
import com.xiaopo.flying.sticker.FontCache;
import com.xiaopo.flying.sticker.MyDrawableSticker;
import com.xiaopo.flying.sticker.MyTextSticker;
import com.xiaopo.flying.sticker.SourceRectBean;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.StickerDataBean;
import com.xiaopo.flying.sticker.StickerDataInfo;
import com.xiaopo.flying.sticker.StickerIconEvent;
import com.xiaopo.flying.sticker.StickerView;
import com.xiaopo.flying.sticker.ZoomIconEvent;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.bean.AddEditorMaterialBean;
import com.zhaotai.uzao.bean.EventBean.EventBean;
import com.zhaotai.uzao.bean.EventBean.EventMessage;
import com.zhaotai.uzao.bean.LayerDataBean;
import com.zhaotai.uzao.bean.LayerMetaJsonBean;
import com.zhaotai.uzao.bean.MKUCarrierBean;
import com.zhaotai.uzao.bean.MKUPositionBean;
import com.zhaotai.uzao.bean.StickerPicInfoBean;
import com.zhaotai.uzao.bean.ThreeDimensionalBean;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.design.contract.EditorFragmentContract;
import com.zhaotai.uzao.ui.design.presenter.EditorFragmentPresenter;
import com.zhaotai.uzao.ui.design.widget.EditorChangeTextDialog;
import com.zhaotai.uzao.utils.ColorUtils;
import com.zhaotai.uzao.utils.FileUtil;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.GsonUtil;
import com.zhaotai.uzao.utils.KeyboardUtils;
import com.zhaotai.uzao.utils.SPUtils;
import com.zhaotai.uzao.utils.ScreenUtils;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * description: editorFragment
 * author : ZP
 * date: 2018/4/9 0009.
 */

public class EditorFragment extends BaseFragment implements EditorFragmentContract.View {
    private static final String TAG = "FixDesignFragment";
    private static final String EXTRA_KEY_MKU_INFO = "extra_key_mku_info";
    private static final String EXTRA_KEY_LAYER_INFO = "extra_key_layer_info";
    private static final String EXTRA_KEY_TECHNOLOGY_POS = "extra_key_technology_pos";
    private static final String MODEL_TYPE = "model_type";
    private static String SIMPLE_3d_MODEL = "simple_3d_model";
    //view宽度
    private float viewWidth;
    private EditorFragmentPresenter mPresenter;
    private String modelType;
    private int technologyPos;
    private int pos;
    //蒙版的缩放系数
    private float maskScale = 1.0f;
    private MKUCarrierBean.MkusBean.MaskGroupsBean maskGroupsBean;
    private LayerMetaJsonBean layerMetaBean;
    private float scale;

    @BindView(R.id.rl_fragment_root)
    public RelativeLayout rlRoot;
    private float stickerViewWidth;
    public StickerView stickerView;
    private ImageView ivBrand;
    private Bitmap thumbnail;
    private EditorChangeTextDialog createUserDialog;
    private int layerMetaSize;
    private boolean lineVisible;
    private Sticker markHandingSticker;


    /**
     * 创建构建编辑器fragment
     *
     * @param type          类型 2d or 3d
     * @param bean          载体信息类
     * @param layerMeta     设计信息
     * @param pos           当前fragment是第几个
     * @param technologyPos 他的工艺信息是第几个
     * @return 设计fragment
     */
    public static Fragment newInstance(String type, MKUCarrierBean.MkusBean.MaskGroupsBean bean, LayerMetaJsonBean layerMeta, int pos, int technologyPos) {
        EditorFragment fragment = new EditorFragment();
        Bundle bundle = new Bundle();

        bundle.putString(MODEL_TYPE, type);
        bundle.putSerializable(EXTRA_KEY_MKU_INFO, bean);
        bundle.putSerializable(EXTRA_KEY_LAYER_INFO, layerMeta);
        bundle.putInt(EXTRA_KEY_TECHNOLOGY_POS, technologyPos);
        bundle.putInt("pos", pos);

        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 初始化构建3d fragment
     *
     * @param type          类型 2d or 3d
     * @param sample3dModel 3d对应的数据数据
     * @param layerMeta     3d的UV面设计
     * @param technologyPos 工艺数据
     * @return 当前fragment
     */
    public static Fragment newInstance(String type, ThreeDimensionalBean.Option sample3dModel, LayerMetaJsonBean layerMeta, int technologyPos) {
        EditorFragment fragment = new EditorFragment();
        Bundle bundle = new Bundle();

        bundle.putString(MODEL_TYPE, type);
        bundle.putSerializable(SIMPLE_3d_MODEL, sample3dModel);
        bundle.putSerializable(EXTRA_KEY_LAYER_INFO, layerMeta);
        bundle.putInt(EXTRA_KEY_TECHNOLOGY_POS, technologyPos);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected int layoutId() {
        return R.layout.frag_on_carrier;
    }

    @Override
    public void initView() {
        //构建一个屏幕宽度大小的控件
        viewWidth = ScreenUtils.getScreenWidth(getActivity());
    }

    @Override
    public void initPresenter() {
        mPresenter = new EditorFragmentPresenter(getActivity(), this);
    }

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        modelType = bundle.getString(MODEL_TYPE);
        technologyPos = bundle.getInt(EXTRA_KEY_TECHNOLOGY_POS);
        pos = bundle.getInt("pos", 0);
        if (GlobalVariable.MODEL_2D.equals(modelType)) {
            //初始化2d数据
            init2DData();
        } else if (GlobalVariable.MODEL_3D.equals(modelType)) {
            //初始化3d数据
            init3DData();
        }
    }

    /**
     * 初始化3d数据
     */
    private void init3DData() {
        ThreeDimensionalBean.Option sample3dModel = (ThreeDimensionalBean.Option) getArguments().getSerializable(SIMPLE_3d_MODEL);
        if (sample3dModel == null) {
            ToastUtil.showShort("3d数据错误");
            return;
        }
        //获取网络信息
        layerMetaBean = (LayerMetaJsonBean) getArguments().getSerializable(EXTRA_KEY_LAYER_INFO);
//        计算缩放比例  3dUV面都是1024*1024
        initScale(1024, 1024);
        if (sample3dModel.coefficient != null) {
            try {
                //获得3d的DPI缩放习俗
                maskScale = Float.valueOf(sample3dModel.returnCoefficient);
            } catch (Exception e) {
                maskScale = 1.0f;
            }
        } else {
            //设置一个默认缩放系数
            maskScale = 1.0f;
        }
        stickerViewWidth = viewWidth;
        RelativeLayout.LayoutParams matchParentParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        matchParentParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        //设置编辑控件域到指定位置
        stickerView = (StickerView) View.inflate(getActivity(), R.layout.item_sticker, null);
        stickerView.setShowLine(false);
        rlRoot.addView(stickerView, matchParentParams);
        //初始化stickerView
        initStickerView();

        //设置背景
        ImageView iv_thumbnail = new ImageView(getActivity());
        GlideLoadImageUtil.load(getActivity(), ApiConstants.UZAOCHINA_IMAGE_HOST + sample3dModel.uvFace, iv_thumbnail);
        rlRoot.addView(iv_thumbnail, matchParentParams);

        //判断有无设计数据
        if (layerMetaBean == null || layerMetaBean.getLayerMeta() == null || layerMetaBean.getLayerMeta().isEmpty()) {
            //空白fragment
            fragmentInitFinish();
        } else {
            //有数据的fragment`
            release();
        }
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    /**
     * 初始化2D数据
     */
    private void init2DData() {
        //        获得数据
        maskGroupsBean = (MKUCarrierBean.MkusBean.MaskGroupsBean) getArguments().getSerializable(EXTRA_KEY_MKU_INFO);
        layerMetaBean = (LayerMetaJsonBean) getArguments().getSerializable(EXTRA_KEY_LAYER_INFO);
        //解析数据
        List<MKUCarrierBean.MkusBean.MaskGroupsBean.EditAreasBean> editAreas = maskGroupsBean.getEditAreas();
        //空白未初始化成功的fragment通知activity
        if (editAreas == null || editAreas.size() == 0) {
            EventBus.getDefault().post(new EventMessage(EventBusEvent.EDITOR_INIT_FAILED));
            return;
        }
        final MKUCarrierBean.MkusBean.MaskGroupsBean.EditAreasBean editAreasBean = editAreas.get(0);

        if (editAreasBean.getScale() != null) {
            if (!editAreasBean.getScale().equals("0")) {
                maskScale = Float.valueOf(editAreasBean.getScale());
            } else {
                maskScale = 1;
            }

        }
        //获得位置大小等信息
        String position = editAreasBean.getPosition();
        // 获得蒙版信息 包括位置 和大小等
        MKUPositionBean mkuPositionBean = GsonUtil.getGson().fromJson(position, MKUPositionBean.class);
        //计算缩放比例
        initScale(mkuPositionBean.getImgWidth(), mkuPositionBean.getImgHeight());
        //设置背景
        ImageView iv_thumbnail = new ImageView(getActivity());
        GlideLoadImageUtil.load(getActivity(), ApiConstants.UZAOCHINA_IMAGE_HOST + maskGroupsBean.getThumbnail(), iv_thumbnail);
        RelativeLayout.LayoutParams underLayoutParams = new RelativeLayout.LayoutParams((int) (mkuPositionBean.getImgWidth() * scale), (int) (mkuPositionBean.getImgHeight() * scale));
        underLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        rlRoot.addView(iv_thumbnail, underLayoutParams);


        int borderWidth = Integer.valueOf(mkuPositionBean.getBorderWidth());
        int borderHeight = Integer.valueOf(mkuPositionBean.getBorderHeight());
        stickerViewWidth = borderWidth * scale;
        RelativeLayout.LayoutParams maskLayoutParams = new RelativeLayout.LayoutParams((int) (borderWidth * scale), (int) (borderHeight * scale));
        maskLayoutParams.setMargins((int) Math.round(mkuPositionBean.getX() * scale), (int) Math.round(mkuPositionBean.getY() * scale), 0, 0);

//      设置编辑区域到指定位置
        stickerView = (StickerView) View.inflate(getActivity(), R.layout.item_sticker, null);
        stickerView.setShowLine(false);
        rlRoot.addView(stickerView, maskLayoutParams);

//                创建imageview 线框
        ivBrand = new ImageView(getActivity());
        ivBrand.setScaleType(ImageView.ScaleType.FIT_XY);
        GlideLoadImageUtil.load(getActivity(), ApiConstants.UZAOCHINA_IMAGE_HOST + editAreasBean.getBorder(), ivBrand);
        rlRoot.addView(ivBrand, maskLayoutParams);
        ivBrand.setAlpha(0.5f);

        initStickerView();
        //这里使用异步在子线程下载蒙版图片并缩放合适大小到stickerView里，处理完成通知activity完成。
        Observable.just(stickerView)
                .doOnNext(new Consumer<StickerView>() {
                    @Override
                    public void accept(@NonNull StickerView stickerView) throws Exception {
                        Bitmap bitmap = null;
                        try {
                            //异步下载蒙版图片
                            bitmap = Glide.with(getActivity())
                                    .load(ApiConstants.UZAOCHINA_IMAGE_HOST + editAreasBean.getMask())
                                    .asBitmap()
                                    .skipMemoryCache(true)
                                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                        } catch (Exception e) {
                            Log.d(TAG, "accept: bitmapError");
                        }
                        if (bitmap != null) {
                            Matrix matrix = new Matrix();
                            matrix.postScale(scale, scale);
                            Bitmap bitmapCopy = null;
                            try {
                                if (bitmap.getWidth() != 0 && bitmap.getHeight() != 0) {
                                    //蒙版大小改成控件合适大小
                                    bitmapCopy = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                                }
                            } catch (Exception e) {
                                Log.d(TAG, "accept: 恢复图片错误");
                            }
                            if (bitmapCopy != null) {
                                //stickerView设置载体控件
                                stickerView.setTemplateBitmap(bitmapCopy);
                            }
                        }
                    }
                }).doOnNext(new Consumer<StickerView>() {
            @Override
            public void accept(@NonNull StickerView stickerView) throws Exception {
                thumbnail = Glide.with(getActivity())
                        .load(ApiConstants.UZAOCHINA_IMAGE_HOST + maskGroupsBean.getThumbnail())
                        .asBitmap()
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();

            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<StickerView>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull StickerView stickerView) throws Exception {
                        stickerView.invalidate();
                        if (layerMetaBean == null) {
                            //空白fragment
                            fragmentInitFinish();
                        } else {
                            //有数据的fragment`
                            release();
                        }
                    }
                });
    }


    /**
     * 解析并还包含设计数据的
     */
    private void release() {
        List<LayerMetaJsonBean.LayerMetaBean> layerMeta = this.layerMetaBean.getLayerMeta();
        layerMetaSize = layerMeta.size();
        mPresenter.parseLayerMeta(layerMeta, scale);
    }


    /**
     * 初始化stickerView
     */
    private void initStickerView() {

//        左上角删除图标
        BitmapStickerIcon deleteIcon = new BitmapStickerIcon(ContextCompat.getDrawable(getActivity(),
                R.drawable.ic_sticker_delete),
                BitmapStickerIcon.LEFT_TOP);
        deleteIcon.setIconEvent(new DeleteIconEvent());

//     右下角的缩放图标
        BitmapStickerIcon scaleIcon = new BitmapStickerIcon(ContextCompat.getDrawable(getActivity(),
                R.drawable.ic_sticker_scale),
                BitmapStickerIcon.RIGHT_BOTOM);
        scaleIcon.setIconEvent(new ZoomIconEvent());
//        左上角的编辑图标
        BitmapStickerIcon changeTextIcon = new BitmapStickerIcon(ContextCompat.getDrawable(getActivity(),
                R.drawable.ic_sticker_edit),
                BitmapStickerIcon.RIGHT_TOP);
        changeTextIcon.setIconEvent(new StickerIconEvent() {
            @Override
            public void onActionDown(StickerView stickerView, MotionEvent event) {

            }

            @Override
            public void onActionMove(StickerView stickerView, MotionEvent event) {

            }

            @Override
            public void onActionUp(StickerView stickerView, MotionEvent event) {
                Sticker handingSticker = stickerView.getHandingSticker();
                if (handingSticker instanceof MyTextSticker) {
                    //修改文字的按钮
                    initChangeTextDialog(((MyTextSticker) handingSticker).getText());
                }
            }
        });

        stickerView.setIcons(Arrays.asList(deleteIcon, scaleIcon, changeTextIcon));


        stickerView.setLocked(false);
        stickerView.setConstrained(true);
    }


    /**
     * 显示化修文字控件文字内容改弹窗dialog
     *
     * @param text 控件的文字
     */
    private void initChangeTextDialog(String text) {
        if (createUserDialog == null) {
            createUserDialog = new EditorChangeTextDialog(getActivity(), text);
            createUserDialog.setListener(new EditorChangeTextDialog.OnTextchangeListener() {
                @Override
                public void onChangeText(Dialog dialog, String changeText) {
                    setText(changeText);
                    KeyboardUtils.dialogBoardCancle(dialog);
                }

                @Override
                public void onCancel(Dialog dialog) {
                    KeyboardUtils.dialogBoardCancle(dialog);
                }
            });
        } else {
            createUserDialog.setText(text);
        }
        createUserDialog.show();
    }

    /**
     * 给当前选中的文字控件设置文字
     *
     * @param textEdit 文字内容
     * @return 是否是增加的sticker
     */
    public boolean setText(String textEdit) {
        if (stickerView != null) {
            if (stickerView.handlingSticker instanceof MyTextSticker) {
                ((MyTextSticker) stickerView.handlingSticker).setText(textEdit).resizeText();
                saveStep();
                stickerView.invalidate();
            } else {
                if (isStickerSizeFull()) return false;
                MyTextSticker textSticker1 = new MyTextSticker(getActivity())
                        .setText(textEdit)
                        .resizeText();
                stickerView.addSticker(textSticker1);
                stickerView.invalidate();
                saveStep();
                return true;
            }
        }
        return false;
    }


    /**
     * 获得sticker总数
     *
     * @return 贴纸控件总数
     */
    private int getStickerCount() {
        List<Sticker> stickers = stickerView.getStickers();
        return stickers.size();
    }


    /**
     * 计算view的缩放显示系数
     *
     * @param width  蒙版宽
     * @param height 蒙版高
     */
    private void initScale(int width, int height) {
        int max = width > height ? width : height;
        scale = viewWidth / max;
    }

    /**
     * 加载完成数据通知 activity
     */
    private void fragmentInitFinish() {
        saveStep();
        EventBus.getDefault().post(new EventBean<>(this, EventBusEvent.DESIGN_MATERIAL_FRAGMENT_INIT_FINISH));
    }

    /**
     * 保存每一步镜像方法
     */
    public void saveStep() {
        if (stickerView != null) {
            List<StickerDataBean> stickerDataBeen = mPresenter.saveStep(stickerView.stickers);
            //步数限制
            if (dataBeanList.size() == GlobalVariable.MAX_STEP + 1) {
//                保存指定步数
                dataBeanList.remove(0);
            }
            dataBeanList.add(stickerDataBeen);
            Log.d(TAG, "saveStep: 镜像保存成:" + GsonUtil.t2Json2(dataBeanList));
            EventBus.getDefault().post(new EventMessage(EventBusEvent.EDITOR_HAS_CHANGED));
        }
    }

    /**
     * 所有贴纸初始化增加完成
     */
    @Override
    public void stickerAllInitFinish() {
        if (stickerView != null) {
            stickerView.invalidate();
        }
        fragmentInitFinish();
    }

    /**
     * 单个贴纸增加完成
     */
    @Override
    public void addStickerSingleFinish() {
        layerMetaSize = layerMetaSize - 1;
        if (layerMetaSize <= 0) {
            stickerAllInitFinish();
        }
    }

    /**
     * 增加数据化恢复的贴纸控件
     *
     * @param stickers 贴纸控件
     */
    @Override
    public void addSticker(Sticker stickers) {
        stickerView.stickers.add(stickers);
        stickerView.invalidate();
    }

    /**
     * 获取当前fragment的序号
     *
     * @return 当前fragment序号
     */
    public int getPos() {
        return pos;
    }

    /**
     * 当前作品工艺序号
     *
     * @return 返回作品工艺序号
     */
    public int getTechnology() {
        return technologyPos;
    }


    /**
     * 应用返回白色背景透明化的图片图片
     *
     * @param alph 是否已经白色背景透明化
     */
    public void changStickerDrawable(boolean alph) {
        if (stickerView != null) {
            Sticker handingSticker = stickerView.getHandingSticker();
            if (handingSticker instanceof MyDrawableSticker) {
                MyDrawableSticker drawableSticker = (MyDrawableSticker) handingSticker;
//                如果透明度和当前一样  那就不更改
                if (drawableSticker.getInfo().isAlph == alph) {
                    Log.d(TAG, "changStickerDrawable: 透明度不改");
                } else {
//                说明有变化了   那么在对应sticker的bean里面设置更改  并刷新
                    Log.d(TAG, "changStickerDrawable: 透明度改了:" + alph);
                    drawableSticker.getInfo().isAlph = alph;
                    changeDrawableSticker(drawableSticker);
                }

            }
        }
    }

    /**
     * 根据stickerInfo构建sticker
     *
     * @param sticker 图片
     */
    public void changeDrawableSticker(final MyDrawableSticker sticker) {
        final StickerDataInfo info = sticker.getInfo();
        //        标准恢复图片
        Observable.just(info)
                .map(new Function<StickerDataInfo, Bitmap>() {
                    @Override
                    public Bitmap apply(@io.reactivex.annotations.NonNull StickerDataInfo info) throws Exception {
                        //1.获取图片：滤镜后的图片或者原图
                        Bitmap resultBitmap;
                        Bitmap bitmap;
                        if (StringUtil.isEmpty(info.filterName)) {
                            if (StringUtil.isEmpty(info.url)) {
                                Observable.error(new Exception("图片信息错误"));
                            }
                            bitmap = Glide.with(getActivity())
                                    .load(ApiConstants.UZAOCHINA_IMAGE_HOST + info.url)
                                    .asBitmap()
                                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                        } else {
                            bitmap = Glide.with(getActivity())
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
        }).map(new Function<Bitmap, MyDrawableSticker>() {
            @Override
            public MyDrawableSticker apply(@io.reactivex.annotations.NonNull Bitmap bitmap) throws Exception {
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                return sticker.setDrawable(bitmapDrawable);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<MyDrawableSticker>(getActivity(), true) {
                    @Override
                    public void _onNext(MyDrawableSticker myDrawableSticker) {
                        stickerView.invalidate();
                        saveStep();
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("图片贴纸更改失败");
                    }
                });
    }


    /**
     * 删除对应的图层
     *
     * @param pos 图层顺序
     */
    public void removeLayer(int pos) {
        if (stickerView != null) {
            stickerView.remove(pos);
        }
    }

    /**
     * 移动图层元素
     *
     * @param oldPos 旧位置
     * @param newPos 新位置
     */
    public void moveSticker(int oldPos, int newPos) {
        if (stickerView != null) {
            stickerView.moveSticker(oldPos, newPos);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseHanding();
    }

    /**
     * 手动清除引用缓存
     */
    private void releaseHanding() {
        if (stickerView != null) {
            stickerView.releaseHanding();
            stickerView.setOnSelectorListener(null);
            stickerView.setOnStickerOperationListener(null);
        }
    }


    //设置listener
    public void setOnStickerOperationListener(
            @Nullable StickerView.OnStickerOperationListener onStickerOperationListener) {
        stickerView.setOnStickerOperationListener(onStickerOperationListener);
    }

    /**
     * 通知sticker改变
     *
     * @param info sticker信息
     */
    public void notifiedStickerChanged(StickerDataInfo info) {
        Sticker handingSticker = stickerView.getHandingSticker();
        if (handingSticker instanceof MyDrawableSticker) {
            try {
                MyDrawableSticker myDrawableSticker = (MyDrawableSticker) handingSticker;
                myDrawableSticker.setInfo(info);
                changeDrawableSticker(myDrawableSticker);
            } catch (OutOfMemoryError e) {
                ToastUtil.showShort("内存溢出啦");
            }
        }
    }

    /**
     * 当前作品工艺序号
     *
     * @param pos 保存工艺序号
     */
    public void setTechnology(int pos) {
        this.technologyPos = pos;
    }


    /**
     * 增加素材
     *
     * @param addEditorMaterialBean 素材对象
     */
    public void addMaterial(final AddEditorMaterialBean addEditorMaterialBean) {
        if (stickerView == null) {
            return;
        }

        if (isStickerSizeFull()) return;
        Observable.just(ApiConstants.UZAOCHINA_IMAGE_HOST + addEditorMaterialBean.thumbnail)
                .map(new Function<String, Bitmap>() {
                    @Override
                    public Bitmap apply(@io.reactivex.annotations.NonNull String url) throws Exception {
                        //异步下载素材
                        Log.d(TAG, "apply: 添加素材网络图片" + url);
                        Bitmap bitmap = Glide
                                .with(getActivity())
                                .load(url)
                                .asBitmap()
                                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                        return BitmapUtils.copyBitmap(bitmap);
                    }
                }).map(new Function<Bitmap, MyDrawableSticker>() {


            @Override
            public MyDrawableSticker apply(@io.reactivex.annotations.NonNull Bitmap bitmap) throws Exception {
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                MyDrawableSticker drawableSticker = new MyDrawableSticker(addEditorMaterialBean.thumbnail, bitmapDrawable);
                StickerDataInfo info = drawableSticker.getInfo();
                //素材有矢量的 没有dpi的概念，
                boolean isVector = "AI".equals(addEditorMaterialBean.fileMime) || "ai".equals(addEditorMaterialBean.fileMime);
                info.isVector = isVector;
                if (addEditorMaterialBean.fileMime != null && addEditorMaterialBean.resizeScale != 0 && !isVector) {
                    info.localStanderScale = maskScale / addEditorMaterialBean.resizeScale * scale;
                    info.resizeScale = addEditorMaterialBean.resizeScale;
                } else {
                    info.localStanderScale = maskScale / 1 * scale;
                    info.resizeScale = 1;
                }
                info.isVector = isVector;
                info.materialId = addEditorMaterialBean.sourceMaterialId;
                info.url = addEditorMaterialBean.thumbnail;
                return drawableSticker;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<MyDrawableSticker>(getActivity()) {
                    @Override
                    public void _onNext(MyDrawableSticker myDrawableSticker) {
                        //初始化成功就添加
                        stickerView.addSticker(myDrawableSticker);
                    }


                    @Override
                    public void _onError(String message) {
                        Log.d(TAG, "添加素材出错 _onError: " + message);
                    }
                });
    }

    /**
     * 判断是否已经达到最大添加个数
     *
     * @return true:不能添加更多了 ，false可以添加更多
     */
    public boolean isStickerSizeFull() {
        int stickerCount = getStickerCount();
        if (stickerCount >= GlobalVariable.MAX_STICKER_COUNT) {
            ToastUtil.showShort("最多只能上传" + GlobalVariable.MAX_STICKER_COUNT + "个元素");
            return true;
        }
        return false;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    /**
     * 根据网络图片创建图片sticker
     *
     * @param picInfoBean 图片网络地址
     */
    public void createDrawableSticker(final StickerPicInfoBean picInfoBean) {
        if (stickerView == null) {
            return;
        }
        if (isStickerSizeFull()) return;
        Observable.just(ApiConstants.UZAOCHINA_IMAGE_HOST + picInfoBean.name)
                .map(new Function<String, Bitmap>() {
                    @Override
                    public Bitmap apply(@io.reactivex.annotations.NonNull String url) throws Exception {
                        Log.d(TAG, "apply: 添加网络图片" + url);
                        Bitmap bitmap = Glide.with(getActivity())
                                .load(url)
                                .asBitmap()
                                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                        return BitmapUtils.copyBitmap(bitmap);
                    }
                }).map(new Function<Bitmap, MyDrawableSticker>() {
            @Override
            public MyDrawableSticker apply(@io.reactivex.annotations.NonNull Bitmap bitmap) throws Exception {
                //设置图片控件相关参数
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                MyDrawableSticker drawableSticker = new MyDrawableSticker(picInfoBean.name, bitmapDrawable);
                StickerDataInfo info = drawableSticker.getInfo();
                info.url = picInfoBean.name;
                info.localStanderScale = maskScale / picInfoBean.resizeScale * scale;
                info.resizeScale = picInfoBean.resizeScale;
                return drawableSticker;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<MyDrawableSticker>(getActivity()) {
                    @Override
                    public void _onNext(MyDrawableSticker myDrawableSticker) {
                        int borderWidth = (int) stickerViewWidth;
                        int borderHeight = (int) stickerViewWidth;

                        float standardScale = maskScale / myDrawableSticker.getInfo().resizeScale;
                        //根据dpi换算
                        float width = myDrawableSticker.getWidth() * standardScale * scale;
                        float height = myDrawableSticker.getHeight() * standardScale * scale;
                        //增加图片到编辑区域 需要注意 根据dpi换算后还很大的图片需要放到合适大小
                        if (borderWidth < width || borderHeight < height) {
                            //换算后很大的图片 需要放到填充的大小
                            stickerView.addSticker(myDrawableSticker);
                            stickerView.addFillStyleSticker(myDrawableSticker, 1);
                            stickerView.invalidate();
                        } else if (myDrawableSticker.getWidth() < 100 || myDrawableSticker.getHeight() < 100 || width < 100 || height < 100) {
                            //换算后特别小的图片 需要按标准100来做  不然太小了不好操作
                            Log.d(TAG, "_onNext: 我是小号 width：" + width + "  height:" + height);
                            float min = Math.min(myDrawableSticker.getWidth(), myDrawableSticker.getHeight());
                            float matrixScale = 100 / min;
                            stickerView.addStickerCenterWithScale(myDrawableSticker, matrixScale);
                        } else {
                            //换算后合适的图片 以缩放后正常尺寸放到编辑器中。
                            Log.d(TAG, "_onNext: 我是中号 width：" + width + "  height:" + height);
                            //计算位移
                            stickerView.addStickerCenterWithScale(myDrawableSticker, standardScale * scale);
                        }
                    }


                    @Override
                    public void _onError(String message) {
                        Log.d(TAG, "createDrawableSticker _onError: " + message);
                        ToastUtil.showShort("图片贴纸添加失败");
                    }
                });
    }

    /**
     * 开始修改TextSticker 主要是这时候将文字变成半透明
     *
     * @param text 文本内容
     */
    public void startChangeHandingTextFont(String text) {
        if (stickerView == null) {
            ToastUtil.showShort("该功能不可用");
            return;
        }
        Sticker handingSticker = stickerView.getHandingSticker();
        if (handingSticker instanceof MyTextSticker) {
            //存在 变透明
            MyTextSticker myTextSticker = (MyTextSticker) handingSticker.setAlpha(168);
            myTextSticker.resizeText();
            stickerView.invalidate();
        } else {

            int stickerCount = getStickerCount();
            if (stickerCount >= GlobalVariable.MAX_STICKER_COUNT) {
                ToastUtil.showShort("最多只能上传" + GlobalVariable.MAX_STICKER_COUNT + "个元素");
                return;
            }
            text = text.replace("<br>", "\n");
            text = text.replace("<bt>", " ");
            String font_file = SPUtils.getSharedStringData(GlobalVariable.DEFAULT_FONT_FILENAME);
            String font_name = SPUtils.getSharedStringData(GlobalVariable.DEFAULT_FONT_NAME);
            final File fontFile = FileUtil.getFontFile(getActivity(), font_file);
            Typeface fromFile = FontCache.getFromFile(font_file, fontFile.getAbsolutePath());
            //不存在创建一个默认的并且设置半透明
            MyTextSticker textSticker1 = new MyTextSticker(getActivity())
                    .setText(text)
                    .setTypeface(font_file, font_name, fromFile, 0, "")
                    .resizeText();
            textSticker1.setAlpha(168);
            stickerView.addSticker(textSticker1);
            stickerView.invalidate();
        }
    }

    /**
     * 修改文字字体
     *
     * @param fontName 字体文件名称
     * @param fileName 字体文件名称
     * @param typeface 字体颜色
     */
    public void changeTextTypeface(String fontName, String fileName, Typeface typeface, String name, int version, String wordartId) {
        name = name.replace("<br>", "\n");
        name = name.replace("<bt>", " ");
        if (stickerView != null) {
            if (stickerView.handlingSticker instanceof MyTextSticker) {
                ((MyTextSticker) stickerView.handlingSticker).setTypeface(fileName, fontName, typeface, version, wordartId).resizeText();
                stickerView.invalidate();
                saveStep();
            } else {
                MyTextSticker textSticker1 = new MyTextSticker(getActivity())
                        .setText(name)
                        .setTypeface(fileName, fontName, typeface, version, wordartId)
                        .resizeText();
                stickerView.addSticker(textSticker1);
                saveStep();
            }
        }
    }

    /**
     * 结束处理text
     */
    public void endChangeHandingText() {
        Sticker handingSticker = stickerView.getHandingSticker();
        if (handingSticker != null) {
            handingSticker.setAlpha(255);
            stickerView.invalidate();
        }
    }

    /**
     * 遍历stickerView  如果字体名称是这个  就给这个字体控件设置这个字体
     *
     * @param fileName 文字文件名称
     * @param fontName 文字名称
     * @param fromFile 文字字体对象
     */
    @Override
    public void reSetStickerTypeFace(String fileName, String fontName, Typeface fromFile, int version, String wordartId) {
        List<Sticker> stickers = stickerView.getStickers();
        for (Sticker s : stickers) {
            if (s instanceof MyTextSticker) {
                MyTextSticker textSticker = (MyTextSticker) s;
                if (textSticker.getFontName().equals(fontName)) {
                    textSticker.setTypeface(fileName, fontName, fromFile, version, wordartId).resizeText();
                    addStickerSingleFinish();
                }
            }
        }
        stickerView.invalidate();
    }


    /**
     * 恢复上一步的数据
     *
     * @param stickers sticker数据
     */
    @Override
    public void reShow(List<Sticker> stickers) {
        if (stickerView != null) {
            stickerView.addNewStickers(stickers);
        }
    }

    /**
     * 改变指定位置的图层显示与否
     *
     * @param pos 隐藏指定位置的图层
     */
    public void hideLayer(int pos) {
        if (stickerView != null) {
            Sticker sticker = stickerView.getStickers().get(pos);
            sticker.setVisable(!sticker.isVisable());
            stickerView.invalidate();
        }
    }

    public List<Sticker> getStickers() {
        return stickerView.stickers;
    }


    /**
     * 设置当前选中的是哪个sticker
     *
     * @param pos 选中位置
     */
    public void setHandingSticker(int pos) {
        if (stickerView != null) {
            List<Sticker> stickers = stickerView.getStickers();
            if (stickers.size() >= pos) {
                stickerView.handlingSticker = stickers.get(pos);
                stickerView.invalidate();
            }
        }
    }

    /**
     * 改变字体颜色
     *
     * @param color 新字体颜色
     */
    public void changTextColor(int color) {
        if (stickerView != null) {
            Sticker handlingSticker = stickerView.handlingSticker;
            if (handlingSticker instanceof MyTextSticker) {
                ((MyTextSticker) handlingSticker).setTextColor(color).resizeText();
                stickerView.invalidate();
                saveStep();
            }
        }
    }

    /**
     * 将当前sticker替换裁剪图片地址
     *
     * @param sourceRectBean 裁剪信息类
     */
    public void changClipStickerDrawable(SourceRectBean sourceRectBean) {
        if (stickerView != null) {
            Sticker handingSticker = stickerView.getHandingSticker();
            if (handingSticker instanceof MyDrawableSticker) {
                try {
                    MyDrawableSticker myDrawableSticker = (MyDrawableSticker) handingSticker;
//                    设置信息
                    myDrawableSticker.getInfo().clipBean = sourceRectBean;
                    changeDrawableSticker(myDrawableSticker);
                } catch (OutOfMemoryError e) {
                    Log.d(TAG, "changClipStickerDrawable: 内存溢出");
                }
            }
        }
    }


    /**
     * 清除所有图层
     */
    public void removeAllLayer() {
        if (stickerView != null) {
            stickerView.removeAllUnLockedStickers();
        }
    }

    /**
     * 清除handingSticker
     */
    public void cleanHandingSticker() {
        if (stickerView != null) {
            stickerView.handlingSticker = null;
            stickerView.invalidate();
        }
    }


    /**
     * 获得图层信息
     *
     * @return 图层信息bean
     */
    public ArrayList<LayerDataBean> getLayerDataBeans() throws Exception {
        if (stickerView != null) {
            List<Sticker> stickers = stickerView.getStickers();
            ArrayList<LayerDataBean> layerDataBeens = new ArrayList<>();
            for (int i = stickers.size() - 1; i >= 0; i--) {
                Sticker sticker = stickers.get(i);
                LayerDataBean layerDataBean = new LayerDataBean();
                Bitmap bitmap = sticker.getBitmap();
                layerDataBean.setVisable(sticker.isVisable());
                layerDataBean.setBitmapContent(bitmap);
                layerDataBean.setLock("Y".equals(sticker.getLocked()));
                layerDataBeens.add(layerDataBean);
            }
            return layerDataBeens;
        }
        return null;
    }

    public Sticker getHandingSticker() {
        if (stickerView != null) {
            return stickerView.getHandingSticker();
        }
        return null;
    }

    /**
     * 创建一个默认的textSticker
     */
    public void addDefaultTextSticker() {
        Sticker handingSticker = getHandingSticker();
        if (handingSticker == null || !(handingSticker instanceof MyTextSticker)) {
            String font_file = SPUtils.getSharedStringData(GlobalVariable.DEFAULT_FONT_FILENAME);
            String font_name = SPUtils.getSharedStringData(GlobalVariable.DEFAULT_FONT_NAME);
            final File fontFile = FileUtil.getFontFile(getActivity(), font_file);
            Typeface fromFile = FontCache.getFromFile(font_file, fontFile.getAbsolutePath());
            //创建一个默认字体的文字
            MyTextSticker textSticker1 = new MyTextSticker(getActivity())
                    .setText(getString(R.string.default_text_sticker))
                    .setTypeface(font_file, font_name, fromFile, 0, "")
                    .resizeText();
            stickerView.addSticker(textSticker1);
            stickerView.invalidate();
        }
    }


    /**
     * 清除截图上的元素
     */
    public void cleanThumbnail() {
        lineVisible = stickerView.isShowLine();
        markHandingSticker = stickerView.handlingSticker;
        stickerView.handlingSticker = null;
        if (ivBrand != null) {
            ivBrand.setVisibility(View.GONE);
        }
        stickerView.setShowLine(false);
        stickerView.invalidate();
    }

    /**
     * 恢复控件的元素
     */
    public void resetThumbnail() {
        stickerView.setShowLine(lineVisible);
        if (ivBrand != null) {
            ivBrand.setVisibility(View.VISIBLE);
        }
        stickerView.handlingSticker = markHandingSticker;
        stickerView.invalidate();

    }


    /**
     * 截图保存当前页面切面的状态
     *
     * @return 保存图片
     * @throws Exception 截图和处理图片可能会内存溢出
     */
    public Bitmap getThumbnail() throws Exception {
        Bitmap bitmap = null;
        if (stickerView != null) {
            if (GlobalVariable.MODEL_3D.equals(modelType)) {
                //3d
                //启用DrawingCache并创建位图
                Bitmap drawingCache = stickerView.createBitmap();
                Matrix matrix = new Matrix();
                matrix.postScale(1024.0f / drawingCache.getWidth(), 1024.0f / drawingCache.getHeight());
                bitmap = Bitmap.createBitmap(drawingCache, 0, 0, drawingCache.getWidth(), drawingCache.getHeight(), matrix, true);
                if (drawingCache != bitmap) {
                    BitmapUtils.recycleBitmap(drawingCache);
                }
                return bitmap;
            } else {
                if (stickerView.getStickers().size() > 0) {
                    rlRoot.setDrawingCacheEnabled(true);
                    rlRoot.buildDrawingCache();
                    Matrix matrix = new Matrix();
                    Bitmap drawingCache = rlRoot.getDrawingCache();
                    //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
                    bitmap = Bitmap.createBitmap(drawingCache, 0, 0, drawingCache.getWidth(),
                            drawingCache.getHeight(), matrix, true);
                    rlRoot.setDrawingCacheEnabled(false);
//                    bitmap = ScreenShotUtils.getViewShot(rlRoot);
                } else {
                    return BitmapUtils.copyBitmap(thumbnail);
                }
            }

        }
        return bitmap;
    }

    /**
     * 保存当前控件内的所有元素
     * 将挡前控件元素宝成数据
     */
    public ArrayList<LayerMetaJsonBean.LayerMetaBean> saveData() {

        ArrayList<LayerMetaJsonBean.LayerMetaBean> layerMetaList = new ArrayList<>();
        if (stickerView == null) {
            return layerMetaList;
        }
        List<Sticker> stickers = stickerView.getStickers();
        for (int i = 0; i < stickers.size(); i++) {
            Sticker sticker = stickers.get(i);
            //控件通用属性  位置 缩放系数 ，角度等
            LayerMetaJsonBean.LayerMetaBean layerMetaBean = new LayerMetaJsonBean.LayerMetaBean();
            //设置序号
            layerMetaBean.setIndex(i);
            //角度
            layerMetaBean.setRotation(sticker.getCurrentAngle());
            LayerMetaJsonBean.LayerMetaBean.LocalBean localBean = new LayerMetaJsonBean.LayerMetaBean.LocalBean();
            PointF mappedCenterPoint = sticker.getMappedCenterPoint();
            //x  y  width height 这些信息需要处理 view尺寸和标准尺寸之间的关系
            float showX = mappedCenterPoint.x / scale;
            float showY = mappedCenterPoint.y / scale;
            float showW = sticker.getCurrentWidth() / scale;
            float showH = sticker.getCurrentHeight() / scale;
            localBean.setX(showX);
            localBean.setY(showY);
            localBean.setWidth(showW);
            localBean.setHeight(showH);
            layerMetaBean.setLocal(localBean);
            layerMetaBean.setLocked(StringUtil.isEmpty(sticker.getLocked()) ? "N" : sticker.getLocked());
            //标准的缩放系数
            layerMetaBean.setScale(sticker.getCurrentScale() / scale);
            if (sticker instanceof MyDrawableSticker) {
                //图片控件
                MyDrawableSticker drawableSticker = (MyDrawableSticker) sticker;
                StickerDataInfo info = drawableSticker.getInfo();
                //图片图片的缩放系数
                layerMetaBean.setResizeScale(info.resizeScale);

                //计算生产的缩放系数  这一步主要原因是maskScale是不能为0的
                if (maskScale == 0) {
                    maskScale = 1;
                }
                //设置缩放后台所需要的生产scale
                layerMetaBean.setDevScale(sticker.getCurrentScale() / (maskScale / info.resizeScale) / scale);
                //设置素材id  如果有的话
                layerMetaBean.setMaterialId(info.materialId);
                //设置是否是矢量图
                layerMetaBean.setVector(info.isVector);
                //设置原始图片地址
                layerMetaBean.setImg(info.url);
                //设置是否是透明的
                layerMetaBean.setTransparent(info.isAlph);
                if (!StringUtil.isEmpty(info.filterType) && !StringUtil.isEmpty(info.filterName)) {
                    layerMetaBean.setFilter(new LayerMetaJsonBean.LayerMetaBean.Filter(info.filterType, info.filterName, info.filterPic));
                }
                //裁剪
                SourceRectBean clipBean = info.clipBean;
                if (clipBean != null) {
                    //设置裁剪的尺寸 xy 宽高等
                    layerMetaBean.setSourceRect(new LayerMetaJsonBean.LayerMetaBean.SourceRectBean(clipBean.getX(), clipBean.getY(), clipBean.getWidth(), clipBean.getHeight()));
                }
                layerMetaBean.setType("bitmap");
            } else if (sticker instanceof MyTextSticker) {
                //s设置文字的数据化
                MyTextSticker textSticker = (MyTextSticker) sticker;
                // 设置文字的默认宽高 这里说的默认是去除掉 增加文字间距的宽高
                localBean.setDefaultHeight(String.valueOf(textSticker.getMatrixScale(textSticker.getMatrix()) * textSticker.getDefineHeight() / scale));
                localBean.setDefaultWidth(String.valueOf(textSticker.getMatrixScale(textSticker.getMatrix()) * textSticker.getDefineWidth() / scale));
                System.out.println("存储矩阵" + textSticker.getMatrix().toString());
                //文字控件的resizeScale没有意义
                layerMetaBean.setResizeScale(0.0f);
                //设置文字的文本内容
                String text = textSticker.getText();
                //数据化需要处理将换行处理成<br>
                String replace = text.replace("\n", "<br>");
                layerMetaBean.setText(replace);
                layerMetaBean.setVersion(textSticker.getVersion());
                layerMetaBean.setFontId(textSticker.getWordartId());
                layerMetaBean.setType("text");
                layerMetaBean.setFontFamily(textSticker.getFontName());
                layerMetaBean.setFontFile(textSticker.getFileName());
                layerMetaBean.setFontSize((int) textSticker.getTextSize());
                layerMetaBean.setLetterSpacing(textSticker.getLineSpacingExtra());
                layerMetaBean.setAlign(textSticker.isHorizon() ? "horizontal" : "vertical");
                int textColor = textSticker.getTextColor();
                String strColor = ColorUtils.toHexFromColor(textColor);
                layerMetaBean.setColor(strColor);
            }
            layerMetaList.add(layerMetaBean);
        }
        Log.d(TAG, "saveData: " + GsonUtil.t2Json2(layerMetaList));
        return layerMetaList;
    }

    /**
     * 当前控件适配
     */
    public void changeStickerFate() {
        if (stickerView != null && stickerView.handlingSticker != null) {
            stickerView.changeFillStyleSticker2(stickerView.handlingSticker, 1);
        }
    }

    /**
     * 当前控件填充
     */
    public void changeStickerFill() {
        if (stickerView != null && stickerView.handlingSticker != null) {
            stickerView.changeFillStyleSticker2(stickerView.handlingSticker, 0);
        }
    }

    /**
     * 改变文字方向
     * 1.排除横向多行此种情况不可操作。
     * 2.重新设置缩放和位移系数--顺序必须是先缩放后位移
     * 3.改变控件参数 将控件的方向改变。
     * 4.保存状态
     */
    public void changeTextOrientation() {
        if (stickerView != null) {
            Sticker handlingSticker = stickerView.handlingSticker;
            if (handlingSticker instanceof MyTextSticker) {
                MyTextSticker mytextSticker = (MyTextSticker) handlingSticker;
                //判断是否是横向多行
                if (mytextSticker.isHorizon() && mytextSticker.getText().contains("\n")) {
                    //这种情况不能换行
                    return;
                }
                //获取中心点计算左上角坐标
                PointF mappedCenterPoint = mytextSticker.getMappedCenterPoint();
                float showX = mappedCenterPoint.x;
                float showY = mappedCenterPoint.y;
                //设置方向相反的操作
                mytextSticker.setHorizon(!mytextSticker.isHorizon()).resizeText();
                float showW = mytextSticker.getCurrentWidth();
                float showH = mytextSticker.getCurrentHeight();
                float x = showX - showW / 2;
                float y = showY - showH / 2;
                //根据中心点进行新的缩放和位移操作
                Matrix matrix = new Matrix();
                matrix.postScale(mytextSticker.getCurrentScale(), mytextSticker.getCurrentScale());
                matrix.postTranslate(x, y);
                mytextSticker.setMatrix(matrix);
                //重新绘制控件
                stickerView.invalidate();
                //改变文字方向触发保存操作
                saveStep();
            }
        }

    }


    /**
     * 改变文字间距
     *
     * @param adExtra 新文字间距
     */
    public void changeTextSpacingExtra(float adExtra) {
        if (stickerView != null) {
            Sticker handlingSticker = stickerView.handlingSticker;
            if (handlingSticker instanceof MyTextSticker) {
                MyTextSticker mytextSticker = (MyTextSticker) handlingSticker;
                float lineSpacingExtra = mytextSticker.getLineSpacingExtra();
                if (lineSpacingExtra + adExtra < 0) {
                    ToastUtil.showShort("不能缩小了");
                    return;
                }
                mytextSticker.setLineSpacing(lineSpacingExtra + adExtra).resizeText();
                stickerView.invalidate();
                saveStep();
            }
        }
    }

    /**
     * 复制当前选中控件
     */
    public void copy() {
        Sticker handingSticker = stickerView.getHandingSticker();
        if (handingSticker instanceof MyDrawableSticker) {
            int stickerCount = getStickerCount();
            if (stickerCount >= GlobalVariable.MAX_STICKER_COUNT) {
                ToastUtil.showShort("最多只能上传" + GlobalVariable.MAX_STICKER_COUNT + "个元素");
                return;
            }
            MyDrawableSticker drawableSticker = (MyDrawableSticker) handingSticker;
            copyDrawableSticker(drawableSticker);
        } else if (handingSticker instanceof MyTextSticker) {
            int stickerCount = getStickerCount();
            if (stickerCount >= GlobalVariable.MAX_STICKER_COUNT) {
                ToastUtil.showShort("最多只能上传" + GlobalVariable.MAX_STICKER_COUNT + "个元素");
                return;
            }
            MyTextSticker myTextSticker = (MyTextSticker) handingSticker;
            copyTextSticker(myTextSticker);
        }
    }


    /**
     * 复制给定控件
     *
     * @param sticker 选中图片控件
     */
    private void copyDrawableSticker(MyDrawableSticker sticker) {
        try {
            StickerDataInfo info = sticker.getInfo();
            StickerDataInfo copyInfo = info.copy();
            BitmapDrawable drawable = (BitmapDrawable) sticker.getDrawable();
            BitmapDrawable bitmapDrawableCopy = BitmapUtils.getBitmapDrawableCopy(getResources(), drawable);
            MyDrawableSticker copySticker = new MyDrawableSticker(info.url, bitmapDrawableCopy);
            copySticker.setInfo(copyInfo);
            stickerView.addStickerNochange(copySticker, sticker.getMatrix());
        } catch (Exception e) {
            ToastUtil.showShort("内存溢出");
        }
    }

    /**
     * 复制选中文字控件
     *
     * @param myTextSticker 选中的文字控件
     */
    private void copyTextSticker(MyTextSticker myTextSticker) {
        MyTextSticker textSticker1 = new MyTextSticker(getActivity())
                .setText(myTextSticker.getText())
                .setTypeface(myTextSticker.getFileName(), myTextSticker.getFontName(), myTextSticker.getTypeface(), myTextSticker.getVersion(), myTextSticker.getWordartId())
                .setTextColor(myTextSticker.getTextColor())
                .setLineSpacing(myTextSticker.getLineSpacingExtra())
                .setHorizon(myTextSticker.isHorizon())
                .resizeText();
        stickerView.addStickerNochange(textSticker1, myTextSticker.getMatrix());
    }

    private List<List<StickerDataBean>> dataBeanList = new ArrayList<>();

    /**
     * 上一步
     */
    public void backStep() {
        if (!dataBeanList.isEmpty()) {
            int saveStep = dataBeanList.size() - 1;
            if (saveStep - 1 < 0) {
                ToastUtil.showShort("不能再后退了");
            } else {
                dataBeanList.remove(saveStep);
                List<StickerDataBean> stickerDataBeen = dataBeanList.get(dataBeanList.size() - 1);
                mPresenter.reShowData(dataBeanList.get(dataBeanList.size() - 1));
                Log.d(TAG, "backStep: 恢复镜像成：" + GsonUtil.t2Json2(stickerDataBeen));
                EventBus.getDefault().post(new EventMessage(EventBusEvent.EDITOR_HAS_CHANGED));
            }

        }
    }

    /**
     * 隐藏显示网格
     */
    public void showLine() {
        if (stickerView != null) {
            stickerView.setShowLine(!stickerView.isShowLine());
            stickerView.invalidate();
        }
    }

    /**
     * 获得使用过的素材id List
     *
     * @return 素材id列表
     */
    public ArrayList<String> getMaterialId() {
        ArrayList<String> materialIds = new ArrayList<>();
        if (stickerView != null) {
            List<Sticker> stickers = stickerView.getStickers();


            for (int i = 0; i < stickers.size(); i++) {
                Sticker sticker = stickers.get(i);
                if (sticker instanceof MyDrawableSticker) {
                    StickerDataInfo info = ((MyDrawableSticker) sticker).getInfo();

                    if (!StringUtil.isEmpty(info.materialId)) {
                        materialIds.add(info.materialId);
                    }
                }
            }
        }
        return materialIds;
    }
}
