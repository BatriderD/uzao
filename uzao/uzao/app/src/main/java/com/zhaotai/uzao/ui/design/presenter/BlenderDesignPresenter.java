package com.zhaotai.uzao.ui.design.presenter;

import android.app.Activity;
import android.content.Context;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pingplusplus.android.Pingpp;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.BaseResult;
import com.zhaotai.uzao.bean.ChargeBean;
import com.zhaotai.uzao.bean.LayerMetaJsonBean;
import com.zhaotai.uzao.bean.MaterialDetailBean;
import com.zhaotai.uzao.bean.OrderGoodsBean;
import com.zhaotai.uzao.bean.PayCallBackBean;
import com.zhaotai.uzao.bean.PayInfo;
import com.zhaotai.uzao.bean.RequestChangeMetaDataBean;
import com.zhaotai.uzao.bean.RequestSaveMyDesignDataBean;
import com.zhaotai.uzao.bean.ResultMetaDataBean;
import com.zhaotai.uzao.bean.ThreeDimensionalBean;
import com.zhaotai.uzao.bean.UnPayMaterialBean;
import com.zhaotai.uzao.bean.UploadFileBean;
import com.zhaotai.uzao.bean.ValidateProductBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.design.activity.EditorActivity;
import com.zhaotai.uzao.ui.design.bean.DesignMetaBean;
import com.zhaotai.uzao.ui.design.contract.BlenderDesignContract;
import com.zhaotai.uzao.ui.order.activity.TemplatePurchaseActivity;
import com.zhaotai.uzao.ui.order.activity.TemplatePutawayActivity;
import com.zhaotai.uzao.utils.GsonUtil;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Time: 2017/9/19
 * Created by LiYou
 * Description : 3d编辑器的presenter
 */

public class BlenderDesignPresenter extends BlenderDesignContract.Presenter {

    private BlenderDesignContract.View view;
    private Gson gson = new Gson();
    private String designId;
    private String tradeId;
    private List<String> materialIds = new ArrayList<>();
    private UnPayMaterialBean materialData = new UnPayMaterialBean();
    private String payWay;
    private List<String> orderIds = new ArrayList<>();

    public BlenderDesignPresenter(BlenderDesignContract.View view, Context context) {
        mContext = context;
        this.view = view;
    }

    /**
     * 获取3D信息
     *
     * @param templateId 载体id
     */
    @Override
    public void get3dDetail(String templateId) {
        Api.getDefault().get3dInfo(templateId)
                .compose(RxHandleResult.<ThreeDimensionalBean>handleResult())
                .subscribe(new RxSubscriber<ThreeDimensionalBean>(mContext, true) {
                    @Override
                    public void _onNext(ThreeDimensionalBean threeDimensionalBean) {
                        view.showDetail(threeDimensionalBean);
                    }

                    @Override
                    public void _onError(String message) {
                        view.showNetworkFail(message);
                    }
                });
    }

