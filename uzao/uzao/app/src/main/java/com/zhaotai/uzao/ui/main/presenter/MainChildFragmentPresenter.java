package com.zhaotai.uzao.ui.main.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.DynamicFormBean;
import com.zhaotai.uzao.bean.DynamicValuesBean;
import com.zhaotai.uzao.bean.GoodsDetailMallBean;
import com.zhaotai.uzao.bean.MaterialDetailBean;
import com.zhaotai.uzao.bean.MultiMainBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.constants.DynamicType;
import com.zhaotai.uzao.ui.category.goods.activity.DesignProductListActivity;
import com.zhaotai.uzao.ui.design.activity.EditorActivity;
import com.zhaotai.uzao.ui.main.contract.MainChildFragmentContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Time: 2017/5/8
 * Created by LiYou
 * Description : 请求分类页面
 */

public class MainChildFragmentPresenter extends MainChildFragmentContract.Presenter {

    private MainChildFragmentContract.View mView;
    private List<MultiMainBean> multiList = new ArrayList<>();

    public MainChildFragmentPresenter(Context context, MainChildFragmentContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getData() {
        Api.getDefault().getHomeData("appHome")
                .compose(RxHandleResult.<DynamicFormBean>handleResult())
                .subscribe(new RxSubscriber<DynamicFormBean>(mContext) {
                    @Override
                    public void _onNext(DynamicFormBean bean) {
                        getMultiTypeData(bean);
                        if (mView != null) {
                            mView.stopRefresh();
                            mView.showContent();
                            mView.bindData(multiList);
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        if (mView != null) {
                            mView.stopRefresh();
                            mView.showNetworkFail(message);
                        }
                    }
                });
    }

    /**
     * 构造多布局数据
     */
    @Override
    public void getMultiTypeData(DynamicFormBean data) {
        multiList.clear();
        if (data != null && data.children != null) {
            for (int i = 0; i < data.children.size(); i++) {
                DynamicFormBean groupItem = data.children.get(i);
                switch (groupItem.groupCode) {
                    case "appHomeBanner"://首页轮播
                        MultiMainBean multiItem = new MultiMainBean(MultiMainBean.TYPE_BANNER);
                        multiItem.values = groupItem.values;
                        multiItem.groupType = groupItem.groupType;
                        multiList.add(multiItem);
                        break;
                    case "appHomeGallery"://灵感画廊
                        multiList.add(new MultiMainBean(MultiMainBean.TYPE_INSPIRATION_GALLERY_TITLE));
                        MultiMainBean galleryItem = new MultiMainBean(MultiMainBean.TYPE_INSPIRATION_GALLERY);
                        galleryItem.values = groupItem.values;
                        multiList.add(galleryItem);
                        break;
                    case "appHomeDesign"://人气设计
                        multiList.add(new MultiMainBean(MultiMainBean.TYPE_POPULAR_DESIGN_TITLE));
                        MultiMainBean popularItem = new MultiMainBean(MultiMainBean.TYPE_POPULAR_DESIGN);
                        popularItem.values = groupItem.values;
                        multiList.add(popularItem);
                        break;
                    case "appHomeProduct"://推荐商品
                        multiList.add(new MultiMainBean(MultiMainBean.TYPE_RECOMMEND_SPU_TITLE));
                        if (groupItem.values != null) {
                            for (int j = 0; j < groupItem.values.size(); j++) {
                                DynamicValuesBean item = groupItem.values.get(j);
                                MultiMainBean recommendSpuItem = new MultiMainBean(MultiMainBean.TYPE_RECOMMEND_SPU);
                                recommendSpuItem.value = item;
                                recommendSpuItem.inSidePos = j;
                                multiList.add(recommendSpuItem);
                            }
                            MultiMainBean more = new MultiMainBean(MultiMainBean.TYPE_MORE);
                            more.referType = DynamicType.CARRIER_DETAIL;
                            multiList.add(more);
                        }
                        break;
                    case "appHomeMaterial"://推荐素材
                        multiList.add(new MultiMainBean(MultiMainBean.TYPE_RECOMMEND_MATERIAL_TITLE));
                        if (groupItem.values != null) {
                            for (int j = 0; j < groupItem.values.size(); j++) {
                                DynamicValuesBean item = groupItem.values.get(j);
                                MultiMainBean recommendMaterialItem = new MultiMainBean(MultiMainBean.TYPE_RECOMMEND_MATERIAL);
                                recommendMaterialItem.value = item;
                                recommendMaterialItem.inSidePos = j;
                                multiList.add(recommendMaterialItem);
                            }
                            MultiMainBean more = new MultiMainBean(MultiMainBean.TYPE_MORE);
                            more.referType = DynamicType.MATERIAL_DETAIL;
                            multiList.add(more);
                        }
                        break;
                    case "appHomeBrand"://合作品牌
                        multiList.add(new MultiMainBean(MultiMainBean.TYPE_BRAND_TITLE));
                        if (groupItem.children != null && groupItem.children.size() == 2) {
                            MultiMainBean brandThemeItem = new MultiMainBean(MultiMainBean.TYPE_BRAND_THEME);
                            brandThemeItem.values = groupItem.children.get(0).values;
                            multiList.add(brandThemeItem);

                            MultiMainBean brandItem = new MultiMainBean(MultiMainBean.TYPE_BRAND);
                            brandItem.values = groupItem.children.get(1).values;
                            multiList.add(brandItem);
                        }
                        break;
                }
            }
            //了解我们
            multiList.add(new MultiMainBean(MultiMainBean.TYPE_ABOUT_US_TITLE));
            multiList.add(new MultiMainBean(MultiMainBean.TYPE_ABOUT_US));

            for (int i = 0; i < multiList.size(); i++) {
                if (multiList.get(i).getItemType() == MultiMainBean.TYPE_RECOMMEND_MATERIAL) {
                } else if (multiList.get(i).getItemType() == MultiMainBean.TYPE_RECOMMEND_SPU) {
                } else {
                    multiList.get(i).inSidePos = -1;
                }
            }
        }
    }

    /**
     * 带着素材去可定制商品列表
     *
     * @param materialId 素材Id
     */
    public void toDesignProductListWithMaterial(String materialId) {
        Api.getDefault().getMaterialDetail(materialId)
                .compose(RxHandleResult.<MaterialDetailBean>handleResult())
                .subscribe(new RxSubscriber<MaterialDetailBean>(mContext, true) {
                    @Override
                    public void _onNext(MaterialDetailBean materialDetailBean) {
                        DesignProductListActivity.launch(mContext, materialDetailBean);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 不带素材 去编辑器
     *
     * @param spuId 商品id
     */
    public void toEdit(final String spuId) {
        mView.showDLoading();
        //获取商品详情
        Api.getDefault().getGoodsDetail(spuId, "all")
                .compose(RxHandleResult.<GoodsDetailMallBean>handleResult())
                .subscribe(new RxSubscriber<GoodsDetailMallBean>(mContext, false) {
                    @Override
                    public void _onNext(GoodsDetailMallBean goodsDetail) {
                        //design类型商品 详情里面直接有mkuId
                        if ("design".equals(goodsDetail.basicInfo.spuModel.spuType)) {
                            mView.dismissDLoading();
                            EditorActivity.launch2D(mContext, goodsDetail.basicInfo.mkuId, spuId, goodsDetail.basicInfo.isTemplate);
                        } else {
                            //不是design类型商品 需要获取mkuId
                            checkIsNeedSku(spuId, goodsDetail);
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        mView.dismissDLoading();
                    }
                });
    }


    /**
     * 获取sku个数
     *
     * @param spuId       商品id
     * @param goodsDetail 商品详情
     */
    private void checkIsNeedSku(final String spuId, final GoodsDetailMallBean goodsDetail) {
        Api.getDefault().checkMkuCountIsSingle(spuId)
                .compose(RxHandleResult.<Boolean>handleResult())
                .subscribe(new RxSubscriber<Boolean>(mContext, false) {
                    @Override
                    public void _onNext(final Boolean s) {
                        if (s) {
                            //单个sku  根据sku获取mkuId
                            getMkuId(goodsDetail.skus.get(0).sequenceNBR, spuId);
                        } else {
                            //多个sku 把商品详情带过去在编辑器里面选择sku
                            mView.dismissDLoading();
                            EditorActivity.launch2D(mContext, spuId, "N", goodsDetail);
                        }

                    }

                    @Override
                    public void _onError(String message) {
                        mView.dismissDLoading();
                    }
                });
    }

    /**
     * 根据skuId 获取mkuId
     *
     * @param skuId skuId
     * @param spuId 商品Id
     */
    public void getMkuId(String skuId, final String spuId) {
        Api.getDefault().getMkuId(skuId)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, false) {
                    @Override
                    public void _onNext(String mkuId) {
                        mView.dismissDLoading();
                        EditorActivity.launch2D(mContext, mkuId, spuId, "N");
                    }

                    @Override
                    public void _onError(String message) {
                        mView.dismissDLoading();
                    }
                });
    }
}
