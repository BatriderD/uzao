package com.zhaotai.uzao.ui.theme.presenter;

import android.content.Context;
import android.util.Log;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeAllRequestBean;
import com.zhaotai.uzao.bean.ThemeBean;
import com.zhaotai.uzao.bean.ThemeModuleBean;
import com.zhaotai.uzao.bean.ThemeTemplateBean;
import com.zhaotai.uzao.bean.WebBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.theme.contract.ThemeTemplateContract;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * description: 主题模板presenter
 * author : ZP
 * date: 2018/2/1 0001.
 */

public class ThemeTemplatePresenter extends ThemeTemplateContract.Presenter {
    private ThemeTemplateContract.View mView;
    private HashMap<String,String> params = new HashMap<>();
    public ThemeTemplatePresenter(Context context, ThemeTemplateContract.View view) {
        this.mView = view;
        mContext = context;
    }

    /**
     * 获得模板列表
     * @param start 开始位置
     * @param isLoading 状态页面
     */
    @Override
    public void getThemeTemplateList(final int start, final boolean isLoading) {
        params.put("start", String.valueOf(start));
        params.put("length", "10");
        Api.getDefault().getThemeTemplateList(params)
                .compose(RxHandleResult.<PageInfo<ThemeTemplateBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<ThemeTemplateBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<ThemeTemplateBean> data) {
                        if (isLoading && start == Constant.PAGING_HOME) {
                            mView.showContent();
                            mView.stopLoadingMore();
                        } else if (start == 0) {
                            mView.stopRefresh();
                        } else {
                            mView.stopLoadingMore();
                        }
                        mView.showThemeTemplateList(data);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            mView.stopRefresh();
                            mView.showNetworkFail(message);
                        } else {
                            mView.loadingFail();
                        }
                    }
                });
    }

    /**
     * 预览主题
     * @param themeId 主题id
     * @param position 主题模板位置
     * @param templateId 模板id
     * @param json 主题json数据
     */
    @Override
    public void getPreviewTheme(String themeId, final int position, String templateId, String json) {
        Api.getDefault().previewTheme(themeId, templateId, json)
                .compose(RxHandleResult.<WebBean>handleResult())
                .subscribe(new RxSubscriber<WebBean>(mContext, true) {
                    @Override
                    public void _onNext(WebBean webBean) {
                        mView.getPreviewUrlSuccess(position, ApiConstants.THEME_TEMPLATE + webBean.getWapUrl());
                        Log.d("预览网址", "_onNext: " + ApiConstants.THEME_TEMPLATE + webBean.getWapUrl());
                    }


                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("预览失败");
                    }
                });
    }

    /**
     * 保存主题
     * @param themeId 主题id
     * @param templateId 模板id
     * @param json json数据
     * @param themeModuleBeanArrayList 主题模块数据
     */
    @Override
    public void saveThemeAll(String themeId, String templateId, String json, ArrayList<ThemeModuleBean> themeModuleBeanArrayList) {
        ThemeAllRequestBean requestBean = new ThemeAllRequestBean(templateId, json, themeModuleBeanArrayList);
        Api.getDefault().saveThemeAll(themeId, requestBean)
                .compose(RxHandleResult.<ThemeBean>handleResult())
                .subscribe(new RxSubscriber<ThemeBean>(mContext) {
                    @Override
                    public void _onNext(ThemeBean themeModuleBean) {
                        mView.showSaveSuccess();
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("保存失败");
                    }
                });
    }
}