    /**
     * 截图 单张
     *
     * @param mWebView webView
     * @param s        可选择面的数据
     */
    @Override
    public void shotSingle(WebView mWebView, String s) {
        String js = "javascript:M.shotSingle(" + s + ")";
        mWebView.evaluateJavascript(js
                , new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                    }
                });
    }


    /**
     * 根据刚进入获得的设计数据来处理目前的设计数据
     *
     * @param metaData 当前设计的元数据
     * @return 过滤的设计数据。
     */
    @Override
    public ValidateProductBean getCheckedDate(List<DesignMetaBean> metaData) {
        ValidateProductBean validateProductBean = new ValidateProductBean();
        if (metaData == null || validateBean == null) {
            return validateProductBean;
        } else {
            //将此时还在使用的艺术字加入到 过滤结果列表中
            if (validateBean.wordart != null && validateBean.wordart.size() != 0) {
                for (DesignMetaBean designMetaBean : metaData) {
                    List<LayerMetaJsonBean.LayerMetaBean> layerMeta = designMetaBean.layerMeta;
                    if (layerMeta != null) {
                        for (LayerMetaJsonBean.LayerMetaBean bean : layerMeta) {
                            for (ValidateProductBean.Content wordContent : validateBean.wordart) {
                                if ("text".equals(bean.getType()) && bean.getFontId().equals(wordContent.id)) {
                                    validateProductBean.wordart.add(new ValidateProductBean.Content(wordContent.id, wordContent.name));
                                }
                            }

                        }
                    }
                }
            }
            //过滤下架素材
            if (validateBean.material.notAvailable != null && validateBean.material.notAvailable.size() != 0) {
                for (DesignMetaBean designMetaBean : metaData) {
                    List<LayerMetaJsonBean.LayerMetaBean> layerMeta = designMetaBean.layerMeta;
                    if (layerMeta != null) {
                        for (LayerMetaJsonBean.LayerMetaBean bean : layerMeta) {
                            for (ValidateProductBean.Content materialContent : validateBean.material.notAvailable) {
                                if ("bitmap".equals(bean.getType()) && bean.getMaterialId() != null && bean.getMaterialId().equals(materialContent.id)) {
                                    ValidateProductBean.Content content = new ValidateProductBean.Content();
                                    content.id = materialContent.id;
                                    content.sourceMaterialName = materialContent.sourceMaterialName;
                                    if ( !validateProductBean.material.notAvailable.contains(content)){
                                        validateProductBean.material.notAvailable.add(content);
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
        return validateProductBean;
    }

    //设计的数据
    private List<DesignMetaBean> metaDataBeans;
    //保存过滤数据
    private ValidateProductBean validateBean;

    /**
     * 获取设计数据
     *
     * @param designId 元数据Id
     */
    public void getMyDesign(final String designId) {

        Api.getDefault()
                .searchMyDesignSpu(designId)
                .compose(RxHandleResult.<ResultMetaDataBean>handleResultMap())
                .flatMap(new Function<ResultMetaDataBean, ObservableSource<BaseResult<ValidateProductBean>>>() {
                    @Override
                    public ObservableSource<BaseResult<ValidateProductBean>> apply(@NonNull ResultMetaDataBean resultMetaDataBean) throws Exception {

                        String designMeta = resultMetaDataBean.getDesignMeta();
                        List<DesignMetaBean> jsonBean = null;

                        if (!StringUtil.isEmpty(designMeta)) {
                            Type mType = new TypeToken<List<DesignMetaBean>>() {
                            }.getType();
                            try {
                                jsonBean = GsonUtil.getGson().fromJson(designMeta, mType);
                                metaDataBeans = jsonBean;
                            } catch (Exception e) {
                                Observable.error(e);
                            }
                        }
                        if (jsonBean == null) {
                            Observable.error(new Exception("数据错误"));
                        }


                        return Api.getDefault().validateDesign(designId);
                    }
                }).compose(RxHandleResult.<ValidateProductBean>handleResult())
                .map(new Function<ValidateProductBean, List<DesignMetaBean>>() {
                    @Override
                    public List<DesignMetaBean> apply(@NonNull ValidateProductBean validateProductBean) throws Exception {
                        validateBean = validateProductBean;
                        view.showOverdue(validateProductBean, true);
                        return metaDataBeans;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<List<DesignMetaBean>>(mContext) {
                    @Override
                    public void _onNext(List<DesignMetaBean> designMetaBeanList) {
                        System.out.println("getDesignMetaData 查询成功" + designMetaBeanList);
                        view.showMetaDetail(designMetaBeanList);
                    }

                    @Override
                    public void _onError(String message) {
                        if (metaDataBeans != null) {
                            view.showMetaDetail(metaDataBeans);
                            return;
                        }
                        System.out.println("getDesignMetaData 查询失败" + message);
                    }
                });
    }


    /**
     * 恢复原数据  根据设计贴素材 材质，颜色、图片
     *
     * @param webView  webView
     * @param metaData 设计id
     */
    public void recoverDesign(WebView webView, List<DesignMetaBean> metaData) {
        if (metaData == null) {
            return;
        }
        for (int i = 0; i < metaData.size(); i++) {
            DesignMetaBean designMetaBean = metaData.get(i);
            String objName = designMetaBean.aspectId;
            //贴材质
            if (!StringUtil.isEmpty(designMetaBean.material)) {
                String js = "javascript:M.changeMaterial('" + designMetaBean.material + "','" + objName + "')";
                webView.evaluateJavascript(js, new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                            }
                        }
                );
            }
            //贴颜色
            if (designMetaBean.layerMeta == null) {
                String js = "javascript:M.changeColor('" + designMetaBean.thumbnail + "','" + objName + "')";
                webView.evaluateJavascript(js, new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                            }
                        }
                );
            }
            //贴图片
            if (!StringUtil.isEmpty(designMetaBean.thumbnail)) {
                webView.evaluateJavascript("javascript:M.changeImage('" + designMetaBean.thumbnail + "','','" + objName + "')", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                            }
                        }
                );
            }
        }
    }

    /**
     * 上传base64图片
     */
    public void upload64Image(List<String> base64) {
        Api.getDefault().uploadBase64Image(base64)
                .compose(RxHandleResult.<List<UploadFileBean>>handleResult())
                .subscribe(new RxSubscriber<List<UploadFileBean>>(mContext) {
                    @Override
                    public void _onNext(List<UploadFileBean> beans) {
                        ArrayList<String> urls = new ArrayList<>();
                        for (int i = 0; i < beans.size(); i++) {
                            String fileId = beans.get(i).fileId;
                            urls.add(fileId);
                        }
                        view.successUploadImage(urls);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 上传base64图片
     *
     * @param base64 base64的刘图片
     */
    public void uploadSingle64Image(String base64) {
        ArrayList<String> base64s = new ArrayList<>();
        base64s.add(base64);
        //上传单张图片
        Api.getDefault().uploadBase64Image(base64s)
                .compose(RxHandleResult.<List<UploadFileBean>>handleResult())
                .subscribe(new RxSubscriber<List<UploadFileBean>>(mContext) {
                    @Override
                    public void _onNext(List<UploadFileBean> beans) {
                        if (beans.size() > 0) {
                            //设计将保存的单张图片
                            view.showDesignImage(beans.get(0).fileId);
                        } else {
                            view.showDesignImage(null);
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        view.showDesignImage(null);
                    }
                });
    }

    /**
     * 保存设计
     *
     * @param spuId     spuId
     * @param thumbnail 图片数据
     * @param meta      设计数据
     */
    public void saveMyDesign(String spuId, String thumbnail, List<DesignMetaBean> meta) {
        String metaJson = gson.toJson(meta);
        //请求数据的Bean
        RequestSaveMyDesignDataBean data = new RequestSaveMyDesignDataBean(metaJson, thumbnail, spuId);
        Api.getDefault().saveMyDesign(data)
                .compose(RxHandleResult.<ResultMetaDataBean>handleResult())
                .subscribe(new RxSubscriber<ResultMetaDataBean>(mContext, true) {
                    @Override
                    public void _onNext(ResultMetaDataBean resultMetaDataBean) {
                        designId = resultMetaDataBean.getSequenceNBR();
                        view.saveDesignSuccess(designId);
                    }

                    @Override
                    public void _onError(String message) {
                        view.saveDesignSuccess(null);
                        ToastUtil.showShort("保存作品失败");
                    }
                });
    }

    /**
     * @param nextStep  true 上架 false 购买
     * @param spuId     商品id
     * @param designId  设计id
     * @param meta      原数据
     * @param spuImages 效果图
     */
    public void putAwayOrBuy(final boolean nextStep, final String spuId, String designId, List<DesignMetaBean> meta, final List<String> spuImages) {
        if (nextStep) {
            TemplatePutawayActivity.launch3D(mContext, spuImages, designId, spuId, materialIds);
        } else {
            TemplatePurchaseActivity.launch(mContext, spuId, designId, spuImages, materialIds, materialData);
        }
    }

    /**
     * 修改设计
     */
    public void changeMyDesign(String thumbnail, List<DesignMetaBean> meta) {
        String metaJson = gson.toJson(meta);
        RequestChangeMetaDataBean data = new RequestChangeMetaDataBean(metaJson, thumbnail);
        Api.getDefault().changeMyDesignSpu(designId, data)
                .compose(RxHandleResult.<ResultMetaDataBean>handleResult())
                .subscribe(new RxSubscriber<ResultMetaDataBean>(mContext, true) {
                    @Override
                    public void _onNext(ResultMetaDataBean resultMetaDataBean) {
                        view.saveDesignSuccess(designId);
                    }

                    @Override
                    public void _onError(String message) {
                        view.saveDesignSuccess(null);
                        ToastUtil.showShort("保存作品失败");
                    }
                });
    }


    /**
     * 先截图
     */
    public void finishDesign(WebView webView) {
        if (webView != null) {
            webView.evaluateJavascript("javascript:M.shot()", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                        }
                    }
            );
        }
    }

    /**
     * 贴图到3d 模型上面
     *
     * @param areaName     区域面的名称
     * @param areaImageUrl 图片的地址
     */
    public void postImageTo3DModel(WebView webView, String areaName, String areaImageUrl) {
        //直接贴图
        if (webView != null) {
            webView.evaluateJavascript("javascript:M.changeImage('" + ApiConstants.UZAOCHINA_IMAGE_HOST + areaImageUrl + "','','" + areaName + "')", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                        }
                    }
            );
        }
    }


    /**
     * 更换材质
     */
    public void changeMaterial(WebView webView, String materialImagePath, String faceName, List<DesignMetaBean> metaData) {
        if (webView != null) {
            String js = "javascript:M.changeMaterial('" + ApiConstants.UZAOCHINA_IMAGE_HOST + materialImagePath + "','" + faceName + "')";
            webView.evaluateJavascript(js, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            System.out.println("上传结果" + value);
                        }
                    }
            );

            for (int i = 0; i < metaData.size(); i++) {
                if (metaData.get(i).aspectId.equals(faceName)) {
                    metaData.get(i).material = materialImagePath;
                }
            }
        }
    }

    /**
     * 更换颜色
     */
    public void changeColor(WebView webView, String colorImagePath, String faceName, List<DesignMetaBean> metaData) {
        if (webView != null) {
            String js = "javascript:M.changeColor('" + ApiConstants.UZAOCHINA_IMAGE_HOST + colorImagePath + "','" + faceName + "')";
            webView.evaluateJavascript(js, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            System.out.println("上传结果" + value);
                        }
                    }
            );
            for (int i = 0; i < metaData.size(); i++) {
                if (metaData.get(i).aspectId.equals(faceName)) {
                    metaData.get(i).thumbnail = colorImagePath;
                    metaData.get(i).layerMeta = null;
                }
            }
        }
    }

    public void setDesignId(String designId) {
        this.designId = designId;
    }

    /**
     * 去设计
     */
    public void gotoDesign(List<DesignMetaBean> metaData, ThreeDimensionalBean data, String areasName) {
        for (int i = 0; i < data.sample3dObjectModels.size(); i++) {
            if (data.sample3dObjectModels.get(i).name.equals(areasName)) {
                if (data.sample3dObjectModels.get(i).canCustomize.equals("Y")) {
                    LayerMetaJsonBean layer = new LayerMetaJsonBean();
                    for (int j = 0; j < metaData.size(); j++) {
                        if (areasName.equals(metaData.get(j).aspectId)) {
                            layer.setAspectId(metaData.get(j).aspectId);
                            layer.setLayerMeta(metaData.get(j).layerMeta);
                            layer.setCraftId(metaData.get(j).craftId);
                        }
                    }
                    String layerJson = gson.toJson(layer);
                    EditorActivity.launch3D(mContext, data.sample3dObjectModels.get(i), layerJson, validateBean);
                    return;
                }
            }
        }
        ToastUtil.showShort("此面不能自由设计");
    }

    /**
     * 获取是否有需要购买的素材
     */
    public void getUnBoughtMaterial(final WebView webView, List<DesignMetaBean> metaData, final boolean nextStep) {
        materialIds.clear();
        for (int i = 0; i < metaData.size(); i++) {
            List<LayerMetaJsonBean.LayerMetaBean> jsonBeans = metaData.get(i).layerMeta;
            if (metaData.get(i).layerMeta != null) {
                for (LayerMetaJsonBean.LayerMetaBean bean : jsonBeans) {
                    String materialId = bean.getMaterialId();
                    if (materialId != null) {
                        this.materialIds.add(materialId);
                    }
                }
            }
        }

        if (materialIds.size() > 0) {
            StringBuilder idBuilder = new StringBuilder(materialIds.get(0));
            for (int i = 1; i < materialIds.size(); i++) {
                idBuilder.append(",");
                idBuilder.append(materialIds.get(i));
            }

            Api.getDefault().getUnBoughtMaterialList(idBuilder.toString())
                    .compose(RxHandleResult.<UnPayMaterialBean>handleResult())
                    .subscribe(new RxSubscriber<UnPayMaterialBean>(mContext) {
                        @Override
                        public void _onNext(UnPayMaterialBean materialListBean) {
                            if (materialListBean.getSourceMaterialModels() != null && materialListBean.getSourceMaterialModels().size() > 0) {
                                materialData = materialListBean;
                                //有需要购买的素材
                                if (nextStep) {
                                    //弹出购买素材
                                    view.showBuyMaterialDialog(materialListBean);
                                } else {
                                    finishDesign(webView);
                                }
                            } else {
                                //没有未购买的素材
                                finishDesign(webView);
                            }
                        }

                        @Override
                        public void _onError(String message) {
                        }
                    });
        } else {
            //没有使用素材
            finishDesign(webView);
        }
    }

    /**
     * 从详情页 立即购买
     *
     * @param way  支付方式
     * @param data 素材信息
     */
    public void getMaterialPayInfo(final String way, List<MaterialDetailBean> data) {
        if (data == null) return;
        view.showProgress();
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
                        view.stopProgress();
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
                        view.stopProgress();
                    }
                });
    }

    /**
     * 重新支付
     */
    public void repayInfo() {
        view.showProgress();
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
                        view.stopProgress();
                    }
                });
    }

    /**
     * ping++ 发起支付
     *
     * @param data 支付信息
     */
    public void pay(String data) {
        view.stopProgress();
        //根据返回值 用ping++拉起支付页面
        Pingpp.createPayment((Activity) mContext, data);
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
}
