package com.zhaotai.uzao.ui.person.myproduct.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.EventBean.myProduct.ModifyMyProductEvent;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.MaterialDetailBean;
import com.zhaotai.uzao.bean.TemplateBean;
import com.zhaotai.uzao.bean.ValidateProductBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.order.activity.MaterialPayConfirmOrderActivity;
import com.zhaotai.uzao.ui.person.myproduct.contract.ModifyTemplateDetailContract;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Time: 2017/9/11
 * Created by LiYou
 * Description :
 */

public class ModifyTemplateDetailPresenter extends ModifyTemplateDetailContract.Presenter {

    private ModifyTemplateDetailContract.View view;

    public ModifyTemplateDetailPresenter(Context context, ModifyTemplateDetailContract.View view) {
        mContext = context;
        this.view = view;
    }

    /**
     * 商品详情
     */
    @Override
    public void getData(String spuId) {
        Api.getDefault().getMyProductDetail(spuId)
                .compose(RxHandleResult.<TemplateBean>handleResult())
                .subscribe(new RxSubscriber<TemplateBean>(mContext, true) {
                    @Override
                    public void _onNext(TemplateBean templateBean) {
                        view.bindData(templateBean);
                    }

                    @Override
                    public void _onError(String message) {
                        System.out.println(message);
                    }
                });
    }

    /**
     * 申请上架
     *
     * @param spuId    商品Id
     * @param data     数据
     * @param name     商品名
     * @param des      商品简介
     * @param idea     商品设计理念
     * @param tags     标签
     * @param position item位置
     */
    @Override
    public void putawayToStore(String spuId, TemplateBean data, String name, String des, String idea,
                               List<TemplateBean.TagsBean> tags, final int position) {


        data.spuModel = new GoodsBean();
        data.spuModel.spuName = name;
        data.spuModel.description = des;
        data.designIdea = idea;
        data.tags = tags;
        if (!verifyData(data)) return;
        //模板商品检测是否有过期下架素材  字体
        if ("Y".equals(data.basicInfo.isTemplate)) {
            validateTemplateSpu(spuId, data, position);
        } else {
            Api.getDefault().submitMyModifyProduct2Check(spuId, data)
                    .compose(RxHandleResult.<TemplateBean>handleResult())
                    .subscribe(new RxSubscriber<TemplateBean>(mContext, true) {
                        @Override
                        public void _onNext(TemplateBean templateBean) {
                            EventBus.getDefault().post(new ModifyMyProductEvent(position, "waitApprove"));
                            view.finishView();
                        }

                        @Override
                        public void _onError(String message) {
                            ToastUtil.showShort("申请上架失败");
                        }
                    });
        }
    }

    /**
     * 保存商品
     */
    @Override
    public void saveTemplate(String spuId, TemplateBean data, final int position) {
        if (!verifyData(data)) return;
        Api.getDefault().saveMyProductTemlate(spuId, data)
                .compose(RxHandleResult.<TemplateBean>handleResult())
                .subscribe(new RxSubscriber<TemplateBean>(mContext, true) {
                    @Override
                    public void _onNext(TemplateBean templateBean) {
                        EventBus.getDefault().post(new ModifyMyProductEvent(position, "unReviewed"));
                        view.finishView();
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("保存商品失败");
                    }
                });
    }


    /**
     * 检查数据
     */
    @Override
    public boolean verifyData(TemplateBean data) {
        for (int i = 0; i < data.skus.size(); i++) {
            TemplateBean.Sku sku = data.skus.get(i);
            float putAwayPrice = Float.valueOf(sku.priceY);
            float marketPrice = Float.valueOf(sku.marketPriceY);
            if (marketPrice > putAwayPrice) {
                ToastUtil.showShort("售卖价格不能低于成本价");
                return false;
            }
        }

        if (StringUtil.isEmpty(data.spuModel.spuName)) {
            ToastUtil.showShort("请输入作品名称");
            return false;
        }

        if (StringUtil.isEmpty(data.spuModel.description)) {
            ToastUtil.showShort("请输入作品简介");
            return false;
        }

        if (StringUtil.isEmpty(data.designIdea)) {
            ToastUtil.showShort("请输入设计理念");
            return false;
        }
        if (data.tags==null || data.tags.size()==0){
            ToastUtil.showShort("请选择标签");
            return false;
        }
        return true;
    }

    /**
     * 检验商品是否有过期续费 素材字体
     */
    private void validateTemplateSpu(final String spuId, final TemplateBean data, final int position) {
        Api.getDefault().validateTemplateSpu(spuId)
                .compose(RxHandleResult.<ValidateProductBean>handleResult())
                .subscribe(new RxSubscriber<ValidateProductBean>(mContext, true) {
                    @Override
                    public void _onNext(ValidateProductBean validateProductBean) {
                        //#1 检测 过期续费素材
                        if (validateProductBean.material.expiredPublished != null &&
                                validateProductBean.material.expiredPublished.size() > 0 &&
                                validateProductBean.wordart != null &&
                                validateProductBean.wordart.isEmpty() &&
                                validateProductBean.material.notAvailable != null &&
                                validateProductBean.material.notAvailable.isEmpty()) {
                            //续费提醒
                            if (view != null) {
                                view.showExpireTip(spuId, "您的商品中使用过期素材,续费后才可以上架到商城,请您前往续费");
                                return;
                            }
                        }

                        StringBuilder tip = new StringBuilder();
                        if (validateProductBean.material.expiredPublished != null && validateProductBean.material.expiredPublished.size() > 0) {
                            tip.append("您的模板商品中使用了过期素材，请您及时续费<br>");
                        }

                        if (validateProductBean.material.notAvailable != null && validateProductBean.material.notAvailable.size() > 0) {
                            tip.append("您的模板商品中使用了已下架的素材，平台已将您的模板商品下架，请知悉<br>");
                        }

                        if (validateProductBean.wordart != null && validateProductBean.wordart.size() > 0) {
                            tip.append("您的模板商品中使用了已下架的艺术字，平台已将您的模板商品下架，请知悉");
                        }

                        if (!StringUtil.isEmpty(tip.toString())) {
                            //过期提醒
                            if (view != null) {
                                view.showTip(tip.toString());
                                return;
                            }
                        }
                        //上架
                        Api.getDefault().submitMyModifyProduct2Check(spuId, data)
                                .compose(RxHandleResult.<TemplateBean>handleResult())
                                .subscribe(new RxSubscriber<TemplateBean>(mContext, true) {
                                    @Override
                                    public void _onNext(TemplateBean templateBean) {
                                        EventBus.getDefault().post(new ModifyMyProductEvent(position, "waitApprove"));
                                        view.finishView();
                                    }

                                    @Override
                                    public void _onError(String message) {
                                        ToastUtil.showShort("申请上架失败");
                                    }
                                });
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 获取素材列表
     *
     * @param spuId    商品Id
     * @param position item位置
     */
    public void getRenewMaterial(final String spuId, final int position) {
        Api.getDefault().getRenewMaterial(spuId)
                .compose(RxHandleResult.<MaterialDetailBean>handleResult())
                .subscribe(new RxSubscriber<MaterialDetailBean>(mContext, true) {
                    @Override
                    public void _onNext(MaterialDetailBean materialDetailBean) {
                        view.dismissTipDialog();
                        //去支付
                        if (materialDetailBean.sourceMaterialModels != null && materialDetailBean.sourceMaterialModels.size() > 0) {
                            MaterialPayConfirmOrderActivity.launch(mContext, materialDetailBean.sourceMaterialModels);
                        } else {
                            //免费素材 自动续费成功
                            view.renewSuccess();
                        }
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }
}
