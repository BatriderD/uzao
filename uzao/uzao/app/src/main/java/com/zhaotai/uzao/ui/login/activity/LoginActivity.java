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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.RequestLoginBean;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.login.contract.LoginContract;
import com.zhaotai.uzao.ui.login.presenter.LoginPresenter;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.utils.ScreenUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * description: 密码登录
 * author : zp
 * date: 2017/7/17
 */

public class LoginActivity extends BaseActivity implements LoginContract.View {
    @BindView(R.id.et_register_phone_number)
    public EditText et_phone;
    @BindView(R.id.et_register_phone_password)
    public EditText et_password;
    @BindView(R.id.tv_register_agreement)
    public TextView tv_agreement;
    //隐藏显示password
    @BindView(R.id.iv_login_see_password)
    public ImageView iv_seePassword;

    @BindView(R.id.tv_login_wx)
    public TextView tvWx;
    @BindView(R.id.tv_login_qq)
    public TextView tvQQ;
    @BindView(R.id.tv_login_sina)
    public TextView tvSina;
    @BindView(R.id.tv_login_msg)
    public TextView tvMsg;


    private LoginPresenter loginPresenter;


    private int inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;


    public static void launch(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_new_login);
        //  透明状态栏
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setFitsSystemWindows(true);
        int statusHeight = ScreenUtils.getStatusBarHeight(this);
//        设置顶部高度
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mToolbar.getLayoutParams();
        layoutParams.setMargins(0, statusHeight, 0, 0);
        mToolbar.setLayoutParams(layoutParams);

        mTitle.setText(R.string.login);
        mTitle.setTextColor(Color.WHITE);
        tv_agreement.setText(Html.fromHtml(getString(R.string.uzao_agreement)));
//        显示密码按钮 不可见
        iv_seePassword.setSelected(false);
//         设置三方登录 按钮
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

        Drawable drawableWaitReceiving = ContextCompat.getDrawable(this, R.drawable.ic_login_third_part_msg);
        drawableWaitReceiving.setBounds(0, 0, picWidth, picWidth);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        tvMsg.setCompoundDrawables(null, drawableWaitReceiving, null, null);//只放左边


        loginPresenter = new LoginPresenter(this, this);
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
        String password = et_password.getText().toString();
        RequestLoginBean requestLoginBean = new RequestLoginBean(phoneNumber, password);
        loginPresenter.login(requestLoginBean);
    }


    @OnClick(R.id.tv_register_forget_password)
    public void forgetPassword() {
        //进入忘记密码
        RegisterActivity1.launch(LoginActivity.this, GlobalVariable.FINDPAS_TYPE);
    }

    @OnClick(R.id.tv_register_immediately_register)
    public void immediatelyRegister() {
        //进入注册页面
        RegisterActivity1.launch(LoginActivity.this, GlobalVariable.REGISTER_TYPE);
    }

    @OnClick(R.id.tv_register_agreement)
    public void registerAgreement() {
        //用户协议
        ProtocolActivity.launch(mContext, GlobalVariable.PROTOCOL_REGISTER);
    }


    /**
     * 点击眼睛切换密码状态
     */
    @OnClick(R.id.iv_login_see_password)
    public void changeShowPassword() {
        if (et_password.getInputType() == inputType) {
            et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            et_password.setSelection(et_password.getText().length());     //把光标设置到当前文本末尾
            iv_seePassword.setSelected(true);

        } else {
            et_password.setInputType(inputType);
            et_password.setSelection(et_password.getText().length());
            iv_seePassword.setSelected(false);
        }

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


    @OnClick(R.id.tv_login_wx)
    public void WXLogin() {
        loginPresenter.uMengLogin(SHARE_MEDIA.WEIXIN);
    }

    @OnClick(R.id.tv_login_qq)
    public void QQLogin() {
        loginPresenter.uMengLogin(SHARE_MEDIA.QQ);
    }

    @OnClick(R.id.tv_login_sina)
    public void SinaLogin() {
        loginPresenter.uMengLogin(SHARE_MEDIA.SINA);
    }

    @OnClick(R.id.tv_login_msg)
    public void MsgLogin() {
//        跳转手机验证码登录页面
        LoginMsgActivity.launch(this);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (resultCode == 5) {
            finish();
        }
    }
}
