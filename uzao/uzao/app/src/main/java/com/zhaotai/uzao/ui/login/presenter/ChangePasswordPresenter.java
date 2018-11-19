package com.zhaotai.uzao.ui.login.presenter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.ChangePasswordRequestBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.login.contract.ChangePasswordContract;
import com.zhaotai.uzao.utils.LogUtils;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

/**
 * description: 修改用户密码Presenter
 * author : zp
 * date: 2017/7/18
 */

public class ChangePasswordPresenter extends ChangePasswordContract.Presenter {
    private ChangePasswordContract.View mView;

    public ChangePasswordPresenter(Context context, ChangePasswordContract.View view) {
        mContext = context;
        mView = view;
    }

    /**
     * 修改用户密码
     *
     * @param bean 修改密码请求bean类
     */
    @Override
    public void changPassword(ChangePasswordRequestBean bean) {
        Api.getDefault().changePassword(bean)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String s) {
                        LoginHelper.exitLogin((AppCompatActivity) mContext);
                        mView.changeSuccess();
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort(message);
                    }
                });
    }

    @Override
    public boolean checkData(String psd_old, String psd1, String psd2) {

        if (StringUtil.isEmpty(psd_old)) {
//            旧密码不能为空
            ToastUtil.showShort(mContext.getString(R.string.old_password_cant_empty));
            return false;
        } else if (!StringUtil.isPassWord(psd_old)) {
            ToastUtil.showShort(mContext.getString(R.string.psd_error));
            return false;
        } else if (StringUtil.isEmpty(psd1)) {
//            密码1为空
            ToastUtil.showShort(mContext.getString(R.string.please_enter_psd));
            return false;
        } else if (StringUtil.isEmpty(psd2)) {
//            密码2为空
            ToastUtil.showShort(mContext.getString(R.string.please_enter_psd2));
            return false;
        } else if (psd1.length() > GlobalVariable.PSD_MAX || psd1.length() < GlobalVariable.PSD_MIN) {
//            验证码为空
            ToastUtil.showShort(mContext.getString(R.string.psd_length_error));
            return false;
        } else if (psd2.length() > GlobalVariable.PSD_MAX || psd2.length() < GlobalVariable.PSD_MIN) {
//            验证码为空
            ToastUtil.showShort(mContext.getString(R.string.psd_length_error));
            return false;
        } else if (!StringUtil.isPassWord(psd1) || !StringUtil.isPassWord(psd2)) {
            ToastUtil.showShort(mContext.getString(R.string.psd_error));
            return false;
        } else if (!psd1.equals(psd2)) {
//            两次输入密码不同
            ToastUtil.showShort(mContext.getString(R.string.please_enter_same_psd));
            return false;
        }


        return true;
    }
}
