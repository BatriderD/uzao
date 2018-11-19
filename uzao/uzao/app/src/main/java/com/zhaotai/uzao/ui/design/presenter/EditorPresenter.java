package com.zhaotai.uzao.ui.design.presenter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pingplusplus.android.Pingpp;
import com.xiaopo.flying.sticker.BitmapUtils;
import com.xiaopo.flying.sticker.FontCache;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.app.MyApplication;
import com.zhaotai.uzao.bean.ArtFontTopicBean;
import com.zhaotai.uzao.bean.BaseResult;
import com.zhaotai.uzao.bean.ChargeBean;
import com.zhaotai.uzao.bean.DefaultFontBean;
import com.zhaotai.uzao.bean.EventBean.EventBean;
import com.zhaotai.uzao.bean.EventBean.EventDownSuccessFont;
import com.zhaotai.uzao.bean.LayerMetaJsonBean;
import com.zhaotai.uzao.bean.MKUCarrierBean;
import com.zhaotai.uzao.bean.MaterialDetailBean;
import com.zhaotai.uzao.bean.OrderGoodsBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.PayCallBackBean;
import com.zhaotai.uzao.bean.PayInfo;
import com.zhaotai.uzao.bean.RequestChangeMetaDataBean;
import com.zhaotai.uzao.bean.RequestSaveMyDesignDataBean;
import com.zhaotai.uzao.bean.ResultMetaDataBean;
import com.zhaotai.uzao.bean.StickerPicInfoBean;
import com.zhaotai.uzao.bean.TechnologyBean;
import com.zhaotai.uzao.bean.UnPayMaterialBean;
import com.zhaotai.uzao.bean.UpLoadPicUtils;
import com.zhaotai.uzao.bean.UploadFileBean;
import com.zhaotai.uzao.bean.ValidateProductBean;
import com.zhaotai.uzao.bean.post.TemplateDesignInfo;
import com.zhaotai.uzao.bean.post.TemplateImageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.design.contract.EditorContract;
import com.zhaotai.uzao.ui.design.utils.FontDownLoadUtils;
import com.zhaotai.uzao.ui.login.activity.LoginMsgActivity;
import com.zhaotai.uzao.ui.order.activity.TemplateDesignPurchaseActivity;
import com.zhaotai.uzao.ui.order.activity.TemplatePurchaseActivity;
import com.zhaotai.uzao.ui.order.activity.TemplatePutawayActivity;
import com.zhaotai.uzao.utils.FileUtil;
import com.zhaotai.uzao.utils.GsonUtil;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.SPUtils;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

/**
 * description: 编辑器Activity Preenter
 * author : ZP
 * date: 2018/4/9 0009.
 */

public class EditorPresenter extends EditorContract.Presenter {
    /**
     * 裁剪图片 封数据
     *
     * @param url 数据的网络地址
     */
    boolean isImageCrop = true;
    private EditorContract.View mView;
    private String TAG = "EditorPresenter:";
    private String payWay;
    private FontDownLoadUtils fontDownLoadUtils;
    private List<String> orderIds = new ArrayList<>();
    private String tradeId;
    /**
     * 截图
     */
    private List<String> thumbnail;

    public EditorPresenter(Context context, EditorContract.View view) {
        mContext = context;
        this.mView = view;
    }

    @Override
    public void getColorList() {
        String[] colorsName = mContext.getResources().getStringArray(R.array.color_name);
        List<String> colorList = Arrays.asList(colorsName);
        mView.showColoList(colorList);
    }

