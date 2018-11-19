package com.zhaotai.uzao.ui.main.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.BaseResult;
import com.zhaotai.uzao.bean.DynamicBodyBean;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.MainTabBean;
import com.zhaotai.uzao.bean.MultiCustomBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.constants.DynamicType;
import com.zhaotai.uzao.ui.category.goods.activity.NavigateProductListActivity;
import com.zhaotai.uzao.ui.main.contract.CustomFragmentContract;
import com.zhaotai.uzao.ui.search.CollectionSearchActivity;
import com.zhaotai.uzao.ui.theme.activity.ThemeDetailActivity;
import com.zhaotai.uzao.ui.web.SpecialActivityWebActivity;
import com.zhaotai.uzao.ui.web.WebActivity;
import com.zhaotai.uzao.utils.LoginHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Time: 2017/5/8
 * Created by LiYou
 * Description : 自定义首页
 */

public class CustomFragmentPresenter extends CustomFragmentContract.Presenter {

    private CustomFragmentContract.View mView;
    private List<MultiCustomBean> multiList = new ArrayList<>();
    private Gson gson;

    public CustomFragmentPresenter(Context context, CustomFragmentContract.View view) {
        mContext = context;
        mView = view;
    }

    /**
     * 获取数据
     *
     * @param groupCode 二级分类Id
     */
    @Override
    public void getData(String groupCode) {
        final boolean isLogin = LoginHelper.getLoginStatus();
        //# 1 获取首页数据
        Api.getDefault().getMainTabData(groupCode)
                .compose(RxHandleResult.<List<MainTabBean>>handleResultMap())
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<List<MainTabBean>, ObservableSource<BaseResult<List<GoodsBean>>>>() {
                    @Override
                    public ObservableSource<BaseResult<List<GoodsBean>>> apply(@NonNull List<MainTabBean> mainTabBeen) throws Exception {
                        //# 2 构造多布局 数据
                        getMultiTypeCategoryData(mainTabBeen);
                        //# 3 判断是否登录 获取不同的推荐数据
                        if (isLogin) {
                            return Api.getDefault().getRecommendLike();
                        } else {
                            return Api.getDefault().getRecommendSpuSearch("");
                        }
                    }
                }).compose(RxHandleResult.<List<GoodsBean>>handleResult())
                .subscribe(new RxSubscriber<List<GoodsBean>>(mContext) {
                    @Override
                    public void _onNext(List<GoodsBean> goodsBeen) {
                        //# 4 构造推荐数据
                        getMultiTypeSpuData(goodsBeen);
                        if (mView != null) {
                            mView.showContent();
                            mView.stopRefresh();
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
     *
     * @param data 首页数据
     */
    @Override
    public void getMultiTypeCategoryData(List<MainTabBean> data) {
        multiList.clear();
        for (int i = 0; i < data.size(); i++) {
            MultiCustomBean item = new MultiCustomBean(MultiCustomBean.TYPE_CATEGORY);
            MainTabBean category = data.get(i);
            if (i == 0) {
                item.isSelect = true;
            }
            item.icon = category.icon;
            item.navigateCode = category.navigateCode;
            item.navigateName = category.navigateName;
            item.associateType = category.associateType;
            item.associateData = category.associateData;
            multiList.add(item);
        }
    }

    /**
     * 构造多布局推荐数据
     *
     * @param data 推荐数据
     */
    @Override
    public void getMultiTypeSpuData(List<GoodsBean> data) {
        multiList.add(new MultiCustomBean(MultiCustomBean.TYPE_LINE));
        multiList.add(new MultiCustomBean(MultiCustomBean.TYPE_RECOMMEND_SPU_TITLE));
        for (GoodsBean goodsItem : data) {
            MultiCustomBean item = new MultiCustomBean(MultiCustomBean.TYPE_RECOMMEND_SPU);
            item.sequenceNBR = goodsItem.sequenceNBR;
            item.spuName = goodsItem.spuName;
            item.thumbnail = goodsItem.thumbnail;
            multiList.add(item);
        }
    }

    /**
     *
     * @param pressPosition
     */
    @Override
    public void changeCategoryTextColor(int pressPosition) {
        if (multiList.size() > 0 && !multiList.get(pressPosition).isSelect) {
            int count = 0;
            for (int i = 0; i < multiList.size(); i++) {
                if (multiList.get(i).getItemType() == MultiCustomBean.TYPE_CATEGORY) {
                    count = i;
                    multiList.get(i).isSelect = pressPosition == i;
                }
            }
            if (mView != null) {
                mView.notifyItemChange(0, count + 1);
            }
        }

    }

    /**
     * 分类跳转
     */
    @Override
    public void categoryRoute(MultiCustomBean item) {
        if (gson == null) {
            gson = new Gson();
        }
        switch (item.associateType) {
            case DynamicType.PAGE://专题
                DynamicBodyBean body = gson.fromJson(item.associateData, DynamicBodyBean.class);
                SpecialActivityWebActivity.launch(mContext, body.webUrl);
                break;
            case DynamicType.THEME://主题
                ThemeDetailActivity.launch(mContext, item.associateData);
                break;
            case DynamicType.CUSTOM://自定义网址
                WebActivity.launch(mContext, item.associateData);
                break;
            case DynamicType.CATEGORY://导航列表
                NavigateProductListActivity.launchNavigate(mContext, item.navigateCode);
                break;
            case DynamicType.COLLECTION://集合
                CollectionSearchActivity.launch(mContext, item.navigateName, item.associateData);
                break;
        }
    }
}
