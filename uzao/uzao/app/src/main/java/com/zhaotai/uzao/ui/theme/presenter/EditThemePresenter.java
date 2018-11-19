package com.zhaotai.uzao.ui.theme.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.ThemeBean;
import com.zhaotai.uzao.bean.UpLoadPicUtils;
import com.zhaotai.uzao.bean.UploadFileBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.theme.contract.EditThemeContract;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.List;

/**
 * Time: 2018/1/19
 * Created by zp
 * Description : 编辑主题
 */

public class EditThemePresenter extends EditThemeContract.Presenter {
    private EditThemeContract.View mView;

    public EditThemePresenter(Context context, EditThemeContract.View mView) {
        this.mView = mView;
        mContext = context;
    }

    /**
     * 获取主题数据
     *
     * @param themeId 主题id
     */
    @Override
    public void getThemeData(String themeId) {
        //查看主题  并设置数据
        Api.getDefault().checkTheme(themeId)
                .compose(RxHandleResult.<ThemeBean>handleResult())
                .subscribe(new RxSubscriber<ThemeBean>(mContext, true) {
                    @Override
                    public void _onNext(ThemeBean themeBean) {
                        mView.showTheme(themeBean);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("主题信息获取失败");
                    }
                });
    }

    /**
     * 上传主题封面图片
     *
     * @param path 主题的封面本地地址
     */
    @Override
    public void upThemePic(String path) {

        UpLoadPicUtils.upLoadAndZipPic(path, new RxSubscriber<UploadFileBean>(mContext, true) {
            @Override
            public void _onNext(UploadFileBean bean) {
                mView.showThemeCover(bean.fileId);
            }

            @Override
            public void _onError(String message) {
                ToastUtil.showShort("网络错误");
            }
        });
    }


    /**
     * 保存主题
     *
     * @param themeBean 主题的数据封装
     */
    @Override
    public void saveTheme(ThemeBean themeBean) {
        Api.getDefault().saveTheme(themeBean)
                .compose(RxHandleResult.<ThemeBean>handleResult())
                .subscribe(new RxSubscriber<ThemeBean>(mContext, true) {
                    @Override
                    public void _onNext(ThemeBean bean) {
                        //通知页面刷新数据
                        mView.showSaveSuccess(bean, bean.sequenceNBR);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("保存失败");
                    }
                });
    }

    /**
     * 修改主题
     *
     * @param themeId   主题id
     * @param themeBean 主题数据封装类
     */
    @Override
    public void changeTheme(String themeId, ThemeBean themeBean) {
        Api.getDefault().changeTheme(themeId, themeBean)
                .compose(RxHandleResult.<ThemeBean>handleResult())
                .subscribe(new RxSubscriber<ThemeBean>(mContext, true) {
                    @Override
                    public void _onNext(ThemeBean bean) {
                        mView.showSaveSuccess(bean, bean.sequenceNBR);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("保存失败");
                    }
                });
    }

    /**
     * 检查数据
     *
     * @param themeId     主题id
     * @param themeName   主题名称
     * @param url         封面图片url
     * @param tags        标签
     * @param description 主题描述
     * @param isPublic    是否公开
     */
    public void checkData(String themeId, String themeName, String url, List<ThemeBean.TagsBean> tags, String description, String isPublic) {
        if (themeName == null || themeName.length() == 0 || themeName.length() > 20) {
            ToastUtil.showShort("主题名称不能为空");
            return;
        }
        if (tags == null || tags.size() == 0) {
            ToastUtil.showShort("请至少选择一个标签");
            return;
        }
        if (description == null || description.length() == 0) {
            ToastUtil.showShort("简介不能为空");
            return;
        }
        if (StringUtil.isEmpty(url)) {
            ToastUtil.showShort("图片不能为空");
            return;
        }
        ThemeBean themeBean = new ThemeBean();
        themeBean.name = themeName;
        themeBean.cover = url;
        themeBean.tags = tags;
        themeBean.description = description;
        themeBean.isPublic = isPublic;
        if (StringUtil.isEmpty(themeId)) {
            saveTheme(themeBean);
        } else {
            changeTheme(themeId, themeBean);
        }


    }

}
