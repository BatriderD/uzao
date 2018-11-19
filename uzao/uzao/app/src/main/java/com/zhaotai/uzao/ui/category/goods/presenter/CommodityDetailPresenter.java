package com.zhaotai.uzao.ui.category.goods.presenter;

import android.content.Context;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.adapter.PropertyAdapter;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.app.AppConfig;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.ActivityModelBean;
import com.zhaotai.uzao.bean.CommentBean;
import com.zhaotai.uzao.bean.EventBean.RefreshCartInfo;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.GoodsDetailMallBean;
import com.zhaotai.uzao.bean.MyTrackRequestBean;
import com.zhaotai.uzao.bean.MyTrackResultBean;
import com.zhaotai.uzao.bean.PosterTemplateBean;
import com.zhaotai.uzao.bean.PropertyBean;
import com.zhaotai.uzao.bean.ShoppingPropertyBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.category.goods.contract.CommodityDetailContract;
import com.zhaotai.uzao.ui.design.activity.EditorActivity;
import com.zhaotai.uzao.utils.LogUtils;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.SPUtils;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Time: 2017/5/17
 * Created by LiYou
 * Description :
 */

public class CommodityDetailPresenter extends CommodityDetailContract.Presenter {

    private CommodityDetailContract.View mView;
    private List<String> skuKeys;
    private String isTemplate = "N";
    private String mkuId = "";

    public CommodityDetailPresenter(Context context, CommodityDetailContract.View view) {
        this.mContext = context;
        this.mView = view;
        skuKeys = new ArrayList<>();
    }

