package com.zhaotai.uzao.ui.theme.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeBean;
import com.zhaotai.uzao.bean.post.AddContentToThemeBody;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.theme.contract.AddThemeContract;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.HashMap;

/**
 * Time: 2018/1/19
 * Created by LiYou
 * Description : 新增主题的列表
 */

public class AddThemePresenter extends AddThemeContract.Presenter {
    private AddThemeContract.View mView;
    private HashMap<String, String> params = new HashMap<>();

    public AddThemePresenter(Context context, AddThemeContract.View mView) {
        this.mView = mView;
        mContext = context;
    }

    /**
     * 获取新增主题列表
     * @param start 起始位置
     */
    @Override
    public void getThemeList(final int start) {

        params.put("start", String.valueOf(start));
        params.put("length", "10");
        Api.getDefault().getMyThemeList(params)
                .compose(RxHandleResult.<PageInfo<ThemeBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<ThemeBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<ThemeBean> data) {
                        if (start == Constant.PAGING_HOME) {
                        } else {
                            mView.stopLoadingMore();
                        }
                        mView.showMyThemeList(data);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            mView.showNetworkFail(message);
                        } else {
                            mView.loadingMoreFail();
                        }
                    }
                });
    }

    /**
     * 保存主题模块
     * @param themeName 主题名称
     * @param entityType 主题类型
     * @param entityId 模块id
     */
    @Override
    public void addTheme(String themeName, final String entityType, final String entityId) {
        ThemeBean themeBean = new ThemeBean();
        themeBean.name = themeName;
        themeBean.cover = GlobalVariable.THEME_DEFAULT_THEME_PIC;
        themeBean.description = "请填写";
        themeBean.isPublic = "N";
        Api.getDefault().saveTheme(themeBean)
                .compose(RxHandleResult.<ThemeBean>handleResult())
                .subscribe(new RxSubscriber<ThemeBean>(mContext) {
                    @Override
                    public void _onNext(ThemeBean bean) {
                        addContentToTheme(bean.sequenceNBR, entityType, entityId);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("保存失败");
                    }
                });
    }

    public void addContentToTheme(String themeId, String entityType, String entityId) {
        AddContentToThemeBody body = new AddContentToThemeBody();
        body.themeId = themeId;
        body.entityType = entityType;
        body.entityId = entityId;
        Api.getDefault().addContentToTheme(body)
                .compose(RxHandleResult.<ThemeBean>handleResult())
                .subscribe(new RxSubscriber<ThemeBean>(mContext) {
                    @Override
                    public void _onNext(ThemeBean s) {
                        ToastUtil.showShort("添加主题成功");
                        mView.addThemeSuccess("");
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort(message);
                    }
                });
    }
}
