package com.zhaotai.uzao.ui.category.material.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.DictionaryBean;
import com.zhaotai.uzao.bean.MaterialDetailBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.MyTrackRequestBean;
import com.zhaotai.uzao.bean.MyTrackResultBean;
import com.zhaotai.uzao.bean.PosterTemplateBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.category.material.contract.MaterialDetailContract;
import com.zhaotai.uzao.utils.LogUtils;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * Time: 2018/1/8
 * Created by LiYou
 * Description : 素材详情
 */

public class MaterialDetailPresenter extends MaterialDetailContract.Presenter {

    private MaterialDetailContract.View view;

    public MaterialDetailPresenter(MaterialDetailContract.View view, Context context) {
        this.view = view;
        mContext = context;
    }

    /**
     * 获取素材详情
     *
     * @param id 素材id
     */
    @Override
    public void getMaterialDetail(String id) {
        Api.getDefault().getMaterialDetail(id)
                .compose(RxHandleResult.<MaterialDetailBean>handleResult())
                .subscribe(new RxSubscriber<MaterialDetailBean>(mContext, true) {
                    @Override
                    public void _onNext(MaterialDetailBean s) {
                        //判断是否下架
                        if ("published".equals(s.status)) {
                            view.showMaterialDetail(s);
                        } else {
                            view.showEmpty("已下架");
                        }
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    private static final String POINT_TYPE_material_benefit = "material_benefit";
    private static final String POINT_TYPE_MATERIAL = "sourceMaterial";
    //海报类型：平台(platform)/海报(theme)
    private static final String PLATFORM_TYPE_PLATFORM = "platform";

    @Override
    public void hasPoster() {

        String posterType = PLATFORM_TYPE_PLATFORM;
        String usePointType = POINT_TYPE_MATERIAL;
        Api.getDefault()
                .getPosterTemplate(posterType, usePointType,"")
                .compose(RxHandleResult.<List<PosterTemplateBean>>handleResult())
                .subscribe(new RxSubscriber<List<PosterTemplateBean>>(mContext) {
                    @Override
                    public void _onNext(List<PosterTemplateBean> posterTemplateBeans) {
                        view.openShareBoard(posterTemplateBeans != null && posterTemplateBeans.size() > 0);
                    }

                    @Override
                    public void _onError(String message) {
                        view.openShareBoard(false);
                    }
                });
    }

    @Override
    public void hasWelfare() {

        String posterType = PLATFORM_TYPE_PLATFORM;
        String usePointType = POINT_TYPE_material_benefit;
        Api.getDefault()
                .getPosterTemplate(posterType, usePointType,"")
                .compose(RxHandleResult.<List<PosterTemplateBean>>handleResult())
                .subscribe(new RxSubscriber<List<PosterTemplateBean>>(mContext,true) {
                    @Override
                    public void _onNext(List<PosterTemplateBean> posterTemplateBeans) {
                       view.showHasWelfare(posterTemplateBeans != null && posterTemplateBeans.size() > 0);
                    }

                    @Override
                    public void _onError(String message) {
                        view.showHasWelfare(false);
                    }
                });
    }



    /**
     * 获取推荐素材
     *
     * @param id 素材id
     */
    public void getRecommendMaterial(String id) {
        Api.getDefault().getRecommendMaterial(id)
                .compose(RxHandleResult.<List<MaterialListBean>>handleResult())
                .subscribe(new RxSubscriber<List<MaterialListBean>>(mContext) {
                    @Override
                    public void _onNext(List<MaterialListBean> goodsBeen) {
                        view.showRecommendMaterial(goodsBeen);
                    }

                    @Override
                    public void _onError(String message) {
                        LogUtils.logd(message);
                    }
                });
    }

    /**
     * 判断是否收藏
     *
     * @param id 素材id
     */
    public void isCollect(String id) {
        Api.getDefault().getIsCollectMaterial(id)
                .compose(RxHandleResult.<Boolean>handleResult())
                .subscribe(new RxSubscriber<Boolean>(mContext) {
                    @Override
                    public void _onNext(Boolean aBoolean) {
                        view.isCollect(aBoolean);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 收藏素材
     *
     * @param id 素材id
     */
    public void collectMaterial(String id) {
        Api.getDefault().collectMaterial(id)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String s) {
                        ToastUtil.showShort("收藏成功");
                        view.collect(true);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("收藏失败");
                    }
                });
    }

    /**
     * 取消收藏
     *
     * @param id 素材id
     */
    public void cancelCollectMaterial(String id) {
        List<String> ids = new ArrayList<>();
        ids.add(id);
        Api.getDefault().cancelCollectMaterial(ids)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String s) {
                        view.collect(false);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("取消失败");
                    }
                });
    }

    /**
     * 判断是否点赞
     *
     * @param id 素材id
     */
    public void isLikeMaterial(String id) {
        Api.getDefault().isLike("sourceMaterial", id)
                .compose(RxHandleResult.<Boolean>handleResult())
                .subscribe(new RxSubscriber<Boolean>(mContext) {
                    @Override
                    public void _onNext(Boolean aBoolean) {
                        view.isLike(aBoolean);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 点赞素材
     *
     * @param id 素材id
     */
    public void likeMaterial(String id) {
        Api.getDefault().likeMaterial("sourceMaterial", id)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String s) {
                        ToastUtil.showShort("点赞成功");
                        view.like(true);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("点赞失败");
                    }
                });
    }

    /**
     * 取消点赞
     *
     * @param id 素材id
     */
    public void cancelLikeMaterial(String id) {
        Api.getDefault().cancelLike("sourceMaterial", id)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String s) {
                        view.like(false);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("取消失败");
                    }
                });
    }

    /**
     * 获取素材
     *
     * @param id 素材id
     */
    public void getFreeMaterial(String id) {
        Api.getDefault().getFreeMaterial(id)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String s) {
                        view.hasMaterial();
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 获取打赏金额
     */
    public void getRewardPrice() {
        //获取打赏金额列表
        Api.getDefault().getAllDictionary("tipOption")
                .compose(RxHandleResult.<List<DictionaryBean>>handleResult())
                .subscribe(new RxSubscriber<List<DictionaryBean>>(mContext, true) {
                    @Override
                    public void _onNext(List<DictionaryBean> dictionaryBeen) {
                        view.showReward(dictionaryBeen);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 判断是否自己的素材
     */
    public boolean isMyMaterial(String userId) {
        return LoginHelper.getUserId().equals(userId);
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
}
