package com.zhaotai.uzao.ui.theme.presenter;

import android.content.Context;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.PosterTemplateBean;
import com.zhaotai.uzao.bean.ThemeBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.theme.contract.ThemeDetailContract;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Time: 2018/2/6
 * Created by LiYou
 * Description : 主题详情presenter
 */

public class ThemeDetailPresenter extends ThemeDetailContract.Presenter {

    private ThemeDetailContract.View mView;

    public ThemeDetailPresenter(ThemeDetailContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    /**
     * 获得主题i详情的数据
     *
     * @param themeId 主题id
     */
    @Override
    public void getDetail(String themeId) {
        Api.getDefault().checkTheme(themeId)
                .compose(RxHandleResult.<ThemeBean>handleResult())
                .subscribe(new RxSubscriber<ThemeBean>(mContext) {
                    @Override
                    public void _onNext(ThemeBean themeBean) {
                        mView.showDetail(themeBean);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 收藏主题id
     *
     * @param themeId 主题id
     */
    @Override
    public void collectTheme(String themeId) {
        Api.getDefault().collectTheme(themeId)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext) {
                    @Override
                    public void _onNext(String s) {
                        if (mView != null) {
                            //通知收藏成功
                            mView.showCollectTheme(true);
                            mView.changeFavoriteCount(true);
                            ToastUtil.showShort("收藏成功");
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("网络请求失败");
                    }
                });
    }

    /**
     * 删除收藏的主题
     *
     * @param themeId 收藏主题失败
     */
    @Override
    public void delTheme(String themeId) {
        ArrayList<String> ids = new ArrayList<>();
        ids.add(themeId);
        Api.getDefault().deleteCollectTheme(ids)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext) {
                    @Override
                    public void _onNext(String s) {
                        if (mView != null) {
                            //通知取消收藏
                            mView.showCollectTheme(false);
                            mView.changeFavoriteCount(false);
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("网络请求失败");
                    }
                });
    }

    private static final String POINT_TYPE_THEME = "theme";
    private static final String PLATFORM_TYPE_THEME = "theme";

    @Override
    public void hasPoster(String themeId) {

        String posterType = PLATFORM_TYPE_THEME;
        String usePointType = POINT_TYPE_THEME;


        Api.getDefault()
                .getPosterTemplate(posterType, usePointType,themeId)
                .compose(RxHandleResult.<List<PosterTemplateBean>>handleResult())
                .subscribe(new RxSubscriber<List<PosterTemplateBean>>(mContext,true) {
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
     * 获取当前的收藏状态
     *
     * @param themeId 主题id
     */
    @Override
    public void getCollectStatus(String themeId) {
        Api.getDefault().getThemeCollectStatus(themeId)
                .compose(RxHandleResult.<Boolean>handleResult())
                .subscribe(new RxSubscriber<Boolean>(mContext) {
                    @Override
                    public void _onNext(Boolean b) {
                        mView.showCollectTheme(b);
                    }

                    @Override
                    public void _onError(String message) {
                    }
                });
    }

    /**
     * 通知webview 收藏数目
     *
     * @param webView       webView
     * @param favoriteCount 收藏数
     */
    public void setFavoriteCount(WebView webView, String favoriteCount) {
        if (webView != null) {
            webView.evaluateJavascript("javascript:favoriteCount(" + favoriteCount + ")", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                }
            });
        }
    }

}
