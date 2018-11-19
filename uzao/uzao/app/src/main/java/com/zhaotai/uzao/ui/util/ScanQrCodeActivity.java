package com.zhaotai.uzao.ui.util;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.utils.LogUtils;
import com.zhaotai.zxing.ZXingView;
import com.zhaotai.zxing.core.QRCodeView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2018/6/4
 * Created by LiYou
 * Description :
 */

public class ScanQrCodeActivity extends BaseActivity implements QRCodeView.Delegate {
    private static final String TAG = "ScanQrCodeActivity";
    @BindView(R.id.zxingview)
    ZXingView mQRCodeView;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, ScanQrCodeActivity.class));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_scan_qr_code);
        mTitle.setText("扫描登录");
        mQRCodeView.setDelegate(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
        mQRCodeView.showScanRect();
        mQRCodeView.startSpot();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    /**
     * 震动
     */
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        LogUtils.logd(TAG, "result:" + result);
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        vibrate();
        mQRCodeView.startSpot();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        LogUtils.logd(TAG, "打开相机出错");
    }

    /**
     * 打开闪光灯
     */
    @OnClick(R.id.open_flashlight)
    public void openFlashLight() {
        mQRCodeView.openFlashlight();
    }

    /**
     * 关闭闪光灯
     */
    @OnClick(R.id.close_flashlight)
    public void closeFlashLight() {
        mQRCodeView.closeFlashlight();
    }


    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }
}
