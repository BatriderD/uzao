package com.zhaotai.uzao.ui.order.activity;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pingplusplus.android.Pingpp;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.PersonBean;
import com.zhaotai.uzao.bean.post.RewardInfo;
import com.zhaotai.uzao.ui.order.contract.DesignerPayConfirmContract;
import com.zhaotai.uzao.ui.order.presenter.DesignerRewardPayConfirmPresenter;
import com.zhaotai.uzao.utils.GlideCircleTransform;
import com.zhaotai.uzao.utils.LogUtils;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.widget.dialog.UITipDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2018/1/9
 * Created by LiYou
 * Description : 设计师 打赏订单确认页
 */

public class DesignerRewardPayConfirmOrderActivity extends BaseActivity implements DesignerPayConfirmContract.View {

    @BindView(R.id.tv_price)
    TextView mPayPrice;
    @BindView(R.id.tv_pay_price)
    TextView mPrice;

    @BindView(R.id.iv_designer_avatar)
    ImageView mDesignerAvatar;
    @BindView(R.id.tv_designer_name)
    TextView mDesignerName;


    @BindView(R.id.iv_zfb_check)
    ImageView mZfbCheck;
    @BindView(R.id.iv_wx_check)
    ImageView mWxCheck;

    private UITipDialog tipDialog;
    private DesignerRewardPayConfirmPresenter mPresenter;
    private static final String PAY_ZFB = "alipay";
    private static final String PAY_WX = "wx";
    private String payWay = PAY_ZFB;
    private String price;

    public static void launch(Activity context, String designerId, String pic, String name, String price, RewardInfo materialInfo) {
        Intent intent = new Intent(context, DesignerRewardPayConfirmOrderActivity.class);
        intent.putExtra("designerId", designerId);
        intent.putExtra("pic", pic);
        intent.putExtra("name", name);
        intent.putExtra("price", price);
        intent.putExtra("payInfo", materialInfo);
        context.startActivityForResult(intent, 1);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_designer_reward_pay_confirm);
        mTitle.setText("订单确认");
    }

    @Override
    protected void initData() {
        String pic = getIntent().getStringExtra("pic");
        String name = getIntent().getStringExtra("name");
        price = getIntent().getStringExtra("price");

        //设计师头像
        Glide.with(mContext).load(ApiConstants.UZAOCHINA_IMAGE_HOST + pic)
                .transform(new GlideCircleTransform(mContext))
                .error(R.drawable.ic_default_head).into(mDesignerAvatar);
        //设计师昵称
        mDesignerName.setText(name);

        mPayPrice.setText("¥" + price);
        mPrice.setText("¥" + price);

        mPresenter = new DesignerRewardPayConfirmPresenter(this, this);
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
                    setResult(RESULT_OK);
                    DesignerRewardSuccessPayResultActivity.launch(mContext, price);
                } else {
                    DesignerRewardFailPayResultActivity.launch(mContext, price);
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

    @OnClick(R.id.rl_wx)
    public void onClickWx() {
        payWay = PAY_WX;
        mWxCheck.setImageResource(R.drawable.icon_circle_selected);
        mZfbCheck.setImageResource(R.drawable.icon_circle_unselected);
    }

    @OnClick(R.id.rl_zfb)
    public void onClickZfb() {
        payWay = PAY_ZFB;
        mZfbCheck.setImageResource(R.drawable.icon_circle_selected);
        mWxCheck.setImageResource(R.drawable.icon_circle_unselected);
    }

}
