package com.zhaotai.uzao.widget;

import android.app.Activity;
import android.app.Dialog;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.widget.dialog.UITipDialog;


/**
 * time:2017/4/7
 * description: 弹框浮动加载进度条
 * author: LiYou
 */

public class LoadingDialog {
    /**
     * 加载数据对话框
     */
    private static UITipDialog mLoadingDialog;

    /**
     * 显示加载对话框
     *
     * @param context    上下文
     * @param msg        对话框显示内容
     */
    public static Dialog showDialogForLoading(Activity context, String msg) {
        mLoadingDialog = new UITipDialog.Builder(context)
                .setIconType(UITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(msg)
                .create();
        mLoadingDialog.show();
        return mLoadingDialog;
    }

    public static Dialog showDialogForLoading(Activity context) {
        mLoadingDialog = new UITipDialog.Builder(context)
                .setIconType(UITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(context.getString(R.string.loading))
                .create();
        mLoadingDialog.show();
        return  mLoadingDialog;
    }

    /**
     * 关闭加载对话框
     */
    public static void cancelDialogForLoading() {
        if(mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }
}
