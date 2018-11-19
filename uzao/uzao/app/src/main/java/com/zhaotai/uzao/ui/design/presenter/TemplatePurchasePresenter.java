package com.zhaotai.uzao.ui.design.presenter;

import android.content.Context;

import com.zhaotai.uzao.adapter.PropertyAdapter;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.EventBean.PutawayEvent;
import com.zhaotai.uzao.bean.GoodsDetailMallBean;
import com.zhaotai.uzao.bean.MaterialDetailBean;
import com.zhaotai.uzao.bean.PropertyBean;
import com.zhaotai.uzao.bean.ShoppingCartBean;
import com.zhaotai.uzao.bean.ShoppingGoodsBean;
import com.zhaotai.uzao.bean.TemplateBean;
import com.zhaotai.uzao.bean.TemplateInfoBean;
import com.zhaotai.uzao.bean.post.DesignInfo;
import com.zhaotai.uzao.bean.post.TemplateDesignInfo;
import com.zhaotai.uzao.bean.post.TemplateImageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.order.activity.ConfirmOrderActivity;
import com.zhaotai.uzao.ui.order.contract.TemplatePurchaseContract;
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Time: 2017/9/8
 * Created by LiYou
 * Description :设计商品购买
 */

public class TemplatePurchasePresenter extends TemplatePurchaseContract.Presenter {

    private TemplatePurchaseContract.View view;
    private List<String> skuKeys;
    private String spuName;