    /**
     * 获取商品详情
     *
     * @param id 商品id
     */
    @Override
    public void getDetail(final String id) {
        Api.getDefault().getGoodsDetail(id, "all")
                .compose(RxHandleResult.<GoodsDetailMallBean>handleResult())
                .subscribe(new RxSubscriber<GoodsDetailMallBean>(mContext, false) {
                    @Override
                    public void _onNext(GoodsDetailMallBean s) {
                        if (s.basicInfo.spuModel.status.equals("published")) {
                            mView.showContent();
                            mView.showDetail(s);
                            if ("mall".equals(s.basicInfo.spuModel.spuType)) {
                                //隐藏定制按钮
                                mView.setVisibilityCustomDesign(false);
                            } else {
                                //显示定制按钮
                                mView.setVisibilityCustomDesign(true);
                                //定制商品 获取 isTemplate mkuId
                                if ("design".equals(s.basicInfo.spuModel.spuType)) {
                                    isTemplate = s.basicInfo.isTemplate;
                                    if (!StringUtil.isEmpty(s.basicInfo.mkuId)) {
                                        mkuId = s.basicInfo.mkuId;
                                    }
                                }
                            }
                            //获取第一条评论
                            getFirstComment(id);
                        } else {
                            mView.showEmpty("很抱歉，您查看的商品已经下架");
                            mView.hasShowEmptyView();
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        if (message.equals(mContext.getString(R.string.no_net))) {
                            mView.showNetworkFail(message);
                        } else {
                            mView.showEmpty("很抱歉，您查看的商品已经下架");
                            mView.hasShowEmptyView();
                        }
                    }
                });
    }

    /**
     * 获取评论
     */
    private void getFirstComment(String spuId) {
        Api.getDefault().getFirstComment(spuId)
                .compose(RxHandleResult.<CommentBean>handleResult())
                .subscribe(new Observer<CommentBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        ((BaseActivity) mContext).add(d);
                    }

                    @Override
                    public void onNext(CommentBean commentBean) {
                        mView.showComment(commentBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showComment(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 收藏商品
     *
     * @param productId 商品id
     */
    @Override
    public void collectProduct(String productId) {
        if (isLogin()) return;
        Api.getDefault().collectProduct(productId)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String s) {
                        ToastUtil.showShort("收藏成功");
                        mView.hasCollect();
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 判断是否收藏
     *
     * @param productId 商品id
     */
    @Override
    public void isCollection(String productId) {
        if (SPUtils.getSharedBooleanData(AppConfig.IS_LOGIN)) {
            Api.getDefault().isMyFavorite(productId)
                    .compose(RxHandleResult.<Boolean>handleResult())
                    .subscribe(new RxSubscriber<Boolean>(mContext, false) {
                        @Override
                        public void _onNext(Boolean aBoolean) {
                            if (aBoolean) {
                                mView.unCollect();
                            } else {
                                mView.hasCollect();
                            }
                        }

                        @Override
                        public void _onError(String message) {

                        }
                    });
        }
    }

    /**
     * 取消收藏
     *
     * @param productId 商品id
     */
    @Override
    public void deleteCollect(String productId) {
        if (isLogin()) return;
        List<String> id = new ArrayList<>();
        id.add(productId);
        Api.getDefault().deleteCollectProduct(id)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String s) {
                        ToastUtil.showShort("取消收藏");
                        mView.unCollect();
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 获取skuId
     *
     * @param propertyAdapter 属性适配器
     * @param data            商品详情
     * @param count           购买数量
     * @return
     */
    @Override
    public GoodsDetailMallBean.Sku getSkuId(PropertyAdapter propertyAdapter, GoodsDetailMallBean data, String count) {
        if (StringUtil.isEmpty(count)) {
            ToastUtil.showShort("请填写商品数量");
            return null;
        }
        skuKeys.clear();
        String skuName = "";
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
                        skuName += ww.propertyValue + "  ";
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
                    sku.skuName = skuName;
                    if (Integer.valueOf(count) > sku.storeNum) {
                        ToastUtil.showShort("库存不足");
                        return null;
                    }
                    return sku;
                }
            }
            ToastUtil.showShort("此商品不能购买");
            return null;
        }
        return null;
    }

    /**
     * 获取sku
     *
     * @param propertyAdapter 规格适配器
     * @param data            详情
     * @return sku
     */
    public GoodsDetailMallBean.Sku getSku(PropertyAdapter propertyAdapter, GoodsDetailMallBean data) {
        skuKeys.clear();
        String skuName = "";
        if (propertyAdapter != null && data != null) {

            List<PropertyBean> list = propertyAdapter.getData();
            //#1 循环遍历一级规格
            for (int i = 0; i < list.size(); i++) {
                PropertyBean property = list.get(i);
                //#1.1如果未选中结束 此判断用来减少用来更换sku图片循环次数
                if (!property.isSelect) {
                    return null;
                }
                //#2 循环遍历二级规格
                for (PropertyBean spuProperty : property.spuProperties) {
                    if (spuProperty.isSelect) {
                        //#2.1 收集选中的规格id
                        skuKeys.add(spuProperty.propertyCode);
                        skuName += spuProperty.propertyValue + "  ";
                    }
                }
            }
            //#3 遍历skus skuKey是由规格id组成的
            for (GoodsDetailMallBean.Sku sku : data.skus) {
                boolean isHave = true;
                for (String skuKey : skuKeys) {
                    //#3.1 遍历规格id集合 如果skuKey 包含每个规格Id 证明此sku为选中的sku
                    if (!sku.skuKey.contains(skuKey)) {
                        isHave = false;
                    }
                }
                //#3.2 返回sku
                if (isHave) {
                    sku.skuName = skuName;
                    return sku;
                }
            }
            return null;
        }
        return null;
    }

    /**
     * 加入购物车
     *
     * @param data 商品数据
     */
    @Override
    public void addShoppingCart(ShoppingPropertyBean data) {
        Api.getDefault().addShoppingCart(data)
                .compose(RxHandleResult.<ShoppingPropertyBean>handleResult())
                .subscribe(new RxSubscriber<ShoppingPropertyBean>(mContext, true) {
                    @Override
                    public void _onNext(ShoppingPropertyBean s) {
                        ToastUtil.showShort("加入购物车成功");
                        //刷新购物车
                        EventBus.getDefault().post(new RefreshCartInfo());
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort(message);
                    }
                });
    }

    /**
     * 判断是否登录
     */
    @Override
    public boolean isLogin() {
        if (!SPUtils.getSharedBooleanData(AppConfig.IS_LOGIN)) {
            LoginHelper.goLogin(mContext);
            return true;
        }
        return false;
    }

    /**
     * 获取活动优惠金额
     *
     * @param activityModelBean 活动
     * @return 价格
     */
    @Override
    public int getCutPrice(ActivityModelBean activityModelBean, int price) {
        //根据满减排序
        Collections.sort(activityModelBean.fullcutConfigModels, new Comparator<ActivityModelBean.FullCutConfigModels>() {
            @Override
            public int compare(ActivityModelBean.FullCutConfigModels o1, ActivityModelBean.FullCutConfigModels o2) {
                return o1.full - o2.full;
            }
        });
        for (int i = 0; i < activityModelBean.fullcutConfigModels.size(); i++) {
            //没有满足活动条件
            if (price < activityModelBean.fullcutConfigModels.get(0).full) {
                return 0;
            }

            if (price < activityModelBean.fullcutConfigModels.get(i).full) {
                return activityModelBean.fullcutConfigModels.get(i - 1).cut;
            }

        }
        return activityModelBean.fullcutConfigModels.get(activityModelBean.fullcutConfigModels.size() - 1).cut;
    }

    /**
     * 获取推荐商品
     *
     * @param spuId 商品id
     */
    public void getRecommendSpu(String spuId) {
        Api.getDefault().getRecommendSpu(spuId, "category")
                .compose(RxHandleResult.<List<GoodsBean>>handleResult())
                .subscribe(new RxSubscriber<List<GoodsBean>>(mContext) {
                    @Override
                    public void _onNext(List<GoodsBean> goodsBeen) {
                        mView.showRecommendSpu(goodsBeen);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 延迟发送增加足迹
     *
     * @param bean 足迹请求数据
     */
    public void addMyTrack(final MyTrackRequestBean bean) {
        LogUtils.logd("素材详情", "addMyTrack: 准备");
        Observable.timer(3, TimeUnit.SECONDS)
                .subscribe(new RxSubscriber<Long>(mContext) {
                    @Override
                    public void _onNext(Long aLong) {
                        LogUtils.logd("素材详情", "addMyTrack: 正式发送");
                        Api.getDefault().addMyTrack(bean)
                                .compose(RxHandleResult.<MyTrackResultBean>handleResult())
                                .subscribe(new RxSubscriber<MyTrackResultBean>(mContext) {
                                    @Override
                                    public void _onNext(MyTrackResultBean s) {
                                        LogUtils.logd("素材详情", "addMyTrack: 发送成功" + s);
                                    }

                                    @Override
                                    public void _onError(String message) {
                                        LogUtils.logd("素材详情", "addMyTrack: 发送失败" + message);
                                    }
                                });
                    }

                    @Override
                    public void _onError(String message) {
                        LogUtils.logd("素材详情", "addMyTrack: 正式发送失败");
                    }

                });
    }

    private static final String PLATFORM_TYPE_PLATFORM = "platform";
    //使用点类型（spu、sourceMaterial、designer、theme）
    private static final String POINT_TYPE_SPU = "spu";

    @Override
    public void hasPoster() {

        String posterType = PLATFORM_TYPE_PLATFORM;
        String usePointType = POINT_TYPE_SPU;


        Api.getDefault()
                .getPosterTemplate(posterType, usePointType, "")
                .compose(RxHandleResult.<List<PosterTemplateBean>>handleResult())
                .subscribe(new RxSubscriber<List<PosterTemplateBean>>(mContext, true) {
                    @Override
                    public void _onNext(List<PosterTemplateBean> posterTemplateBeans) {
                        mView.openShareBoard(posterTemplateBeans != null && posterTemplateBeans.size() > 0);
                    }

                    @Override
                    public void _onError(String message) {
                        mView.openShareBoard(false);
                    }
                });
    }

    /**
     * 获取mku
     *
     * @param spuId 商品id
     * @param skuId 单品Id
     * @param data  详情
     */
    public void getMku(final String spuId, final String skuId, final GoodsDetailMallBean data) {
        //#1 判断是否有mkuId design商品有mkuId
        if (StringUtil.isEmpty(mkuId)) {
            mView.showProgressDialog();
            Api.getDefault().checkMkuCountIsSingle(spuId)
                    .compose(RxHandleResult.<Boolean>handleResult())
                    .subscribe(new RxSubscriber<Boolean>(mContext, false) {
                        @Override
                        public void _onNext(Boolean s) {
                            //#2 判断是否需要选择规格 单skus 直接获取mkuId
                            if (s && data.skus.size() > 0) {
                                getMkuId(data.skus.get(0).sequenceNBR, spuId);
                            } else {
                                //判断 是否选择了 规格
                                if (StringUtil.isEmpty(skuId)) {
                                    mView.stopProgressDialog();
                                    mView.showBottomSheetToCustomDesign();
                                } else {
                                    getMkuId(skuId, spuId);
                                }
                            }
                        }

                        @Override
                        public void _onError(String message) {
                            mView.stopProgressDialog();
                        }
                    });
        } else {
            //跳转编辑器
            EditorActivity.launch2D(mContext, mkuId, spuId, isTemplate);
        }

    }

    /**
     * 获取mkuId
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
                        mView.stopProgressDialog();
                        EditorActivity.launch2D(mContext, mkuId, spuId, "N");
                    }

                    @Override
                    public void _onError(String message) {
                        mView.stopProgressDialog();
                    }
                });
    }
}
