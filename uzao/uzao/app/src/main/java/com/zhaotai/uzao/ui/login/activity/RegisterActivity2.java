package com.zhaotai.uzao.ui.login.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.widget.EditText;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.ForgetRequestBean;
import com.zhaotai.uzao.bean.RegisterRequestBean;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.login.contract.RegisterContract;
import com.zhaotai.uzao.ui.login.presenter.RegisterPresenter;
import com.zhaotai.uzao.utils.MD5Utils;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * description: 注册界面2/找回密码页面2
 * author : zp
 * date: 2017/7/17
 */

public class RegisterActivity2 extends BaseActivity implements RegisterContract.View {

    @BindView(R.id.et_register2_verification_code)
    public EditText et_verification;

    @BindView(R.id.et_register2_password)
    public EditText et_password1;


    @BindView(R.id.et_register2_password_again)
    public EditText et_password2;

    @BindView(R.id.tv_register2_countdown)
    public TextView tv_countDown;

    @BindView(R.id.tv_register2_show_number)
    public TextView tv_show_number;
    // 协议
    @BindView(R.id.tv_register2_agreement)
    public TextView tv_agreement;

    private RegisterPresenter registerPresenter;
    //    从上个页面获取的手机号
    private String phone_number;

    private String pageType;

    public static void launch(Activity context, String phoneNumber, String type) {
        Intent intent = new Intent(context, RegisterActivity2.class);
        intent.putExtra("PHONE_NUMBER", phoneNumber);
//        存放页面类别
        intent.putExtra(GlobalVariable.PAGE_TYPE, type);
        context.startActivityForResult(intent, 1);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_register2);
        mTitle.setText(R.string.register);
        mToolbar.setBackground(ContextCompat.getDrawable(this, R.color.white));
        tv_agreement.setText(Html.fromHtml(getString(R.string.uzao_agreement2)));
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
//      取出当前页面类型
        pageType = intent.getStringExtra(GlobalVariable.PAGE_TYPE);
        if (GlobalVariable.REGISTER_TYPE.equals(pageType)) {
            //页面为注册页面1
            mTitle.setText(R.string.register);
        } else {
            //页面为找回密码1
            mTitle.setText(R.string.set_new_password);
        }

        //取出电话号码
        phone_number = intent.getStringExtra("PHONE_NUMBER");
        tv_show_number.setText(getString(R.string.send_msg_to_phone) + "  " + phone_number);
        registerPresenter = new RegisterPresenter(this, this);
        //倒计时
        registerPresenter.countDown();
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }


    @OnClick(R.id.tv_register2_agreement)
    public void registerAgreement() {
        //用户协议
        ProtocolActivity.launch(mContext, GlobalVariable.PROTOCOL_REGISTER);
    }

    /**
     * 下一步  注册或者找回密码
     * 这里的密码更换成md5加密过后的字符串
     */
    @OnClick(R.id.btn_next)
    public void checkAndFindPsd() {
        if (checkData()) {
            String psd1 = et_password1.getText().toString();
            psd1 = MD5Utils.getMD5(psd1);
            String vet_number = et_verification.getText().toString();
            if (GlobalVariable.REGISTER_TYPE.equals(pageType)) {
//            注册页面
                RegisterRequestBean registerRequestBean = new RegisterRequestBean(phone_number, psd1, vet_number);
                registerPresenter.register(registerRequestBean);
            } else if (GlobalVariable.FINDPAS_TYPE.equals(pageType)) {
//                找回密码页面
                ForgetRequestBean forgetRequestBean = new ForgetRequestBean(phone_number, psd1, vet_number);
                registerPresenter.foregetPassword(forgetRequestBean);
            }
        }

    }

    private boolean checkData() {
        String psd1 = et_password1.getText().toString();
        String psd2 = et_password2.getText().toString();
        String vet_number = et_verification.getText().toString();
        if (StringUtil.isEmpty(psd1)) {
//            密码1为空
            ToastUtil.showShort(getString(R.string.please_enter_psd));
            return false;
        }
        if (StringUtil.isEmpty(psd2)) {
//            密码2为空
            ToastUtil.showShort(getString(R.string.please_enter_psd2));
            return false;
        }
        if (StringUtil.isEmpty(vet_number)) {
//            验证码为空
            ToastUtil.showShort(getString(R.string.please_input_verification));
            return false;
        }
        if (psd1.length() > GlobalVariable.PSD_MAX || psd1.length() < GlobalVariable.PSD_MIN) {
//            验证码为空
            ToastUtil.showShort(getString(R.string.psd_length_error));
            return false;
        }
        if (psd2.length() > GlobalVariable.PSD_MAX || psd2.length() < GlobalVariable.PSD_MIN) {
//            验证码为空
            ToastUtil.showShort(getString(R.string.psd_length_error));
            return false;
        }
        if (!StringUtil.isPassWord(psd1) || !StringUtil.isPassWord(psd2)) {
            ToastUtil.showShort(getString(R.string.psd_error));
            return false;
        }
        if (!psd1.equals(psd2)) {
//            两次输入密码不同
            ToastUtil.showShort(getString(R.string.please_enter_same_psd));
            return false;
        }

        return true;
    }

    @OnClick(R.id.tv_register2_countdown)
    public void getMsg() {
        if (getString(R.string.get_sms_code).equals(tv_countDown.getText().toString()) || getString(R.string.retry_get_sms_code).equals(tv_countDown.getText().toString())) {
//            倒计时结束 可以重新获取验证码
            registerPresenter.getSmsCode(phone_number);
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
    public void loginSuccess() {
        setResult(5);
        finish();
    }

    @Override
    public void changePasswordSuccess() {
        setResult(RESULT_OK);
        ToastUtil.showShort("密码找回成功!");
        finish();
    }
}
