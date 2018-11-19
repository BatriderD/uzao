package com.zhaotai.uzao.ui.login.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.InputType;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.login.contract.LoginMsgContract;
import com.zhaotai.uzao.ui.login.presenter.LoginMsgPresenter;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.utils.ScreenUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * description: 验证码登录
 * author : zp
 * date: 2017/7/17
 */

public class LoginMsgActivity extends BaseActivity implements LoginMsgContract.View {
    @BindView(R.id.et_register_phone_number)
    public EditText et_phone;
    @BindView(R.id.et_reigster_phone_verification)
    public EditText et_verification;

    @BindView(R.id.getSmsCode)
    public TextView tv_smsCode;
    @BindView(R.id.tv_register_agreement)
    public TextView tv_agreement;
   @BindView(R.id.tv_register_agreement1)
    public TextView tv_agreement1;


    @BindView(R.id.tv_login_wx)
    public TextView tvWx;
    @BindView(R.id.tv_login_qq)
    public TextView tvQQ;
    @BindView(R.id.tv_login_sina)
    public TextView tvSina;
    @BindView(R.id.tv_login_pds)
    public TextView tvPsd;


    private LoginMsgPresenter loginPresenter;


    private int inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;


    public static void launch(Context context) {
        Intent intent = new Intent(context, LoginMsgActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_new_login_msg);

        //  透明状态栏
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setFitsSystemWindows(true);
        int statusHeight = ScreenUtils.getStatusBarHeight(this);
//        设置顶部高度
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mToolbar.getLayoutParams();
        layoutParams.setMargins(0,statusHeight,0,0);
        mToolbar.setLayoutParams(layoutParams);

        mTitle.setText(R.string.login);
        mTitle.setTextColor(Color.WHITE);
        tv_agreement.setText(Html.fromHtml(getString(R.string.uzao_agreement1)));


        //设置三方登录 按钮
        int picWidth = (int) PixelUtil.dp2px(43);
        //修改待xx的的图片
        Drawable drawableWaitPay = ContextCompat.getDrawable(this, R.drawable.ic_login_third_part_wx);
        drawableWaitPay.setBounds(0, 0, picWidth, picWidth);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        tvWx.setCompoundDrawables(null, drawableWaitPay, null, null);//只放左边

        Drawable drawableWaitCheck = ContextCompat.getDrawable(this, R.drawable.ic_login_third_part_qq);
        drawableWaitCheck.setBounds(0, 0, picWidth, picWidth);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        tvQQ.setCompoundDrawables(null, drawableWaitCheck, null, null);//只放左边

        Drawable drawableWaitSend = ContextCompat.getDrawable(this, R.drawable.ic_login_third_part_sina);
        drawableWaitSend.setBounds(0, 0, picWidth, picWidth);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        tvSina.setCompoundDrawables(null, drawableWaitSend, null, null);//只放左边

        Drawable drawableWaitReceiving = ContextCompat.getDrawable(this, R.drawable.ic_login_third_part_psd);
        drawableWaitReceiving.setBounds(0, 0, picWidth, picWidth);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        tvPsd.setCompoundDrawables(null, drawableWaitReceiving, null, null);//只放左边

        //获取验证码
        tv_smsCode.setText(getString(R.string.get_sms_code));
        tv_smsCode.setSelected(true);

        loginPresenter = new LoginMsgPresenter(this, this);
    }


    @Override
    protected void initData() {

    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }


    @OnClick(R.id.btn_next)
    public void toLogin() {
        String phoneNumber = et_phone.getText().toString();
        String verification = et_verification.getText().toString();
        loginPresenter.loginRequest(phoneNumber,verification);

    }


    @OnClick(R.id.tv_register_agreement)
    public void registerAgreement() {
        //用户协议
        ProtocolActivity.launch(mContext, GlobalVariable.PROTOCOL_REGISTER);
    }

    /**
     * 获取验证码
     */
    @OnClick(R.id.getSmsCode)
    public void getSmsCode() {
        //获取验证码
        loginPresenter.getSmsCode(et_phone.getText().toString());
    }

    /**
     * 登录成功
     */
    @Override
    public void loginSuccess() {
        finish();
    }


    @Override
    public void showBindPhonePage() {
        BindPhoneActivity.launch(this);
    }

    /**
     * 开始倒计时
     */
    @Override
    public void startGetSmsCode() {
        tv_smsCode.setText(getString(R.string.sixty_second));
        tv_smsCode.setSelected(false);
        tv_smsCode.setClickable(false);
    }


    /**
     * 倒计时完成
     */
    @Override
    public void finishCountDown() {
        tv_smsCode.setText(getString(R.string.get_sms_code));
        tv_smsCode.setSelected(true);
        tv_smsCode.setClickable(true);
    }


    /**
     * 登录成功
     */
    @Override
    public void success() {
        finish();
    }

    /**
     * 刷新倒计时
     *
     * @param time 时间
     */
    @Override
    public void refreshCountDown(Long time) {
        if (time == 0) {
            finishCountDown();
            return;
        }
        String s = String.valueOf(time) + "S";
        tv_smsCode.setText(s);
    }


    @OnClick(R.id.tv_login_wx)
    public void WXlogin() {
        loginPresenter.uMengLogin(SHARE_MEDIA.WEIXIN);
    }

    @OnClick(R.id.tv_login_qq)
    public void QQlogin() {
        loginPresenter.uMengLogin(SHARE_MEDIA.QQ);
    }

    @OnClick(R.id.tv_login_sina)
    public void Sinalogin() {
        loginPresenter.uMengLogin(SHARE_MEDIA.SINA);
    }

    @OnClick(R.id.tv_login_pds)
    public void Msglogin() {
//        跳转手机验证码登录页面
        LoginActivity.launch(this);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


}
