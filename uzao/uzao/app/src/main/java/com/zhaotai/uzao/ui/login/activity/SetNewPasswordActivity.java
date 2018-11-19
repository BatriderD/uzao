package com.zhaotai.uzao.ui.login.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.RequestNewPasswordBean;
import com.zhaotai.uzao.ui.login.contract.SetNewPasswordContract;
import com.zhaotai.uzao.ui.login.presenter.SetNewPasswordPresenter;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.MD5Utils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * description: 设置新密码
 * author : zp
 * date: 2017/7/17
 */

public class SetNewPasswordActivity extends BaseActivity implements SetNewPasswordContract.View {

    //新密码1
    @BindView(R.id.et_change_password_password_new1)
    public EditText et_password1;

    //新密码2
    @BindView(R.id.et_change_password_password_new2)
    public EditText et_password2;

    //旧密码
    @BindView(R.id.et_change_password_password_old)
    public EditText et_password_old;


    private SetNewPasswordPresenter setNewPasswordPresenter;


    public static void launch(Context context) {
        Intent intent = new Intent(context, SetNewPasswordActivity.class);
//        存放页面类别
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_change_password);
        et_password_old.setVisibility(View.GONE);
        mTitle.setText(R.string.change_password);

    }

    @Override
    protected void initData() {
        setNewPasswordPresenter = new SetNewPasswordPresenter(this, this);

    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @OnClick(R.id.btn_next)
    public void setNewPassword() {
        String psd1 = et_password1.getText().toString();
        String psd2 = et_password2.getText().toString();
        if (setNewPasswordPresenter.checkData(psd1, psd2)) {
            RequestNewPasswordBean changePasswordRequestBean = new RequestNewPasswordBean(MD5Utils.getMD5(psd1));
            setNewPasswordPresenter.setNewPassword(changePasswordRequestBean);
        }

    }

    /**
     * 设置新密码成功
     */
    @Override
    public void setNewSuccess() {
        finish();
        LoginHelper.exitLoginUiChange();
        LoginHelper.goLogin(this);
    }
}
