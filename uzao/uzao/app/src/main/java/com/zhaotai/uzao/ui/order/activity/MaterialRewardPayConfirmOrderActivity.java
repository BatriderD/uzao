package com.zhaotai.uzao.ui.order.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pingplusplus.android.Pingpp;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.MaterialDetailBean;
import com.zhaotai.uzao.bean.post.RewardInfo;
import com.zhaotai.uzao.ui.order.adapter.MaterialPayConfirmAdapter;
import com.zhaotai.uzao.ui.order.contract.MaterialPayConfirmContract;
import com.zhaotai.uzao.ui.order.presenter.MaterialPayConfirmPresenter;
import com.zhaotai.uzao.utils.LogUtils;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.widget.dialog.UITipDialog;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2018/1/9
 * Created by LiYou
 * Description : 素材 打赏订单确认页
 */

public class MaterialRewardPayConfirmOrderActivity extends BaseActivity implements MaterialPayConfirmContract.View, View.OnClickListener {

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
    private String price;

    public static void launch(Activity context, List<MaterialDetailBean> data, String price, RewardInfo materialInfo) {
        Intent intent = new Intent(context, MaterialRewardPayConfirmOrderActivity.class);
        intent.putExtra("material", (Serializable) data);
        intent.putExtra("price", price);
        intent.putExtra("payInfo", materialInfo);
        context.startActivityForResult(intent, 1);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_material_reward_pay_confirm);
        mTitle.setText("打赏订单确认");
    }

    @Override
    protected void initData() {
        data = (List<MaterialDetailBean>) getIntent().getSerializableExtra("material");
        price = getIntent().getStringExtra("price");

        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        MaterialPayConfirmAdapter mAdapter = new MaterialPayConfirmAdapter(data);
        mRecycler.setAdapter(mAdapter);
        View footView = View.inflate(mContext, R.layout.foot_material_pay_confirm, null);
        footView.findViewById(R.id.rl_zfb).setOnClickListener(this);
        footView.findViewById(R.id.rl_wx).setOnClickListener(this);
        footView.findViewById(R.id.rl_reward_price).setVisibility(View.VISIBLE);
        TextView mRewardPrice = (TextView) footView.findViewById(R.id.tv_reward_price);
        mZfbCheck = (ImageView) footView.findViewById(R.id.iv_zfb_check);
        mWxCheck = (ImageView) footView.findViewById(R.id.iv_wx_check);
        mAdapter.addFooterView(footView);

        mRewardPrice.setText("¥" + price);
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
                LogUtils.logd("支付错误信息,errorMsg:" + errorMsg + "---- extraMsg:" + extraMsg);

                mPresenter.callback();

                if ("success".equals(result)) {
                    ToastUtil.showShort("打赏成功");
                    setResult(RESULT_OK);
                } else {
                    ToastUtil.showShort("打赏失败");
                }
                finish();
            }
        }
    }

    /**
     * 支付
     */
    @OnClick(R.id.tv_pay)
    public void onClickPay() {
        mPresenter.getPayInfoReward(payWay, (RewardInfo) getIntent().getSerializableExtra("payInfo"));
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
