package com.zhaotai.uzao.ui.order.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaotai.uzao.HomeActivity;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.EventBean.PutawayEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/6/13
 * Created by LiYou
 * Description :
 */

public class PayResultActivity extends BaseActivity {

    @BindView(R.id.ll_pay_success)
    LinearLayout llSuccess;
    @BindView(R.id.ll_pay_failure)
    LinearLayout llFailure;

    @BindView(R.id.return_main)
    TextView mRightBtn;
    @BindView(R.id.pay_state_img)
    ImageView mStateImg;
    @BindView(R.id.pay_state_text)
    TextView mStateText;
    @BindView(R.id.pay_num)
    TextView mNum;
    @BindView(R.id.pay_money)
    TextView mMoney;// 支付金额
    @BindView(R.id.tv_putaway_tip)
    TextView mPutawayTip;

    private PutawayEvent putawayEvent;

    private int state = 0;// 0 随便逛逛 1 去上架

    /**
     * 支付成功/失败
     *
     * @param state    "success" "fail"
     * @param num      购买了几件商品
     * @param money    价格
     * @param orderIds 订单id
     */
    public static void launch(Context context, String state, String num, String money, ArrayList<String> orderIds) {
        Intent intent = new Intent(context, PayResultActivity.class);
        intent.putExtra("payState", state);
        intent.putExtra("payNum", num);
        intent.putExtra("payMoney", money);
        intent.putExtra("orderIds", orderIds);
        context.startActivity(intent);
    }


    @Override
    protected void initView() {
        setContentView(R.layout.activity_pay_result);
        mTitle.setText("支付结果");
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        String state = getIntent().getStringExtra("payState");
        if (state.equals("success")) {
            //支付成功
            llSuccess.setVisibility(View.VISIBLE);
            llFailure.setVisibility(View.GONE);
            mStateImg.setBackgroundResource(R.drawable.ic_pay_success);
            mStateText.setText("恭喜你,支付成功");
            mNum.setText(getString(R.string.pay_goods_num, getIntent().getStringExtra("payNum")));
            mMoney.setText(getString(R.string.pay_goods_money, getIntent().getStringExtra("payMoney")));
        } else {
            //支付失败
            llSuccess.setVisibility(View.GONE);
            llFailure.setVisibility(View.VISIBLE);
            mStateImg.setBackgroundResource(R.drawable.ic_pay_fail);
            mStateText.setText("支付失败");
            mNum.setText(getString(R.string.pay_goods_num, getIntent().getStringExtra("payNum")));
            mMoney.setText(getString(R.string.pay_goods_money, getIntent().getStringExtra("payMoney")));
        }
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @OnClick(R.id.return_main)
    public void onClickRightBtn() {
        switch (state) {
            case 0:
                //返回首页
                HomeActivity.launch(mContext, 0);
                finish();
                break;
            case 1:
                //去上架
                if (putawayEvent != null) {
                    TemplatePutawayActivity.launch(mContext,putawayEvent.mkuId,putawayEvent.spuImages,putawayEvent.skuImages,putawayEvent.designId,putawayEvent.isTemplate,putawayEvent.type);
                    finish();
                }
                break;
        }

    }

    @OnClick(R.id.return_main1)
    public void onClickBack() {
        //返回首页
        HomeActivity.launch(mContext, 0);
        finish();
    }

    /**
     * 个人中心
     */
    @OnClick(R.id.tv_pay_result_go_order)
    public void goOrder() {
        HomeActivity.launch(mContext, 3);
    }

    /**
     * 重新支付
     */
    @OnClick(R.id.repay)
    public void rePay() {
        Bundle b = getIntent().getExtras();
        ArrayList<String> orderId = (ArrayList<String>) b.getSerializable("orderIds");
        PayConfirmOrderActivity.launch(mContext, orderId);
        finish();
    }

    /**
     * 申请上架接收 上架商品信息
     * @param putawayEvent
     */
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PutawayEvent putawayEvent) {
        mRightBtn.setText("去上架");
        state = 1;
        mPutawayTip.setVisibility(View.VISIBLE);
        this.putawayEvent = putawayEvent;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(this);
    }
}
