package com.zhaotai.uzao.ui.productOrder.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.pingplusplus.android.Pingpp;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.ui.productOrder.contract.ProducePayContract;
import com.zhaotai.uzao.ui.productOrder.presenter.ProducePayPresenter;
import com.zhaotai.uzao.utils.LogUtils;
import com.zhaotai.uzao.widget.dialog.UITipDialog;

import butterknife.OnClick;

/**
 * Time: 2017/8/23
 * Created by LiYou
 * Description : 选择支付方式界面 生产订单
 */

public class ProductOrderSelectPayWayActivity extends BaseActivity implements ProducePayContract.View {

    //支付方式 true zfb false 微信
    private String payWay = "alipay";

    private static final String EXTRA_KEY_PRODUCE_PAY_ORDER_NO = "extra_key_produce_pay_order_no";
    private static final String EXTRA_KEY_PRODUCE_PAY_SOURCE = "extra_key_produce_pay_source";
    private ProducePayPresenter producePayPresenter;
    private String orderNo;
    private String source;
    private UITipDialog tipDialog;

    /**
     * @param source pay全部  firstPay首付 lastPay尾款
     */
    public static void launch(Context context, String orderNo, String source) {
        Intent intent = new Intent(context, ProductOrderSelectPayWayActivity.class);
        intent.putExtra(EXTRA_KEY_PRODUCE_PAY_ORDER_NO, orderNo);
        intent.putExtra(EXTRA_KEY_PRODUCE_PAY_SOURCE, source);
        context.startActivity(intent);
    }
    //用来关闭详情页
    public static void launch(BaseActivity context, String orderNo, String source) {
        Intent intent = new Intent(context, ProductOrderSelectPayWayActivity.class);
        intent.putExtra(EXTRA_KEY_PRODUCE_PAY_ORDER_NO, orderNo);
        intent.putExtra(EXTRA_KEY_PRODUCE_PAY_SOURCE, source);
        context.startActivity(intent);
        context.startActivityForResult(intent,RESULT_OK);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_product_order_select_pay_way);
        mTitle.setText("选择支付方式");
    }

    @Override
    protected void initData() {
        orderNo = getIntent().getStringExtra(EXTRA_KEY_PRODUCE_PAY_ORDER_NO);
        source = getIntent().getStringExtra(EXTRA_KEY_PRODUCE_PAY_SOURCE);
        producePayPresenter = new ProducePayPresenter(this, this);
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @OnClick(R.id.pay_way_zfb)
    public void productOrderZfb() {
//        findViewById(R.id.pay_way_zfb_image).setVisibility(View.VISIBLE);
//        findViewById(R.id.pay_way_wx_image).setVisibility(View.GONE);
//        findViewById(R.id.pay_way_remit_image).setVisibility(View.GONE);
        payWay = "alipay";
        btnPay();
    }

    @OnClick(R.id.pay_way_wx)
    public void productOrderWx() {
//        findViewById(R.id.pay_way_zfb_image).setVisibility(View.GONE);
//        findViewById(R.id.pay_way_wx_image).setVisibility(View.VISIBLE);
//        findViewById(R.id.pay_way_remit_image).setVisibility(View.GONE);
        payWay = "wx";
        btnPay();
    }
    /*
      转账汇款
     */
    @OnClick(R.id.pay_way_remit)
    public void productOrderRemit() {
//        findViewById(R.id.pay_way_zfb_image).setVisibility(View.GONE);
//        findViewById(R.id.pay_way_wx_image).setVisibility(View.GONE);
//        findViewById(R.id.pay_way_remit_image).setVisibility(View.VISIBLE);
        CompanyRemitActivity.launch(this,orderNo,source);
    }

    private void btnPay() {
//        if(View.VISIBLE == findViewById(R.id.pay_way_remit_image).getVisibility()){
//            CompanyRemitActivity.launch(this,orderNo,source);
//        }else {
            showProgress();
            producePayPresenter.getProducePayInfo(payWay, orderNo, source);
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - 支付成功
                 * "fail"    - 支付失败
                 * "cancel"  - 取消支付
                 * "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）
                 * "unknown" - app进程异常被杀死(一般是低内存状态下,app进程被杀死)
                 */
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                LogUtils.logd("支付错误信息,errorMsg:" + errorMsg + "---- extraMsg:" + extraMsg);
                producePayPresenter.callBack(true);
                if ("success".equals(result)) {
                    setResult(RESULT_OK);
                    ProducePayResultActivity.launch(mContext,ProducePayResultActivity.SUCCESS,orderNo);
                } else {
                    ProducePayResultActivity.launch(mContext,ProducePayResultActivity.FAILURE,orderNo);
                }
                finish();
            }
        }

        if(requestCode == 4321){
            finish();
        }
    }

    @Override
    public void showProgress() {
        tipDialog = new UITipDialog.Builder(mContext)
                .setIconType(UITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在发起支付")
                .create();
        tipDialog.show();
    }

    @Override
    public void dismissProgress() {
        tipDialog.dismiss();
    }

    @Override
    public void pay(String payInfo) {
        dismissProgress();
        Pingpp.createPayment(this, payInfo);
    }

    @Override
    public void finishView() {
        finish();
    }
}
