package com.zhaotai.uzao.ui.order.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pingplusplus.android.Pingpp;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.EventBean.MaterialBuySuccessEvent;
import com.zhaotai.uzao.bean.MaterialDetailBean;
import com.zhaotai.uzao.ui.order.adapter.MaterialPayConfirmAdapter;
import com.zhaotai.uzao.ui.order.contract.MaterialPayConfirmContract;
import com.zhaotai.uzao.ui.order.presenter.MaterialPayConfirmPresenter;
import com.zhaotai.uzao.utils.LogUtils;
import com.zhaotai.uzao.widget.dialog.UITipDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2018/1/9
 * Created by LiYou
 * Description : 素材 订单确认页
 */

public class MaterialPayConfirmOrderActivity extends BaseActivity implements MaterialPayConfirmContract.View, View.OnClickListener {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    @BindView(R.id.tv_pay_price)
    TextView mPayPrice;

    ImageView mZfbCheck;
    ImageView mWxCheck;

    private List<MaterialDetailBean> data;
    private UITipDialog tipDialog;
    private MaterialPayConfirmPresenter mPresenter;
    private static final String PAY_ZFB = "alipay";
    private static final String PAY_WX = "wx";
    private String payWay = PAY_ZFB;
    private String price = "0";


    //立即购买
    private static final int SOURCE_BUY_NOW = 0;
    //从订单支付 已经有订单id了
    private static final int SOURCE_ORDER = 1;

    private int source;

    public static void launch(Context context, List<MaterialDetailBean> data) {
        Intent intent = new Intent(context, MaterialPayConfirmOrderActivity.class);
        intent.putExtra("material", (Serializable) data);
        intent.putExtra("payType", SOURCE_BUY_NOW);
        context.startActivity(intent);
    }

    /**
     * 从订单支付
     *
     * @param context 上下文
     * @param data    素材详情
     * @param orders  订单id
     * @param type    true 全部订单  false 待付款
     */
    public static void launch(Context context, List<MaterialDetailBean> data, ArrayList<String> orders, boolean type) {
        Intent intent = new Intent(context, MaterialPayConfirmOrderActivity.class);
        intent.putExtra("material", (Serializable) data);
        intent.putStringArrayListExtra("orderIds", orders);
        intent.putExtra("filterName", type);
        intent.putExtra("payType", SOURCE_ORDER);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_material_pay_confirm);
        mTitle.setText("订单确认");
    }

    @Override
    protected void initData() {
        data = (List<MaterialDetailBean>) getIntent().getSerializableExtra("material");
        source = getIntent().getIntExtra("payType", SOURCE_BUY_NOW);

        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        MaterialPayConfirmAdapter mAdapter = new MaterialPayConfirmAdapter(data);
        mRecycler.setAdapter(mAdapter);
        //View footView = View.inflate(mContext, R.layout.foot_material_pay_confirm, null);
        findViewById(R.id.rl_zfb).setOnClickListener(this);
        findViewById(R.id.rl_wx).setOnClickListener(this);
        mZfbCheck = (ImageView) findViewById(R.id.iv_zfb_check);
        mWxCheck = (ImageView) findViewById(R.id.iv_wx_check);
        //mAdapter.addFooterView(footView);

        for (int i = 0; i < data.size(); i++) {
            BigDecimal b1 = new BigDecimal(data.get(i).priceY);
            BigDecimal b2 = new BigDecimal(price);
            price = b1.add(b2).toString();
            //price = price + Float.valueOf(data.get(i).priceY);
        }

        mPayPrice.setText("¥" + price);

        mPresenter = new MaterialPayConfirmPresenter(this, this);
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @Override
    public void finishView() {
        finish();
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
    public void stopProgress() {
        if (tipDialog != null)
            tipDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                LogUtils.logd("支付结果,result:" + result);
                LogUtils.logd("支付错误信息,errorMsg:" + errorMsg + "---- extraMsg:" + extraMsg);

                switch (source) {
                    case SOURCE_BUY_NOW:
                        mPresenter.callback();
                        break;
                    case SOURCE_ORDER:
                        mPresenter.callback(getIntent().getBooleanExtra("filterName", true));
                        break;
                }

                if ("success".equals(result)) {
                    if (this.data != null && this.data.size() > 0) {
                        if (this.data.size() > 1) {
                            PayResultActivity.launch(mContext, "success", String.valueOf(this.data.size()), String.valueOf(price), new ArrayList<String>());
                        } else {
                            if (this.data != null && this.data.size() > 0)
                                MaterialSuccessPayResultActivity.launch(mContext, String.valueOf(price), this.data.get(0));
                        }
                    }
                    //发送EventBus通知购买成功
                    EventBus.getDefault().post(new MaterialBuySuccessEvent());
                    finish();
                } else {
                    ArrayList<String> orderIds;
                    if (source == SOURCE_BUY_NOW) {
                        orderIds = mPresenter.getOrderId();
                    } else {
                        orderIds = getIntent().getStringArrayListExtra("orderIds");
                    }
                    MaterialFailPayResultActivity.launch(mContext, this.data, String.valueOf(price), orderIds, getIntent().getBooleanExtra("filterName", true));
                }
            }
        }
    }

    /**
     * 支付
     */
    @OnClick(R.id.tv_pay)
    public void onClickPay() {
        switch (source) {
            case SOURCE_BUY_NOW:
                mPresenter.getPayInfoFromBuyNow(payWay, data);
                break;
            case SOURCE_ORDER:
                showProgress();
                ArrayList<String> orderIds = getIntent().getStringArrayListExtra("orderIds");
                mPresenter.getPayInfoFromOrder(payWay, orderIds);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_wx://微信
                payWay = PAY_WX;
                mWxCheck.setImageResource(R.drawable.icon_circle_selected);
                mZfbCheck.setImageResource(R.drawable.icon_circle_unselected);
                break;
            case R.id.rl_zfb://支付宝
                payWay = PAY_ZFB;
                mZfbCheck.setImageResource(R.drawable.icon_circle_selected);
                mWxCheck.setImageResource(R.drawable.icon_circle_unselected);
                break;
        }
    }
}
