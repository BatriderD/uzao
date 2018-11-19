package com.zhaotai.uzao.ui.design.presenter;

import android.content.Context;

import com.zhaotai.uzao.HomeActivity;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.TemplateBean;
import com.zhaotai.uzao.bean.TemplateInfoBean;
import com.zhaotai.uzao.bean.post.DesignInfo;
import com.zhaotai.uzao.bean.post.DesignSkuInfo;
import com.zhaotai.uzao.bean.post.TemplateImageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.order.contract.TemplatePutawayContract;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Time: 2017/9/9
 * Created by LiYou
 * Description : 上架
 */

public class TemplatePutawayPresenter extends TemplatePutawayContract.Presenter {

    private TemplatePutawayContract.View view;

    public TemplatePutawayPresenter(Context context, TemplatePutawayContract.View view) {
        mContext = context;
        this.view = view;
    }

    /**
     * 获取2d 待发布商品详情
     *
     * @param mkuId 蒙版组Id
     */
    @Override
    public void getTemplateDetail(String mkuId) {
        Api.getDefault().getWaitPublishTemplateInfo(mkuId)
                .compose(RxHandleResult.<TemplateInfoBean>handleResult())
                .subscribe(new RxSubscriber<TemplateInfoBean>(mContext, true) {
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
     * 获取3d 待发布商品信息
     *
     * @param spuId 商品id
     */
    @Override
    public void getTemplatePutaway3DDetail(String spuId) {
        Api.getDefault().getWaitPublish3DTemplateInfo(spuId)
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
     * 上架商城 2d
     *
     * @param des        简介
     * @param desIdea    设计理念
     * @param datas      数据
     * @param designId   设计id
     * @param isTemplate 是否模板
     * @param tags       标签
     */
    @Override
    public void putawayToStore2D(String spuName, String des, String desIdea, TemplateBean datas, String designId, String isTemplate,
                                 List<String> spuImages, List<TemplateImageInfo> skuImages, List<String> materialIds, List<TemplateBean.TagsBean> tags) {
        //判断是否选择要上架的规格
        boolean hasSku = false;
        List<TemplateBean.Sku> msku = datas.productSkus;
        for (int i = 0; i < msku.size(); i++) {
            TemplateBean.Sku sku = msku.get(i);
            if (sku.isSelected) {
                hasSku = true;
                try {
                    float marketPrice = Float.valueOf(sku.priceY);
                    float putAwayPrice = Float.valueOf(sku.salePrice);
                    if (marketPrice > putAwayPrice) {
                        ToastUtil.showShort("售卖价格不能低于成本价");
                        return;
                    }
                } catch (Exception e) {
                    ToastUtil.showShort("价格数据错误");
                    return;
                }
            }
        }
        if (!hasSku) {
            ToastUtil.showShort("请选择要上架的规格");
            return;
        }
        if (StringUtil.isTrimEmpty(spuName)) {
            ToastUtil.showShort("请填写商品名字");
            return;
        }
        if (StringUtil.isTrimEmpty(des)) {
            ToastUtil.showShort("请填写商品简介");
            return;
        }
        if (StringUtil.isTrimEmpty(desIdea)) {
            ToastUtil.showShort("请填写商品设计理念");
            return;
        }
        if (tags.size() == 0) {
            ToastUtil.showShort("请选择标签");
            return;
        }
        DesignInfo designInfo = new DesignInfo();
        designInfo.spuName = spuName;
        designInfo.description = des;
        designInfo.designIdea = desIdea;
        designInfo.designId = designId;
        designInfo.spuImages = spuImages;
//        designInfo.salesPriceY = price;
        designInfo.isTemplate = isTemplate;
        designInfo.sampleId = datas.spuId;
        designInfo.tags = tags;
        designInfo.skus = new ArrayList<>();
        for (int i = 0; i < msku.size(); i++) {
            TemplateBean.Sku sku = msku.get(i);
            if (sku.isSelected) {
                DesignSkuInfo skuInfo = new DesignSkuInfo();
                skuInfo.sequenceNBR = sku.sequenceNBR;
                skuInfo.thumbnail = skuImages;
                skuInfo.priceY = sku.salePrice;
                designInfo.skus.add(skuInfo);
            }
        }

        //素材id
        String sourceMaterialIds = "";
        for (int i = 0; i < materialIds.size(); i++) {
            sourceMaterialIds += materialIds.get(i) + ",";
        }
        designInfo.sourceMaterialIds = sourceMaterialIds;

        Api.getDefault().putawayTemplateToStore(designInfo)
                .compose(RxHandleResult.<Object>handleResult())
                .subscribe(new RxSubscriber<Object>(mContext, true) {
                    @Override
                    public void _onNext(Object templateBean) {
                        ToastUtil.showShort("操作成功,请去个人中心查看");
                        //跳转个人中心
                        HomeActivity.launch(mContext, 4);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("上架失败");
                    }
                });
    }

    /**
     * 上架商城 3d
     *
     * @param des        简介
     * @param desIdea    设计理念
     * @param datas      数据
     * @param designId   设计id
     * @param isTemplate 是否模板
     * @param tags       标签
     */
    @Override
    public void putawayToStore3D(String spuName, String des, String desIdea, TemplateBean datas, String designId, String isTemplate,
                                 List<String> spuImages, List<String> materialIds, List<TemplateBean.TagsBean> tags) {
        //判断是否选择要上架的规格
        boolean hasSku = false;
        List<TemplateBean.Sku> msku = datas.skus;
        for (int i = 0; i < msku.size(); i++) {
            TemplateBean.Sku sku = msku.get(i);
            if (sku.isSelected) {
                hasSku = true;
                if (StringUtil.isEmpty(sku.priceY)) {
                    ToastUtil.showShort("价格不能为空");
                    return;
                }
                float marketPrice = Float.valueOf(sku.priceY);
                float putAwayPrice = Float.valueOf(sku.marketPriceY);
                if (marketPrice > putAwayPrice) {
                    ToastUtil.showShort("售卖价格不能低于成本价");
                    return;
                }
            }
        }
        if (!hasSku) {
            ToastUtil.showShort("请选择要上架的规格");
            return;
        }

        if (StringUtil.isTrimEmpty(spuName)) {
            ToastUtil.showShort("请填写商品名字");
            return;
        }
        if (StringUtil.isTrimEmpty(des)) {
            ToastUtil.showShort("请填写商品简介");
            return;
        }
        if (StringUtil.isTrimEmpty(desIdea)) {
            ToastUtil.showShort("请填写商品设计理念");
            return;
        }
        DesignInfo designInfo = new DesignInfo();
        designInfo.spuName = spuName;
        designInfo.description = des;
        designInfo.designIdea = desIdea;
        designInfo.designId = designId;
        designInfo.spuImages = spuImages;
        designInfo.isTemplate = isTemplate;
        designInfo.sampleId = datas.sequenceNBR;
        designInfo.skus = new ArrayList<>();
        for (int i = 0; i < datas.skus.size(); i++) {
            if (datas.skus.get(i).isSelected) {
                DesignSkuInfo sku = new DesignSkuInfo();
                sku.sequenceNBR = datas.skus.get(i).sequenceNBR;
                sku.priceY = datas.skus.get(i).priceY;
                designInfo.skus.add(sku);
            }
        }

        //素材id
        String sourceMaterialIds = "";
        for (int i = 0; i < materialIds.size(); i++) {
            sourceMaterialIds += materialIds.get(i) + ",";
        }
        designInfo.sourceMaterialIds = sourceMaterialIds;

        Api.getDefault().putawayTemplateToStore(designInfo)
                .compose(RxHandleResult.<Object>handleResult())
                .subscribe(new RxSubscriber<Object>(mContext, true) {
                    @Override
                    public void _onNext(Object templateBean) {
                        ToastUtil.showShort("操作成功,请去个人中心查看");
                        //跳转个人中心
                        HomeActivity.launch(mContext, 4);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

}
