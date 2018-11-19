package com.zhaotai.uzao.ui.login.presenter;

import android.content.Context;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.RequestNewPasswordBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.login.contract.SetNewPasswordContract;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

/**
 * description: 设置新用户密码Presenter
 * author : zp
 * date: 2017/7/18
 */

public class SetNewPasswordPresenter extends SetNewPasswordContract.Presenter {
    private SetNewPasswordContract.View mView;

    public SetNewPasswordPresenter(Context context, SetNewPasswordContract.View view) {
        mContext = context;
        mView = view;
    }


    @Override
    public void setNewPassword(RequestNewPasswordBean bean) {
        Api.getDefault().setNewPassword(bean)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, false) {
                    @Override
                    public void _onNext(String s) {
                        mView.setNewSuccess();
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("修改");
                    }
                });
    }

    @Override
    public boolean checkData(String psd1, String psd2) {
        if (StringUtil.isEmpty(psd1)) {
//            密码1为空
            ToastUtil.showShort(mContext.getString(R.string.please_enter_psd));
            return false;
        }
        if (StringUtil.isEmpty(psd2)) {
//            密码2为空
            ToastUtil.showShort(mContext.getString(R.string.please_enter_psd2));
            return false;
        }
        if (!psd1.equals(psd2)) {
//            两次输入密码不同
            ToastUtil.showShort(mContext.getString(R.string.please_enter_same_psd));
            return false;
        }

        return true;
    }
}
