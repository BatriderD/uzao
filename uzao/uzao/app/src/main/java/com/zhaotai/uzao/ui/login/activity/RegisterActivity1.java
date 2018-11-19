package com.zhaotai.uzao.ui.login.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.login.contract.Register1Contract;
import com.zhaotai.uzao.ui.login.presenter.Register1Presenter;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * description: 注册页面1  & 找回密码页面1
 * 两页面相同 filterName = REGISTER_TYPE 为注册页面    filterName = FINDPAS_TYPE 为找回密码页
 * author : zp
 * date: 2017/7/17
 */

public class RegisterActivity1 extends BaseActivity implements Register1Contract.View {
    @BindView(R.id.et_register_phone_number)
    public EditText et_phone;
    @BindView(R.id.tv_register_phone_txt)
    public TextView tv_phone_text;

    private String pageType;
    private Register1Presenter mPresenter;


    public static void launch(AppCompatActivity context, String type) {
        Intent intent = new Intent(context, RegisterActivity1.class);
        intent.putExtra(GlobalVariable.PAGE_TYPE, type);
        context.startActivityForResult(intent, 1);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_register1);
    }

    @Override
    protected void initData() {

        pageType = getIntent().getStringExtra(GlobalVariable.PAGE_TYPE);
//        根据页面类型设置页面名称
        if (GlobalVariable.REGISTER_TYPE.equals(pageType)) {
//            页面为注册页面1
            mTitle.setText(R.string.register);
            tv_phone_text.setText(R.string._86);
            et_phone.setHint(R.string.enter_your_phone);

        } else {
//            页面为找回密码1
            mTitle.setText(R.string.forget_password);
            tv_phone_text.setVisibility(View.GONE);
            et_phone.setHint(R.string.phone_number);
        }

        mPresenter = new Register1Presenter(this, this);

    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @OnClick(R.id.btn_next)
    public void checkPhoneAndSendMsg() {
        String phoneNumber = et_phone.getText().toString();
        if (StringUtil.checkPhoneNumber(phoneNumber)) {
            //进入下一页 1 先发送短信 成功跳转下一页
            mPresenter.getSmsCode(phoneNumber);
        } else {
            ToastUtil.showShort(getString(R.string.please_input_correct_phone_number));
        }

    }

    //    成功获得验证码
    @Override
    public void getSmsSuccess() {
        String phoneNumber = et_phone.getText().toString();
        RegisterActivity2.launch(RegisterActivity1.this, phoneNumber, pageType);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 5 || resultCode == RESULT_OK) {
            if (GlobalVariable.REGISTER_TYPE.equals(pageType)) {
                //页面为注册页面1
                setResult(resultCode);
            }
            finish();
        }
    }
}