    public TemplatePurchasePresenter(Context context, TemplatePurchaseContract.View view) {
        mContext = context;
        this.view = view;
        skuKeys = new ArrayList<>();
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    public String getSpuName() {
        return this.spuName;
    }

    /**
     * 根据mkuId获取待发布的商品信息
     */
    @Override
    public void getTemplatePurchaseDetail(String mkuId) {
        Api.getDefault().getWaitPublishTemplateInfo(mkuId)
                .compose(RxHandleResult.<TemplateInfoBean>handleResult())
                .subscribe(new RxSubscriber<TemplateInfoBean>(mContext) {
                    @Override
                    public void _onNext(TemplateInfoBean templateBean) {
                        view.templateDetail(templateBean);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 根据spuId获取商品详情
     */
    public void getTemplateDesignProductDetail(String spuId) {
        Api.getDefault().getGoodsDetail(spuId, "all")
                .compose(RxHandleResult.<GoodsDetailMallBean>handleResult())
                .subscribe(new RxSubscriber<GoodsDetailMallBean>(mContext) {
                    @Override
                    public void _onNext(GoodsDetailMallBean goodsDetailMallBean) {
                        view.templateDesignProductDetail(goodsDetailMallBean);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 获取3d 待发布商品信息
     *
     * @param templateId 商品id
     */
    @Override
    public void getTemplatePurchase3DDetail(String templateId) {
        Api.getDefault().getWaitPublish3DTemplateInfo(templateId)
                .compose(RxHandleResult.<TemplateInfoBean>handleResult())
                .subscribe(new RxSubscriber<TemplateInfoBean>(mContext) {
                    @Override
                    public void _onNext(TemplateInfoBean templateBean) {
                        view.template3DDetail(templateBean);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 检查库存
     */
    public boolean verifyStoreNum(TemplateBean data, String skuId, int buyNum) {
        if (data.skus != null && data.skus.size() > 0) {
            for (TemplateBean.Sku sku : data.skus) {
                if (sku.sequenceNBR.equals(skuId)) {
                    if (buyNum > sku.storeNum) {
                        ToastUtil.showShort("库存不足");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 检查模板商品库存
     */
    public boolean verifySampleStoreNum(GoodsDetailMallBean data, String skuId, int buyNum) {
        if (data.skus != null && data.skus.size() > 0) {
            for (GoodsDetailMallBean.Sku sku : data.skus) {
                if (sku.sequenceNBR.equals(skuId)) {
                    if (buyNum > sku.storeNum) {
                        ToastUtil.showShort("库存不足");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取skuId
     *
     * @param propertyAdapter 规格属性适配器
     * @param data            详情
     * @return sku
     */
    public TemplateBean.Sku getSkuId(PropertyAdapter propertyAdapter, TemplateBean data) {
        skuKeys.clear();
        if (propertyAdapter != null && data != null) {

            List<PropertyBean> list = propertyAdapter.getData();
            for (int i = 0; i < list.size(); i++) {
                PropertyBean property = list.get(i);
                //#1 判断是否选择了规格
                if (!property.isSelect) {
                    ToastUtil.showShort("请选择" + property.propertyTypeName);
                    return null;
                }
                //#
                for (PropertyBean ww : property.spuProperties) {
                    if (ww.isSelect) {
                        skuKeys.add(ww.propertyCode);
                    }
                }
            }

            for (TemplateBean.Sku sku : data.skus) {
                boolean isHave = true;
                for (String skuKey : skuKeys) {
                    if (!sku.skuKey.contains(skuKey)) {
                        isHave = false;
                    }
                }
                if (isHave) {
                    return sku;
                }
            }
            ToastUtil.showShort("此商品不能购买");
            return null;
        }
        return null;
    }

    public GoodsDetailMallBean.Sku getSkuId(PropertyAdapter propertyAdapter, GoodsDetailMallBean data) {
        skuKeys.clear();
        if (propertyAdapter != null && data != null) {

            List<PropertyBean> list = propertyAdapter.getData();
            for (int i = 0; i < list.size(); i++) {
                PropertyBean property = list.get(i);
                if (!property.isSelect) {
                    ToastUtil.showShort("请选择" + property.propertyTypeName);
                    return null;
                }

                for (PropertyBean ww : property.spuProperties) {
                    if (ww.isSelect) {
                        skuKeys.add(ww.propertyCode);
                    }
                }
            }

            for (GoodsDetailMallBean.Sku sku : data.skus) {
                boolean isHave = true;
                for (String skuKey : skuKeys) {
                    if (!sku.skuKey.contains(skuKey)) {
                        isHave = false;
                    }
                }
                if (isHave) {
                    return sku;
                }
            }
            ToastUtil.showShort("此商品不能购买");
            return null;
        }
        return null;
    }

    /**
     * 购买载体商品
     */
    public void buyTemplate(final TemplateBean data, final String skuId, final int buyNum, final String skuValues, final int marketPrice,
                            final List<String> spuImages, final List<TemplateImageInfo> skuImages, final String designId, final String mkuId, final String isTemplate,
                            List<String> materialIds, final List<MaterialDetailBean> materialDetailList) {

        DesignInfo info = new DesignInfo();
        info.sampleId = data.spuId;
        info.skuId = skuId;
        //效果图
        info.spuImages = spuImages;
        //sku图片
        info.skuImages = skuImages;
        //商品名字
        info.spuName = spuName;
        //设计Id
        info.designId = designId;
        //素材id
        String sourceMaterialIds = "";
        if (materialIds != null && materialIds.size() > 0) {
            for (int i = 0; i < materialIds.size(); i++) {
                sourceMaterialIds += materialIds.get(i) + ",";
            }
            info.sourceMaterialIds = sourceMaterialIds;
        }
        Api.getDefault().buyTemplate(info)
                .compose(RxHandleResult.<TemplateBean>handleResult())
                .subscribe(new RxSubscriber<TemplateBean>(mContext, true) {
                    @Override
                    public void _onNext(TemplateBean templateBean) {
                        //发送粘性事件\
                        EventBus.getDefault().postSticky(new PutawayEvent(mkuId, designId, isTemplate, "2d", spuImages, skuImages));

                        ShoppingCartBean shoppingCartBean = new ShoppingCartBean();
                        ShoppingCartBean.Cart cart = new ShoppingCartBean.Cart();
                        List<ShoppingCartBean.Cart> cartList = new ArrayList<>();
                        List<ShoppingGoodsBean> list = new ArrayList<>();
                        ShoppingGoodsBean shoppingGoodsBean = new ShoppingGoodsBean();

                        shoppingGoodsBean.spuName = templateBean.spuModel.spuName;
                        shoppingGoodsBean.count = String.valueOf(buyNum);
                        shoppingGoodsBean.spuPic = templateBean.spuModel.thumbnail;
                        shoppingGoodsBean.spuId = templateBean.sequenceNBR;
                        shoppingGoodsBean.skuId = templateBean.skuId;
                        shoppingGoodsBean.properties = skuValues;
                        shoppingGoodsBean.spuType = "sample";
                        shoppingGoodsBean.unitPrice = String.valueOf(marketPrice);
                        list.add(shoppingGoodsBean);

                        int materialPrice = 0;
                        if (materialDetailList != null && materialDetailList.size() > 0) {
                            for (int i = 0; i < materialDetailList.size(); i++) {
                                ShoppingGoodsBean material = new ShoppingGoodsBean();

                                material.spuName = materialDetailList.get(i).sourceMaterialName;
                                material.count = "1";
                                material.spuPic = materialDetailList.get(i).thumbnail;
                                material.spuId = materialDetailList.get(i).sequenceNBR;
                                if (materialDetailList.get(i).designer != null) {
                                    material.properties = materialDetailList.get(i).designer.nickName;
                                }
                                material.unitPrice = materialDetailList.get(i).price;
                                material.spuType = "material";
                                materialPrice += Integer.valueOf(materialDetailList.get(i).price);
                                list.add(material);
                            }
                        }
                        cart.cartModels = list;
                        cartList.add(cart);
                        shoppingCartBean.carts = cartList;
                        shoppingCartBean.finalPrice = String.valueOf(marketPrice * buyNum + materialPrice);
                        shoppingCartBean.preferentiaPrice = "0";

                        ConfirmOrderActivity.launch(mContext, shoppingCartBean, true);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 购买模板商品
     *
     * @param data               商品详情
     * @param skuId              skuId
     * @param buyNum             购买数量
     * @param skuValues          规格
     * @param marketPrice        单价
     * @param spuImages          效果图
     * @param skuImages          sku图片
     * @param materialDetailList 素材详情集合
     * @param templateDesignInfo 载体详情
     * @param spuId              商品id
     */
    public void buyTemplateSpu(GoodsDetailMallBean data, String skuId, int buyNum, String skuValues, int marketPrice, List<String> spuImages,
                               List<TemplateImageInfo> skuImages, List<MaterialDetailBean> materialDetailList,
                               TemplateDesignInfo templateDesignInfo, String spuId) {

        //发送粘性事件\
        //EventBus.getDefault().postSticky(new PutawayEvent(mkuId, designId, isTemplate, "2d", spuImages, skuImages));

        ShoppingCartBean shoppingCartBean = new ShoppingCartBean();
        ShoppingCartBean.Cart cart = new ShoppingCartBean.Cart();
        List<ShoppingCartBean.Cart> cartList = new ArrayList<>();
        List<ShoppingGoodsBean> list = new ArrayList<>();
        ShoppingGoodsBean shoppingGoodsBean = new ShoppingGoodsBean();

        shoppingGoodsBean.spuName = data.basicInfo.spuModel.spuName;
        shoppingGoodsBean.count = String.valueOf(buyNum);
        if (spuImages != null && spuImages.size() > 0) {
            shoppingGoodsBean.spuPic = spuImages.get(0);
        }
        shoppingGoodsBean.spuId = spuId;
        shoppingGoodsBean.sampleSpuId = data.basicInfo.sampleId;
        shoppingGoodsBean.skuId = skuId;
        shoppingGoodsBean.properties = skuValues;
        shoppingGoodsBean.spuType = "sample";
        shoppingGoodsBean.unitPrice = String.valueOf(marketPrice);
        shoppingGoodsBean.graphicDesignModel = templateDesignInfo;
        DesignInfo info = new DesignInfo();
        //效果图
        info.spuImages = spuImages;
        //sku图片
        info.skuImages = skuImages;
        shoppingGoodsBean.designSpuModel = info;
        shoppingGoodsBean.isUseTemplateSpu = "Y";
        list.add(shoppingGoodsBean);

        int materialPrice = 0;
        if (materialDetailList != null && materialDetailList.size() > 0) {
            for (int i = 0; i < materialDetailList.size(); i++) {
                ShoppingGoodsBean material = new ShoppingGoodsBean();

                material.spuName = materialDetailList.get(i).sourceMaterialName;
                material.count = "1";
                material.spuPic = materialDetailList.get(i).thumbnail;
                material.spuId = materialDetailList.get(i).sequenceNBR;
                if (materialDetailList.get(i).designer != null) {
                    material.properties = materialDetailList.get(i).designer.nickName;
                }
                material.unitPrice = materialDetailList.get(i).price;
                material.spuType = "material";
                materialPrice += Integer.valueOf(materialDetailList.get(i).price);
                list.add(material);
            }
        }
        cart.cartModels = list;
        cartList.add(cart);
        shoppingCartBean.carts = cartList;
        shoppingCartBean.finalPrice = String.valueOf(marketPrice * buyNum + materialPrice);
        shoppingCartBean.preferentiaPrice = "0";

        ConfirmOrderActivity.launch(mContext, shoppingCartBean, true);

    }
}
