package com.zhaotai.uzao.ui.util;

import android.content.Context;
import android.content.Intent;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.utils.ToastUtil;

import butterknife.OnClick;

/**
 * Time: 2018/6/4
 * Created by LiYou
 * Description : 扫描登录确认页面
 */

public class ScanLoginActivity extends BaseActivity {

    private static final String EXTRA_KEY_QRTOKEN = "extra_key_qrtoken";

    public static void launch(Context context, String qrToken) {
        Intent intent = new Intent(context, ScanLoginActivity.class);
        intent.putExtra(EXTRA_KEY_QRTOKEN, qrToken);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_scan_login);
        mTitle.setText("扫码登录");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    /**
     * 确认登录
     */
    @OnClick(R.id.tv_scan_login)
    public void onClickLogin() {
        String qrToken = getIntent().getStringExtra(EXTRA_KEY_QRTOKEN);
        Api.getDefault().scanQrLogin(qrToken)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, false) {
                    @Override
                    public void _onNext(String s) {
                        ToastUtil.showShort(s);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort(message);
                    }
                });
    }

    /**
     * 取消登录
     */
    @OnClick(R.id.tv_scan_login_cancel)
    public void onClickLoginCancel() {
        finish();
    }
}
