package com.zhaotai.uzao.ui.login.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.RequestBindPhoneBean;
import com.zhaotai.uzao.ui.login.contract.BindPhoneContract;
import com.zhaotai.uzao.ui.login.presenter.BindPhonePresenter;
import com.zhaotai.uzao.utils.StringUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * description: 绑定手机号页面
 * author : zp
 * date: 2017/7/17
 */

public class BindPhoneActivity extends BaseActivity implements BindPhoneContract.View {


    @BindView(R.id.et_bind_phone_verification_code)
    public EditText et_verification;

    @BindView(R.id.et_bind_phone_number)
    public EditText et_phone_number;

    @BindView(R.id.tv_bind_phone_countdown)
    public TextView tv_countDown;


    private BindPhonePresenter bindPhonePresenter;


    public static void launch(Context context) {
        Intent intent = new Intent(context, BindPhoneActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_bind_phone);
        mTitle.setText(R.string.bind_phone);

        tv_countDown.setText(R.string.get_sms_code);
        tv_countDown.setSelected(true);

    }

    @Override
    protected void initData() {
        bindPhonePresenter = new BindPhonePresenter(this, this);
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }


    @OnClick(R.id.btn_next)
    public void checkAndBindPhone() {
        String number = et_phone_number.getText().toString();
        String code = et_verification.getText().toString();
        RequestBindPhoneBean requestBindPhoneBean = new RequestBindPhoneBean(number, code);

        bindPhonePresenter.bindPhone(requestBindPhoneBean);


    }


    @OnClick(R.id.tv_bind_phone_countdown)
    public void getMsg() {
        String number = et_phone_number.getText().toString();
        if ((getString(R.string.get_sms_code).equals(tv_countDown.getText().toString()) || getString(R.string.retry_get_sms_code).equals(tv_countDown.getText().toString()))
                && StringUtil.checkPhoneNumber(number)) {
//            倒计时结束 可以重新获取验证码
            bindPhonePresenter.getSmsCode(number);
        }
    }

    @Override
    public void refreshCountDown(Long time) {
        if (time == 0) {
            tv_countDown.setText(R.string.retry_get_sms_code);
            tv_countDown.setSelected(true);
            return;
        }
        tv_countDown.setSelected(false);
        String s = String.valueOf(time) + getString(R.string.after_second_retry);
        tv_countDown.setText(s);
    }

    @Override
    public void bindSuccess() {
        finish();
    }
}