    @Override
        public void getFontList() {
        HashMap<String, String> params = new HashMap<>();
        params.put("start", "0");
        params.put("getLockStatus", "N");
        Api.getDefault()
                .getArtFontTopicList(params)
                .compose(RxHandleResult.<PageInfo<ArtFontTopicBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<ArtFontTopicBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<ArtFontTopicBean> info) {
                        List<ArtFontTopicBean> list = info.list;
                        Iterator<ArtFontTopicBean> iter = list.iterator();
                        while (iter.hasNext()) {
                            ArtFontTopicBean bean = iter.next();
                            //名字是否合适
                            if ("Y".equals(bean.getLockStatus())) {
                                iter.remove();
                                continue;
                            }
                        }
                        mView.showArtFonts(info);
                    }

                    @Override
                    public void _onError(String message) {
                        mView.showNetworkFail(message);
                        System.out.println(message);
                    }
                });
    }

    @Override
    public void getDefaultFont() {
//        下载默认字体
//        1获取默认字体列表
        Api.getDefault().getDefaultFont()
                .compose(RxHandleResult.<List<DefaultFontBean>>handleResult())
                .subscribe(new RxSubscriber<List<DefaultFontBean>>(mContext) {
                    @Override
                    public void _onNext(List<DefaultFontBean> defaultFontBeen) {
                        if (defaultFontBeen != null) {
                            DefaultFontBean defaultFontBean = defaultFontBeen.get(0);
                            if (defaultFontBean != null) {
                                String file = defaultFontBean.getFile();
                                String name = defaultFontBean.getName();
                                downLoadFont(file, name);
                            } else {
                                mView.showNetworkFail("初始化失败");
                                ToastUtil.showShort("字体初始化失败");
                            }

                        } else {
                            mView.showNetworkFail("初始化失败");
                            ToastUtil.showShort("字体初始化失败");
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        mView.showNetworkFail(message);
                    }
                });
    }

    /**
     * 根据MKUid获取MKU信息
     *
     * @param mkuId MkuId
     */
    @Override
    public void getCarrierInfo(String mkuId) {
        Api.getDefault().getMKUInfo(mkuId).compose(RxHandleResult.<MKUCarrierBean>handleResult())
                .subscribe(new RxSubscriber<MKUCarrierBean>(mContext) {
                    @Override
                    public void _onNext(MKUCarrierBean bean) {
                        mView.showCarrierData(bean);
                    }

                    @Override
                    public void _onError(String message) {
                        mView.showCarrierData(null);
                    }
                });
    }

    private List<LayerMetaJsonBean> metaJsonBeanList = null;

    /**
     * 获取设计数据 和过滤的请求结果
     *
     * @param designId 设计id
     */
    @Override
    public void getDesignMetaData(final String designId) {
        Api.getDefault()
                .searchMyDesignSpu(designId)
                .compose(RxHandleResult.<ResultMetaDataBean>handleResultMap())
                .flatMap(new Function<ResultMetaDataBean, ObservableSource<BaseResult<ValidateProductBean>>>() {
                    @Override
                    public ObservableSource<BaseResult<ValidateProductBean>> apply(@NonNull ResultMetaDataBean resultMetaDataBean) throws Exception {
                        String designMeta = resultMetaDataBean.getDesignMeta();
                        if (!StringUtil.isEmpty(designMeta)) {
                            Type mType = new TypeToken<List<LayerMetaJsonBean>>() {
                            }.getType();
                            try {
                                metaJsonBeanList = GsonUtil.getGson().fromJson(designMeta, mType);
                            } catch (Exception e) {
                                Observable.error(e);
                            }
                        }
                        return Api.getDefault().validateDesign(designId);
                    }
                }).compose(RxHandleResult.<ValidateProductBean>handleResultMap())
                .map(new Function<ValidateProductBean, List<LayerMetaJsonBean>>() {
                    @Override
                    public List<LayerMetaJsonBean> apply(@NonNull ValidateProductBean validateProductBean) throws Exception {
                        mView.showOverdue(validateProductBean);
                        return filterData(validateProductBean, metaJsonBeanList);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<List<LayerMetaJsonBean>>(mContext) {
                    @Override
                    public void _onNext(List<LayerMetaJsonBean> layerMetaJsonBeen) {
                        System.out.println("getDesignMetaData 查询成功" + layerMetaJsonBeen);
                        mView.showDesignMetaData(layerMetaJsonBeen);
                    }

                    @Override
                    public void _onError(String message) {
                        if (metaJsonBeanList!=null){
                            mView.showDesignMetaData(metaJsonBeanList);
                            return;
                        }
                        System.out.println("getDesignMetaData 查询失败" + message);
                        mView.showDesignMetaData(null);
                    }
                });
    }

    /**
     * 过滤数据
     *
     * @param bean     需要删除的数据bean
     * @param jsonBean 保存的设计bean
     */
    private List<LayerMetaJsonBean> filterData(ValidateProductBean bean, List<LayerMetaJsonBean> jsonBean) {
        //过滤艺术字
        for (ValidateProductBean.Content content : bean.wordart) {
            //过期的艺术字1
            for (LayerMetaJsonBean jsonBean1 : jsonBean) {
                List<LayerMetaJsonBean.LayerMetaBean> layerMeta = jsonBean1.getLayerMeta();
                Iterator<LayerMetaJsonBean.LayerMetaBean> iter = layerMeta.iterator();
                while (iter.hasNext()) {
                    LayerMetaJsonBean.LayerMetaBean layerMetaBean = iter.next();
                    //名字是否合适
                    if ("text".equals(layerMetaBean.getType())) {
                        if (content.id.equals(layerMetaBean.getFontId())) {
                            iter.remove();
                            continue;
                        }
                    }
                }
            }
        }
        //过滤素材
        for (ValidateProductBean.Content content : bean.material.notAvailable) {
            //过滤素材
            for (LayerMetaJsonBean jsonBean1 : jsonBean) {
                List<LayerMetaJsonBean.LayerMetaBean> layerMeta = jsonBean1.getLayerMeta();
                Iterator<LayerMetaJsonBean.LayerMetaBean> iter = layerMeta.iterator();
                while (iter.hasNext()) {
                    LayerMetaJsonBean.LayerMetaBean layerMetaBean = iter.next();
                    if ("bitmap".equals(layerMetaBean.getType())) {
                        if (content.id.equals(layerMetaBean.getMaterialId())) {
                            iter.remove();
                        }
                    }
                }
            }
        }
        return jsonBean;
    }

    /**
     * 过滤数据
     *
     * @param bean     需要删除的数据bean
     * @param jsonBean 保存的设计bean
     */
    public LayerMetaJsonBean filterData(ValidateProductBean bean, LayerMetaJsonBean jsonBean) {
        //过滤艺术字
        for (ValidateProductBean.Content content : bean.wordart) {
            //过期的艺术字1
            List<LayerMetaJsonBean.LayerMetaBean> layerMeta = jsonBean.getLayerMeta();
            Iterator<LayerMetaJsonBean.LayerMetaBean> iter = layerMeta.iterator();
            while (iter.hasNext()) {
                LayerMetaJsonBean.LayerMetaBean layerMetaBean = iter.next();
                //名字是否合适
                if ("text".equals(layerMetaBean.getType())) {
                    if (content.id.equals(layerMetaBean.getFontId())) {
                        iter.remove();
                        continue;
                    }
                }
            }
        }

        //过滤素材 过期 没下架
        for (ValidateProductBean.Content content : bean.material.expiredPublished) {
            //过期的
            List<LayerMetaJsonBean.LayerMetaBean> layerMeta = jsonBean.getLayerMeta();
            Iterator<LayerMetaJsonBean.LayerMetaBean> iter = layerMeta.iterator();
            while (iter.hasNext()) {
                LayerMetaJsonBean.LayerMetaBean layerMetaBean = iter.next();
                if ("bitmap".equals(layerMetaBean.getType())) {
                    if (content.id.equals(layerMetaBean.getMaterialId())) {
                        iter.remove();
                    }
                }
            }
        }
        //过滤素材
        for (ValidateProductBean.Content content : bean.material.notAvailable) {
            //过滤素材
            List<LayerMetaJsonBean.LayerMetaBean> layerMeta = jsonBean.getLayerMeta();
            Iterator<LayerMetaJsonBean.LayerMetaBean> iter = layerMeta.iterator();
            while (iter.hasNext()) {
                LayerMetaJsonBean.LayerMetaBean layerMetaBean = iter.next();
                if ("bitmap".equals(layerMetaBean.getType())) {
                    if (content.id.equals(layerMetaBean.getMaterialId())) {
                        iter.remove();
                    }
                }

            }
        }
        return jsonBean;
    }

    @Override
    public void getTemplateInfo(String spuId) {
        Api.getDefault().getTemplateDesign(spuId)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext) {
                    @Override
                    public void _onNext(String layerJSon) {
                        mView.showTemplateInfo(layerJSon);
                    }

                    @Override
                    public void _onError(String message) {
                        mView.showTemplateInfo(null);
                    }
                });
    }

    /**
     * 对比默认工艺的位置
     *
     * @param craftId     默认工艺id
     * @param craftModels 默认工艺数组
     * @return 默认工艺位置
     */
    @Override
    public int getCraftIdPos(String craftId, List<TechnologyBean> craftModels) {
        int pos = 0;
        if (craftId == null) {
            return pos;
        }
        for (int i = 0; i < craftModels.size(); i++) {
            TechnologyBean bean = craftModels.get(i);
            if (bean != null && bean.getSequenceNBR() != null && craftId.equals(bean.getSequenceNBR())) {
                pos = i;
                break;
            }
        }
        return pos;
    }

    /**
     * 从详情页 立即购买
     *
     * @param way  支付方式
     * @param data 素材信息
     */
    public void getMaterialPayInfo(final String way, List<MaterialDetailBean> data) {
        if (data == null) return;
        mView.showProgress();
        payWay = way;
        List<String> materialIds = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            materialIds.add(data.get(i).sequenceNBR);
        }
        Api.getDefault().createMaterialOrder(materialIds)
                .compose(RxHandleResult.<List<OrderGoodsBean>>handleResult())
                .subscribe(new RxSubscriber<List<OrderGoodsBean>>(mContext) {
                    @Override
                    public void _onNext(List<OrderGoodsBean> orderGoodsBeen) {
                        orderIds.clear();
                        for (int i = 0; i < orderGoodsBeen.size(); i++) {
                            orderIds.add(orderGoodsBeen.get(i).orderNo);
                        }
                        getPayInfo(way, orderIds);
                    }

                    @Override
                    public void _onError(String message) {
                        mView.stopProgress();
                    }
                });
    }

    /**
     * 根据订单id 获取支付结果
     */
    public void getPayInfo(String way, List<String> orderIds) {
        PayInfo payInfo = new PayInfo();
        payInfo.orderNos = orderIds;
        Api.getDefault().getPayInfo(way, payInfo)
                .compose(RxHandleResult.<ChargeBean>handleResult())
                .subscribe(new RxSubscriber<ChargeBean>(mContext, false) {
                    @Override
                    public void _onNext(ChargeBean chargeBean) {
                        //支付id
                        tradeId = chargeBean.order_no;
                        Gson gson = new Gson();
                        String data = gson.toJson(chargeBean);
                        pay(data);
                    }

                    @Override
                    public void _onError(String message) {
                        mView.stopProgress();
                    }
                });
    }

    /**
     * 根据素材id查询素材信息
     *
     * @param materialId 素材id
     */
    @Override
    public void addMaterialDetail(String materialId) {
        Api.getDefault().getMaterialDetail(materialId)
                .compose(RxHandleResult.<MaterialDetailBean>handleResult())
                .subscribe(new RxSubscriber<MaterialDetailBean>(mContext) {
                    @Override
                    public void _onNext(MaterialDetailBean s) {
                        mView.showMaterialDetail(s);
                    }

                    @Override
                    public void _onError(String message) {
                        mView.showMaterialDetail(null);
                    }
                });

    }

    /**
     * @param fontName 字体名称
     * @param fileName 字体文件名称
     * @param text     文字内容
     */
    @Override
    public synchronized void changeTypeFace(final String fontName, final String fileName, final String text, final int version, final String wordartId) {
        if (StringUtil.isEmpty(text)) {
            mView.handingTextFontChangeStart("优造中国");
        } else {
            mView.handingTextFontChangeStart(text);
        }

        File fontFile = FileUtil.getFontFile(mContext, fileName);

        if (fontFile.exists()) {
//           字体存在并可用
            Log.d(TAG, "getTypeFace: " + fileName + "这个字体本地有");
            mView.handingTextFontChangeEnd(fontName, fileName, text, version, wordartId);
            return;
        }
        fontDownLoadUtils = new FontDownLoadUtils();
        fontDownLoadUtils.downLoadFont(mContext, fileName, fontName, new FontDownLoadUtils.FontDownLoadListener() {
            @Override
            public void onReadyLoading() {
                ToastUtil.showShort("字体下载中...");
            }

            @Override
            public void onSuccess(File file) {
                mView.handingTextFontChangeEnd(fontName, fileName, text, version, wordartId);
            }

            @Override
            public void onError(String message) {
                ToastUtil.showShort("字体下载失败");
                mView.handingTextFontChangeEndFailed();
            }
        });
    }

    /**
     * 恢复数据的下载字体
     *
     * @param fileName 字体文件名
     * @param fontName 字体名称
     */
    @Override
    public void releaseDownLoadFont(final String fileName, final String fontName) {
        FontDownLoadUtils fontDownLoadUtils = new FontDownLoadUtils();
        final EventDownSuccessFont successFont = new EventDownSuccessFont(fileName, fontName);
        fontDownLoadUtils.downLoadFont(mContext, fileName, fontName, new FontDownLoadUtils.FontDownLoadListener() {
            @Override
            public void onReadyLoading() {
                Log.d(TAG, "onReadyLoading: 字体已经在下下载了" + fontName);
            }

            @Override
            public void onSuccess(File file) {
                successFont.typeface = FontCache.reFreshFile(fileName, file.getAbsolutePath());
                EventBus.getDefault().post(successFont);
            }

            @Override
            public void onError(String message) {
                Log.d(TAG, "_onError: 字体" + fileName + "下载失败:" + message);
                successFont.typeface = null;
                EventBus.getDefault().post(successFont);
            }
        });
    }

    /**
     * 下载默认字体
     *
     * @param file 字体文件名
     * @param name 字体名称
     */
    private void downLoadFont(final String file, final String name) {
        final File fontFile = FileUtil.getFontFile(mContext, file);

        if (fontFile.exists()) {
//           字体存在并可用
            SPUtils.setSharedStringData(GlobalVariable.DEFAULT_FONT_FILENAME, file);
            SPUtils.setSharedStringData(GlobalVariable.DEFAULT_FONT_NAME, name);
            mView.initCarrieData();
        } else {
//            下载这个字体
            Log.d(TAG, "getTypeFace: " + file + "这个字体还没有去下载");
            FontDownLoadUtils fontDownLoadUtils = new FontDownLoadUtils();
            fontDownLoadUtils.downLoadFont(mContext, file, name, new FontDownLoadUtils.FontDownLoadListener() {
                @Override
                public void onReadyLoading() {
                    Log.d(TAG, "onReadyLoading: 字体已经在下下载了" + name);
                }

                @Override
                public void onSuccess(File files) {
                    SPUtils.setSharedStringData(GlobalVariable.DEFAULT_FONT_FILENAME, file);
                    SPUtils.setSharedStringData(GlobalVariable.DEFAULT_FONT_NAME, name);
                    FontCache.reFreshFile(file, fontFile.getAbsolutePath());
                    Log.d(TAG, "onComplete: +初始化字体完成");
                    mView.initCarrieData();
                }

                @Override
                public void onError(String message) {
                    mView.showNetworkFail("初始化失败");
                    ToastUtil.showShort("字体初始化失败");
                }
            });
        }
    }

    /**
     * 上传本地图片到相册
     *
     * @param originalAddressPath 本地地址
     */
    @Override
    public void upLoadBitmap(final String originalAddressPath) {
        File file = new File(originalAddressPath);
        //上传到我自己的图片里去
        UpLoadPicUtils.upLoadMyDesign(file, new RxSubscriber<UploadFileBean>(mContext) {
            @Override
            public void _onNext(UploadFileBean upDesignPictureBean) {
                if (!LoginHelper.getLoginStatus()) {
                    //未登录状态下保存上传图片信息
                    saveUnLoadData(upDesignPictureBean.sequenceNBR);
                }
                EventBus.getDefault().post(new EventBean<>(new StickerPicInfoBean(upDesignPictureBean.fileId, Float.valueOf(upDesignPictureBean.resizeScale)), EventBusEvent.RECEIVE_NETWORK_PICTURE_INFO));

            }

            @Override
            public void _onError(String message) {
                EventBus.getDefault().post(new EventBean<>(null, EventBusEvent.RECEIVE_NETWORK_PICTURE_INFO));
                ToastUtil.showShort("图片上传失败~");
            }
        });
    }

    /**
     * 支付回调
     */
    public void callback() {
        if (StringUtil.isEmpty(tradeId)) return;
        Api.getDefault().callbackPay(tradeId, "")
                .compose(RxHandleResult.<PayCallBackBean>handleResult())
                .subscribe(new RxSubscriber<PayCallBackBean>(mContext, false) {
                    @Override
                    public void _onNext(PayCallBackBean s) {
                    }

                    @Override
                    public void _onError(String message) {
                    }
                });
    }

    /**
     * 重新支付
     */
    public void repayInfo() {
        mView.showProgress();
        PayInfo payInfo = new PayInfo();
        payInfo.orderNos = orderIds;
        Api.getDefault().getPayInfo(payWay, payInfo)
                .compose(RxHandleResult.<ChargeBean>handleResult())
                .subscribe(new RxSubscriber<ChargeBean>(mContext, false) {
                    @Override
                    public void _onNext(ChargeBean chargeBean) {
                        //支付id
                        tradeId = chargeBean.order_no;
                        Gson gson = new Gson();
                        String data = gson.toJson(chargeBean);
                        pay(data);
                    }

                    @Override
                    public void _onError(String message) {
                        mView.stopProgress();
                    }
                });
    }

    /**
     * 筛选使用的素材 获得其中未付费的素材
     *
     * @param allUsedMaterials 使用的素材列表
     */
    @Override
    public void getUnBoughtMaterial(ArrayList<String> allUsedMaterials) {

        if (allUsedMaterials.size() > 0) {
            StringBuilder idBuilder = new StringBuilder(allUsedMaterials.get(0));
            for (int i = 1; i < allUsedMaterials.size(); i++) {
                idBuilder.append("," + allUsedMaterials.get(i));
            }
            //处理所有使用的素材文件
            Api.getDefault().getUnBoughtMaterialList(idBuilder.toString())
                    .compose(RxHandleResult.<UnPayMaterialBean>handleResult())
                    .subscribe(new RxSubscriber<UnPayMaterialBean>(mContext) {
                        @Override
                        public void _onNext(UnPayMaterialBean materialListBean) {
                            //通知view处理 筛选过未付费的素材文件
                            mView.showUnBoughtMaterial(materialListBean);
                        }

                        @Override
                        public void _onError(String message) {
                            mView.showUnBoughtMaterial(null);
                        }
                    });
        }
    }

    @Override
    public void finishToSaveFor3d(final Bitmap thumbnail) {
        UpLoadPicUtils.upLoadPic(thumbnail, new RxSubscriber<UploadFileBean>(mContext) {
            @Override
            public void _onNext(UploadFileBean bean) {
                BitmapUtils.recycleBitmap(thumbnail);
                mView.showSave3DSuccess(bean.fileId);
            }

            @Override
            public void _onError(String message) {
                BitmapUtils.recycleBitmap(thumbnail);
                mView.showSave3DSuccess("");
                ToastUtil.showShort("保存失败请稍后再试");
            }
        });
    }

    //结束并保存设计
    @Override
    public void finishToSaveDesign(final String mkuId, final String templateSpuId, final String sampleId, final ArrayList<Bitmap> thumbnails,
                                   final String sequenceNBR, final String layerJson, final ArrayList<TemplateImageInfo> templateImageInfos, final String isTemplate, final ArrayList<String> materialIds, final UnPayMaterialBean materialListBean, final int doWhat) {
        final ArrayList<MultipartBody.Part> bodyList = UpLoadPicUtils.getBodyList(thumbnails);

        Api.getDefault().uploadTheFiles(bodyList)
                .subscribeOn(Schedulers.io())
                .compose(RxHandleResult.<List<UploadFileBean>>handleResultMap())
                .flatMap(new Function<List<UploadFileBean>, ObservableSource<BaseResult<UploadFileBean>>>() {
                    @Override
                    public ObservableSource<BaseResult<UploadFileBean>> apply(@NonNull List<UploadFileBean> urls) throws Exception {
                        if (templateImageInfos.size() == urls.size()) {
                            for (int i = 0; i < templateImageInfos.size(); i++) {
                                TemplateImageInfo templateImageInfo = templateImageInfos.get(i);
                                templateImageInfo.thumbnail = urls.get(i).fileId;
                            }
                        } else {
                            Observable.error(new Exception("信息不对"));
                        }
                        List<String> urlStrings = new ArrayList<>();
                        for (int i = 0; i < urls.size(); i++) {
                            urlStrings.add(urls.get(i).fileId);
                        }
                        thumbnail = urlStrings;
                        MultipartBody.Part body = UpLoadPicUtils.getBody(thumbnails.get(0));
                        return Api.getDefault().uploadTheFile(body);
                    }
                }).compose(RxHandleResult.<UploadFileBean>handleResultMap())
                .flatMap(new Function<UploadFileBean, ObservableSource<BaseResult<ResultMetaDataBean>>>() {
                    @Override
                    public ObservableSource<BaseResult<ResultMetaDataBean>> apply(@NonNull UploadFileBean photoUrl) throws Exception {
                        RequestSaveMyDesignDataBean requestSaveMyDesignDataBean = new RequestSaveMyDesignDataBean(layerJson, photoUrl.fileId, sampleId, mkuId);
                        if (StringUtil.isEmpty(sequenceNBR)) {
                            return Api.getDefault().saveMyDesign(requestSaveMyDesignDataBean);
                        } else {
                            return Api.getDefault().changeMyDesignSpu(sequenceNBR, new RequestChangeMetaDataBean(layerJson, photoUrl.fileId));
                        }
                    }
                }).compose(RxHandleResult.<ResultMetaDataBean>handleResult())
                .subscribe(new RxSubscriber<ResultMetaDataBean>(mContext) {
                    @Override
                    public void _onNext(ResultMetaDataBean resultMetaDataBean) {

//                        获取成功
                        mView.showFinishSaveFinish();
                        System.out.println(mkuId + templateSpuId + resultMetaDataBean.getSequenceNBR() + templateImageInfos + thumbnail.toString());

                        if (doWhat == 0) {
                            // 购买
                            TemplatePurchaseActivity.launch(mContext, mkuId, templateImageInfos, thumbnail, resultMetaDataBean.getSequenceNBR(), isTemplate, materialIds, materialListBean, templateSpuId);
                        } else {
                            //上架
                            TemplatePutawayActivity.launch(mContext, mkuId, thumbnail, templateImageInfos, resultMetaDataBean.getSequenceNBR(), isTemplate, "2d", materialIds);
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        mView.showFinishSaveFinish();
                        for (Bitmap b : thumbnails) {
                            BitmapUtils.recycleBitmap(b);
                        }
                        if ("您没有该资源的访问权限".equals(message)) {
                            ToastUtil.showShort("请登录后再保存");
                            LoginHelper.goLogin(mContext);
                        } else if (MyApplication.getAppContext().getString(R.string.no_net).equals(message)) {
                            ToastUtil.showShort("网络错误");
                        } else {
                            ToastUtil.showShort("保存错误");
                        }

                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        for (Bitmap b : thumbnails) {
                            BitmapUtils.recycleBitmap(b);
                        }
                    }
                });
    }

    /**
     * ping++ 发起支付
     *
     * @param data 支付信息
     */
    public void pay(String data) {
        mView.stopProgress();
        //根据返回值 用ping++拉起支付页面
        Pingpp.createPayment((Activity) mContext, data);
    }

    /**
     * 将匿名登录下 上传的图片保存起来  等待登录成功将其保存上传到我的图片里去
     *
     * @param sqnNBR 图片地址
     */
    public void saveUnLoadData(String sqnNBR) {
        String pics = SPUtils.getSharedStringData(GlobalVariable.UNLOADDESIGNPICS);
        if (StringUtil.isEmpty(pics)) {
            pics = sqnNBR;
        } else {
            pics = pics + "," + sqnNBR;
        }
        SPUtils.setSharedStringData(GlobalVariable.UNLOADDESIGNPICS, pics);
    }

    //
    @Override
    public void finishToBuyDesign(final String mkuId, final String templateSpuId, final String sampleId, final ArrayList<Bitmap> thumbnails,
                                  String sequenceNBR, final String layerJson, final List<TemplateImageInfo> templateImageInfos, final String isTemplate, final ArrayList<String> materialIds, final UnPayMaterialBean materialListBean, final int doWhat) {
        final ArrayList<MultipartBody.Part> bodyList = UpLoadPicUtils.getBodyList(thumbnails);

        Api.getDefault().uploadTheFiles(bodyList)
                .subscribeOn(Schedulers.io())
                .compose(RxHandleResult.<List<UploadFileBean>>handleResultMap())
                .flatMap(new Function<List<UploadFileBean>, ObservableSource<BaseResult<UploadFileBean>>>() {
                    @Override
                    public ObservableSource<BaseResult<UploadFileBean>> apply(@NonNull List<UploadFileBean> urls) throws Exception {
                        if (templateImageInfos.size() == urls.size()) {
                            for (int i = 0; i < templateImageInfos.size(); i++) {
                                TemplateImageInfo templateImageInfo = templateImageInfos.get(i);
                                templateImageInfo.thumbnail = urls.get(i).fileId;
                            }
                        } else {
                            Observable.error(new Exception("信息不对"));
                        }
                        List<String> urlStrings = new ArrayList<>();
                        for (int i = 0; i < urls.size(); i++) {
                            urlStrings.add(urls.get(i).fileId);
                        }
                        thumbnail = urlStrings;
                        MultipartBody.Part body = UpLoadPicUtils.getBody(thumbnails.get(0));
                        return Api.getDefault().uploadTheFile(body);
                    }
                }).compose(RxHandleResult.<UploadFileBean>handleResultMap())
                .subscribe(new RxSubscriber<UploadFileBean>(mContext) {
                    @Override
                    public void _onNext(UploadFileBean photoUrl) {
                        mView.showFinishSaveFinish();
                        TemplateDesignInfo templateDesignInfo = new TemplateDesignInfo(sampleId, mkuId, photoUrl.fileId, layerJson);
                        TemplateDesignPurchaseActivity.launch(mContext, templateImageInfos, thumbnail, materialListBean, templateDesignInfo, templateSpuId);
                    }

                    @Override
                    public void _onError(String message) {
                        mView.showFinishSaveFinish();
                        for (Bitmap b : thumbnails) {
                            BitmapUtils.recycleBitmap(b);
                        }
                        if ("您没有该资源的访问权限".equals(message)) {
                            ToastUtil.showShort("请登录后再保存");
                            LoginMsgActivity.launch(mContext);
                        } else if (MyApplication.getAppContext().getString(R.string.no_net).equals(message)) {
                            ToastUtil.showShort("网络错误");
                        } else {
                            ToastUtil.showShort("保存错误");
                        }

                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        for (Bitmap b : thumbnails) {
                            BitmapUtils.recycleBitmap(b);
                        }
                    }
                });
    }

    public void imageCrop(final String url) {
        if (!isImageCrop) {
            return;
        }
        Observable.just(url)
                .map(new Function<String, File>() {
                    @Override
                    public File apply(@NonNull String s) throws Exception {
                        isImageCrop = false;
                        Bitmap bitmap = Glide.with(mContext)
                                .load(url)
                                .asBitmap()
                                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .get();

//                         获得图片
                        StringBuilder path = new StringBuilder();
                        File externalCacheDir = mContext.getExternalCacheDir();
                        if (FileUtil.isSDAvailable() && externalCacheDir != null) {
                            path.append(externalCacheDir.getAbsolutePath());
                        } else {
                            path.append(mContext.getCacheDir().getAbsolutePath());
                        }
                        File file = new File(path.toString(), FileUtil.getFileName(url) + ".png");
//                        path.append(File.separator + FileUtil.getFileName(url) + ".png");
                        Log.d(TAG, "apply: 图片保存地址" + file.getAbsolutePath());

                        return BitmapUtils.saveImageToGallery(file, bitmap);

                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<File>(mContext) {
                    @Override
                    public void _onNext(File file) {

                        Uri source = Uri.fromFile(file);
                        File desCrop = FileUtil.getNewFile(mContext, "crop");
                        Uri des = Uri.fromFile(desCrop);

                        UCrop.Options options = new UCrop.Options();
                        options.setHideBottomControls(true);
                        options.setToolbarColor(ContextCompat.getColor(mContext, R.color.color_black));
                        options.setToolbarTitle("");
                        options.setStatusBarColor(ContextCompat.getColor(mContext, R.color.color_black));
                        options.setFreeStyleCropEnabled(true);
                        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.SCALE, UCropActivity.SCALE);
                        options.setMaxBitmapSize(GlobalVariable.CROP_MAX_SIZE);

                        isImageCrop = true;
                        UCrop.of(source, des)
                                .withOptions(options)
                                .start((Activity) mContext);
                    }

                    @Override
                    public void _onError(String message) {
                        isImageCrop = true;
                        Log.d(TAG, "_onError: 图片保存失败");
                    }
                });
    }


    /**
     * 一步保存我的设计
     *
     * @param thumbnail     首页截图
     * @param layerJson     数据化json
     * @param sequenceNBR   保存商品SequenceNBR
     * @param mkuId         mkuid
     * @param templateSpuId 模板商品id
     */
    @Override
    public void saveArtWorkAll(final Bitmap thumbnail, final String layerJson, final String sequenceNBR, final String mkuId, final String templateSpuId) {

        Api.getDefault().uploadTheFile(UpLoadPicUtils.getBody(thumbnail))
                .compose(RxHandleResult.<UploadFileBean>handleResultMap())
                .flatMap(new Function<UploadFileBean, ObservableSource<BaseResult<ResultMetaDataBean>>>() {
                    @Override
                    public ObservableSource<BaseResult<ResultMetaDataBean>> apply(@NonNull UploadFileBean uploadFileBean) throws Exception {
                        return Api.getDefault().saveMyDesign(new RequestSaveMyDesignDataBean(layerJson, uploadFileBean.fileId, sequenceNBR, mkuId));
                    }
                }).compose(RxHandleResult.<ResultMetaDataBean>handleResult())
                .subscribe(new RxSubscriber<ResultMetaDataBean>(mContext) {
                    @Override
                    public void _onNext(ResultMetaDataBean s) {
                        System.out.println("getDesignMetaData 保存成功成功" + s);
                        BitmapUtils.recycleBitmap(thumbnail);
                        mView.saveArtWorkSuccess(s);
                    }

                    @Override
                    public void _onError(String message) {
                        BitmapUtils.recycleBitmap(thumbnail);
                        mView.saveArtWorkSuccess(null);
                        System.out.println("getDesignMetaData 保存失败" + message);
                    }
                });
    }


    /**
     * 修改设计
     *
     * @param designMeta 设计元数据
     * @param thumbnail  位图图片
     */
    @Override
    public void chaneArtWork(final String sequenceNBR, final String designMeta, final Bitmap thumbnail) {
        Api.getDefault().uploadTheFile(UpLoadPicUtils.getBody(thumbnail))
                .compose(RxHandleResult.<UploadFileBean>handleResultMap())
                .flatMap(new Function<UploadFileBean, ObservableSource<BaseResult<ResultMetaDataBean>>>() {
                    @Override
                    public ObservableSource<BaseResult<ResultMetaDataBean>> apply(@NonNull UploadFileBean bean) throws Exception {
                        return Api.getDefault()
                                .changeMyDesignSpu(sequenceNBR, new RequestChangeMetaDataBean(designMeta, bean.fileId));
                    }
                }).compose(RxHandleResult.<ResultMetaDataBean>handleResult())
                .subscribe(new RxSubscriber<ResultMetaDataBean>(mContext) {
                    @Override
                    public void _onNext(ResultMetaDataBean s) {
                        System.out.println("getDesignMetaData 修改成功" + s);
                        mView.saveArtWorkSuccess(s);
                    }

                    @Override
                    public void _onError(String message) {
                        System.out.println("getDesignMetaData 修改失败" + message);
                        mView.saveArtWorkSuccess(null);
                    }
                });
    }

    /**
     * 已经保存过的设计 进行上架或者购买的操作
     *
     * @param mkuId              商品设计mku
     * @param spuId              商品id
     * @param thumbnails         各个面截图
     * @param designId           设计id
     * @param templateImageInfos 各个面的图片数据
     * @param isTemplate         是否是模板
     * @param allUsedMaterials   所有素材
     * @param unBoughtMaterials  未购买素材
     * @param doWhat             购买或者上架 0 购买1 上架
     */
    @Override
    public void finishToNextWithoutSave(final String mkuId, final String spuId, final ArrayList<Bitmap> thumbnails, final String designId, final ArrayList<TemplateImageInfo> templateImageInfos, final String isTemplate, final ArrayList<String> allUsedMaterials, final UnPayMaterialBean unBoughtMaterials, final int doWhat) {
        final ArrayList<MultipartBody.Part> bodyList = UpLoadPicUtils.getBodyList(thumbnails);

        Api.getDefault().uploadTheFiles(bodyList)
                .subscribeOn(Schedulers.io())
                .compose(RxHandleResult.<List<UploadFileBean>>handleResultMap())
                .subscribe(new RxSubscriber<List<UploadFileBean>>(mContext) {
                    @Override
                    public void _onNext(List<UploadFileBean> uploadFileBeen) {
                        //  获取成功
                        mView.showFinishSaveFinish();
                        if (doWhat == 0) {
                            // 购买
                            TemplatePurchaseActivity.launch(mContext, mkuId, templateImageInfos, thumbnail, designId, isTemplate, allUsedMaterials, unBoughtMaterials, spuId);
                        } else {
                            //上架
                            TemplatePutawayActivity.launch(mContext, mkuId, thumbnail, templateImageInfos, designId, isTemplate, "2d", allUsedMaterials);
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        mView.showFinishSaveFinish();
                        for (Bitmap b : thumbnails) {
                            BitmapUtils.recycleBitmap(b);
                        }
                        if ("您没有该资源的访问权限".equals(message)) {
                            ToastUtil.showShort("请登录后再保存");
                            LoginHelper.goLogin(mContext);
                        } else if (MyApplication.getAppContext().getString(R.string.no_net).equals(message)) {
                            ToastUtil.showShort("网络错误");
                        } else {
                            ToastUtil.showShort("保存错误");
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        for (Bitmap b : thumbnails) {
                            BitmapUtils.recycleBitmap(b);
                        }
                    }
                });
    }

}
