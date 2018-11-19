package com.zhaotai.uzao.ui.person.myproduct.presenter;

import android.content.Context;

import com.zhaotai.uzao.adapter.MyProductListAdapter;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.MaterialDetailBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.TemplateBean;
import com.zhaotai.uzao.bean.ValidateProductBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.order.activity.MaterialPayConfirmOrderActivity;
import com.zhaotai.uzao.ui.person.myproduct.contract.MyProductFragmentContract;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Time: 2017/5/22
 * Created by LiYou
 * Description : 我的商品操作类
 */

public class MyProductPresenter extends MyProductFragmentContract.Presenter {

    private MyProductFragmentContract.View view;
    private List<String> spuIds;

    /**
     * 上架
     */
    private static final int VALIDATE_TYPE_PUBLISH_IMMEDIATELY = 0x01;
    /**
     * 提交审核
     */
    private static final int VALIDATE_TYPE_CHECK = 0x02;

    private int validateType;

    public MyProductPresenter(MyProductFragmentContract.View view, Context context) {
        this.view = view;
        mContext = context;
        spuIds = new ArrayList<>();
    }

    /**
     * 获取我的商品列表
     *
     * @param start 开始位置
     */
    @Override
    public void getMyProductList(final int start, final boolean inLoginStatus, String status) {
        Api.getDefault().getMyProductList(start, status)
                .compose(RxHandleResult.<PageInfo<GoodsBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<GoodsBean>>(mContext, false) {
                    @Override
                    public void _onNext(PageInfo<GoodsBean> goodsBeanPageInfo) {
                        if (inLoginStatus && start == Constant.PAGING_HOME) {
                            view.showContent();
                            view.stopLoadingMore();
                        } else if (start == 0) {
                            view.stopRefresh();
                        } else {
                            view.stopLoadingMore();
                        }
                        view.showMyProductList(goodsBeanPageInfo);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            view.stopRefresh();
                            view.showNetworkFail(message);
                        } else {
                            view.loadingFail();
                        }
                    }
                });
    }


    /**
     * 提交审核
     *
     * @param spuId      商品id
     * @param isTemplate 是否模板商品
     * @param position   item位置
     */
    @Override
    public void submitMyProductToCheck(String spuId, String isTemplate, final int position) {
        if ("Y".equals(isTemplate)) {
            validateType = VALIDATE_TYPE_CHECK;
            //模板商品 需要验证数据
            validateTemplateSpu(spuId, position);
        } else {
            Api.getDefault().submitMyProductToCheck(spuId)
                    .compose(RxHandleResult.<TemplateBean>handleResult())
                    .subscribe(new RxSubscriber<TemplateBean>(mContext, false) {
                        @Override
                        public void _onNext(TemplateBean s) {
                            //改变
                            view.dismissProgressDialog();
                            view.changeStatus("waitApprove", position);
                        }

                        @Override
                        public void _onError(String message) {
                            view.dismissProgressDialog();
                            ToastUtil.showShort(message);
                        }
                    });
        }
    }

    /**
     * 下架商品
     */
    public void soldOutProduct(String spuId, final int position) {
        Api.getDefault().soldOutProduct(spuId)
                .compose(RxHandleResult.<GoodsBean>handleResult())
                .subscribe(new RxSubscriber<GoodsBean>(mContext, true) {
                    @Override
                    public void _onNext(GoodsBean s) {
                        ToastUtil.showShort("下架商品成功");
                        view.changeStatus("unPublished", position);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("下架商品失败");
                    }
                });
    }

    /**
     * 立即发布
     *
     * @param spuId      商品Id
     * @param isTemplate 是否模板商品
     * @param position   item位置
     */
    public void publishImmediately(String spuId, String isTemplate, final int position) {
        if ("Y".equals(isTemplate)) {
            //模板商品 需要验证数据
            validateType = VALIDATE_TYPE_PUBLISH_IMMEDIATELY;
            validateTemplateSpu(spuId, position);
        } else {
            Api.getDefault().publishMyProductImmediately(spuId)
                    .compose(RxHandleResult.handleResult())
                    .subscribe(new RxSubscriber<Object>(mContext, false) {
                        @Override
                        public void _onNext(Object s) {
                            view.dismissProgressDialog();
                            ToastUtil.showShort("发布成功");
                            view.changeStatus("published", position);
                        }

                        @Override
                        public void _onError(String message) {
                            ToastUtil.showShort("发布失败");
                            view.dismissProgressDialog();
                        }
                    });
        }
    }

    /**
     * 检验 模板商品
     *
     * @param spuId    商品Id
     * @param position item位置
     */
    private void validateTemplateSpu(final String spuId, final int position) {
        Api.getDefault().validateTemplateSpu(spuId)
                .compose(RxHandleResult.<ValidateProductBean>handleResult())
                .subscribe(new RxSubscriber<ValidateProductBean>(mContext, false) {
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
                                view.dismissProgressDialog();
                                view.showExpireTip(spuId, "您的商品中使用过期素材，续费后才可以上架到商城，请您前往续费", position);
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
                                view.dismissProgressDialog();
                                view.showTip(tip.toString());
                                return;
                            }
                        }
                        switch (validateType) {
                            case VALIDATE_TYPE_CHECK://提交审核
                                submitMyProductToCheck(spuId, "N", position);
                                break;
                            case VALIDATE_TYPE_PUBLISH_IMMEDIATELY://立即上架
                                publishImmediately(spuId, "N", position);
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        view.dismissProgressDialog();
                    }
                });
    }

    /**
     * 删除商品
     */
    public void deleteSpu(String spuId, final int position, final MyProductListAdapter adapter) {
        spuIds.clear();
        spuIds.add(spuId);
        Api.getDefault().deleteMyProduct(spuIds)
                .compose(RxHandleResult.handleResult())
                .subscribe(new RxSubscriber<Object>(mContext, true) {
                    @Override
                    public void _onNext(Object s) {
                        adapter.remove(position);
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
        view.showProgressDialog();
        Api.getDefault().getRenewMaterial(spuId)
                .compose(RxHandleResult.<MaterialDetailBean>handleResult())
                .subscribe(new RxSubscriber<MaterialDetailBean>(mContext, false) {
                    @Override
                    public void _onNext(MaterialDetailBean materialDetailBean) {
                        //自动免费素材续费成功
                        view.renewSuccess();
                        //去续费
                        if (materialDetailBean.sourceMaterialModels != null && materialDetailBean.sourceMaterialModels.size() > 0) {
                            view.dismissProgressDialog();
                            MaterialPayConfirmOrderActivity.launch(mContext, materialDetailBean.sourceMaterialModels);
                        } else {
                            switch (validateType) {
                                case VALIDATE_TYPE_CHECK://提交审核
                                    submitMyProductToCheck(spuId, "N", position);
                                    break;
                                case VALIDATE_TYPE_PUBLISH_IMMEDIATELY://立即上架
                                    publishImmediately(spuId, "N", position);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        view.dismissProgressDialog();
                    }
                });
    }
}
