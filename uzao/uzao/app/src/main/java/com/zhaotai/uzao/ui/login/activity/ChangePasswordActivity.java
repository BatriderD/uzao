package com.zhaotai.uzao.ui.login.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.ChangePasswordRequestBean;
import com.zhaotai.uzao.ui.login.contract.ChangePasswordContract;
import com.zhaotai.uzao.ui.login.presenter.ChangePasswordPresenter;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.MD5Utils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * description: 注册界面2/找回密码页面2
 * author : zp
 * date: 2017/7/17
 */

public class ChangePasswordActivity extends BaseActivity implements ChangePasswordContract.View {

    //    旧密码
    @BindView(R.id.et_change_password_password_old)
    public EditText et_PasswordOld;

    //新密码1
    @BindView(R.id.et_change_password_password_new1)
    public EditText et_password1;

    //新密码2
    @BindView(R.id.et_change_password_password_new2)
    public EditText et_password2;


    private ChangePasswordPresenter changePasswordPresenter;


    public static void launch(Context context) {
        Intent intent = new Intent(context, ChangePasswordActivity.class);
//        存放页面类别
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_change_password);
        mTitle.setText(R.string.change_password);

        changePasswordPresenter = new ChangePasswordPresenter(this, this);
    }

    @Override
    protected void initData() {


    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @OnClick(R.id.btn_next)
    public void changePassWord() {
        String psdOld = et_PasswordOld.getText().toString();
        String psd1 = et_password1.getText().toString();
        String psd2 = et_password2.getText().toString();
        if (changePasswordPresenter.checkData(psdOld, psd1, psd2)) {
            ChangePasswordRequestBean changePasswordRequestBean = new ChangePasswordRequestBean(MD5Utils.getMD5(psdOld), MD5Utils.getMD5(psd1));
            changePasswordPresenter.changPassword(changePasswordRequestBean);
        }
    }


    /**
     * 修改成功回调
     */
    @Override
    public void changeSuccess() {
        finish();
        LoginHelper.exitLoginUiChange();
        LoginHelper.goLogin(this);
    }
}
